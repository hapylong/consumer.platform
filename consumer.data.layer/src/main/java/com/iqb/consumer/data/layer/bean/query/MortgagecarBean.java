/**
 * @Copyright (c) http:www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2017年4月25日
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.query;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="haojinlong@iqianbang.com">haojinlong</a>
 */
public class MortgagecarBean extends BaseEntity {

    private static final long serialVersionUID = -9022751070049742549L;
    /** 订单id **/
    private String orderId;
    /** 用户名 **/
    private String realName;
    /** 手机号 **/
    private String regId;
    /** 订单金额 **/
    private BigDecimal orderAmt;
    /** 月利率 **/
    private double feeratio;
    /** 月供 **/
    private BigDecimal monthInterest;
    /** GPS流量费 **/
    private BigDecimal gpsTrafficFee;
    /** 期数 **/
    private int orderItems;
    /** 车帮服务费 **/
    private BigDecimal serviceCharge;
    /** 商户息差 **/
    private BigDecimal interestDiff;
    /** 费用总额 **/
    private BigDecimal feeTotal;
    /** 上标日期 **/
    private Date applyTime;
    /** 商户 **/
    private String merchantName;
    /** 支付状态 **/
    private String payStatus;
    /** 最迟还款日 **/
    private Date lastRepayDate;
    /** 商户编号 **/
    private String merchantNo;
    /** 还款顺序 **/
    private Integer repayNo;
    /** 还款状态 **/
    private Integer status;

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the realName
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realName the realName to set
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * @return the regId
     */
    public String getRegId() {
        return regId;
    }

    /**
     * @param regId the regId to set
     */
    public void setRegId(String regId) {
        this.regId = regId;
    }

    /**
     * @return the orderAmt
     */
    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    /**
     * @param orderAmt the orderAmt to set
     */
    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    /**
     * @return the feeratio
     */
    public double getFeeratio() {
        return feeratio;
    }

    /**
     * @param feeratio the feeratio to set
     */
    public void setFeeratio(double feeratio) {
        this.feeratio = feeratio;
    }

    /**
     * @return the monthInterest
     */
    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    /**
     * @param monthInterest the monthInterest to set
     */
    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    /**
     * @return the gpsTrafficFee
     */
    public BigDecimal getGpsTrafficFee() {
        return gpsTrafficFee;
    }

    /**
     * @param gpsTrafficFee the gpsTrafficFee to set
     */
    public void setGpsTrafficFee(BigDecimal gpsTrafficFee) {
        this.gpsTrafficFee = gpsTrafficFee;
    }

    /**
     * @return the orderItems
     */
    public int getOrderItems() {
        return orderItems;
    }

    /**
     * @param orderItems the orderItems to set
     */
    public void setOrderItems(int orderItems) {
        this.orderItems = orderItems;
    }

    /**
     * @return the serviceCharge
     */
    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    /**
     * @param serviceCharge the serviceCharge to set
     */
    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    /**
     * @return the interestDiff
     */
    public BigDecimal getInterestDiff() {
        return interestDiff;
    }

    /**
     * @param interestDiff the interestDiff to set
     */
    public void setInterestDiff(BigDecimal interestDiff) {
        this.interestDiff = interestDiff;
    }

    /**
     * @return the feeTotal
     */
    public BigDecimal getFeeTotal() {
        return feeTotal;
    }

    /**
     * @param feeTotal the feeTotal to set
     */
    public void setFeeTotal(BigDecimal feeTotal) {
        this.feeTotal = feeTotal;
    }

    /**
     * @return the applyTime
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * @param applyTime the applyTime to set
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * @return the merchantName
     */
    public String getMerchantName() {
        return merchantName;
    }

    /**
     * @param merchantName the merchantName to set
     */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /**
     * @return the payStatus
     */
    public String getPayStatus() {
        return payStatus;
    }

    /**
     * @param payStatus the payStatus to set
     */
    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * @return the lastRepayDate
     */
    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    /**
     * @param lastRepayDate the lastRepayDate to set
     */
    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    /**
     * @return the merchantNo
     */
    public String getMerchantNo() {
        return merchantNo;
    }

    /**
     * @param merchantNo the merchantNo to set
     */
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    /**
     * @return the repayNo
     */
    public Integer getRepayNo() {
        return repayNo;
    }

    /**
     * @param repayNo the repayNo to set
     */
    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

}
