package com.iqb.consumer.data.layer.bean.finance.pojo.f;

import java.math.BigDecimal;
import java.util.Date;

public class FinanceBillPojo {
    private String orderId;// 订单号
    private String subOrderId; // 子订单
    private String regId;// 手机号
    private String realName;// 借款人
    private String merchantNo;// 商户
    private Integer repayNo;// 期数
    private Integer installTerms;// 总期数
    private Date lastRepayDate;// 最后还款日
    private Integer overdueDays;// 逾期天数
    private BigDecimal curRepayAmt;// 本期应还
    private BigDecimal cutOverdueInterest;// 减免罚息
    private BigDecimal curRepayOverdueInterest;// 罚息
    private BigDecimal fix;// 违约金
    private BigDecimal curRealRepayamt;// 还款金额
    private Date curRepayDate;// 实际还款日期
    private Integer status;// 账单状态

    private BigDecimal installSumAmt; // 借款金额
    private BigDecimal installAmt; // 分期金额
    private BigDecimal dqAmt; // 当期月供
    private BigDecimal curRepayPrincipal; //
    private BigDecimal curRepayInterest; //

    private String openId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
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

    public Integer getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

    public Integer getInstallTerms() {
        return installTerms;
    }

    public void setInstallTerms(Integer installTerms) {
        this.installTerms = installTerms;
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

    public BigDecimal getCurRepayAmt() {
        return curRepayAmt;
    }

    public void setCurRepayAmt(BigDecimal curRepayAmt) {
        this.curRepayAmt = curRepayAmt;
    }

    public BigDecimal getCutOverdueInterest() {
        return cutOverdueInterest;
    }

    public void setCutOverdueInterest(BigDecimal cutOverdueInterest) {
        this.cutOverdueInterest = cutOverdueInterest;
    }

    public BigDecimal getCurRepayOverdueInterest() {
        return curRepayOverdueInterest;
    }

    public void setCurRepayOverdueInterest(BigDecimal curRepayOverdueInterest) {
        this.curRepayOverdueInterest = curRepayOverdueInterest;
    }

    public BigDecimal getFix() {
        return fix;
    }

    public void setFix(BigDecimal fix) {
        this.fix = fix;
    }

    public BigDecimal getCurRealRepayamt() {
        return curRealRepayamt;
    }

    public void setCurRealRepayamt(BigDecimal curRealRepayamt) {
        this.curRealRepayamt = curRealRepayamt;
    }

    public Date getCurRepayDate() {
        return curRepayDate;
    }

    public void setCurRepayDate(Date curRepayDate) {
        this.curRepayDate = curRepayDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getInstallSumAmt() {
        return installSumAmt;
    }

    public void setInstallSumAmt(BigDecimal installSumAmt) {
        this.installSumAmt = installSumAmt;
    }

    public BigDecimal getInstallAmt() {
        return installAmt;
    }

    public void setInstallAmt(BigDecimal installAmt) {
        this.installAmt = installAmt;
    }

    public BigDecimal getDqAmt() {
        return dqAmt;
    }

    public void setDqAmt(BigDecimal dqAmt) {
        this.dqAmt = dqAmt;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
