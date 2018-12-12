package com.iqb.consumer.data.layer.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.query.AchievementBean;

public interface AchievementDao {

    public List<AchievementBean> queryAllAchievements(JSONObject objs);
}
