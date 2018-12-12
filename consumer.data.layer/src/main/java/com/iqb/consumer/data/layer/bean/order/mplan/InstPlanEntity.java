package com.iqb.consumer.data.layer.bean.order.mplan;

import java.util.Date;

import javax.persistence.Table;

@Table(name = "inst_plan")
public class InstPlanEntity {
    private Long id;
    private String orgCode;
    private Integer days; // 天/年
    private Integer version;
    private Date createTime;
    private Date updateTime;

    private String planShortName;// 计划简称
    private String planFullName;// 计划全称
    private String merchantNo;// 商户号
    private Double downPaymentRatio;// 首付比例
    private Double serviceFeeRatio;// 服务费比例
    private Double marginRatio;// 保证金比例
    private Double feeRatio;// 费率比例
    private Integer feeYear;// 上收年限
    private Integer takePayment;// 是否上收月供
    private Integer installPeriods;// 分期期数
    private String remark;// 自定义描述
    private Integer planId;// 账户系统计划ID

    private String bizType;// 业务类型

    private Double reCharge;// 充值费率
    private Integer status;// 计划状态
    private Integer greenChannel;// 绿色通道
    private Double upInterestFee; // 上收息

    /*
     * private String bizTypeName;// 业务类型名称 private String planName;// 账务系统对应的计划名称
     */

    public boolean checkEntity() {
        if (marginRatio != null && takePayment != null) {
            planFullName = "保证金比例  " + marginRatio + ";" + (takePayment == 1 ? "上收月供" : "");
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPlanShortName() {
        return planShortName;
    }

    public void setPlanShortName(String planShortName) {
        this.planShortName = planShortName;
    }

    public String getPlanFullName() {
        return planFullName;
    }

    public void setPlanFullName(String planFullName) {
        this.planFullName = planFullName;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Double getDownPaymentRatio() {
        return downPaymentRatio;
    }

    public void setDownPaymentRatio(Double downPaymentRatio) {
        this.downPaymentRatio = downPaymentRatio;
    }

    public Double getServiceFeeRatio() {
        return serviceFeeRatio;
    }

    public void setServiceFeeRatio(Double serviceFeeRatio) {
        this.serviceFeeRatio = serviceFeeRatio;
    }

    public Double getMarginRatio() {
        return marginRatio;
    }

    public void setMarginRatio(Double marginRatio) {
        this.marginRatio = marginRatio;
    }

    public Double getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(Double feeRatio) {
        this.feeRatio = feeRatio;
    }

    public Integer getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(Integer feeYear) {
        this.feeYear = feeYear;
    }

    public Integer getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(Integer takePayment) {
        this.takePayment = takePayment;
    }

    public Integer getInstallPeriods() {
        return installPeriods;
    }

    public void setInstallPeriods(Integer installPeriods) {
        this.installPeriods = installPeriods;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public Double getReCharge() {
        return reCharge;
    }

    public void setReCharge(Double reCharge) {
        this.reCharge = reCharge;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGreenChannel() {
        return greenChannel;
    }

    public void setGreenChannel(Integer greenChannel) {
        this.greenChannel = greenChannel;
    }

    public Double getUpInterestFee() {
        return upInterestFee;
    }

    public void setUpInterestFee(Double upInterestFee) {
        this.upInterestFee = upInterestFee;
    }

    public void createFullName() {
        if (marginRatio != null && takePayment != null) {
            planFullName = "保证金比例  " + marginRatio + ";" + (takePayment == 1 ? "上收月供" : "");
        }
    }

}
