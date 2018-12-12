package com.iqb.consumer.data.layer.bean.order;

import java.math.BigDecimal;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年6月26日下午5:52:42 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class ArchivesBean {
    /** 订单号 **/
    private String orderId;
    /** 姓名 **/
    private String realName;
    /** 手机号码 **/
    private String regId;
    /** 订单日期 **/
    private String orderDate;
    /** 订单期数 **/
    private int orderItems;
    /** 订单金额 **/
    private BigDecimal orderAmt;
    /** 产品方案 **/
    private String planName;
    /** 月供 **/
    private BigDecimal monthInterest;
    /** 首付款 **/
    private BigDecimal preAmt;
    /** 业务类型 **/
    private String bizType;
    /** 订单状态 **/
    private String riskStatus;
    /** 商户名称 **/
    private String merchantName;
    /** 商户id **/
    private String orgCode;
    /** 车架号 **/
    private String vin;
    /** 车牌号 **/
    private String licenseNo;
    /** 发动机号 **/
    private String engineNumber;

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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(int orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

}
