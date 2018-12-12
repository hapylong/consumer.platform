package com.iqb.consumer.data.layer.bean.order.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

import jodd.util.StringUtil;

public class HouseBillCreateRequestMessage extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private BigDecimal installAmt;
    private String beginDate;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getInstallAmt() {
        return installAmt;
    }

    public void setInstallAmt(BigDecimal installAmt) {
        this.installAmt = installAmt;
    }

    public boolean checkRequest() {
        print();
        if (StringUtil.isEmpty(orderId) || beginDate == null || installAmt == null
                || installAmt.compareTo(BigDecimal.ZERO) != 1) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HouseBillCreateRequestMessage [orderId=" + orderId + ", installAmt=" + installAmt + ", beginDate="
                + beginDate + "]";
    }
}
