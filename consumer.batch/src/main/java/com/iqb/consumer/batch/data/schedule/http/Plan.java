package com.iqb.consumer.batch.data.schedule.http;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Plan {
    private Integer number;// 期数
    private BigDecimal capital;// 应还本金
    private BigDecimal interest; // 应还利息
    private String finalRepaymentDate; // 最后还款日

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number + 1;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = new BigDecimal(capital);
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = new BigDecimal(interest);
    }

    public String getFinalRepaymentDate() {
        return finalRepaymentDate;
    }

    public void setFinalRepaymentDate(String i) {
        this.finalRepaymentDate = convert(i);
    }

    private String convert(String i) {
        if (i == null) {
            return null;
        }
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(new Date(Integer.parseInt(i) * 1000L));
    }
}
