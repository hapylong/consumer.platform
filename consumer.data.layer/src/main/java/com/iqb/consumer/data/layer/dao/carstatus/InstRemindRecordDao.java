package com.iqb.consumer.data.layer.dao.carstatus;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindRecordBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;

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
 * 2018年4月26日下午4:11:21 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstRemindRecordDao {
    /**
     * 根据订单号 期数查询电话提醒列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月25日
     */
    public List<InstRemindRecordBean> selectInstRemindRecordList(JSONObject objs);

    /**
     * 
     * 插入电话信息信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月26日
     */
    public int insertInstRemindRecordInfo(InstRemindRecordBean bean);

    /**
     * 
     * 根据订单号 回显贷后客户信息流程页面数据
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月27日 14:11:50
     */
    public InstRemindRecordBean queryCustomerInfo(JSONObject objs);

    public List<ManageCarInfoBean> queryManagecarInfo(String orderId);
}
