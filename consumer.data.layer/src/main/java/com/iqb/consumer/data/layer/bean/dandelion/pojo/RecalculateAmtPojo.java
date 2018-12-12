package com.iqb.consumer.data.layer.bean.dandelion.pojo;

import java.math.BigDecimal;

public class RecalculateAmtPojo {

    private BigDecimal sfkTotalAmt; // 首付款合计
    private BigDecimal sfkAmt; // 首付款
    private BigDecimal bzAmt; // 保证金
    private BigDecimal fwAmt; // 服务费
    private BigDecimal ssxAmt; // 上收息
    private Integer qs; // 期数
    private BigDecimal ygAmt; // 月供金额
    private BigDecimal sbAmt; // 上标金额
    private BigDecimal fkAmt; // 放款金额
    private Double gpsTrafficFee; // gps流量费
    private String royaltyRate;// 提成比例
    private String applyLoanDate;// 申请放款日期
    private BigDecimal applyLoanAmt;// 申请放款金额
    private BigDecimal preAmt;// 预付款
    private BigDecimal underAmt;// 线下费用
    
    public BigDecimal getSbAmt() {
        return sbAmt;
    }

    public void setSbAmt(BigDecimal sbAmt) {
        this.sbAmt = sbAmt;
    }

    public BigDecimal getFkAmt() {
        return fkAmt;
    }

    public void setFkAmt(BigDecimal fkAmt) {
        this.fkAmt = fkAmt;
    }

    public BigDecimal getBzAmt() {
        return bzAmt;
    }

    public void setBzAmt(BigDecimal bzAmt) {
        this.bzAmt = bzAmt;
    }

    public BigDecimal getFwAmt() {
        return fwAmt;
    }

    public void setFwAmt(BigDecimal fwAmt) {
        this.fwAmt = fwAmt;
    }

    public BigDecimal getYgAmt() {
        return ygAmt;
    }

    public void setYgAmt(BigDecimal ygAmt) {
        this.ygAmt = ygAmt;
    }

    public BigDecimal getSfkTotalAmt() {
        return sfkTotalAmt;
    }

    public void setSfkTotalAmt(BigDecimal sfkTotalAmt) {
        this.sfkTotalAmt = sfkTotalAmt;
    }

    public BigDecimal getSfkAmt() {
        return sfkAmt;
    }

    public void setSfkAmt(BigDecimal sfkAmt) {
        this.sfkAmt = sfkAmt;
    }

    public BigDecimal getSsxAmt() {
        return ssxAmt;
    }

    public void setSsxAmt(BigDecimal ssxAmt) {
        this.ssxAmt = ssxAmt;
    }

    public Integer getQs() {
        return qs;
    }

    public void setQs(Integer qs) {
        this.qs = qs;
    }

    public Double getGpsTrafficFee() {
        return gpsTrafficFee;
    }

    public void setGpsTrafficFee(Double gpsTrafficFee) {
        this.gpsTrafficFee = gpsTrafficFee;
    }

    public String getRoyaltyRate() {
        return royaltyRate;
    }

    public void setRoyaltyRate(String royaltyRate) {
        this.royaltyRate = royaltyRate;
    }

    public String getApplyLoanDate() {
        return applyLoanDate;
    }

    public void setApplyLoanDate(String applyLoanDate) {
        this.applyLoanDate = applyLoanDate;
    }

    public BigDecimal getApplyLoanAmt() {
        return applyLoanAmt;
    }

    public void setApplyLoanAmt(BigDecimal applyLoanAmt) {
        this.applyLoanAmt = applyLoanAmt;
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
    }

    public BigDecimal getUnderAmt() {
        return underAmt;
    }

    public void setUnderAmt(BigDecimal underAmt) {
        this.underAmt = underAmt;
    }
    
}
