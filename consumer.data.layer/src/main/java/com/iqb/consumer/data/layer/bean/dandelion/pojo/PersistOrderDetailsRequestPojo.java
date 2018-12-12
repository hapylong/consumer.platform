package com.iqb.consumer.data.layer.bean.dandelion.pojo;

import java.math.BigDecimal;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistOrderDetailsRequestPojo {

    private static final Logger logger = LoggerFactory.getLogger(PersistOrderDetailsRequestPojo.class);

    private String orderId;
    private Long planId;
    private BigDecimal orderAmt;
    private String bizType;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public boolean checkRequest() {
        logger.info("PersistOrderDetailsRequestPojo.checkRequest :" + toString());
        if (StringUtil.isEmpty(orderId) ||
                planId == null ||
                orderAmt == null ||
                bizType == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PersistOrderDetailsRequestPojo [orderId=" + orderId + ", planId=" + planId + ", orderAmt=" + orderAmt
                + ", bizType=" + bizType + "]";
    }
}
