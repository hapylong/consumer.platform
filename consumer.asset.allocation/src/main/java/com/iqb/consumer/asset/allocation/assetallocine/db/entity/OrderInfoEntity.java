package com.iqb.consumer.asset.allocation.assetallocine.db.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

@Table(name = "jys_orderinfo")
public class OrderInfoEntity {
    private long id;
    private String orderId;
    private String batchNo;
    private long userId;
    private String regId;
    private String merchantNo;
    private String bizType;
    private String orderName;
    private BigDecimal orderAmt;
    private Short orderItems;
    private String orderRemark;
    private BigDecimal preAmt;
    private Short preAmtStatus;
    private Short status;
    private Short wfStatus;
    private Short riskStatus;
    private String riskRetRemark;
    private Short refundFlag;
    private BigDecimal margin;
    private BigDecimal downPayment;
    private BigDecimal serviceFee;
    private long planId;
    private long qrCodeId;
    private Integer takePayment;
    private Integer feeYear;
    private BigDecimal feeAmount;
    private BigDecimal monthInterest;
    private Integer chargeWay;
    private String procInstId;
    private Double fee;
    private BigDecimal sumMoney;
    private BigDecimal applyAmt;
    private BigDecimal gpsAmt;
    private String assessPrice;
    private Date expireDate;
    private String version;
    private Date createTime;
    private Date updateTime;

    /** 新增 **/
    private String proName; // 项目名称
    private String bakOrgan; // 备案机构
    private String url; // 摘牌网址
    private String proDetail; // 项目概况
    private String tranCondition; // 转让条件transferConditions
    private String safeWay; // SafeguardWay

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Short getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Short orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
    }

    public Short getPreAmtStatus() {
        return preAmtStatus;
    }

    public void setPreAmtStatus(Short preAmtStatus) {
        this.preAmtStatus = preAmtStatus;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(Short wfStatus) {
        this.wfStatus = wfStatus;
    }

    public Short getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Short riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getRiskRetRemark() {
        return riskRetRemark;
    }

    public void setRiskRetRemark(String riskRetRemark) {
        this.riskRetRemark = riskRetRemark;
    }

    public Short getRefundFlag() {
        return refundFlag;
    }

    public void setRefundFlag(Short refundFlag) {
        this.refundFlag = refundFlag;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

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

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public long getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(long qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public Integer getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(Integer takePayment) {
        this.takePayment = takePayment;
    }

    public Integer getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(Integer feeYear) {
        this.feeYear = feeYear;
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

    public Integer getChargeWay() {
        return chargeWay;
    }

    public void setChargeWay(Integer chargeWay) {
        this.chargeWay = chargeWay;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public BigDecimal getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public String getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(String assessPrice) {
        this.assessPrice = assessPrice;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getBakOrgan() {
        return bakOrgan;
    }

    public void setBakOrgan(String bakOrgan) {
        this.bakOrgan = bakOrgan;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProDetail() {
        return proDetail;
    }

    public void setProDetail(String proDetail) {
        this.proDetail = proDetail;
    }

    public String getTranCondition() {
        return tranCondition;
    }

    public void setTranCondition(String tranCondition) {
        this.tranCondition = tranCondition;
    }

    public String getSafeWay() {
        return safeWay;
    }

    public void setSafeWay(String safeWay) {
        this.safeWay = safeWay;
    }
}
