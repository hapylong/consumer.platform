package com.iqb.consumer.data.layer.bean.order;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.common.utils.StringUtil;

public class FinishBillRequestMessage {

    private String orderId;
    private BigDecimal amt;
    private Date finishTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public boolean check() {
        if (StringUtil.isEmpty(orderId) || finishTime == null) {
            return false;
        }
        return true;
    }

}
