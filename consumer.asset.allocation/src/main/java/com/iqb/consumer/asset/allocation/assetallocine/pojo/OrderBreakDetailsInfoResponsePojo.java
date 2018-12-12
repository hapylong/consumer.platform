package com.iqb.consumer.asset.allocation.assetallocine.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.common.utils.BigDecimalUtil;

public class OrderBreakDetailsInfoResponsePojo {
    /** jys_orderinfo **/
    private String orderId; // 订单号
    private String orderName; // 订单名称
    private Date createTime; // 订单时间
    private BigDecimal orderAmt;// 金额
    private Integer orderItems; // 期数
    private BigDecimal downPayment; // 首付款
    private BigDecimal margin; // 保证金
    private BigDecimal serviceFee; // 服务费
    private String monthInterest; // 月供
    private BigDecimal feeAmount; // 上收息
    private BigDecimal takePayment; // 上收月供
    private BigDecimal preAmt; // 预付款
    private Integer chargeWay; // 收费方式
    private Integer riskStatus;

    /** inst_merchantinfo **/
    private String merchantShortName; // 车商简称

    /** inst_plan **/
    private String planFullName; // 计划全称

    /** jys_user **/
    private String realName; // 姓名
    private String regId; // 手机号

    /** jys_assetallocation **/
    private Date sbTime; // 上标时间

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt == null ? null : BigDecimalUtil.format(orderAmt);
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment == null ? null : BigDecimalUtil.format(downPayment);
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin == null ? null : BigDecimalUtil.format(margin);
    }

    public String getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(String monthInterest) {
        this.monthInterest = monthInterest;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount == null ? null : BigDecimalUtil.format(feeAmount);
    }

    public BigDecimal getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(BigDecimal takePayment) {
        this.takePayment = takePayment == null ? null : BigDecimalUtil.format(takePayment);
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt == null ? null : BigDecimalUtil.format(preAmt);
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee == null ? null : BigDecimalUtil.format(serviceFee);
    }

    public Integer getChargeWay() {
        return chargeWay;
    }

    public void setChargeWay(Integer chargeWay) {
        this.chargeWay = chargeWay;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getPlanFullName() {
        return planFullName;
    }

    public void setPlanFullName(String planFullName) {
        this.planFullName = planFullName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Date getSbTime() {
        return sbTime;
    }

    public void setSbTime(Date sbTime) {
        this.sbTime = sbTime;
    }

    public Integer getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Integer riskStatus) {
        this.riskStatus = riskStatus;
    }
}
