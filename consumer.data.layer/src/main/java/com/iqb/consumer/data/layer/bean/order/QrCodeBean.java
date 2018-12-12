/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月16日 上午10:25:03
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.order;

import java.math.BigDecimal;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class QrCodeBean extends BaseEntity {

    private long planId;// 计划ID
    private String projectName;// 项目名称
    private String projectDetail;// 项目详情
    private String merchantNo;// 商户号
    private BigDecimal installAmount;// 分期总金额
    private int installPeriods;// 分期期数
    private BigDecimal downPayment;// 首付金额
    private BigDecimal serviceFee;// 服务费金额
    private BigDecimal margin;// 保证金金额
    private BigDecimal fee;// 首付金额
    private String remark;// 备注
    private String imgName;// 二维码名称

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDetail() {
        return projectDetail;
    }

    public void setProjectDetail(String projectDetail) {
        this.projectDetail = projectDetail;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public BigDecimal getInstallAmount() {
        return installAmount;
    }

    public void setInstallAmount(BigDecimal installAmount) {
        this.installAmount = installAmount;
    }

    public int getInstallPeriods() {
        return installPeriods;
    }

    public void setInstallPeriods(int installPeriods) {
        this.installPeriods = installPeriods;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

}
