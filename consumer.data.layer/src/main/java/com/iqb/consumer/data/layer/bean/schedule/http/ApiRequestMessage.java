package com.iqb.consumer.data.layer.bean.schedule.http;

import com.alibaba.fastjson.JSONObject;

public class ApiRequestMessage {

    private String orderId;
    private Integer status; // (1待支付、2支付成功，3分期、4拒绝的)

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
