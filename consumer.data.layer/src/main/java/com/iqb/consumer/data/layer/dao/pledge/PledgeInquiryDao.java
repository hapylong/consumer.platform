package com.iqb.consumer.data.layer.dao.pledge;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.Pledge.PledgeInfoBean;

/**
 * 质押车Dao
 * 
 * @author guojuan
 * 
 */
public interface PledgeInquiryDao {

    /**
     * 获取车辆基本信息
     * 
     * @param objs
     * @return
     */
    public PledgeInfoBean getPledgeOrderInfo(JSONObject objs);

    /**
     * 更新订单信息及车辆信息
     * 
     * @param object
     * @return
     */
    public void updateOrderInfo(JSONObject object);

    public void updatePledgeInfo(JSONObject object);

    /**
     * 复评更新状态
     * 
     * @param params
     */
    public void updateOrderInfoRiskStatus(Map<String, String> params);

    /**
     * 更新评估车状态
     * 
     * @param params
     * @return
     */
    int updatePledgeStatus(Map<String, String> params);

    public String getUuidByOid(String orderId);

}
