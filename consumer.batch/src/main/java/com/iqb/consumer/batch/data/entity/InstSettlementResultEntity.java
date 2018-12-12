package com.iqb.consumer.batch.data.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.iqb.consumer.batch.util.RandomUtils;

@Table(name = "inst_settlementresult")
public class InstSettlementResultEntity {
    public static final int NOT_SEND = 1;
    public static final int SEND = 2;
    public static final int HK_SUCCESS = 3;
    public static final int HK_PART_SUCCESS = 4;
    public static final int HK_FAIL = 5;

    private Long id;
    private String orderId;
    private String tradeNo;
    private Integer repayNo;
    private Integer openId;
    private BigDecimal curRepayAmt;
    private Integer tradeType;
    private Integer status; // 1,未发送 2 发送成功,3,划扣成功，4，划扣部分成功，5，划扣失败
    private Integer number;
    private Integer version;
    private Date createTime;
    private Date sendTime;
    private Date updateTime;
    private String describe;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

    public Integer getOpenId() {
        return openId;
    }

    public void setOpenId(Integer openId) {
        this.openId = openId;
    }

    public BigDecimal getCurRepayAmt() {
        return curRepayAmt;
    }

    public void setCurRepayAmt(BigDecimal curRepayAmt) {
        this.curRepayAmt = curRepayAmt;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String generateTradeNo() {
        return orderId + "@" + repayNo + "@" + RandomUtils.randomInt(8);
    }
}
