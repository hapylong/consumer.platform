package com.iqb.consumer.asset.allocation.assetallocine.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class AssetBreakDetailsInfoResponsePojo {

    /** jys_packinfo **/
    private String raiseInstitutions; // 募捐机构
    private String guaranteeInstitution; // 担保机构

    /** jys_orderinfo **/
    private String orderId;
    private String regId; // 手机号
    private BigDecimal margin;// 保证金
    private String bizType;// 业务类型
    private Date expireDate; // 到期日
    private Integer orderItems;// 期数
    private Long planId; // 分期方案ID
    private Date createtime; // 订单时间
    private String proName; // 订单名称 orderName
    private Integer riskStatus;

    /** jys_breakorderinfo **/
    private String recordNum; // 交易所备案编号
    private String delistingMechanism;// 摘牌机构
    private BigDecimal orderAmt; // 订单金额

    /** inst_plan **/
    private String planShortName; // 方案名称
    private Double feeratio; // 年化利率 = fee * 12
    private Double recharge; // 通道费率

    /** jys_assetallocation **/
    private Integer channel; // 资金来源
    private String bakOrgan; // 交易所（全称）
    private String url;// 挂牌链接
    private Date paymentDate; // 付息日 createTime As paymentDate

    /** jys_user **/
    private String bankName;// 开户行
    private String bankCardNo;// 银行卡号
    private String realName; // userId 和 user表 关联
    private String idNo; // userId 和 user表关联

    /** inst_merchantinfo **/
    private String merchantNo; // 商户

    /** inst_inst_orderotherinfo **/
    private String projectName1;// 项目名称

    public String getProjectName1() {
        return projectName1;
    }

    public void setProjectName1(String projectName1) {
        this.projectName1 = projectName1;
    }

    public String getRaiseInstitutions() {
        return raiseInstitutions;
    }

    public void setRaiseInstitutions(String raiseInstitutions) {
        this.raiseInstitutions = raiseInstitutions;
    }

    public String getGuaranteeInstitution() {
        return guaranteeInstitution;
    }

    public void setGuaranteeInstitution(String guaranteeInstitution) {
        this.guaranteeInstitution = guaranteeInstitution;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public String getBakOrgan() {
        return bakOrgan;
    }

    public void setBakOrgan(String bakOrgan) {
        this.bakOrgan = bakOrgan;
    }

    public String getDelistingMechanism() {
        return delistingMechanism;
    }

    public void setDelistingMechanism(String delistingMechanism) {
        this.delistingMechanism = delistingMechanism;
    }

    public String getPlanShortName() {
        return planShortName;
    }

    public void setPlanShortName(String planShortName) {
        this.planShortName = planShortName;
    }

    public Double getFeeratio() {
        return feeratio;
    }

    public void setFeeratio(Double feeratio) {
        this.feeratio = feeratio;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Double getRecharge() {
        return recharge;
    }

    public void setRecharge(Double recharge) {
        this.recharge = recharge;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Integer getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Integer riskStatus) {
        this.riskStatus = riskStatus;
    }

}
