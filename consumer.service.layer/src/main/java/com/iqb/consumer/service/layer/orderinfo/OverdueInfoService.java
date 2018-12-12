package com.iqb.consumer.service.layer.orderinfo;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.order.ArchivesBean;
import com.iqb.consumer.data.layer.bean.order.OverdueInfoBean;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description:违约信息Service接口
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年3月16日上午11:54:48 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface OverdueInfoService {
    /**
     * 
     * Description:批量插入违约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    public int batchInsertOverdueInfo(JSONObject objs);

    /**
     * 
     * Description:客户履约保证金结算分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    public PageInfo<OverdueInfoBean> selectOverdueInfoSettlementList(JSONObject objs);

    /**
     * 
     * Description:更新违约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    int updateOverdueInfo(OverdueInfoBean overdueInfoBean);

    /**
     * 
     * Description:根据batchId更新违约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    int updateOverdueInfoByBatchId(OverdueInfoBean overdueInfoBean);

    /**
     * 
     * Description:根据批次号查询逾期订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月19日
     */
    OverdueInfoBean getOverdueInfoByBatchId(JSONObject objs);

    /**
     * 
     * Description:客户违约结算流程启动
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月19日
     */
    int startOverdueInfoProcess(JSONObject objs) throws IqbException;

    /**
     * 
     * Description:保证金结算查询导出
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月20日
     */
    public String exportSelectOverdueInfoSettlementList(JSONObject objs, HttpServletResponse response);

    /**
     * 档案查询 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月26日
     */
    public PageInfo<ArchivesBean> queryArchivesList(JSONObject objs);
}
