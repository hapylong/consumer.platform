package com.iqb.consumer.batch.data.pojo;

import java.math.BigDecimal;

import com.github.pagehelper.StringUtil;

public class GetPlanDetailsRequestMessage {

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
        if (orderAmt == null || planId == null) {
            return false;
        }
        return true;
    }

    public boolean checkRequestX() {
        if (orderAmt == null || planId == null || StringUtil.isEmpty(orderId)) {
            return false;
        }
        if (!orderId.endsWith(InstOrderInfoEntity.SPECIAL_ORDER_IDENTIFIER)) {
            return false;
        }
        return true;
    }
}
