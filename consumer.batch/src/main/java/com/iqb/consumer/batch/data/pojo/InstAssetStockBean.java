package com.iqb.consumer.batch.data.pojo;

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
 * 2018年8月1日下午6:13:48 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstAssetStockBean {
    /** 订单号 **/
    private String orderId;
    /** 手机号码 **/
    private String regId;
    /** 姓名 **/
    private String realName;
    /** 商户号 **/
    private String merchantNo;
    /** 机构名称 **/
    private String merchantShortName;
    /** 开户ID **/
    private String openId;
    /** 上标ID **/
    private String fundId;
    /** 资金端 **/
    private String sourcesFunding;
    /** 总期数 **/
    private int installTerms;
    /** 已还期数 **/
    private int repayNo;
    /** 未还期数 **/
    private int nonRepayno;
    /** 借款总额 **/
    private BigDecimal contractAmt;
    /** 首付金额 **/
    private BigDecimal preAmt;
    /** 应还总额 **/
    private BigDecimal installSumAmt;
    /** 已还本金 **/
    private BigDecimal curRepayPrincipal;
    /** 未还本金 **/
    private BigDecimal remainPrincipal;
    /** 资产所属体系 **/
    private String parentMerName;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId.equals("") ? null : fundId;
    }

    public String getSourcesFunding() {
        return sourcesFunding;
    }

    public void setSourcesFunding(String sourcesFunding) {
        this.sourcesFunding = sourcesFunding;
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

    public int getNonRepayno() {
        return nonRepayno;
    }

    public void setNonRepayno(int nonRepayno) {
        this.nonRepayno = nonRepayno;
    }

    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
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

    public String getParentMerName() {
        return parentMerName;
    }

    public void setParentMerName(String parentMerName) {
        this.parentMerName = parentMerName;
    }
}
