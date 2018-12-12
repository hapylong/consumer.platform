/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月16日 上午10:24:42
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.plan;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class PlanBean extends BaseEntity {
    private String planShortName;// 计划简称
    private String planFullName;// 计划全称
    private String merchantNo;// 商户号
    private double downPaymentRatio;// 首付比例
    private double serviceFeeRatio;// 服务费比例
    private double marginRatio;// 保证金比例
    private double feeRatio;// 费率比例
    private int feeYear;// 上收年限
    private int takePayment;// 是否上收月供
    private int installPeriods;// 分期期数
    private String remark;// 自定义描述
    private int planId;// 账户系统计划ID
    private String planName;// 账务系统对应的计划名称
    private String bizType;// 业务类型
    private String bizTypeName;// 业务类型名称
    private double reCharge;// 充值费率
    private String status;// 计划状态
    private Integer greenChannel;// 绿色通道
    private double upInterestFee; // 上收息
    private double floatMarginRatio; // 上浮保证金比例
    private int floatOrNot;// 是否上浮
    private double floatServiceFeeRatio;// 上浮服务费比例
    private double collectServiceFeeRatio;// 代收服务费

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getInstallPeriods() {
        return installPeriods;
    }

    public void setInstallPeriods(int installPeriods) {
        this.installPeriods = installPeriods;
    }

    public String getPlanShortName() {
        return planShortName;
    }

    public int getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(int takePayment) {
        this.takePayment = takePayment;
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

    public double getDownPaymentRatio() {
        return downPaymentRatio;
    }

    public void setDownPaymentRatio(double downPaymentRatio) {
        this.downPaymentRatio = downPaymentRatio;
    }

    public double getServiceFeeRatio() {
        return serviceFeeRatio;
    }

    public void setServiceFeeRatio(double serviceFeeRatio) {
        this.serviceFeeRatio = serviceFeeRatio;
    }

    public double getMarginRatio() {
        return marginRatio;
    }

    public void setMarginRatio(double marginRatio) {
        this.marginRatio = marginRatio;
    }

    public double getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(double feeRatio) {
        this.feeRatio = feeRatio;
    }

    public int getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(int feeYear) {
        this.feeYear = feeYear;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public double getReCharge() {
        return reCharge;
    }

    public void setReCharge(double reCharge) {
        this.reCharge = reCharge;
    }

    public Integer getGreenChannel() {
        return greenChannel;
    }

    public void setGreenChannel(Integer greenChannel) {
        this.greenChannel = greenChannel;
    }

    public double getUpInterestFee() {
        return upInterestFee;
    }

    public void setUpInterestFee(double upInterestFee) {
        this.upInterestFee = upInterestFee;
    }

    public double getFloatMarginRatio() {
        return floatMarginRatio;
    }

    public void setFloatMarginRatio(double floatMarginRatio) {
        this.floatMarginRatio = floatMarginRatio;
    }

    public int getFloatOrNot() {
        return floatOrNot;
    }

    public void setFloatOrNot(int floatOrNot) {
        this.floatOrNot = floatOrNot;
    }

    public double getFloatServiceFeeRatio() {
        return floatServiceFeeRatio;
    }

    public void setFloatServiceFeeRatio(double floatServiceFeeRatio) {
        this.floatServiceFeeRatio = floatServiceFeeRatio;
    }

    public double getCollectServiceFeeRatio() {
        return collectServiceFeeRatio;
    }

    public void setCollectServiceFeeRatio(double collectServiceFeeRatio) {
        this.collectServiceFeeRatio = collectServiceFeeRatio;
    }

    public void copy(PlanBean pb) {
        bizType = pb.getBizType();
        bizTypeName = pb.getBizTypeName();
        createTime = pb.getCreateTime();
        downPaymentRatio = pb.getDownPaymentRatio();
        feeRatio = pb.getFeeRatio();
        greenChannel = pb.getGreenChannel();
        id = pb.getId();
        installPeriods = pb.getInstallPeriods();
        marginRatio = pb.getMarginRatio();
        merchantNo = pb.getMerchantNo();
        planFullName = pb.getPlanFullName();
        planId = pb.getPlanId();
        planName = pb.getPlanName();
        planShortName = pb.getPlanShortName();
        reCharge = pb.getReCharge();
        remark = pb.getRemark();
        serviceFeeRatio = pb.getServiceFeeRatio();
        status = pb.getStatus();
        takePayment = pb.getTakePayment();
        updateTime = pb.getUpdateTime();
        version = pb.getVersion();
        upInterestFee = pb.getUpInterestFee();
        floatMarginRatio = pb.getFloatMarginRatio();
        floatOrNot = pb.floatOrNot;
        floatServiceFeeRatio = pb.getFloatServiceFeeRatio();
        collectServiceFeeRatio = pb.getCollectServiceFeeRatio();
    }

}
