package com.iqb.consumer.data.layer.bean.order.house;

public class InstdetailPojo {
    private Integer repayNo;// 期数
    private Integer days;// 天数
    private Double feeRatio;// 费率

    public Integer getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(Integer repayNo) {
        this.repayNo = repayNo;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Double getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(Double feeRatio) {
        this.feeRatio = feeRatio;
    }
}
