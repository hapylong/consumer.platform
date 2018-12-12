package com.iqb.consumer.data.layer.bean.dandelion.pojo;

import java.math.BigDecimal;

public class MortgageInfoPojo {

    private BigDecimal loanAmt;
    private String creditType;

    public BigDecimal getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(BigDecimal loanAmt) {
        this.loanAmt = loanAmt;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

}
