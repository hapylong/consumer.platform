package com.iqb.consumer.service.layer.pushRecord;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.pushRecord.PushRecordBean;

public interface IPushRecordService {
    /**
     * 根据订单号获取推标记录 2018年2月26日 19:00:28 chengzhen
     */
    PageInfo<PushRecordBean> pushRecordByOrderIdList(JSONObject obj);

    /**
     * 根据id删除推标记录 2018年2月26日 19:00:28 chengzhen
     */
    int delPushRecordById(JSONObject objs);

}
