package com.iqb.consumer.service.layer.query;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.query.AchievementBean;

public interface IAchievementQueryService {

    PageInfo<AchievementBean> queryAllAchievements(JSONObject objs);
}
