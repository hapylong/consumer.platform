package com.iqb.consumer.asset.allocation.assetallocine.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class AssetDivisionPojo {
    /** jys_packinfo **/
    private String orderId; // 资产包编号 -> orderId
    private Date beginInterestDate; // 起息日
    private String raiseInstitutions; // 募集机构
    private String guaranteeInstitution;// 担保机构
    private String remark; // 备注

    /** jys_orderinfo **/
    private long id;
    private BigDecimal orderAmt; // 订单金额
    private Date expireDate; // 到期日
    private Double fee; // 费率
    private Short orderItems; // 订单期数
    private String bizType;
    private long packageId;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getBeginInterestDate() {
        return beginInterestDate;
    }

    public void setBeginInterestDate(Date beginInterestDate) {
        this.beginInterestDate = beginInterestDate;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Short getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Short orderItems) {
        this.orderItems = orderItems;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

}
