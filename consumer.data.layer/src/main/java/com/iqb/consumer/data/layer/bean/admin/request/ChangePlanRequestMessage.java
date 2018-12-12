package com.iqb.consumer.data.layer.bean.admin.request;

import java.util.Date;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangePlanRequestMessage {
    private static final Logger logger = LoggerFactory.getLogger(ChangePlanRequestMessage.class);

    public static final String PLAN_TARGET_STOP = "STOP";
    public static final String PLAN_TARGET_RESET = "RESET";
    private String target;
    private String describe; // 处理意见
    private String orderId; // 订单号
    private String handler; // 审批人
    private String merchantId; // 商户表id
    private Date beginDate;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public boolean checkRequest() {
        logger.info("ChangePlanRequestMessage.checkRequest :" + toString());
        if (target == null ||
                StringUtil.isEmpty(describe) ||
                StringUtil.isEmpty(orderId) ||
                StringUtil.isEmpty(handler) ||
                StringUtil.isEmpty(merchantId)) {
            return false;
        }
        if (target.equals(PLAN_TARGET_RESET)) {
            return beginDate != null;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ChangePlanRequestMessage [target=" + target + ", describe=" + describe + ", orderId=" + orderId
                + ", handler=" + handler + ", merchantId=" + merchantId + "]";
    }
}
