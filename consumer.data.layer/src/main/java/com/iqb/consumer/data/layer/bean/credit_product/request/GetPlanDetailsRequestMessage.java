package com.iqb.consumer.data.layer.bean.credit_product.request;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.StringUtil;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;

public class GetPlanDetailsRequestMessage {

    protected static final Logger logger = LoggerFactory.getLogger(GetPlanDetailsRequestMessage.class);
    private String orderId;
    private BigDecimal orderAmt;
    private Long planId;

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean checkRequest() {
        logger.info("GetPlanDetailsRequestMessage.checkRequest :" + toString());
        if (orderAmt == null || planId == null) {
            return false;
        }
        return true;
    }

    public boolean checkRequestX() {
        logger.info("GetPlanDetailsRequestMessage.checkRequestX :" + toString());
        if (orderAmt == null || planId == null || StringUtil.isEmpty(orderId)) {
            return false;
        }
        if (!orderId.endsWith(InstOrderInfoEntity.SPECIAL_ORDER_IDENTIFIER)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GetPlanDetailsRequestMessage [orderId=" + orderId + ", orderAmt=" + orderAmt + ", planId=" + planId
                + "]";
    }

}
