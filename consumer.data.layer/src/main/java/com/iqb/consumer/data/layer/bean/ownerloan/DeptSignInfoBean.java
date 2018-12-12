package com.iqb.consumer.data.layer.bean.ownerloan;

import java.io.Serializable;
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
 * 2017年11月16日上午11:52:53 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class DeptSignInfoBean implements Serializable {

    /** 订单号 **/
    private String orderId;
    /** 预付款 **/
    private BigDecimal preAmt;
    /** 订单金额-核准金额 **/
    private BigDecimal orderAmt;
    /** gps安装费 **/
    private BigDecimal gpsAmt;
    /** gps流量费 **/
    private BigDecimal gpsTrafficFee;
    /** 预付款状态 **/
    private int preAmtStatus;
    /** 保证金 **/
    private BigDecimal margin;
    /** 手续费 **/
    private BigDecimal serviceFee;
    /** 月供 **/
    private BigDecimal monthInterest;
    /** 商标金额 **/
    private BigDecimal sbAmt;
    /** 合同金额 **/
    private BigDecimal contractAmt;
    /** 风控终审意见 **/
    private String finalRiskAdvice;
    /** 订单状态 **/
    private int riskStatus;
    /** 手机号码 **/
    private String regId;
    /** 业务类型名称 **/
    private String bizTypeName;
    /** 产品方案 **/
    private String planName;
    /** 项目名称 **/
    private String projectName;
    /** 担保公司 **/
    private String guarantee;
    /** 担保法人 **/
    private String guaranteeName;
    /** 交易流水号 **/
    private String tradeNo;
    /** 商户名称 **/
    private String merchantName;
    /** 姓名 **/
    private String realName;
    /** gps安装日期 **/
    private String gpsInstDate;
    /** 设备号 **/
    private String gpsDeviceNum;
    /** gps安装地址 **/
    private String gpsDeviceAddress;
    /** gps安装地址 **/
    private int gpsNum;
    /** 银行账号 **/
    private String bankCardNo;
    /** 开户行 **/
    private String bankName;
    /** 债权人姓名 **/
    private String creditorName;
    /** 债权人身份证号码 **/
    private String creditorIdNo;
    /** 债权人银行卡号 **/
    private String creditorBankNo;
    /** 债权人开户行 **/
    private String creditorBankName;
    /** 债权人 手机号码 **/
    private String creditorPhone;
    /** 放款金额 **/
    private BigDecimal loanAmt;
    /** 放款日期 **/
    private String loanDate;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public BigDecimal getGpsTrafficFee() {
        return gpsTrafficFee;
    }

    public void setGpsTrafficFee(BigDecimal gpsTrafficFee) {
        this.gpsTrafficFee = gpsTrafficFee;
    }

    public int getPreAmtStatus() {
        return preAmtStatus;
    }

    public void setPreAmtStatus(int preAmtStatus) {
        this.preAmtStatus = preAmtStatus;
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

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public BigDecimal getSbAmt() {
        return sbAmt;
    }

    public void setSbAmt(BigDecimal sbAmt) {
        this.sbAmt = sbAmt;
    }

    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
    }

    public String getFinalRiskAdvice() {
        return finalRiskAdvice;
    }

    public void setFinalRiskAdvice(String finalRiskAdvice) {
        this.finalRiskAdvice = finalRiskAdvice;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public String getGuaranteeName() {
        return guaranteeName;
    }

    public void setGuaranteeName(String guaranteeName) {
        this.guaranteeName = guaranteeName;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public int getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(int riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGpsInstDate() {
        return gpsInstDate;
    }

    public void setGpsInstDate(String gpsInstDate) {
        this.gpsInstDate = gpsInstDate;
    }

    public String getGpsDeviceNum() {
        return gpsDeviceNum;
    }

    public void setGpsDeviceNum(String gpsDeviceNum) {
        this.gpsDeviceNum = gpsDeviceNum;
    }

    public String getGpsDeviceAddress() {
        return gpsDeviceAddress;
    }

    public void setGpsDeviceAddress(String gpsDeviceAddress) {
        this.gpsDeviceAddress = gpsDeviceAddress;
    }

    public int getGpsNum() {
        return gpsNum;
    }

    public void setGpsNum(int gpsNum) {
        this.gpsNum = gpsNum;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorIdNo() {
        return creditorIdNo;
    }

    public void setCreditorIdNo(String creditorIdNo) {
        this.creditorIdNo = creditorIdNo;
    }

    public String getCreditorBankNo() {
        return creditorBankNo;
    }

    public void setCreditorBankNo(String creditorBankNo) {
        this.creditorBankNo = creditorBankNo;
    }

    public String getCreditorBankName() {
        return creditorBankName;
    }

    public void setCreditorBankName(String creditorBankName) {
        this.creditorBankName = creditorBankName;
    }

    public String getCreditorPhone() {
        return creditorPhone;
    }

    public void setCreditorPhone(String creditorPhone) {
        this.creditorPhone = creditorPhone;
    }

    public BigDecimal getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(BigDecimal loanAmt) {
        this.loanAmt = loanAmt;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

}
