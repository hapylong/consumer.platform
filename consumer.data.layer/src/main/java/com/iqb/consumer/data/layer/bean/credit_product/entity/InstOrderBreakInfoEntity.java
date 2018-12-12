package com.iqb.consumer.data.layer.bean.credit_product.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.github.pagehelper.StringUtil;

@Table(name = "inst_orderbreakinfo")
public class InstOrderBreakInfoEntity {
    private long id;
    private String orderId;
    private BigDecimal carAmt;
    private BigDecimal gpsAmt;
    private BigDecimal insAmt;
    private BigDecimal taxAmt;
    private BigDecimal otherAmt;
    private BigDecimal businessTaxAmt;
    private Integer version;
    private Date createTime;
    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getCarAmt() {
        return carAmt;
    }

    public void setCarAmt(BigDecimal carAmt) {
        this.carAmt = carAmt;
    }

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public BigDecimal getInsAmt() {
        return insAmt;
    }

    public void setInsAmt(BigDecimal insAmt) {
        this.insAmt = insAmt;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public BigDecimal getOtherAmt() {
        return otherAmt;
    }

    public void setOtherAmt(BigDecimal otherAmt) {
        this.otherAmt = otherAmt;
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

    public BigDecimal getBusinessTaxAmt() {
        return businessTaxAmt;
    }

    public void setBusinessTaxAmt(BigDecimal businessTaxAmt) {
        this.businessTaxAmt = businessTaxAmt;
    }

    public boolean checkEntity() {
        if (StringUtil.isEmpty(orderId) ||
                carAmt == null ||
                gpsAmt == null ||
                insAmt == null ||
                taxAmt == null ||
                otherAmt == null) {
            return false;
        }
        return true;
    }

    public void updateEntity(BigDecimal carAmt) {
        this.carAmt = carAmt;
        updateTime = new Date();
    }
}
