/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午6:47:20
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.data.layer.bean.project.ProjectBaseInfoBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface ProjectBaseInfoDao {
    /**
     * 增加车基础信息
     * 
     * @param lanlordBean
     * @return
     */
    int insertProBaseInfo(ProjectBaseInfoBean projectBaseInfoBean);

    /**
     * 修改车基础信息
     * 
     * @param lanlordBean
     * @return
     */
    int updateProBaseInfo(ProjectBaseInfoBean projectBaseInfoBean);

    /**
     * 删除车基础信息
     * 
     * @param lanlordBean
     * @return
     */
    void delProBaseInfoByID(long id);

    /**
     * 根据商户列表、品牌查询车系
     * 
     * @param merchantNos
     * @return
     */
    List<ProjectBaseInfoBean> getProBaseInfoByMerNos(Map<String, Object> map);

    /**
     * 查询指定车详细信息
     * 
     * @param id
     * @return
     */
    ProjectBaseInfoBean getProBaseInfoById(long id);
}
