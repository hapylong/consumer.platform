package com.iqb.consumer.service.layer.pay;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;

public interface PayService {

    void callback(JSONObject requestMessage) throws GenerallyException;

    Map<String, String> savePaytemRecord(String payList, String orderId, String settleId);

    Map<String, Object> refundBills(String orderId, Map<String, String> params, String merchantNo);

    /**
     * 查询支付主体
     * 
     * @param payOwnerId
     * @param merchantNo
     * @return
     */
    PayConfBean getPayChannelBy(String payOwnerId, String merchantNo);
}
