package com.iqb.consumer.data.layer.biz.query;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.query.AchievementBean;
import com.iqb.consumer.data.layer.dao.AchievementDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Repository
public class AchievementBiz extends BaseBiz {
    @Resource
    private AchievementDao achievementDao;

    public List<AchievementBean> queryAllAchievements(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        List<AchievementBean> list = achievementDao.queryAllAchievements(objs);
        return list;
    }
}
