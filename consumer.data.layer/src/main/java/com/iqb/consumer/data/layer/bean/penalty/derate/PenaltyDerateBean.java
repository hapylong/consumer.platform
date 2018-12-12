package com.iqb.consumer.data.layer.bean.penalty.derate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * Description: 减免逾期罚息
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月9日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class PenaltyDerateBean {
    /** 主键id **/
    private String id;
    /** 分期明细id **/
    private String installDetailId;
    /** 订单id **/
    private String orderId;
    /** 流程id **/
    private String procInstId;
    /** uuid **/
    private String uuid;
    /** 商户简称名 **/
    private String merchantShortName;
    /** 商户号 **/
    private String merchantNo;
    /** 注册号（手机号） **/
    private String regId;
    /** 客户真实姓名 **/
    private String realName;
    /** 分期期数 **/
    private Integer installTerms;
    /** 当前期数 **/
    private Integer repayNo;
    /** 还款日 **/
    private Date lastRepayDate;
    /** 逾期天数 **/
    private Integer overdueDays;
    /** 当前逾期利息 **/
    private BigDecimal curRepayOverdueInterest;
    /** 滞纳金（违约金） **/
    private BigDecimal fixedOverdueAmt;
    /** 减免类型 **/
    private String derateType;
    /** 减免逾期罚息 **/
    private BigDecimal cutOverdueInterest;
    /** 减免后罚息 **/
    private BigDecimal cutOverdueInterestAfter;
    /** 减免滞纳金 **/
    private BigDecimal cutFixedOverdueAmt;
    /** 减免后滞纳金 **/
    private BigDecimal cutFixedOverdueAmtAfter;
    /** 减免总金额 **/
    private BigDecimal cutAmt;
    /** 本期应还 **/
    private BigDecimal curRepayAmt;
    /** 应还总金额（计算之后） **/
    private BigDecimal curRepayAmtTotal;
    /** 减免原因 **/
    private String cutReason;
    /** 减免备注 **/
    private String remark;
    /** 线上支付失败图片 **/
    private String onlinePayFailImg;
    /** 扣款银行流水图片 **/
    private String debitBankWaterImg;
    /** 其他图片 **/
    private String otherImg;
    /** 申请状态 **/
    private String applyStatus;
    /** 申请发起日期 **/
    private Date applyDate;
    /** 申请通过日期 **/
    private Date applySuccDate;
    /** 版本 **/
    private Integer version;
    /** 借款金额 **/
    private BigDecimal orderAmt;
    /** 当前系统时间与上次减免罚息日期差值 **/
    private int days;
    /** 月供 **/
    private BigDecimal monthInterest;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstallDetailId() {
        return installDetailId;
    }

    public void setInstallDetailId(String installDetailId) {
        this.installDetailId = installDetailId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
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

    public Integer getInstallTerms() {
        return installTerms;
    }

    public void setInstallTerms(Integer installTerms) {
        this.installTerms = installTerms;
    }

    public Integer getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public BigDecimal getCurRepayOverdueInterest() {
        return curRepayOverdueInterest;
    }

    public void setCurRepayOverdueInterest(BigDecimal curRepayOverdueInterest) {
        this.curRepayOverdueInterest = curRepayOverdueInterest;
    }

    public BigDecimal getFixedOverdueAmt() {
        return fixedOverdueAmt;
    }

    public void setFixedOverdueAmt(BigDecimal fixedOverdueAmt) {
        this.fixedOverdueAmt = fixedOverdueAmt;
    }

    public String getDerateType() {
        return derateType;
    }

    public void setDerateType(String derateType) {
        this.derateType = derateType;
    }

    public BigDecimal getCutOverdueInterest() {
        return cutOverdueInterest;
    }

    public void setCutOverdueInterest(BigDecimal cutOverdueInterest) {
        this.cutOverdueInterest = cutOverdueInterest;
    }

    public BigDecimal getCutOverdueInterestAfter() {
        return cutOverdueInterestAfter;
    }

    public void setCutOverdueInterestAfter(BigDecimal cutOverdueInterestAfter) {
        this.cutOverdueInterestAfter = cutOverdueInterestAfter;
    }

    public BigDecimal getCutFixedOverdueAmt() {
        return cutFixedOverdueAmt;
    }

    public void setCutFixedOverdueAmt(BigDecimal cutFixedOverdueAmt) {
        this.cutFixedOverdueAmt = cutFixedOverdueAmt;
    }

    public BigDecimal getCutFixedOverdueAmtAfter() {
        return cutFixedOverdueAmtAfter;
    }

    public void setCutFixedOverdueAmtAfter(BigDecimal cutFixedOverdueAmtAfter) {
        this.cutFixedOverdueAmtAfter = cutFixedOverdueAmtAfter;
    }

    public BigDecimal getCutAmt() {
        return cutAmt;
    }

    public void setCutAmt(BigDecimal cutAmt) {
        this.cutAmt = cutAmt;
    }

    public BigDecimal getCurRepayAmt() {
        return curRepayAmt;
    }

    public void setCurRepayAmt(BigDecimal curRepayAmt) {
        this.curRepayAmt = curRepayAmt;
    }

    public BigDecimal getCurRepayAmtTotal() {
        return curRepayAmtTotal;
    }

    public void setCurRepayAmtTotal(BigDecimal curRepayAmtTotal) {
        this.curRepayAmtTotal = curRepayAmtTotal;
    }

    public String getCutReason() {
        return cutReason;
    }

    public void setCutReason(String cutReason) {
        this.cutReason = cutReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOnlinePayFailImg() {
        return onlinePayFailImg;
    }

    public void setOnlinePayFailImg(String onlinePayFailImg) {
        this.onlinePayFailImg = onlinePayFailImg;
    }

    public String getDebitBankWaterImg() {
        return debitBankWaterImg;
    }

    public void setDebitBankWaterImg(String debitBankWaterImg) {
        this.debitBankWaterImg = debitBankWaterImg;
    }

    public String getOtherImg() {
        return otherImg;
    }

    public void setOtherImg(String otherImg) {
        this.otherImg = otherImg;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getApplySuccDate() {
        return applySuccDate;
    }

    public void setApplySuccDate(Date applySuccDate) {
        this.applySuccDate = applySuccDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

}
