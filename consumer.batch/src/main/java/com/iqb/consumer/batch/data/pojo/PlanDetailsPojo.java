package com.iqb.consumer.batch.data.pojo;

import java.math.BigDecimal;

public class PlanDetailsPojo {
    private BigDecimal downPayment; // 首付
    private BigDecimal serviceFee; // 服务费
    private BigDecimal margin; // 保证金
    private BigDecimal leftAmt; // 剩余期数
    private BigDecimal feeAmount; // 上收利息
    private Double fee; // 上收利息
    private BigDecimal monthInterest; // 月供
    private BigDecimal takePayment; // 上收月供
    private BigDecimal preAmt; // 上收月供
    private Integer orderItems; // 期数

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getLeftAmt() {
        return leftAmt;
    }

    public void setLeftAmt(BigDecimal leftAmt) {
        this.leftAmt = leftAmt;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public BigDecimal getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(BigDecimal takePayment) {
        this.takePayment = takePayment;
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }
}
