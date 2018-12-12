/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: 订单表
 * @date 2016年12月5日 下午6:44:26
 * @version V1.0
 */
package com.iqb.consumer.batch.data.pojo;

import java.math.BigDecimal;

/**
 * 
 * Description: 订单信息(包含工作流)
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年12月14日    wangxinbang       1.0        1.0 Version
 *          </pre>
 */
@SuppressWarnings("serial")
public class OrderBean extends BaseEntity {
    /** 订单号 **/
    private String orderId;
    /** 关联用户Id **/
    private String userId;
    /** 注册号 **/
    private String regId;
    /** 商户号 **/
    private String merchantNo;
    /**
     * 业务类型(2001 以租代售新车 2002以租代售二手车 2100 抵押车 2200 质押车 1100 易安家 1000 医美 1200 旅游)
     **/
    private String bizType;
    /** 订单名称 **/
    private String orderName;
    /** 订单金额 **/
    private String orderAmt;
    /** 订单期数 **/
    private String orderItems;
    /** 订单备注 **/
    private String orderRemark;
    /** 预付金 **/
    private String preAmt;
    /** 预付金支付状态(0：未支付，1：已支付) **/
    private String preAmtStatus;
    /** 状态(0 未生效订单,1 生效订单) **/
    private String status;
    /** 风控状态(0,1,2,3,4) **/
    private Integer riskStatus;
    /** 风控返回备注 **/
    private String riskRetRemark;
    /** 是否退款标识 **/
    private Integer refundFlag;
    // 评估价格
    private String assessPrice;
    /** 保证金(该字段需要退款金额) **/
    private String margin;
    /** 首付款 **/
    private String downPayment;
    /** 服务费 **/
    private String serviceFee;
    /** 分期计划id **/
    private String planId;
    /** 二维码id **/
    private String qrCodeId;
    /** ? **/
    private int takePayment;
    /** 年费 **/
    private int feeYear;
    /** ? **/
    private BigDecimal feeAmount;
    /** 月利率 **/
    private BigDecimal monthInterest;
    /** 收费方式 **/
    private int chargeWay = 0;
    /** 启动流程的流程实例ID **/
    private String procInstId;
    /** 费率 **/
    private int fee;
    /** 利息+本金总和 **/
    private BigDecimal sumMoney;
    private BigDecimal applyAmt;// 申请金额
    private BigDecimal gpsAmt;// GPS价格
    private int wfStatus;
    /* 合同状态 */
    private int contractStatus;// 合同签约状态(1，待签约 2，已签约)

    private String realName;// 姓名
    private String merchantFullName;// 商户号

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMerchantFullName() {
        return merchantFullName;
    }

    public void setMerchantFullName(String merchantFullName) {
        this.merchantFullName = merchantFullName;
    }

    public int getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(int wfStatus) {
        this.wfStatus = wfStatus;
    }

    public BigDecimal getSumMoney() {
        return sumMoney;
    }

    public String getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(String assessPrice) {
        this.assessPrice = assessPrice;
    }

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public OrderOtherInfo getOrderOtherInfo() {
        return orderOtherInfo;
    }

    public void setOrderOtherInfo(OrderOtherInfo orderOtherInfo) {
        this.orderOtherInfo = orderOtherInfo;
    }

    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }

    private OrderOtherInfo orderOtherInfo;
    /** 用户信息 **/
    private UserBean userBean;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(String preAmt) {
        this.preAmt = preAmt;
    }

    public String getPreAmtStatus() {
        return preAmtStatus;
    }

    public void setPreAmtStatus(String preAmtStatus) {
        this.preAmtStatus = preAmtStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Integer riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getRiskRetRemark() {
        return riskRetRemark;
    }

    public void setRiskRetRemark(String riskRetRemark) {
        this.riskRetRemark = riskRetRemark;
    }

    public Integer getRefundFlag() {
        return refundFlag;
    }

    public void setRefundFlag(Integer refundFlag) {
        this.refundFlag = refundFlag;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(String downPayment) {
        this.downPayment = downPayment;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public int getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(int takePayment) {
        this.takePayment = takePayment;
    }

    public int getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(int feeYear) {
        this.feeYear = feeYear;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public int getChargeWay() {
        return chargeWay;
    }

    public void setChargeWay(int chargeWay) {
        this.chargeWay = chargeWay;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public int getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(int contractStatus) {
        this.contractStatus = contractStatus;
    }

}
