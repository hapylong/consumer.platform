package com.iqb.consumer.data.layer.bean.pay;

import com.iqb.consumer.data.layer.bean.BaseEntity;

public class PayRecordBean extends BaseEntity {
    // 用户注册号
    private String regId;
    // 订单id
    private String orderId;
    // neiro内容
    private String content;

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
