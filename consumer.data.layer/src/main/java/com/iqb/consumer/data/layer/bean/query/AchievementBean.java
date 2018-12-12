package com.iqb.consumer.data.layer.bean.query;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AchievementBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String orderId;// 订单id
    private String userId;// 用户id
    private String merchId;// 关联商户id
    private String orderTime;// 时间
    private String orderName;// 订单名称
    private String orderAmt;// 订单金额
    private String orderItems;// 订单分期
    private String preAmount;// 预付金额
    private String preAmountStatus;// 预付金额
    private String orderRem;// 订单备注
    private String clrDate;// 清算日期
    private String orgTranceNo;// 冲正交易
    private String orderNo;// 流水号
    private String status;// 订单状态
    private Integer riskStatus;
    private String regId;// 用户号
    private String assessPrice;// 估价
    private String margin; // 保证金
    private String downPayment; // 首付款
    private String serviceFee;// 服务费
    private String finalPayment; // 尾款
    private int takePayment;// 是否上收月供 1:上收； 0：不上收
    private int feeYear;// 上收期数
    private BigDecimal monthInterest;// 月供
    private Date updateTime = new Date();// 更新时间
    private int orderPeriod;
    private String guarntee;// 担保人

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public String getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(String preAmount) {
        this.preAmount = preAmount;
    }

    public String getPreAmountStatus() {
        return preAmountStatus;
    }

    public void setPreAmountStatus(String preAmountStatus) {
        this.preAmountStatus = preAmountStatus;
    }

    public String getOrderRem() {
        return orderRem;
    }

    public void setOrderRem(String orderRem) {
        this.orderRem = orderRem;
    }

    public String getClrDate() {
        return clrDate;
    }

    public void setClrDate(String clrDate) {
        this.clrDate = clrDate;
    }

    public String getOrgTranceNo() {
        return orgTranceNo;
    }

    public void setOrgTranceNo(String orgTranceNo) {
        this.orgTranceNo = orgTranceNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Integer riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(String assessPrice) {
        this.assessPrice = assessPrice;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(String downPayment) {
        this.downPayment = downPayment;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(String finalPayment) {
        this.finalPayment = finalPayment;
    }

    public int getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(int takePayment) {
        this.takePayment = takePayment;
    }

    public int getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(int feeYear) {
        this.feeYear = feeYear;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getOrderPeriod() {
        return orderPeriod;
    }

    public void setOrderPeriod(int orderPeriod) {
        this.orderPeriod = orderPeriod;
    }

    public String getGuarntee() {
        return guarntee;
    }

    public void setGuarntee(String guarntee) {
        this.guarntee = guarntee;
    }

}
