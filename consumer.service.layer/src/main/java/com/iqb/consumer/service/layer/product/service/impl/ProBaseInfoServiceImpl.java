/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午9:51:09
 * @version V1.0
 */
package com.iqb.consumer.service.layer.product.service.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.project.ProjectBaseInfoBean;
import com.iqb.consumer.data.layer.biz.pro.ProjectBaseInfoBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.product.service.IProBaseInfoService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service("proBaseInfoService")
public class ProBaseInfoServiceImpl extends BaseService implements IProBaseInfoService {

    @Resource
    private ProjectBaseInfoBiz projectBaseInfoBiz;

    @Override
    public int insertProBaseInfo(JSONObject objs) {
        return projectBaseInfoBiz.insertProBaseInfo(objs);
    }

    @Override
    public int updateProBaseInfo(JSONObject objs) {
        return projectBaseInfoBiz.updateProBaseInfo(objs);
    }

    @Override
    public void delProBaseInfoByID(JSONObject objs) {
        projectBaseInfoBiz.deleteProBaseInfo(objs);
    }

    @Override
    public PageInfo<ProjectBaseInfoBean> getProBaseInfoByMerNos(JSONObject objs) {
        // 获取商户权限的Objs
        objs = getMerchLimitObject(objs);
        if (objs == null) {
            return null;
        }
        List<ProjectBaseInfoBean> list = projectBaseInfoBiz.getProBaseInfoByMerNos(objs, true);
        PageInfo<ProjectBaseInfoBean> pageInfo = new PageInfo<ProjectBaseInfoBean>(list);
        return pageInfo;
    }

    @Override
    public ProjectBaseInfoBean getProBaseInfoById(JSONObject objs) {
        return projectBaseInfoBiz.getProBaseInfoById(objs);
    }

}
