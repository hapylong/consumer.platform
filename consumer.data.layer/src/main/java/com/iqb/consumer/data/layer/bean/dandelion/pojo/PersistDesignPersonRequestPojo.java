package com.iqb.consumer.data.layer.bean.dandelion.pojo;

import jodd.util.StringUtil;

public class PersistDesignPersonRequestPojo {

    private String id;// e. zhangxindan
    private String text;// e. 张馨丹(zhangxindan)
    private String orderId;// 订单号

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean checkRequest() {
        if (id == null || StringUtil.isEmpty(text) || StringUtil.isEmpty(orderId)) {
            return false;
        }
        return true;
    }
}
