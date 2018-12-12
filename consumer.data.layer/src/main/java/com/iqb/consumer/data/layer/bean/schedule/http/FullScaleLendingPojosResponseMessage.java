package com.iqb.consumer.data.layer.bean.schedule.http;

import com.github.pagehelper.StringUtil;

public class FullScaleLendingPojosResponseMessage {
    private final String SUCCESS = "success";
    private String orderId;
    private String retCode;
    private String retMsg;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public boolean check() {
        if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(retCode) || !retCode.equals(SUCCESS)) {
            return false;
        }
        return true;
    }
}
