package com.iqb.consumer.data.layer.bean.carowner;

import java.math.BigDecimal;
import java.util.List;

import com.iqb.consumer.data.layer.bean.order.pojo.InstdetailChatFPojo;

public class CarOwnerBreakBillChatFRequestPojo {

    private String orderId;
    private String regId;
    private String orderDate;// 订单时间 20161217
    private String beginDate;// 分期时间 20170809
    private String openId;// 10301;
    private String merchantNo;// 商户号
    private BigDecimal contractAmt;// 核准金额 元
    private BigDecimal installSumAmt;// 核准金额 元
    private BigDecimal installAmt;// 实际分期金额 元
    private Integer takeMonth;// 是否上收月供 1 是2 否 计划表获得
    private Integer installTerms;// 分期期数 计算
    private List<InstdetailChatFPojo> instDetails;// 分期详情规则
    private Integer planId;// 计划 Id

    private final String takePaymentAmt = "0";//
    private final String takePayment = "0";//
    private final String otherAmt = "";
    private final String takeInterest = "2";
    private final String interestAmt = "";
    private final Integer sourcesFunding = 6;
    private final Integer fundId = 6;

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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
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

    public BigDecimal getInstallAmt() {
        return installAmt;
    }

    public void setInstallAmt(BigDecimal installAmt) {
        this.installAmt = installAmt;
    }

    public Integer getTakeMonth() {
        return takeMonth;
    }

    public void setTakeMonth(Integer takeMonth) {
        this.takeMonth = takeMonth;
    }

    public Integer getInstallTerms() {
        return installTerms;
    }

    public void setInstallTerms(Integer installTerms) {
        this.installTerms = installTerms;
    }

    public List<InstdetailChatFPojo> getInstDetails() {
        return instDetails;
    }

    public void setInstDetails(List<InstdetailChatFPojo> instDetails) {
        this.instDetails = instDetails;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getTakePaymentAmt() {
        return takePaymentAmt;
    }

    public String getTakePayment() {
        return takePayment;
    }

    public String getOtherAmt() {
        return otherAmt;
    }

    public String getTakeInterest() {
        return takeInterest;
    }

    public String getInterestAmt() {
        return interestAmt;
    }

    public Integer getSourcesFunding() {
        return sourcesFunding;
    }

    public Integer getFundId() {
        return fundId;
    }

}
