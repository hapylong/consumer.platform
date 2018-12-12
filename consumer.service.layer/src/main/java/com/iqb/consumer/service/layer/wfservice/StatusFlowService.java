/*
 * 软件著作权：北京爱钱帮财富科技有限公司 项目名称：
 * 
 * NAME : StatusFlowService.java
 * 
 * PURPOSE :
 * 
 * AUTHOR : crw
 * 
 * 
 * 创建日期: 2017年5月17日 HISTORY： 变更日期
 */
package com.iqb.consumer.service.layer.wfservice;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.data.layer.bean.wf.StatusFlowBean;

/**
 * @author haojinlong
 * 
 */
public interface StatusFlowService {
    /**
     * 根据条件查询工作流-流程单条记录
     * 
     * @param params
     * @return StatusFlowBean Author haojinlong Create Date: 2017年5月17日
     */
    StatusFlowBean selectOne(Map<String, Object> params);

    /**
     * 根据条件修改工作流-流程记录
     * 
     * @param params
     * @return int Author haojinlong Create Date: 2017年5月17日
     */
    int updateStatusFlowByParams(Map<String, Object> params);

    /**
     * 根据条件查询工作流-流程列表
     * 
     * @param params
     * @return List<StatusFlowBean> Author haojinlong Create Date: 2017年5月17日
     */
    List<StatusFlowBean> selectStatusFlowListByParams(Map<String, Object> params);
}
