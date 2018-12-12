package com.iqb.consumer.manage.front.query;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.query.AchievementBean;
import com.iqb.consumer.service.layer.query.IAchievementQueryService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/achievementQuery")
public class AchievementQueryController extends BaseService {

    private static Logger logger = LoggerFactory.getLogger(AchievementQueryController.class);

    @Resource
    private IAchievementQueryService service;

    @ResponseBody
    @RequestMapping(value = "/getAchievements", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getAllIAchievements(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始分页查询业绩数据...");
            PageInfo<AchievementBean> list = service.queryAllAchievements(objs);
            logger.debug("IQB信息---分页查询业绩数据完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
