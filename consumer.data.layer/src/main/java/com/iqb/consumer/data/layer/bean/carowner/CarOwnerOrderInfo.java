package com.iqb.consumer.data.layer.bean.carowner;

import java.math.BigDecimal;
import java.util.Date;

public class CarOwnerOrderInfo {

    private Long id;
    private String orderId;
    private String realName;
    private String regId;
    private Date orderDate;// 订单时间
    private BigDecimal amt; // 借款金额
    private Integer orderItem; // 期数
    private String planShortName;// 方案
    private BigDecimal margin;// 保证金
    private BigDecimal serviceFee;// 服务费
    private BigDecimal gpsTrafficFee;// GPS流量费
    private BigDecimal gpsAmt;// GPS安装费
    private BigDecimal monthInterest;// 月供
    private BigDecimal preAmt;// 预付款
    private BigDecimal orderAmt;// 核准金额
    private String riskStatus; // 订单状态
    private String wfStatus; // 工作流状态
    private String merchName; // 商户号
    private Date stageDate; // 分期时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public Integer getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(Integer orderItem) {
        this.orderItem = orderItem;
    }

    public String getPlanShortName() {
        return planShortName;
    }

    public void setPlanShortName(String planShortName) {
        this.planShortName = planShortName;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getGpsTrafficFee() {
        return gpsTrafficFee;
    }

    public void setGpsTrafficFee(BigDecimal gpsTrafficFee) {
        this.gpsTrafficFee = gpsTrafficFee;
    }

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(String wfStatus) {
        this.wfStatus = wfStatus;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public Date getStageDate() {
        return stageDate;
    }

    public void setStageDate(Date stageDate) {
        this.stageDate = stageDate;
    }

}
