package com.iqb.consumer.data.layer.bean.admin.request;

import java.util.Date;

import jodd.util.StringUtil;

import com.iqb.consumer.data.layer.bean.BaseEntity;

public class ChatToChangePlanRequestPojo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String regId; // 注册号
    private String openId; // 开户号
    private String orderId; // 订单号
    private Date beginDate; // 起息日

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public static void main(String[] args) {
        new ChatToChangePlanRequestPojo().print();
    }

    @Override
    public String toString() {
        return "ChatToChangePlanRequestPojo [regId=" + regId + ", openId=" + openId + ", orderId=" + orderId + "]";
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public boolean checkPojo() {
        print();
        if (StringUtil.isEmpty(regId) ||
                StringUtil.isEmpty(openId) ||
                StringUtil.isEmpty(orderId)) {
            return false;
        }
        return true;
    }
}
