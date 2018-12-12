/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月14日 上午11:29:35
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.wf;

import java.math.BigDecimal;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class CombinationQueryBean {

    // 订单信息为主体
    private String orderId;// 订单id
    private String orderName;// 订单名称
    private int takePayment;// 是否上收月供 1:上收； 0：不上收
    private Integer riskStatus;// 风控状态
    private double preAmt;// 预付款金额
    private String receivedPreAmt;// 已收到的预付款
    private Integer preAmtStatus;// 预付款状态
    private String orderAmt;// 订单金额
    private String orderItems;// 订单分期
    private String assessPrice;// 估价
    private String downPayment; // 首付款
    private String serviceFee;// 服务费
    private BigDecimal monthInterest;// 月供
    private String planId;// 计划id
    private int chargeWay;// 收取方式默认线上收取0,线下收取1
    private BigDecimal feeAmount;// 上收金额、上收息
    private String margin; // 保证金
    private Integer bizType;// 业务类型
    private BigDecimal gpsAmt;// gps 金额
    private BigDecimal gpsTrafficFee;// gps 流量费
    private String merchName;// 商户名称
    private String merchantNo;// 商户名称
    private BigDecimal applyAmt;// 申请金额

    private String realName;// 姓名
    private String regId;
    private String planFullName;// 计划全称
    private String greenChannel;// 是否绿色通道
    private String guarantee;// 担保人
    private String guaranteeName;// 担保人法人姓名
    /** gps备注 **/
    private String gpsRemark;
    /** 到账金额 **/
    private String receiveAmt;
    /** 备注 **/
    private String remark;
    private String riskRetRemark;

    private String amtLines; // 价格区间
    private int useCreditLoan; // 是否使用信用贷 1是 0 否

    private Integer backFlag; // 是否退回

    public String getReceivedPreAmt() {
        return receivedPreAmt;
    }

    public void setReceivedPreAmt(String receivedPreAmt) {
        this.receivedPreAmt = receivedPreAmt;
    }

    public String getGreenChannel() {
        return greenChannel;
    }

    public void setGreenChannel(String greenChannel) {
        this.greenChannel = greenChannel;
    }

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public String getGpsRemark() {
        return gpsRemark;
    }

    public void setGpsRemark(String gpsRemark) {
        this.gpsRemark = gpsRemark;
    }

    public String getReceiveAmt() {
        return receiveAmt;
    }

    public void setReceiveAmt(String receiveAmt) {
        this.receiveAmt = receiveAmt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(int takePayment) {
        this.takePayment = takePayment;
    }

    public Integer getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Integer riskStatus) {
        this.riskStatus = riskStatus;
    }

    public double getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(double preAmt) {
        this.preAmt = preAmt;
    }

    public Integer getPreAmtStatus() {
        return preAmtStatus;
    }

    public void setPreAmtStatus(Integer preAmtStatus) {
        this.preAmtStatus = preAmtStatus;
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

    public String getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(String assessPrice) {
        this.assessPrice = assessPrice;
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

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public int getChargeWay() {
        return chargeWay;
    }

    public void setChargeWay(int chargeWay) {
        this.chargeWay = chargeWay;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
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

    public String getPlanFullName() {
        return planFullName;
    }

    public void setPlanFullName(String planFullName) {
        this.planFullName = planFullName;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public String getGuaranteeName() {
        return guaranteeName;
    }

    public void setGuaranteeName(String guaranteeName) {
        this.guaranteeName = guaranteeName;
    }

    public String getAmtLines() {
        return amtLines;
    }

    public void setAmtLines(String amtLines) {
        this.amtLines = amtLines;
    }

    public int getUseCreditLoan() {
        return useCreditLoan;
    }

    public void setUseCreditLoan(int useCreditLoan) {
        this.useCreditLoan = useCreditLoan;
    }

    public String getRiskRetRemark() {
        return riskRetRemark;
    }

    public void setRiskRetRemark(String riskRetRemark) {
        this.riskRetRemark = riskRetRemark;
    }

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public BigDecimal getGpsTrafficFee() {
        return gpsTrafficFee;
    }

    public void setGpsTrafficFee(BigDecimal gpsTrafficFee) {
        this.gpsTrafficFee = gpsTrafficFee;
    }

    public Integer getBackFlag() {
        return backFlag;
    }

    public void setBackFlag(Integer backFlag) {
        this.backFlag = backFlag;
    }
}
