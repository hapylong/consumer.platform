package com.iqb.consumer.service.layer.query.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.query.AchievementBean;
import com.iqb.consumer.data.layer.biz.query.AchievementBiz;
import com.iqb.consumer.service.layer.query.IAchievementQueryService;

@Service
public class AchievementQueryServiceImpl implements IAchievementQueryService {

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(AchievementQueryServiceImpl.class);

    @Autowired
    private AchievementBiz achievementBiz;

    @Override
    public PageInfo<AchievementBean> queryAllAchievements(JSONObject objs) {
        List<AchievementBean> list = achievementBiz.queryAllAchievements(objs);
        return new PageInfo<AchievementBean>(list);
    }

}
