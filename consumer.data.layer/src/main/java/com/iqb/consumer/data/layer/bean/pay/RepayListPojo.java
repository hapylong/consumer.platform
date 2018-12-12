package com.iqb.consumer.data.layer.bean.pay;

import java.math.BigDecimal;

public class RepayListPojo {
    private String repayNo;
    private BigDecimal amt;

    public String getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(String repayNo) {
        this.repayNo = repayNo;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
}
