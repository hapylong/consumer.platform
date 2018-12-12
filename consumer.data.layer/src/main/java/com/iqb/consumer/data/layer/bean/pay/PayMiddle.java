package com.iqb.consumer.data.layer.bean.pay;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 支付记录表
 * 
 * @author Yeoman
 */
public class PayMiddle extends BaseEntity {
    /** 订单号 **/
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
