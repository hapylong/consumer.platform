package com.iqb.consumer.data.layer.bean.sublet.pojo;

import java.math.BigDecimal;

import jodd.util.StringUtil;

public class GetSubletRecordPojo {

    /** inst_orderinfo **/
    private String orderId;// 新订单号
    private String regId;// 新用户手机号
    private Integer orderItems;// 总期数
    private BigDecimal monthInterest;// 月供
    /** http **/
    private Integer overItems;// 已还期数

    /** inst_subletrecord **/
    private String subletOrderId;// 转租人订单号
    private String subletRegId;// 转租人手机号
    private String rollOverItems;// 展期期数
    private Integer rollOverFlag;// 是否展期
    private Integer manageFeeFlag;// 是否管理费用
    private BigDecimal manageFee;// 管理费用

    /** inst_plan **/
    private String planShortName;// 产品方案
    /** inst_user **/
    private String subletRealName;// 转租人姓名

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getSubletOrderId() {
        return subletOrderId;
    }

    public void setSubletOrderId(String subletOrderId) {
        this.subletOrderId = subletOrderId;
    }

    public String getSubletRegId() {
        return subletRegId;
    }

    public void setSubletRegId(String subletRegId) {
        this.subletRegId = subletRegId;
    }

    public String getRollOverItems() {
        return rollOverItems;
    }

    public void setRollOverItems(String rollOverItems) {
        this.rollOverItems = rollOverItems;
    }

    public Integer getRollOverFlag() {
        return rollOverFlag;
    }

    public void setRollOverFlag(Integer rollOverFlag) {
        this.rollOverFlag = rollOverFlag;
    }

    public Integer getManageFeeFlag() {
        return manageFeeFlag;
    }

    public void setManageFeeFlag(Integer manageFeeFlag) {
        this.manageFeeFlag = manageFeeFlag;
    }

    public BigDecimal getManageFee() {
        return manageFee;
    }

    public void setManageFee(BigDecimal manageFee) {
        this.manageFee = manageFee;
    }

    public String getPlanShortName() {
        return planShortName;
    }

    public void setPlanShortName(String planShortName) {
        this.planShortName = planShortName;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getOverItems() {
        return overItems;
    }

    public void setOverItems(Integer overItems) {
        this.overItems = overItems;
    }

    public String getSubletRealName() {
        return subletRealName;
    }

    public void setSubletRealName(String subletRealName) {
        this.subletRealName = subletRealName;
    }

    public boolean checkEntity() {
        if (StringUtil.isEmpty(orderId) ||
                StringUtil.isEmpty(regId) ||
                null == orderItems ||
                null == monthInterest ||
                StringUtil.isEmpty(subletOrderId) ||
                StringUtil.isEmpty(subletRegId) ||
                StringUtil.isEmpty(rollOverItems) ||
                null == rollOverFlag ||
                null == manageFeeFlag ||
                null == manageFee ||
                null == overItems ||
                StringUtil.isEmpty(planShortName) ||
                StringUtil.isEmpty(subletRealName)) {
            return false;
        }
        return true;
    }
}
