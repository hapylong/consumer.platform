package com.iqb.consumer.data.layer.dao.carstatus;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CgetCarStatusInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindPhoneBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindRecordBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;

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
 * 2018年4月25日下午3:46:28 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstRemindPhoneDao {
    /**
     * 根据条件查询电话提醒列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月25日
     */
    public List<InstRemindPhoneBean> selectInstRemindPhoneList(JSONObject objs);

    /**
     * 根据条件更新单条信息
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月26日 17:36:02
     */
    public void updateInstRemindPhoneBean(InstRemindPhoneBean instRemindPhoneBean);

    public InstRemindPhoneBean selectInstRemindPhoneOne(InstRemindRecordBean instRemindRecordBean);

    public List<CgetCarStatusInfoResponseMessage> afterLoanList(JSONObject requestMessage);

    public OrderBean queryOrderInfoByOrderId(JSONObject requestMessage);

    public int saveManageCarInfo(JSONObject requestMessage);

    public int updateManagerCarInfo(JSONObject objs);

    public List<ManageCarInfoBean> queryManagecarInfo(String orderId);

    public List<InstRemindPhoneBean> selectInstRemindPhoneList2(JSONObject objs);

    /**
     * 
     * Description:根据订单号查询风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月6日
     */
    public InstRemindPhoneBean getRiskInfoByOrderId(JSONObject objs);

    /**
     * 
     * Description:根据订单号查询电话电催信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月6日
     */
    public InstRemindPhoneBean getRemindInfoByOrderId(JSONObject objs);
}
