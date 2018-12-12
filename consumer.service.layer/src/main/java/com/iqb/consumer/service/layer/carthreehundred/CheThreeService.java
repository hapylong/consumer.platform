package com.iqb.consumer.service.layer.carthreehundred;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
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
 * 2018年8月7日下午6:46:14 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface CheThreeService {
    /**
     * 
     * Description:发起代签反欺诈查询请求
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    public Map<String, String> requestPreLoanAntiFraud(JSONObject objs);

    /**
     * 
     * Description:检查贷前反欺诈查询结果
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    public Map<String, String> checkPreLoanAntiFraudResult(JSONObject objs);

    /**
     * 
     * Description:注册贷后监控车辆信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    public Map<String, String> registerPostLoanMonitorCar(JSONObject objs);

    /**
     * 
     * Description:关闭贷后监控
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    public Map<String, String> stopPostLoanMonitor(JSONObject objs);

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
    public PageInfo<Map<String, Object>> selectCheThreeHWaitSendList(JSONObject objs);

    /**
     * 
     * 车贷监控列表查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    public PageInfo<Map<String, Object>> selectCheThreeMonitorList(JSONObject objs);

    /**
     * 
     * 车贷监控列表查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    public String exportCheThreeMonitorList(JSONObject objs, HttpServletResponse response);

    /**
     * 
     * 反欺诈详情查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    public PageInfo<Map<String, Object>> selectCheThreeMonitorReceive(JSONObject objs);

    /**
     * 
     * Description:贷后风险信息回调
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月16日
     */
    public int loanRiskAccept(JSONArray jsonArray);

}
