package com.iqb.consumer.service.layer.api.dto;

import java.math.BigDecimal;

public class CarAssetDto {

    private BigDecimal orderAmt;// 订单金额
    private String orderName;// 订单名称
    private int planId;// 计划ID
    private String merchantNo;// 商户号
    private String bizType;// 业务类型
    private String regId;// 手机号
    private String realName;// 真实姓名
    private String idCardNo;// 身份证
    private String bankCardNo;// 银行卡
    private String riskInfo;// 风控信息
    private String asynnotifyUrl;// 异步通知地址

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
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

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getRiskInfo() {
        return riskInfo;
    }

    public void setRiskInfo(String riskInfo) {
        this.riskInfo = riskInfo;
    }

    public String getAsynnotifyUrl() {
        return asynnotifyUrl;
    }

    public void setAsynnotifyUrl(String asynnotifyUrl) {
        this.asynnotifyUrl = asynnotifyUrl;
    }
}
