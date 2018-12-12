/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月12日 下午2:22:42
 * @version V1.0
 */
package com.iqb.consumer.service.layer.bill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.constant.FinanceConstant;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.ServiceParaConfig;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.consumer.service.layer.dto.refund.PaymentDto;
import com.iqb.consumer.service.layer.dto.refund.RepayList;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class BillInfoServiceImpl implements BillInfoService {

    protected static final Logger logger = LoggerFactory.getLogger(BillInfoServiceImpl.class);
    @Resource
    private ServiceParaConfig serviceParaConfig;
    @Resource
    private EncryptUtils encryptUtils;
    @Resource
    private ConsumerConfig consumerConfig;
    @Autowired
    private DictService dictServiceImpl;
    @Autowired
    private OrderBiz orderBiz;
    @Resource
    private UserBeanBiz userBiz;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getBillInfo(String orderId, String regId) {
        // 发送账户系统查询用户代偿信息
        JSONObject params = new JSONObject();
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        params.put("openId", openId);
        params.put("orderId", orderId);
        params.put("regId", regId);
        params.put("status", 5);// 所有未还款账单
        params.put("pageNum", 1);
        params.put("numPerPage", 1);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillGetPagelistUrl(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
        Map<String, Object> result = null;
        if (recordList != null && recordList.size() > 0) {
            result = recordList.get(0);
        }
        return result;
    }

    @Override
    public Map<String, Object> repay(Map<String, Object> billInfo, Map<String, String> signParameters) {
        // 1.获取还款参数
        String paymentJson = getRepayParams(billInfo, signParameters);
        // 2.调用账户系统
        Map<String, Object> result = null;
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillRefundUrl(),
                    encryptUtils.encrypt(paymentJson));
            logger.info("订单:{},平账返回结果:{}", billInfo.get("orderId"), resultStr);
            result = JSONObject.parseObject(resultStr);
        } catch (Exception e) {
            logger.error("发送给账户系统出现异常:{}", e);
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "发送给账户系统异常");
        }
        return result;
    }

    @Override
    public Map<String, Object> validateRepay(Map<String, Object> billInfo, Map<String, String> signParameters) {
        // 1.获取还款参数
        String paymentJson = getRepayParams(billInfo, signParameters);
        // 2.调用账户系统
        Map<String, Object> result = null;
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillValidateUrl(),
                    encryptUtils.encrypt(paymentJson));
            logger.info("订单:{},校验返回结果:{}", billInfo.get("orderId"), resultStr);
            result = JSONObject.parseObject(resultStr);
        } catch (Exception e) {
            logger.error("发送给账户系统出现异常:{}", e);
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "发送给账户系统异常");
        }
        return result;
    }

    @Override
    public String getRepayParams(Map<String, Object> billInfo, Map<String, String> signParameters) {
        String orderId = (String) billInfo.get("orderId");
        String regId = (String) billInfo.get("regId");
        // // 从redis中取
        // String key = "Payment." + orderId + "." + regId;
        // String paymentJson = redisPlatformDao.getValueByKey(key);
        // if(paymentJson != null) {
        // return paymentJson;
        // }

        // 获取参数
        String tranTime = null;
        String tradeNo = null;
        Date repayDate = null;
        String bankCardNo = null;
        String bankName = null;
        String repayType = null;
        if (signParameters != null) {
            tranTime = signParameters.get("tranTime");
            tradeNo = signParameters.get("tradeNo");
            repayDate = DateUtil.parseDate(tranTime, DateUtil.SIMPLE_DATE_FORMAT);
            bankCardNo = signParameters.get("bankCardNo");
            bankName = signParameters.get("bankName");
            repayType = signParameters.get("repayType");
        }

        String merchantNo = (String) billInfo.get("merchantNo");
        BigDecimal sumAmt = BigDecimal.ZERO;
        if (billInfo.get("curRepayAmt") != null && billInfo.get("curRepayAmt") instanceof BigDecimal) {
            sumAmt = (BigDecimal) billInfo.get("curRepayAmt");
        } else if (billInfo.get("curRepayAmt") != null && billInfo.get("curRepayAmt") instanceof String) {
            sumAmt = new BigDecimal((String) billInfo.get("curRepayAmt"));
        }
        List<RepayList> repayList = new ArrayList<RepayList>();
        RepayList rl = new RepayList();
        rl.setAmt(sumAmt);
        if (billInfo.get("repayNo") != null && billInfo.get("repayNo") instanceof Integer) {
            rl.setRepayNo((int) billInfo.get("repayNo"));
        } else if (billInfo.get("repayNo") != null && billInfo.get("repayNo") instanceof String) {
            rl.setRepayNo(Integer.parseInt((String) billInfo.get("repayNo")));
        }
        repayList.add(rl);

        // 组装平账参数
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setMerchantNo(merchantNo);
        paymentDto.setOpenId((String) billInfo.get("openId"));
        paymentDto.setOrderId(orderId);
        paymentDto.setRegId(regId);
        paymentDto.setRepayDate(repayDate);
        paymentDto.setRepayList(repayList);
        paymentDto.setRepayModel("normal");
        paymentDto.setSumAmt(sumAmt);
        paymentDto.setTradeNo(tradeNo);
        paymentDto.setBankCardNo(bankCardNo);
        paymentDto.setBankName(bankName);
        paymentDto.setRepayType(repayType);
        List<PaymentDto> result = new ArrayList<PaymentDto>();
        result.add(paymentDto);
        return JSON.toJSONString(result);
    }

    @Override
    public List<Map<String, Object>> getAllBillInfo(String orderId, String regId) {
        // 发送账户系统查询用户代偿信息
        JSONObject params = new JSONObject();
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        params.put("openId", openId);
        params.put("orderId", orderId);
        params.put("regId", regId);
        params.put("status", 0);// 所有账单
        params.put("pageNum", 1);
        params.put("numPerPage", 100);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillGetPagelistUrl(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");
        if (res == null) {
            return null;
        }
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
        return recordList;
    }

    /**
     * 查询订单下所有的账单-中阁使用
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年5月31日
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getAllBillInfo(String orderId) {
        // 发送账户系统查询用户代偿信息
        JSONObject params = new JSONObject();
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        params.put("openId", openId);
        params.put("orderId", orderId);
        params.put("pageNum", 1);
        params.put("numPerPage", 100);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceAllBillGetPagelistUrl(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");
        if (res == null) {
            return null;
        }
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
        return recordList;
    }

    /**
     * 查询订单下所有的账单-中阁使用
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年5月31日
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getAllJysBillInfo(String orderId) {
        // 发送账户系统查询用户代偿信息
        JSONObject params = new JSONObject();
        String openId = dictServiceImpl.getJysOpenIdByOrderId(orderId);
        params.put("openId", openId);
        params.put("orderId", orderId);
        params.put("pageNum", 1);
        params.put("numPerPage", 100);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceAllBillGetPagelistUrl(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");
        if (res == null) {
            return null;
        }
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
        return recordList;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年6月14日
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getBillInfo(String orderId, String regId, String repayNos) {
        Map<String, Object> result = new HashMap<>();
        // 发送账户系统查询用户代偿信息
        JSONObject params = new JSONObject();
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        params.put("openId", openId);
        params.put("orderId", orderId);
        params.put("regId", regId);
        params.put("repayNo", repayNos);

        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getIntoXfNormalRepayBillInfoUrl(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) retMap.get("result");

        result.put("orderId", orderId);
        result.put("regId", regId);
        result.put("openId", openId);
        if (recordList != null && recordList.size() > 0) {
            result.put("repayList", recordList);
        }
        return result;
    }

    @Override
    public Map<String, Object> getAssignBill(String orderId, String repayNo, String regId) {
        // 发送账户系统查询用户代偿信息
        JSONObject params = new JSONObject();
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        params.put("openId", openId);
        params.put("orderId", orderId);
        params.put("regId", regId);
        params.put("repayNo", repayNo);
        String resultStr =
                SimpleHttpUtils.httpPost(consumerConfig.getAssignBillUrl(), encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) retMap.get("result");
        for (Map<String, Object> map : recordList) {
            if (repayNo.equalsIgnoreCase((String) map.get("repayNo"))) {
                map.put("curRepayAmt", map.get("amt"));
                return map;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getDesignBillInfo(String orderId, String repayNo, String regId) {
        // 发送账户系统查询用户代偿信息
        JSONObject params = new JSONObject();
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        params.put("openId", openId);
        params.put("orderId", orderId);
        params.put("regId", regId);
        params.put("status", 5);// 所有未还款账单
        params.put("pageNum", 1);
        params.put("numPerPage", 1);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillGetPagelistUrl(),
                encryptUtils.encrypt(params));
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
        for (Map<String, Object> result : recordList) {
            if (repayNo.equalsIgnoreCase(result.get("repayNo") + "")) {
                return result;
            }
        }
        return null;
    }

    /**
     * 账单全部结清-平账接口
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年1月16日
     */
    @Override
    public Map<String, Object> allRepay(Map<String, Object> billInfo, Map<String, String> signParameters) {
        // 1.获取还款参数
        String paymentJson = getAllRepayParams(billInfo, signParameters);
        // 2.调用账户系统
        Map<String, Object> result = null;
        try {
            String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillRefundUrl(),
                    encryptUtils.encrypt(paymentJson));
            logger.info("订单:{},平账返回结果:{}", billInfo.get("orderId"), resultStr);
            result = JSONObject.parseObject(resultStr);
        } catch (Exception e) {
            logger.error("发送给账户系统出现异常:{}", e);
            result.put("retCode", FinanceConstant.ERROR);
            result.put("retMsg", "发送给账户系统异常");
        }
        return result;
    }

    /**
     * 
     * Description:全部结清参数处理
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月16日
     */
    private String getAllRepayParams(Map<String, Object> billInfo, Map<String, String> signParameters) {
        String orderId = (String) billInfo.get("orderId");
        String regId = (String) billInfo.get("regId");

        // 获取参数
        String tranTime = null;
        String tradeNo = null;
        Date repayDate = null;
        String bankCardNo = null;
        String bankName = null;
        String repayType = null;
        if (signParameters != null) {
            tranTime = signParameters.get("tranTime");
            tradeNo = signParameters.get("tradeNo");
            repayDate = DateUtil.parseDate(tranTime, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
            repayType = signParameters.get("repayType");
        }

        String merchantNo = (String) billInfo.get("merchantNo");
        BigDecimal sumAmt = BigDecimal.ZERO;
        if (billInfo.get("curRepayAmt") != null && billInfo.get("curRepayAmt") instanceof BigDecimal) {
            sumAmt = (BigDecimal) billInfo.get("curRepayAmt");
        } else if (billInfo.get("curRepayAmt") != null && billInfo.get("curRepayAmt") instanceof String) {
            sumAmt = new BigDecimal((String) billInfo.get("curRepayAmt"));
        }
        List<RepayList> repayList = new ArrayList<RepayList>();
        RepayList rl = new RepayList();
        rl.setAmt(sumAmt);
        if (billInfo.get("repayNo") != null && billInfo.get("repayNo") instanceof Integer) {
            rl.setRepayNo((int) billInfo.get("repayNo"));
        } else if (billInfo.get("repayNo") != null && billInfo.get("repayNo") instanceof String) {
            rl.setRepayNo(Integer.parseInt((String) billInfo.get("repayNo")));
        }
        repayList.add(rl);

        // 组装平账参数
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setMerchantNo(merchantNo);
        paymentDto.setOpenId((String) billInfo.get("openId"));
        paymentDto.setOrderId(orderId);
        paymentDto.setRegId(regId);
        paymentDto.setRepayDate(repayDate);
        paymentDto.setRepayList(repayList);
        paymentDto.setRepayModel("all");
        paymentDto.setSumAmt(sumAmt);
        paymentDto.setTradeNo(tradeNo);
        paymentDto.setBankCardNo(bankCardNo);
        paymentDto.setBankName(bankName);
        paymentDto.setRepayType(repayType);
        List<PaymentDto> result = new ArrayList<PaymentDto>();
        result.add(paymentDto);
        return JSON.toJSONString(result);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年1月16日
     */
    @Override
    public Map<String, Object> allClearPay(String orderId, Map<String, String> preResultParam) {
        logger.info("---提前结清-平账---开始--{}", preResultParam);
        Map<String, Object> resultMap = new HashMap<>();
        // 1.查询订单
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        OrderBean orderBean = orderBiz.selectOne(params);
        // 2.查询账单
        UserBean ub = userBiz.getUserInfo(Long.parseLong(orderBean.getUserId()));

        String repayNos = preResultParam.get("repayNo");
        Map<String, Object> billMap = getBillInfo(orderId, ub.getRegId(), repayNos);
        billMap.put("curRepayAmt", preResultParam.get("amount"));
        billMap.put("merchantNo", orderBean.getMerchantNo());

        if (billMap == null) {
            logger.debug("---提前结清-平账---订单{}异步还款未查询到账单", orderId);
            resultMap.put("msg", "未查询到账单");
            return resultMap;
        }
        // 3.平账
        Map<String, String> signParameters = new HashMap<>();
        signParameters.put("repayType", "22");
        signParameters.put("tranTime", preResultParam.get("tranTime"));

        resultMap = allRepay(billMap, signParameters);
        return resultMap;
    }
}
