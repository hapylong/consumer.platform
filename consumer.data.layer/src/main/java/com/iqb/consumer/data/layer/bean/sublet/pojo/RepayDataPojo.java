package com.iqb.consumer.data.layer.bean.sublet.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class RepayDataPojo {
    private BigDecimal overdueAmt;// 违约金
    private Integer curItems;// 当前期数
    private Date expiryDate;// 最后还款日
    // private BigDecimal payAmt; //已还款金额
    private BigDecimal hasRepayAmt; // 已还金额
    private BigDecimal repayAmt;// 应还金额
    private Integer hasRepayNo;// 已还期数
    private BigDecimal remainPrincipal;// 剩余本金
    private BigDecimal totalRepayAmt; // //累计应还=应还金额+违约金-应退保证金-应退上收息

    private BigDecimal monthAmt;// 月供
    private BigDecimal monthPrincipal;// 月供本金
    private BigDecimal monthInterest;// 月利息
    private BigDecimal totalOverdueInterest;// 总罚息
    private BigDecimal backInterest;// 退还上收息
    private Integer overdueItems;// 逾期期数
    private int status;// 当期账单还款状态

    public BigDecimal getOverdueAmt() {
        return overdueAmt;
    }

    public void setOverdueAmt(BigDecimal overdueAmt) {
        this.overdueAmt = overdueAmt;
    }

    public Integer getCurItems() {
        return curItems;
    }

    public void setCurItems(Integer curItems) {
        this.curItems = curItems;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getHasRepayAmt() {
        return hasRepayAmt;
    }

    public void setHasRepayAmt(BigDecimal hasRepayAmt) {
        this.hasRepayAmt = hasRepayAmt;
    }

    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }

    public Integer getHasRepayNo() {
        return hasRepayNo;
    }

    public void setHasRepayNo(Integer hasRepayNo) {
        this.hasRepayNo = hasRepayNo;
    }

    public BigDecimal getRemainPrincipal() {
        return remainPrincipal;
    }

    public void setRemainPrincipal(BigDecimal remainPrincipal) {
        this.remainPrincipal = remainPrincipal;
    }

    public BigDecimal getTotalRepayAmt() {
        return totalRepayAmt;
    }

    public void setTotalRepayAmt(BigDecimal totalRepayAmt) {
        this.totalRepayAmt = totalRepayAmt;
    }

    public BigDecimal getMonthAmt() {
        return monthAmt;
    }

    public void setMonthAmt(BigDecimal monthAmt) {
        this.monthAmt = monthAmt;
    }

    public BigDecimal getMonthPrincipal() {
        return monthPrincipal;
    }

    public void setMonthPrincipal(BigDecimal monthPrincipal) {
        this.monthPrincipal = monthPrincipal;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public BigDecimal getTotalOverdueInterest() {
        return totalOverdueInterest;
    }

    public void setTotalOverdueInterest(BigDecimal totalOverdueInterest) {
        this.totalOverdueInterest = totalOverdueInterest;
    }

    public BigDecimal getBackInterest() {
        return backInterest;
    }

    public void setBackInterest(BigDecimal backInterest) {
        this.backInterest = backInterest;
    }

    public Integer getOverdueItems() {
        return overdueItems;
    }

    public void setOverdueItems(Integer overdueItems) {
        this.overdueItems = overdueItems;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void checkResponse() throws Throwable {
        if (overdueAmt == null ||
                curItems == null ||
                expiryDate == null ||
                hasRepayAmt == null ||
                repayAmt == null ||
                hasRepayNo == null ||
                remainPrincipal == null) {
            throw new Throwable("ChatToGetRepayParamsResponseMessage 参数校验失败.");
        }
    }
}
