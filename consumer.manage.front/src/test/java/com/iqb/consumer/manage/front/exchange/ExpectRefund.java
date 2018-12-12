package com.iqb.consumer.manage.front.exchange;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by ckq.
 */
public class ExpectRefund {

    private Integer refundNumber;

    @JSONField(format = "yyyy-MM-dd")
    private Date refundDetailDay;

    private Integer refundStatu;

    private Double refundMoney;

    private Double refundCapital;

    private Double refundInterest;

    public Integer getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(Integer refundNumber) {
        this.refundNumber = refundNumber;
    }

    public Date getRefundDetailDay() {
        return refundDetailDay;
    }

    public void setRefundDetailDay(Date refundDetailDay) {
        this.refundDetailDay = refundDetailDay;
    }

    public Integer getRefundStatu() {
        return refundStatu;
    }

    public void setRefundStatu(Integer refundStatu) {
        this.refundStatu = refundStatu;
    }

    public Double getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(Double refundMoney) {
        this.refundMoney = refundMoney;
    }

    public Double getRefundCapital() {
        return refundCapital;
    }

    public void setRefundCapital(Double refundCapital) {
        this.refundCapital = refundCapital;
    }

    public Double getRefundInterest() {
        return refundInterest;
    }

    public void setRefundInterest(Double refundInterest) {
        this.refundInterest = refundInterest;
    }
}
