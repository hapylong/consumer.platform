package com.iqb.consumer.batch.data.pojo;

import java.util.Date;

public class UserPaymentInfoBean {

    private String regId;// 手机号
    private String orderId;// 订单号
    private String merchantNo;// 商户号
    private int repayNo;// 期数
    private int installTerms;// 分期期数
    private Date lastRepayDate;// 最后还款日
    private int overdueDays;// 逾期天数
    private String curRepayAmt;// 本期应还总金额
    private String curRepayOverdueInterest;// 本期应还逾期金额
    private String curRealRepayamt;// 已经还款金额
    private int status;// 账单状态

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public int getInstallTerms() {
        return installTerms;
    }

    public void setInstallTerms(int installTerms) {
        this.installTerms = installTerms;
    }

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getCurRepayAmt() {
        return curRepayAmt;
    }

    public void setCurRepayAmt(String curRepayAmt) {
        this.curRepayAmt = curRepayAmt;
    }

    public String getCurRepayOverdueInterest() {
        return curRepayOverdueInterest;
    }

    public void setCurRepayOverdueInterest(String curRepayOverdueInterest) {
        this.curRepayOverdueInterest = curRepayOverdueInterest;
    }

    public String getCurRealRepayamt() {
        return curRealRepayamt;
    }

    public void setCurRealRepayamt(String curRealRepayamt) {
        this.curRealRepayamt = curRealRepayamt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
