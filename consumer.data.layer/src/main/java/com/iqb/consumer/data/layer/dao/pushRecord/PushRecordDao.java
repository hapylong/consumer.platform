package com.iqb.consumer.data.layer.dao.pushRecord;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.pushRecord.PushRecordBean;

public interface PushRecordDao {
    /**
     * 根据订单号获取推标记录 2018年2月27日 10:47:42 chengzhen
     */
    List<PushRecordBean> pushRecordByOrderIdList(JSONObject obj);

    /**
     * 根据订单号打包拆分状态 2018年2月27日 14:52:41 10:47:42 chengzhen
     */
    List<Integer> getJSYStateByOrderId(JSONObject objs);

    /**
     * 保存删除原因 2018年2月27日 15:14:56 10:47:42 chengzhen
     */
    void insertDelPRRemark(JSONObject objs);

    /**
     * 删除推标记录 2018年2月27日 15:37:42 chengzhen
     */
    void delPushRecordById(HashMap<String, Long> delIdMap);

    void delJSYPushRecordById(HashMap<String, Long> delIdMap);

}
