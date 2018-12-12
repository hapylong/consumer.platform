package com.iqb.consumer.data.layer.bean.Pledge.pojo;

import java.math.BigDecimal;

import jodd.util.StringUtil;

public class UpdateAmtRequestPojo {

    private String id;
    private BigDecimal assessPrice;
    private BigDecimal orderAmt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(BigDecimal assessPrice) {
        this.assessPrice = assessPrice;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public boolean checkRequest() {
        if (StringUtil.isEmpty(id) || assessPrice == null || orderAmt == null) {
            return false;
        }
        return true;
    }

}
