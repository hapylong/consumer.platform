package com.iqb.consumer.asset.allocation.assetinfo.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class AssetInfoFormBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    // order
    private String orderId;// 订单id
    private String orderName;// 项目名称
    private String orderAmt;// 订单金额
    private String orderItems;// 订单分期
    private String orderTime;// 订单申请日期
    private String orderNo;// 订单号
    private String guarantee;// 担保公司
    private String guaranteeName;// 担保法人
    private String margin; // 保证金
    private String downPayment; // 首付款
    private String serviceFee;// 服务费
    private int chargeWay = 0;// 收取方式默认线上收取0,线下收取1
    private BigDecimal feeAmount;// 上收金额、上收息
    private BigDecimal monthInterest;// 月供

    // user
    private String realName;// 实际借款人姓名
    private String idCard;// 实际借款人身份证号
    private String acctMobile;// 实际借款人手机号

    // cf_creditorinfo
    private String creditName;// 债权人
    private String creditCardNo;// 债权人身份证号
    private String creditBankCard;// 债权人银行卡号
    private String creditBank;// 债权人开户行
    private String creditPhone;// 债权人手机号

    // fqzf_merchant_user
    private String merchName;// 机构名称
    private String contacts;// 姓名
    private String riskType;// 机构类型
    private String merchPhone;// 联系方式

    private String productPlan;// 产品方案
    private String APPLYITEMS;// 申请期数
    private String LEFTITEMS;// 剩余期数
    private String preAmount;//
    private String merchantId; // 商户id

    private String curRepayNo;// 当前期数
    private String pushMode;// 推标金额方式 1 按订单金额 2 按剩余未还本金

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

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAcctMobile() {
        return acctMobile;
    }

    public void setAcctMobile(String acctMobile) {
        this.acctMobile = acctMobile;
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

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public String getMerchPhone() {
        return merchPhone;
    }

    public void setMerchPhone(String merchPhone) {
        this.merchPhone = merchPhone;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductPlan() {
        return productPlan;
    }

    public void setProductPlan(String productPlan) {
        this.productPlan = productPlan;
    }

    public String getAPPLYITEMS() {
        return APPLYITEMS;
    }

    public void setAPPLYITEMS(String aPPLYITEMS) {
        APPLYITEMS = aPPLYITEMS;
    }

    public String getLEFTITEMS() {
        return LEFTITEMS;
    }

    public void setLEFTITEMS(String lEFTITEMS) {
        LEFTITEMS = lEFTITEMS;
    }

    public String getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(String preAmount) {
        this.preAmount = preAmount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCurRepayNo() {
        return curRepayNo;
    }

    public void setCurRepayNo(String curRepayNo) {
        this.curRepayNo = curRepayNo;
    }

    public String getPushMode() {
        return pushMode;
    }

    public void setPushMode(String pushMode) {
        this.pushMode = pushMode;
    }

}
