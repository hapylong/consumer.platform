/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: 订单表
 * @date 2016年12月5日 下午6:44:26
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.jys;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.bean.user.UserBean;

/**
 * Description: 交易所订单类
 */
@SuppressWarnings("serial")
public class JYSOrderBean extends BaseEntity {
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
    private Integer riskRetRemark;
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
    private BigDecimal takePayment;
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
    private double fee;
    /** 利息+本金总和 **/
    private BigDecimal sumMoney;
    private BigDecimal applyAmt;// 申请金额
    private BigDecimal gpsAmt;// GPS价格
    private int wfStatus;
    /* 合同状态 */
    private int contractStatus;// 合同签约状态(1，待签约 2，已签约)
    private Date expireDate; // 到期日
    private String planShortName;// 计划简称
    private String realName;// 真实姓名
    private String batchNo;// 批次号
    private String procOrgCode;// 当前热本文由流程ID
    private Date upperTime;// 上标时间

    private String creditName;// 债权人姓名
    private String creditCardNo;// 债权人身份证号
    private String creditBankCard;// 债权人银行卡号
    private String creditBank;// 债权人开户银行
    private String creditPhone;// 债权人手机号
    private long creditorId;// 债权人id

    // 修改by chengzhen 2018年3月6日 16:10:29
    private String cfRequestMoneyId;// 对应保存过后的cf_requestmoney表ID

    public String getCfRequestMoneyId() {
        return cfRequestMoneyId;
    }

    public void setCfRequestMoneyId(String cfRequestMoneyId) {
        this.cfRequestMoneyId = cfRequestMoneyId;
    }

    public String getProcOrgCode() {
        return procOrgCode;
    }

    public void setProcOrgCode(String procOrgCode) {
        this.procOrgCode = procOrgCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getPlanShortName() {
        return planShortName;
    }

    public void setPlanShortName(String planShortName) {
        this.planShortName = planShortName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
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

    public Integer getRiskRetRemark() {
        return riskRetRemark;
    }

    public void setRiskRetRemark(Integer riskRetRemark) {
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

    public BigDecimal getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(BigDecimal takePayment) {
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

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
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

    public Date getUpperTime() {
        return upperTime;
    }

    public void setUpperTime(Date upperTime) {
        this.upperTime = upperTime;
    }

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getCreditBankCard() {
        return creditBankCard;
    }

    public void setCreditBankCard(String creditBankCard) {
        this.creditBankCard = creditBankCard;
    }

    public String getCreditBank() {
        return creditBank;
    }

    public void setCreditBank(String creditBank) {
        this.creditBank = creditBank;
    }

    public String getCreditPhone() {
        return creditPhone;
    }

    public void setCreditPhone(String creditPhone) {
        this.creditPhone = creditPhone;
    }

    public long getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(long creditorId) {
        this.creditorId = creditorId;
    }

}
