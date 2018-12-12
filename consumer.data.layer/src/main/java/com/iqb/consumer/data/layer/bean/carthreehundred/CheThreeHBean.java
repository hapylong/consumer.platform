package com.iqb.consumer.data.layer.bean.carthreehundred;

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
 * 2018年8月10日上午10:35:25 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class CheThreeHBean {
    /** 订单号 **/
    private String orderId;
    /** 借款人姓名 **/
    private String realName;
    /** 借款人手机号 **/
    private String regId;
    /** 借款人身份证号 **/
    private String idNo;
    /** 借款人银行卡号 **/
    private String bankCardNo;
    /** 业务类型 **/
    private String bizType;
    /** 借款人所在城市 **/
    private String city;
    /** 订单名称 **/
    private String orderName;
    /** 订单金额 **/
    private BigDecimal orderAmt;
    /** 车架号 **/
    private String carNo;
    /** 发动机号 **/
    private String engine;
    /** 车牌号 **/
    private String plate;
    /** 初次上牌时间 **/
    private String firstRegDate;
    /** 行驶里程 **/
    private int mileage;
    /** 贷款期数 **/
    private int orderItems;
    /** 首付款 **/
    private BigDecimal preAmt;
    /** 月供 **/
    private BigDecimal monthInterest;
    /** 尾款 **/
    private BigDecimal balancePayment;
    /** 第一期账单还款时间 **/
    private String lastRepayDate;
    /** 放款日期 **/
    private String loanDate;
    /** 申请日期 **/
    private String applyDate;
    /** 首次还款时间 **/
    private String firstPaymentDate;
    /** 贷款开始时间 **/
    private String startLoanDate;
    /** 贷款结束时间 **/
    private String endLoanDate;

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

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getFirstRegDate() {
        return firstRegDate;
    }

    public void setFirstRegDate(String firstRegDate) {
        this.firstRegDate = firstRegDate;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(int orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public BigDecimal getBalancePayment() {
        return balancePayment;
    }

    public void setBalancePayment(BigDecimal balancePayment) {
        this.balancePayment = balancePayment;
    }

    public String getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(String lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(String firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public String getStartLoanDate() {
        return startLoanDate;
    }

    public void setStartLoanDate(String startLoanDate) {
        this.startLoanDate = startLoanDate;
    }

    public String getEndLoanDate() {
        return endLoanDate;
    }

    public void setEndLoanDate(String endLoanDate) {
        this.endLoanDate = endLoanDate;
    }

}
