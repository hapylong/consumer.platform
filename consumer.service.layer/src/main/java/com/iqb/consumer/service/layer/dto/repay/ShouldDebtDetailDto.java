package com.iqb.consumer.service.layer.dto.repay;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 应收明细
 */
public class ShouldDebtDetailDto {
    private String realName;// 真实姓名
    private String idNo;// 身份证号
    private String orderId;// 订单id
    private String regId;// 注册号
    private String merchantNo;// 商户号
    private String sourcesFunding;// 资金来源
    private String fundId;// 资金端上标ID号
    private int installTerms;// 分期期数
    private int repayNo;// 还款顺序
    private Date lastRepayDate;// 最后还款日
    private Date curRepayDate;// 还款时间
    private BigDecimal curRepayAmt;// 本期应还总金额
    private BigDecimal curRealRepayAmt;// 已经还款金额
    private BigDecimal curRepayOverdueInterest;// 本期应还逾期金额
    private BigDecimal curRepayPrincipal;// 本期应还本金
    private BigDecimal curRepayInterest;// 本期应还利息
    private BigDecimal realPayAmt;// 实际分摊金额，月供
    private BigDecimal contractAmt;// 合同金额
    private int overdueDays;// 逾期天数
    private Integer status;// 账单状态
    private String bankCardNo;// 银行卡
    private String bankName;// 银行名称
    private String bankId;// 银行简称
    private String repayType;// 还款方式

    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public BigDecimal getCurRepayPrincipal() {
        return curRepayPrincipal;
    }

    public void setCurRepayPrincipal(BigDecimal curRepayPrincipal) {
        this.curRepayPrincipal = curRepayPrincipal;
    }

    public BigDecimal getCurRepayInterest() {
        return curRepayInterest;
    }

    public void setCurRepayInterest(BigDecimal curRepayInterest) {
        this.curRepayInterest = curRepayInterest;
    }

    public BigDecimal getRealPayAmt() {
        return realPayAmt;
    }

    public void setRealPayAmt(BigDecimal realPayAmt) {
        this.realPayAmt = realPayAmt;
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

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public Date getCurRepayDate() {
        return curRepayDate;
    }

    public void setCurRepayDate(Date curRepayDate) {
        this.curRepayDate = curRepayDate;
    }

    public BigDecimal getCurRepayAmt() {
        return curRepayAmt;
    }

    public void setCurRepayAmt(BigDecimal curRepayAmt) {
        this.curRepayAmt = curRepayAmt;
    }

    public BigDecimal getCurRealRepayAmt() {
        return curRealRepayAmt;
    }

    public void setCurRealRepayAmt(BigDecimal curRealRepayAmt) {
        this.curRealRepayAmt = curRealRepayAmt;
    }

    public BigDecimal getCurRepayOverdueInterest() {
        return curRepayOverdueInterest;
    }

    public void setCurRepayOverdueInterest(BigDecimal curRepayOverdueInterest) {
        this.curRepayOverdueInterest = curRepayOverdueInterest;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
