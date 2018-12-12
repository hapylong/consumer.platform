package com.iqb.consumer.batch.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.batch.data.pojo.InstRemindPhoneBean;

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
 * 2018年4月27日下午5:01:06 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstRemindPhoneDao {
    /**
     * 
     * Description:保存inst_settlementResult
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    public long insertInstRemindPhoneResult(List<InstRemindPhoneBean> list);

    /**
     * 
     * Description:查询完成处理的电话提醒记录列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月27日
     */
    public List<InstRemindPhoneBean> selectInstRemindPhoneList(JSONObject objs);

    /**
     * 
     * Description:根据订单号期数修改电话提醒信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月27日
     */
    public long updateInstRemindPhoneResult(JSONObject objs);

    /**
     * 
     * Description:根据订单号当前期数查询电话提醒信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月2日
     */
    public InstRemindPhoneBean selectInstRemindPhone(JSONObject objs);

    /**
     * 
     * Description:根据订单号查询贷后车辆信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    public InstRemindPhoneBean selectInstManageCarInfo(String orderId);

}
