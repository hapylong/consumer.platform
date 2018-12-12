package com.iqb.consumer.data.layer.bean.wf;

import java.math.BigDecimal;

/**
 * 
 * @author chengzhen FINANCE-2681 以租代购提前还款页面调整FINANCE-2730门店申请分页查询接口
 */
public class SettleApplyOrderPojo {
    private Long id;
    private String merchantShortName;
    private String orderId;
    private String realName;
    private String regId;
    private BigDecimal orderAmt;
    private Integer orderItems;
    private BigDecimal monthInterest;
    private Integer settleStatus;

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public BigDecimal getOrderAmt() {
        if (orderAmt == null) {
            return BigDecimal.ZERO;
        }
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getMonthInterest() {
        if (monthInterest == null) {
            return BigDecimal.ZERO;
        }
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public Integer getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(Integer settleStatus) {
        this.settleStatus = settleStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
