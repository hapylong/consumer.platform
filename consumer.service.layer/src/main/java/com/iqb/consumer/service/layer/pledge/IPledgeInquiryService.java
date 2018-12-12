package com.iqb.consumer.service.layer.pledge;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 质押车接口
 * 
 * @author guojuan
 * 
 */
public interface IPledgeInquiryService {

    /**
     * 通过订单号获取质押车及订单信息
     * 
     * @param objs
     * @return
     */
    public Object getPledgeOrderInfo(JSONObject objs);

    /**
     * 更新订单流程状态及数据
     */
    public void updateOrderInfo(JSONObject object);

    /**
     * 复评更新订单状态
     * 
     * @param params
     */
    public void updateOrderInfoRiskStatus(Map<String, String> params);

    public Object getPledgeOrderInfoByOid(String orderId) throws Exception;

}
