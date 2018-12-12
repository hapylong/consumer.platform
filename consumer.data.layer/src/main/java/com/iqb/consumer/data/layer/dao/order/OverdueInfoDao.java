package com.iqb.consumer.data.layer.dao.order;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.order.ArchivesBean;
import com.iqb.consumer.data.layer.bean.order.OverdueInfoBean;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年3月16日上午11:31:26 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface OverdueInfoDao {
    /**
     * 
     * Description:客户履约保证金结算分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    List<OverdueInfoBean> selectOverdueInfoSettlementList(JSONObject objs);

    /**
     * 查询订单是否已经发起流程
     * 
     * @param objs
     * @return
     */
    int selOrderCount(JSONObject objs);

    /**
     * 
     * Description:根据条件查询单条违约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    OverdueInfoBean selectOverdueInfo(JSONObject objs);

    /**
     * 
     * Description:插入违约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    int insertOverdueInfo(OverdueInfoBean overdueInfoBean);

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
     * Description:根据批次号查询总结算金额、总保证金、总笔数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月19日
     */
    OverdueInfoBean getOverdueInfoByBatchId(JSONObject objs);

    /**
     * 
     * Description:档案查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月26日
     */
    List<ArchivesBean> queryArchivesList(JSONObject objs);
}
