package com.iqb.consumer.asset.allocation.assetallocine.request;

import java.math.BigDecimal;

public class BreakRegulationRequestMessage {
    /** 用于计算的拆分规则 **/
    private String orderId; // orderInfo 资产包编号
    private String breakType; // 拆分规则 1,按份数拆分 2,按金额拆分
    private Integer num;
    private BigDecimal amount;

    /** 回显 **/
    private String bOrderId; // 拆分子订单号
    private String orderItems; // 期数
    private BigDecimal bOrderAmt; // 子订单金额
    private String recordNum; // 备案号
    private String delistingMechanism; // 摘牌机构,
    private String remark; // 备注

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBreakType() {
        return breakType;
    }

    public void setBreakType(String breakType) {
        this.breakType = breakType;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getbOrderId() {
        return bOrderId;
    }

    public void setbOrderId(String bOrderId) {
        this.bOrderId = bOrderId;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getbOrderAmt() {
        return bOrderAmt;
    }

    public void setbOrderAmt(BigDecimal bOrderAmt) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
