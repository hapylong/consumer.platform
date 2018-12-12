package com.iqb.consumer.data.layer.dao.afterLoan;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.afterLoan.InstManageCarReceiveBean;

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
 * 2018年7月11日下午6:15:44 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstManageCarReceiveDao {
    /**
     * 
     * 保存贷后车辆回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月11日
     */
    int saveInstManageCarReceive(InstManageCarReceiveBean bean);

    /**
     * 
     * 更新贷后车辆回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月11日
     */
    int updateInstManageCarReceiveByOrderId(JSONObject objs);

    /**
     * 
     * 根据订单号查询车辆回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月26日
     */
    InstManageCarReceiveBean getInstManageCarReceiveInfoByOrderId(JSONObject objs);
}
