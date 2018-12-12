package com.iqb.consumer.data.layer.bean.sublet.db.entity;

import java.math.BigDecimal;

import javax.persistence.Table;

import jodd.util.StringUtil;

import com.iqb.consumer.data.layer.bean.BaseEntity;

@Table(name = "inst_subletRecord")
public class InstSubletRecordEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private final int INIT_STATUS = 1;
    private String orderId;// 订单号
    private String regId;// 手机号
    private String subletOrderId;// 转租人订单
    private String subletRegId;// 转租人电话
    private Integer rollOverFlag;// 展期标识
    private Integer rollOverItems;// 展期期数
    private Integer manageFeeFlag;// 是否管理费用
    private BigDecimal manageFee;// 管理费用
    private Integer status;// 1申请 2完成转租
    private Integer repayItems;

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

    public Integer getRollOverFlag() {
        return rollOverFlag;
    }

    public void setRollOverFlag(Integer rollOverFlag) {
        this.rollOverFlag = rollOverFlag;
    }

    public Integer getRollOverItems() {
        return rollOverItems;
    }

    public void setRollOverItems(Integer rollOverItems) {
        this.rollOverItems = rollOverItems;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRepayItems() {
        return repayItems;
    }

    public void setRepayItems(Integer repayItems) {
        this.repayItems = repayItems;
    }

    public void setOverItems(Integer overItems) {
        this.repayItems = overItems;
    }

    public boolean checkEntity() {
        if (StringUtil.isEmpty(orderId) ||
                StringUtil.isEmpty(regId) ||
                StringUtil.isEmpty(subletOrderId) ||
                StringUtil.isEmpty(subletRegId) ||
                rollOverFlag == null ||
                rollOverItems == null ||
                manageFeeFlag == null ||
                repayItems == null ||
                manageFee == null) {
            return false;
        }
        status = INIT_STATUS;
        return true;
    }
}
