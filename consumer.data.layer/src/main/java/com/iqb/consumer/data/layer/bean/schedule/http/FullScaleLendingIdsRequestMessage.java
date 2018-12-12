package com.iqb.consumer.data.layer.bean.schedule.http;

import java.util.List;

public class FullScaleLendingIdsRequestMessage {

    private List<String> orderIds;
    private String sign;

    public List<String> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public boolean generatorIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        /*
         * String orderIds = JSONObject.toJSONString(ids); orderIds = orderIds.replace("\",\"",
         * ";").replace("[\"", "").replace("\"]", "");
         */
        this.orderIds = ids;
        return true;
    }
}
