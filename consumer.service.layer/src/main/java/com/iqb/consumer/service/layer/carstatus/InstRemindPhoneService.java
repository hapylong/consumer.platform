package com.iqb.consumer.service.layer.carstatus;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CgetCarStatusInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindPhoneBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindRecordBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.etep.common.exception.IqbException;

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
 * 2018年4月26日下午2:44:50 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstRemindPhoneService {
    /**
     * 根据条件查询电话提醒列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月25日
     */
    public PageInfo<InstRemindPhoneBean> selectInstRemindPhoneList(JSONObject objs);

    public PageInfo<InstRemindPhoneBean> selectInstRemindPhoneList2(JSONObject objs);

    /**
     * 根据订单号 期数查询电话提醒列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月25日
     */
    public PageInfo<InstRemindRecordBean> selectInstRemindRecordList(JSONObject objs);

    /**
     * 
     * 插入电话信息信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月26日
     */
    public int insertInstRemindRecordInfo(InstRemindRecordBean bean) throws IqbException;

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

    /**
     * 贷后客户信息流程页面点击审核通过前执行该接口,修改状态为未处理
     * 
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月27日 16:03:02
     */
    public void updateInstRemindPhoneBean(JSONObject objs);

    /**
     * 
     * 导出电话提醒列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月26日
     */
    public String exportInstRemindPhoneList(JSONObject objs, HttpServletResponse response);

    /**
     * 
     * 导出电催列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月26日
     */
    public String exportInstRemindPhoneList2(JSONObject objs, HttpServletResponse response);

    /**
     * 贷后列表页
     * 
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月4日 10:27:44
     */
    public PageInfo<CgetCarStatusInfoResponseMessage> afterLoanList(JSONObject requestMessage);

    public OrderBean queryOrderInfoByOrderId(JSONObject requestMessage);

    public void insertManagecarInfo(JSONObject requestMessage);

    public List<Map<String, Object>> queryBillIfoByOId(String string);

    public int saveAfterRiskAndStartWf(JSONObject objs) throws IqbException;

    public List<ManageCarInfoBean> queryManagecarInfo(String string);

    String exportInstRemindPhoneList3(JSONObject objs, HttpServletResponse response);

    String exportInstRemindPhoneList4(JSONObject objs, HttpServletResponse response);

    /**
     * 
     * Description:根据条件查询电话提醒 电催总记录数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月1日
     */
    public int getInstRemindRecordListTotal(JSONObject objs);

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
     * Description:将电话提醒、电催信息抽取到贷后车辆信息表中
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月13日
     */
    public void saveInstManagerCarInfo(String orderId);

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

    /**
     * 
     * Description:根据条件获取电话电催标记信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月13日
     */
    public List<InstRemindRecordBean> getInstRemindRecordList(JSONObject objs);
}
