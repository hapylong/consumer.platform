package com.iqb.consumer.asset.allocation.assetallocine.db.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.iqb.etep.common.utils.StringUtil;

@Table(name = "jys_breakorderinfo")
public class BreakOrderInfoEntity {
    private long id;
    private String bOrderId; // 拆分后的子订单号
    private String orderId; // 原始订单号
    private Short orderItems; // 期数
    private BigDecimal bOrderAmt; // 拆分后的订单金额
    private String recordNum; // 备案号
    private String delistingMechanism; // 摘牌机构
    private Short breakType; // 拆分规则 1，金额 2分数
    private Short status; // 默认 0
    private String remark; // 备注
    private Integer version; // 版本
    private Date createTime;
    private Date updateTime;
    private Short breakPackNum;
    private long jysOrderId;// 交易所订单表主键id

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBOrderId() {
        return bOrderId;
    }

    public void setBOrderId(String bOrderId) {
        this.bOrderId = bOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Short getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Short orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getBOrderAmt() {
        return bOrderAmt;
    }

    public void setBOrderAmt(BigDecimal bOrderAmt) {
        this.bOrderAmt = bOrderAmt;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public String getDelistingMechanism() {
        return delistingMechanism;
    }

    public void setDelistingMechanism(String delistingMechanism) {
        this.delistingMechanism = delistingMechanism;
    }

    public Short getBreakType() {
        return breakType;
    }

    public void setBreakType(Short breakType) {
        this.breakType = breakType;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Short getBreakPackNum() {
        return breakPackNum;
    }

    public void setBreakPackNum(Short breakPackNum) {
        this.breakPackNum = breakPackNum;
    }

    public long getJysOrderId() {
        return jysOrderId;
    }

    public void setJysOrderId(long jysOrderId) {
        this.jysOrderId = jysOrderId;
    }

    public static enum STATUS_ENUM {
        BREAK_SUCCESS(1), CHAT_WITH_RAR_SUCCESS(2);

        private int intValue;

        private STATUS_ENUM(int val) {
            this.intValue = val;
        }

        public short intValue() {
            return (short) intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }
    }

    public boolean checkEntity() {
        if (StringUtil.isEmpty(bOrderId) || StringUtil.isEmpty(orderId)
                || StringUtil.isEmpty(recordNum)
                || StringUtil.isEmpty(delistingMechanism) || orderItems == null
                || bOrderAmt == null || breakType == null
                || breakPackNum == null) {
            return false;
        }
        createTime = new Date();
        status = STATUS_ENUM.BREAK_SUCCESS.intValue();
        return true;
    }

    @Override
    public String toString() {
        return "BreakOrderInfoEntity [bOrderId=" + bOrderId + ", orderId="
                + orderId + ", orderItems=" + orderItems + ", bOrderAmt="
                + bOrderAmt + ", recordNum=" + recordNum
                + ", delistingMechanism=" + delistingMechanism + ", breakType="
                + breakType + ", breakPackNum=" + breakPackNum + "]";
    }

}
