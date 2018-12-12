package com.iqb.consumer.service.layer.dto;

import java.math.BigDecimal;

/**
 * 存量统计bean
 */
public class StockStatisticsDto {

    private String orderId;// 订单Id
    private String realName;// 真实姓名
    private String regId;// 手机号
    private String openId;// 账户号
    private String merchantNo;// 商户号
    private String sourcesFunding;// 资金来源
    private String fundId;// 上标ID
    private int installTerms;// 分期期数
    private int repayNo;// 已还期数
    private int nonRepayNo;// 未还期数
    private BigDecimal contractAmt;// 合同金额
    private BigDecimal installSumAmt;// 分期总金额
    private BigDecimal curRepayPrincipal;// 本期应还本金
    private BigDecimal remainPrincipal;// 剩余本金
    private String merchantShortName;// 商户简称
    private String parentMerName;// 父商户名称
    private BigDecimal preAmt;// 首付金额

    public int getNonRepayNo() {
        return nonRepayNo;
    }

    public void setNonRepayNo(int nonRepayNo) {
        this.nonRepayNo = nonRepayNo;
    }

    public String getParentMerName() {
        return parentMerName;
    }

    public void setParentMerName(String parentMerName) {
        this.parentMerName = parentMerName;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getSourcesFunding() {
        return sourcesFunding;
    }

    public void setSourcesFunding(String sourcesFunding) {
        this.sourcesFunding = sourcesFunding;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public int getInstallTerms() {
        return installTerms;
    }

    public void setInstallTerms(int installTerms) {
        this.installTerms = installTerms;
    }

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
    }

    public BigDecimal getInstallSumAmt() {
        return installSumAmt;
    }

    public void setInstallSumAmt(BigDecimal installSumAmt) {
        this.installSumAmt = installSumAmt;
    }

    public BigDecimal getCurRepayPrincipal() {
        return curRepayPrincipal;
    }

    public void setCurRepayPrincipal(BigDecimal curRepayPrincipal) {
        this.curRepayPrincipal = curRepayPrincipal;
    }

    public BigDecimal getRemainPrincipal() {
        return remainPrincipal;
    }

    public void setRemainPrincipal(BigDecimal remainPrincipal) {
        this.remainPrincipal = remainPrincipal;
    }

}
