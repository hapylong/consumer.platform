package com.iqb.consumer.data.layer.bean.dandelion.pojo;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RiskNoticeRequestPojo {
    private static final Logger logger = LoggerFactory.getLogger(RiskNoticeRequestPojo.class);
    private String orderId;
    private String riskStatus;
    private String messageInfo = "";
    private String message = "";

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean checkRequest() {
        logger.info(toString());
        if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(riskStatus)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RiskNoticeRequestPojo [orderId=" + orderId + ", riskStatus=" + riskStatus + ", messageInfo="
                + messageInfo + ", message=" + message + "]";
    }

}
