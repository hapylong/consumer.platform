package com.iqb.consumer.data.layer.bean.finance.pojo.c;

import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

import jodd.util.StringUtil;

public class BillRefundRequestPojo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String orderId;
    private String subOrderId;
    private String reason;
    private Date repayDate;
    private Integer repayNo;
    private String tradeNo;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Integer getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public boolean checkEntity() {
        print();
        if (StringUtil.isEmpty(orderId) ||
                StringUtil.isEmpty(subOrderId) ||
                StringUtil.isEmpty(reason) ||
                null == repayDate ||
                null == repayNo ||
                StringUtil.isEmpty(tradeNo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BillRefundRequestPojo [orderId=" + orderId + ", subOrderId=" + subOrderId + ", reason=" + reason
                + ", repayDate=" + repayDate + ", repayNo=" + repayNo + ", tradeNo=" + tradeNo + "]";
    }

}
