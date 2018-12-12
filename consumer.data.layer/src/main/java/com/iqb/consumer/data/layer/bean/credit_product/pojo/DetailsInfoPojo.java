package com.iqb.consumer.data.layer.bean.credit_product.pojo;

import java.math.BigDecimal;

import com.github.pagehelper.StringUtil;

public class DetailsInfoPojo {

    private String merchantShortName; // 机构名称
    private String realName; // user
    private String regId; // 手机号
    private String orderId; // *X
    private String bizType; //
    private BigDecimal orderAmt; // 借款金额
    private String planFullName; // 计划名称", plan
    private Byte orderItems; // 期数
    private Double fee; // 月利率
    private BigDecimal monthInterest; // 月供
    private String planShortName;

    private BigDecimal margin; // 保证金
    private BigDecimal serviceFee; // 服务费
    private BigDecimal contractAmt; // 合同金额
    private BigDecimal loanAmt; // 放款金额
    private BigDecimal sbAmt; // 上标金额
    private Integer preAmtStatus;

    private BigDecimal gpsAmt;// GPS费用
    private BigDecimal riskAmt;// 支付保险
    private BigDecimal taxAmt;// 购置税
    private BigDecimal serverAmt;// 车商服务费
    private BigDecimal otherAmt;// 其他费用

    public BigDecimal getGpsAmt() {
        return gpsAmt == null ? BigDecimal.ZERO : this.gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public BigDecimal getRiskAmt() {
        return riskAmt == null ? BigDecimal.ZERO : this.riskAmt;
    }

    public void setRiskAmt(BigDecimal riskAmt) {
        this.riskAmt = riskAmt;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt == null ? BigDecimal.ZERO : this.taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public BigDecimal getServerAmt() {
        return serverAmt == null ? BigDecimal.ZERO : this.serverAmt;
    }

    public void setServerAmt(BigDecimal serverAmt) {
        this.serverAmt = serverAmt;
    }

    public BigDecimal getOtherAmt() {
        return otherAmt == null ? BigDecimal.ZERO : this.otherAmt;
    }

    public void setOtherAmt(BigDecimal otherAmt) {
        this.otherAmt = otherAmt;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getPlanFullName() {
        return planFullName;
    }

    public void setPlanFullName(String planFullName) {
        this.planFullName = planFullName;
    }

    public Byte getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Byte orderItems) {
        this.orderItems = orderItems;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getPlanShortName() {
        return planShortName;
    }

    public void setPlanShortName(String planShortName) {
        this.planShortName = planShortName;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
    }

    public BigDecimal getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(BigDecimal loanAmt) {
        this.loanAmt = loanAmt;
    }

    public BigDecimal getSbAmt() {
        return sbAmt;
    }

    public void setSbAmt(BigDecimal sbAmt) {
        this.sbAmt = sbAmt;
    }

    public Integer getPreAmtStatus() {
        return preAmtStatus;
    }

    public void setPreAmtStatus(Integer preAmtStatus) {
        this.preAmtStatus = preAmtStatus;
    }

    public boolean checkPojo() {
        if (StringUtil.isEmpty(merchantShortName)
                || StringUtil.isEmpty(realName) || StringUtil.isEmpty(regId)
                || StringUtil.isEmpty(orderId) || StringUtil.isEmpty(bizType)) {
            return false;
        }
        return true;
    }

}
