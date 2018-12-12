package com.iqb.consumer.data.layer.biz.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.BuckleConfig;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.pay.ChatFinanceToRefundRequestMessage;
import com.iqb.consumer.data.layer.bean.pay.SettlementCenterBuckleCallbackRequestMessage;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;
import com.iqb.consumer.data.layer.bean.settlementresult.SettlementResultBean;
import com.iqb.consumer.data.layer.bean.settlementresult.SettlementResultWithHoldBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.admin.AdminManager;
import com.iqb.consumer.data.layer.biz.paylog.PaymentLogBiz;
import com.iqb.consumer.data.layer.biz.settlementresult.SettlementResultBiz;
import com.iqb.consumer.data.layer.biz.settlementresult.SettlementResultWithHoldBiz;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.utils.StringUtil;

@Repository
public class PayManager extends BaseBiz {

    private static final Logger logger = LoggerFactory.getLogger(AdminManager.class);

    @Autowired
    private OrderBiz orderBiz;

    @Autowired
    private ConsumerConfig consumerConfig;

    @Autowired
    private BuckleConfig buckleConfig;

    @Autowired
    private PaymentLogBiz paymentLogBiz;

    @Autowired
    private SettlementResultBiz settlementResultBiz;
    @Resource
    private SettlementResultWithHoldBiz settlementResultWithHoldBiz;

    /**
     * 
     * Description: FINANCE-1301 (F)逾期代扣FINANCE-1322 平账 && 结算中心 代扣 回调 && 全部还款
     * 
     * @param openId
     * @param orderId
     * @param
     * @return Object
     * @throws Exception
     * @throws @Author adam Create Date: 2017年6月21日 下午4:07:40
     */
    public void callBackSA(SettlementCenterBuckleCallbackRequestMessage scbc, String openId, String orderId)
            throws Exception {
        List<ChatFinanceToRefundRequestMessage> requestMessage = new ArrayList<ChatFinanceToRefundRequestMessage>();
        ChatFinanceToRefundRequestMessage cft = orderBiz.getSCBCRequestMessageByOid(orderId);
        cft.createChatMsg(scbc, openId);
        requestMessage.add(cft);
        String response = SimpleHttpUtils.httpPost(
                buckleConfig.getFinanceBillRefundUrl(),
                SignUtil.chatEncode(
                        JSONObject.toJSONString(requestMessage),
                        consumerConfig.getCommonPrivateKey()));
        JSONObject jo = JSONObject.parseObject(response);
        if (jo.get("retCode").equals("success")) {
            PaymentLogBean plb = new PaymentLogBean();
            plb.create(cft);
            paymentLogBiz.persistLog(plb);
            SettlementResultBean srb = new SettlementResultBean();
            srb.setTradeNo(scbc.getTradeNo());
            srb.setStatusBySCBC(scbc);
            settlementResultBiz.updateSRBByTN(srb);
        } else {
            throw new RuntimeException(JSONObject.toJSONString(response));
        }
    }

    /**
     * 
     * Description: FINANCE-1301 (F)逾期代扣FINANCE-1322 平账 && 结算中心 代扣 回调 && 部分还款
     * 
     * @param openId
     * @param
     * @return Object
     * @throws Exception
     * @throws @Author adam Create Date: 2017年6月21日 下午4:08:11
     */
    public void callBackSP(SettlementCenterBuckleCallbackRequestMessage scbc, String openId, String orderId)
            throws Exception {
        callBackSA(scbc, openId, orderId);
    }

    /**
     * 
     * Description: FINANCE-1301 (F)逾期代扣FINANCE-1322 平账 && 结算中心 代扣 回调 && 全部失败
     * 
     * @param openId
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年6月21日 下午4:08:11
     */
    public void callBackF(SettlementCenterBuckleCallbackRequestMessage scbc, String openId, String orderId) {
        SettlementResultBean srb = new SettlementResultBean();
        srb.setTradeNo(scbc.getTradeNo());
        srb.setStatusBySCBC(scbc);
        settlementResultBiz.updateSRBByTN(srb);
    }

    /**
     * 
     * Description:还款代扣单笔全部划扣成功回调
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    public int callBackSAForWithHold(SettlementCenterBuckleCallbackRequestMessage scbc, String openId, String orderId)
            throws Exception {
        logger.debug("---还款代扣回调平账开始---{}", JSONObject.toJSON(scbc));
        int result = 0;
        List<ChatFinanceToRefundRequestMessage> requestMessage = new ArrayList<ChatFinanceToRefundRequestMessage>();
        ChatFinanceToRefundRequestMessage cft = orderBiz.getSCBCRequestMessageByOid(orderId);
        cft.createChatMsg(scbc, openId);
        requestMessage.add(cft);
        String response = SimpleHttpUtils.httpPost(
                buckleConfig.getFinanceBillRefundUrl(),
                SignUtil.chatEncode(
                        JSONObject.toJSONString(requestMessage),
                        consumerConfig.getCommonPrivateKey()));
        if (!StringUtil.isNull(response)) {
            JSONObject jo = JSONObject.parseObject(response);
            if (jo.get("retCode").equals("success")) {
                PaymentLogBean plb = new PaymentLogBean();
                plb.create(cft);
                paymentLogBiz.persistLog(plb);
                SettlementResultWithHoldBean bean = new SettlementResultWithHoldBean();
                bean.setTradeNo(scbc.getTradeNo());
                bean.setStatusBySCBC(scbc);
                bean.setBillStatus(String.valueOf(jo.getIntValue("billStatus")));
                result = settlementResultWithHoldBiz.updateSettleByTradeNo(bean);
                logger.debug("---还款代扣回调平账完成---");
            } else {
                logger.error("---还款代扣回调平账失败--{}", response);
                throw new RuntimeException(JSONObject.toJSONString(response));
            }
        } else {
            logger.error("---还款代扣回调平账报错---{}");
            throw new RuntimeException("还款代扣回调平账报错");
        }
        return result;
    }

    /**
     * 
     * Description:还款代扣单笔部分划扣成功回调
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    public int callBackSPForWithHold(SettlementCenterBuckleCallbackRequestMessage scbc, String openId, String orderId)
            throws Exception {
        return callBackSAForWithHold(scbc, openId, orderId);
    }

    /**
     * 
     * Description:还款代扣单笔划扣失败回调
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    public int callBackFail(SettlementCenterBuckleCallbackRequestMessage scbc, String openId, String orderId) {
        SettlementResultWithHoldBean bean = new SettlementResultWithHoldBean();
        bean.setTradeNo(scbc.getTradeNo());
        bean.setStatusBySCBC(scbc);
        return settlementResultWithHoldBiz.updateSettleByTradeNo(bean);
    }
}
