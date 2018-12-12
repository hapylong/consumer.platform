/*
 * 软件著作权：北京爱钱帮财富科技有限公司 项目名称：
 * 
 * NAME : StatusFlowBiz.java
 * 
 * PURPOSE :
 * 
 * AUTHOR : crw
 * 
 * 
 * 创建日期: 2017年5月17日 HISTORY： 变更日期
 */
package com.iqb.consumer.data.layer.biz.wf;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.wf.StatusFlowBean;
import com.iqb.consumer.data.layer.dao.wf.StatusFlowDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author haojinlong
 * 
 */
@Component
public class StatusFlowBiz extends BaseBiz {
    @Resource
    private StatusFlowDao statusFlowDao;

    /**
     * 根据条件查询工作流-流程单条记录
     * 
     * @param params
     * @return StatusFlowBean Author haojinlong Create Date: 2017年5月17日
     */
    public StatusFlowBean selectOne(Map<String, Object> params) {
        setDb(0, super.MASTER);
        return statusFlowDao.selectOne(params);
    }

    /**
     * 根据条件修改工作流-流程记录
     * 
     * @param params
     * @return int Author haojinlong Create Date: 2017年5月17日
     */
    public int updateStatusFlowByParams(Map<String, Object> params) {
        setDb(0, super.MASTER);
        return statusFlowDao.updateStatusFlowByParams(params);
    }

    /**
     * 根据条件查询工作流-流程列表
     * 
     * @param params
     * @return List<StatusFlowBean> Author haojinlong Create Date: 2017年5月17日
     */
    public List<StatusFlowBean> selectStatusFlowListByParams(Map<String, Object> params) {
        setDb(0, super.MASTER);
        return statusFlowDao.selectStatusFlowListByParams(params);
    }
}
