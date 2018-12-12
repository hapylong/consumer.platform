package com.iqb.consumer.batch.data.pojo;

import java.math.BigDecimal;

import com.github.pagehelper.StringUtil;

public class GuaranteePledgeInfoRequestMessage {

    private String orderId; // 订单号
    private BigDecimal assessPrice; // 评估价格
    private BigDecimal orderAmt; // 订单金额
    private BigDecimal carAmt; // 购车价格
    private String planId; // 计划ID，购车的计划ID
    private Integer useCreditLoan; // 是否使用信用贷 1 是 0 否
    private String amtLines; // 额度区间

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public BigDecimal getCarAmt() {
        return carAmt;
    }

    public void setCarAmt(BigDecimal carAmt) {
        this.carAmt = carAmt;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Integer getUseCreditLoan() {
        return useCreditLoan;
    }

    public void setUseCreditLoan(Integer useCreditLoan) {
        this.useCreditLoan = useCreditLoan;
    }

    public String getAmtLines() {
        return amtLines;
    }

    public void setAmtLines(String amtLines) {
        this.amtLines = amtLines;
    }

    public boolean checkRequest() {
        if (StringUtil.isEmpty(orderId) ||
                StringUtil.isEmpty(planId) ||
                useCreditLoan == null ||
                assessPrice == null ||
                orderAmt == null ||
                carAmt == null) {
            return false;
        }
        if (useCreditLoan == 1 && StringUtil.isEmpty(amtLines)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GuaranteePledgeInfoRequestMessage [orderId=" + orderId
                + ", assessPrice=" + assessPrice + ", orderAmt=" + orderAmt
                + ", carAmt=" + carAmt + ", planId=" + planId
                + ", useCreditLoan=" + useCreditLoan + ", amtLines=" + amtLines
                + "]";
    }

}
