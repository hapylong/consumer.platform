package com.iqb.consumer.data.layer.bean.finance.pojo.f;

import java.math.BigDecimal;

public class FinanceRefundPiece {

    private int repayNo;// 序号
    private BigDecimal amt;// 金额

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

}
