package com.iqb.consumer.data.layer.bean.order;

import java.math.BigDecimal;

import com.iqb.consumer.data.layer.bean.BaseEntity;

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
 * 2017年10月23日下午2:08:19 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class InstOrderBean extends BaseEntity {
    /** 订单号 **/
    private String orderId;
    /** 用户名 **/
    private String realName;
    /** 注册号 **/
    private String regId;
    /** 订单名称 **/
    private String orderName;
    /** 订单金额 **/
    private String orderAmt;
    /** 订单期数 **/
    private String orderItems;
    /** 分期计划id **/
    private String planShortName;
    /** 首付款 **/
    private String downPayment;
    /** 保证金(该字段需要退款金额) **/
    private String margin;
    /** 服务费 **/
    private String serviceFee;
    /** 月供 **/
    private BigDecimal monthInterest;
    /** 上收息 **/
    private String feeAmount;
    /** 上收月供 **/
    private int takePayment;
    /** 预付金 **/
    private String preAmt;
    /** 风控状态(0,1,2,3,4) **/
    private Integer riskStatus;
    /** 放款时间 **/
    private String loanDate;
    /** 业务类型 **/
    private String bizType;
    /** 放款主体 **/
    private String lendersSubject;
    /** 商户名称 **/
    private String merchantFullName;
    /** 方案全民 **/
    private String planFullName;
    /** 支付状态 **/
    private String preAmtStatus;

    public String getPreAmtStatus() {
        return preAmtStatus;
    }

    public void setPreAmtStatus(String preAmtStatus) {
        this.preAmtStatus = preAmtStatus;
    }

    public String getPlanFullName() {
        return planFullName;
    }

    public void setPlanFullName(String planFullName) {
        this.planFullName = planFullName;
    }

    public String getMerchantFullName() {
        return merchantFullName;
    }

    public void setMerchantFullName(String merchantFullName) {
        this.merchantFullName = merchantFullName;
    }

    public String getLendersSubject() {
        return lendersSubject;
    }

    public void setLendersSubject(String lendersSubject) {
        this.lendersSubject = lendersSubject;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
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

    public String getPlanShortName() {
        return planShortName;
    }

    public void setPlanShortName(String planShortName) {
        this.planShortName = planShortName;
    }

    public String getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(String downPayment) {
        this.downPayment = downPayment;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public int getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(int takePayment) {
        this.takePayment = takePayment;
    }

    public String getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(String preAmt) {
        this.preAmt = preAmt;
    }

    public Integer getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Integer riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }
}
