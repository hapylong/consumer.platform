package com.iqb.consumer.data.layer.dao.carthreehundred;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.carthreehundred.CheThreeHBean;

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
 * 2018年8月10日上午9:16:53 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface CheThreeHDao {
    /**
     * 
     * Description:根据订单号查询订单相关信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月10日
     */
    public CheThreeHBean getOrderInfoByOrderId(String orderId);

    /**
     * 
     * 灰名单待发送列表查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    public List<Map<String, Object>> selectCheThreeHWaitSendList(JSONObject objs);

    /**
     * 
     * 车贷监控列表查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    public List<Map<String, Object>> selectCheThreeMonitorList(JSONObject objs);

    /**
     * 
     * 反欺诈详情查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    public List<Map<String, Object>> selectCheThreeMonitorReceive(@Param("carNo") String carNo);

}
