/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午3:39:35
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.downpayment;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@SuppressWarnings("serial")
public class DownPaymentBean extends BaseEntity {

    private String orderId;// 订单号
    private String realName;// 客户姓名
    private String regId;// 手机号
    private String merchantShortName;// 门店名称（中文）
    private String sourcesFunding;// 资金来源
    private String fundingId;// 资金端上标ID号
    private String orderAmt;// 借款总额
    private String orderItems;// 总期数
    private String bizType;// 业务类型
    private String downPayment;// 首付款
    private String takePayment;// 上收月供
    private String feeAmount;// 上收息
    private String margin;// 保证金
    private String serviceFee;// 服务费
    private String cZF;// 充值费（目前线下收取，暂时显示为0就可以）
    private String repayDate;// 实际支付日期
    private String riskStatus;// 订单状态
    private String monthInterest;// 月供
    private String sumAmt;// 首付款总额

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

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getSourcesFunding() {
        return sourcesFunding;
    }

    public void setSourcesFunding(String sourcesFunding) {
        this.sourcesFunding = sourcesFunding;
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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(String downPayment) {
        this.downPayment = downPayment;
    }

    public String getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(String takePayment) {
        this.takePayment = takePayment;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getcZF() {
        return cZF;
    }

    public void setcZF(String cZF) {
        this.cZF = cZF;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getFundingId() {
        return fundingId;
    }

    public void setFundingId(String fundingId) {
        this.fundingId = fundingId;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(String monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getSumAmt() {
        return sumAmt;
    }

    public void setSumAmt(String sumAmt) {
        this.sumAmt = sumAmt;
    }

}
