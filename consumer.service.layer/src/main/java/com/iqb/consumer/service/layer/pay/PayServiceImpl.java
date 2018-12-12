package com.iqb.consumer.service.layer.pay;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.BuckleConfig;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;
import com.iqb.consumer.data.layer.bean.pay.OverDueBillPojo;
import com.iqb.consumer.data.layer.bean.pay.PayRecordBean;
import com.iqb.consumer.data.layer.bean.pay.SettlementCenterBuckleCallbackRequestMessage;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.ProductBiz;
import com.iqb.consumer.data.layer.biz.pay.PayManager;
import com.iqb.consumer.data.layer.biz.pay.PaytempRecordBiz;
import com.iqb.consumer.data.layer.biz.paylog.PaymentLogBiz;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.consumer.service.layer.dto.refund.PaymentDto;
import com.iqb.consumer.service.layer.dto.refund.RepayList;
import com.iqb.etep.common.redis.RedisPlatformDao;

@Service
public class PayServiceImpl implements PayService {

    protected static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    private static final String REFUNDKEY = "refund_key_";
    @Autowired
    private BuckleConfig buckleConfig;

    @Autowired
    private ConsumerConfig consumerConfig;
    @Resource
    private EncryptUtils encryptUtils;
    @Autowired
    private OrderBiz orderBiz;

    @Autowired
    private PayManager payManager;

    @Autowired
    private DictService dictServiceImpl;

    @Autowired
    private PaymentLogBiz paymentLogBiz;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Resource
    private PaytempRecordBiz paytempRecordBiz;
    @Autowired
    ProductBiz productBiz;

    /**
     * 完成支付平账
     * 
     * @param orderId
     * @param params
     * @param merchantNo
     * @return
     */
    @Override
    public Map<String, Object> refundBills(String orderId, Map<String, String> params, String merchantNo) {
        Map<String, Object> result = null;
        String billKey = REFUNDKEY + orderId;
        String billInfoJson = redisPlatformDao.getValueByKey(billKey);
        logger.info("从redis中取出账单信息为:{}", billInfoJson);
        if (billInfoJson == null) {
            // 从数据库中取账单信息
            PayRecordBean payRecordBean = paytempRecordBiz.queryPayRecordByOrderId(orderId);
            billInfoJson = payRecordBean.getContent();
            logger.info("从数据库中取出id:{}的账单信息为:{}", payRecordBean.getId(), billInfoJson);
        }

        List<PaymentDto> paymentDtoList = null;
        try {
            paymentDtoList = JSONObject.parseArray(billInfoJson, PaymentDto.class);// BeanUtil.toJavaList(billInfoJson,
                                                                                   // PaymentDto.class);
        } catch (Exception e) {
            logger.error("解析账单信息出现异常:{}", e);
        }

        // 拼装平账参数
        billInfoJson = getRefundParams(paymentDtoList, params, merchantNo);
        logger.info("平账传参：{}", billInfoJson);
        try {
            String resultStr =
                    SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillRefundUrl(),
                            encryptUtils.encrypt(billInfoJson));
            logger.info("订单:{},平账返回结果:{}", orderId, resultStr);
            result = JSONObject.parseObject(resultStr);
            // 根据平账结果返回成功与否
            if (result != null && "success".equals(result.get("retCode"))) {
                result = new HashMap<>();
                result.put("errno", "0");
                result.put("errorMsg", "交易成功");
                // 删除支付信息临时记录
                paytempRecordBiz.delPayRecord(orderId);
                // 修改订单状态为结清
                orderBiz.updateStatus(orderId, "10");
            }
        } catch (Exception e) {
            logger.error("发送给账户系统出现异常:{}", e);
        }
        return result;
    }

    private String getRefundParams(List<PaymentDto> paymentDtoList, Map<String, String> params, String merchantNo) {
        Date repayDate = new Date();
        for (int i = 0; i < paymentDtoList.size(); i++) {
            PaymentDto paymentDto = paymentDtoList.get(i);
            paymentDto.setTradeNo(params.get("tradeNo"));
            paymentDto.setRepayDate(repayDate);
            paymentDto.setOpenId(dictServiceImpl.getOpenIdByOrderId(paymentDto.getOrderId()));
            paymentDto.setMerchantNo(merchantNo);
            paymentDto.setBankCardNo(params.get("bankCardNo"));
            paymentDto.setBankName(params.get("bankName"));
            paymentDto.setRepayType("21");// 客户自还
            if (paymentDto.getRepayList() == null || paymentDto.getRepayList().size() == 0) {
                paymentDto.setRepayList(new ArrayList<RepayList>(1));
            }
        }
        return JSON.toJSONString(paymentDtoList);
    }

    /**
     * 保存支付中间记录
     * 
     * @param objs
     * @return
     */
    @Override
    public Map<String, String> savePaytemRecord(String payList, String orderId, String settleId) {
        Map<String, String> result = new HashMap<String, String>();
        List<PaymentDto> paymentDtoList = null;
        try {
            paymentDtoList = JSONArray.parseArray(payList, PaymentDto.class);
        } catch (Exception e) {
            logger.error("解析账单信息出现异常:{}", e);
        }
        // 2.5 redis中放入平账信息
        String billKey = REFUNDKEY + orderId;
        String content = JSON.toJSONString(paymentDtoList);
        redisPlatformDao.setKeyAndValueTimeout(billKey, content, 60 * 30);
        // 插入还款记录
        PayRecordBean payRecordBean = new PayRecordBean();
        payRecordBean.setContent(content);
        payRecordBean.setRegId(settleId);
        payRecordBean.setOrderId(orderId);
        paytempRecordBiz.insertPayRecord(payRecordBean);
        result.put("result", "success");
        result.put("msg", "中间记录保存成功");
        return result;
    }

    @Override
    public void callback(JSONObject requestMessage) throws GenerallyException {
        SettlementCenterBuckleCallbackRequestMessage scbc = null;
        try {
            scbc = JSONObject.
                    toJavaObject(requestMessage, SettlementCenterBuckleCallbackRequestMessage.class);
            if (scbc == null || !scbc.checkRequest()) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
            }
        } catch (Throwable e) {
            logger.error("PayServiceImpl.callback error :", e);
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
        }
        String orderId = OverDueBillPojo.getOidByTradeNo(scbc.getTradeNo());
        if (paymentLogBiz.getPayLogByTNo(scbc.getTradeNo()) != null) {
            return;
        }
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        try {
            switch (scbc.getStatus()) {
                case SettlementCenterBuckleCallbackRequestMessage.SUCCESS_ALL:
                    payManager.callBackSA(scbc, openId, orderId);
                    break;
                case SettlementCenterBuckleCallbackRequestMessage.SUCCESS_PART:
                    payManager.callBackSP(scbc, openId, orderId);
                    break;
                case SettlementCenterBuckleCallbackRequestMessage.FAIL:
                    payManager.callBackF(scbc, openId, orderId);
                    break;
                default:
                    throw new GenerallyException(Reason.UNKNOW_TYPE, Layer.SERVICE, Location.A);
            }
        } catch (Throwable e) {
            logger.error("PayServiceImpl.callback error2 :", e);
            throw new GenerallyException(Reason.UNKNOWN_ERROR, Layer.SERVICE, Location.A);
        }

    }

    @Override
    public PayConfBean getPayChannelBy(String payOwnerId, String merchantNo) {
        PayConfBean payConfBean = null;
        if (StringUtils.isNoneEmpty(payOwnerId)) {
            JSONObject objs = new JSONObject();
            objs.put("id", payOwnerId);
            payConfBean = productBiz.getPayConfById(objs);
        } else {
            JSONObject objs = new JSONObject();
            objs.put("merchantNo", merchantNo);
            // 通过商户号查询支付主体信息
            if (StringUtils.isNoneEmpty(merchantNo)) {
                payConfBean = productBiz.getPayConfByMno2(objs);
            }
            if (payConfBean == null) {
                objs.put("merchantNo", "default");
                payConfBean = productBiz.getPayConfByMno2(objs);
            }
        }
        return payConfBean;
    }

}
