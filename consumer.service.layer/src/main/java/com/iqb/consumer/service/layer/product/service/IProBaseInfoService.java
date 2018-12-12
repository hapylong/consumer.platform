/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午9:50:31
 * @version V1.0
 */
package com.iqb.consumer.service.layer.product.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.project.ProjectBaseInfoBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface IProBaseInfoService {

    /**
     * 增加车基础信息
     * 
     * @param lanlordBean
     * @return
     */
    int insertProBaseInfo(JSONObject objs);

    /**
     * 修改车基础信息
     * 
     * @param lanlordBean
     * @return
     */
    int updateProBaseInfo(JSONObject objs);

    /**
     * 删除车基础信息
     * 
     * @param lanlordBean
     * @return
     */
    void delProBaseInfoByID(JSONObject objs);

    /**
     * 根据商户列表、品牌查询车系
     * 
     * @param objs
     * @param flag 分页标志
     * @return
     */
    PageInfo<ProjectBaseInfoBean> getProBaseInfoByMerNos(JSONObject objs);

    /**
     * 查询指定车详细信息
     * 
     * @param id
     * @return
     */
    ProjectBaseInfoBean getProBaseInfoById(JSONObject objs);
}
