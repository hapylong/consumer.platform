package com.iqb.consumer.data.layer.bean.order.pojo;

import java.math.BigDecimal;

public class InstdetailChatFPojo {
    private Integer repayNo; // 期数
    private Integer days; // 天数
    private BigDecimal interest;// 每期利息

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

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
}
