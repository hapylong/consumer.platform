package com.iqb.consumer.data.layer.biz.carthreehundred;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.carthreehundred.CheThreeHBean;
import com.iqb.consumer.data.layer.dao.carthreehundred.CheThreeHDao;
import com.iqb.etep.common.base.biz.BaseBiz;

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
 * 2018年8月10日上午9:17:02 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class CheThreeHBiz extends BaseBiz {
    @Autowired
    private CheThreeHDao cheThreeHDao;

    /**
     * 
     * Description:根据订单号查询订单相关信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月10日
     */
    public CheThreeHBean getOrderInfoByOrderId(String orderId) {
        setDb(0, super.SLAVE);
        return cheThreeHDao.getOrderInfoByOrderId(orderId);
    }

    /**
     * 
     * 灰名单待发送列表查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    public List<Map<String, Object>> selectCheThreeHWaitSendList(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return cheThreeHDao.selectCheThreeHWaitSendList(objs);
    }

    /**
     * 
     * 车贷监控列表查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    public List<Map<String, Object>> selectCheThreeMonitorList(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return cheThreeHDao.selectCheThreeMonitorList(objs);
    }

    /**
     * 
     * 车贷监控列表查询-不分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    public List<Map<String, Object>> selectCheThreeMonitorListNoPage(JSONObject objs) {
        setDb(0, super.SLAVE);
        return cheThreeHDao.selectCheThreeMonitorList(objs);
    }

    /**
     * 
     * 反欺诈详情查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    public List<Map<String, Object>> selectCheThreeMonitorReceive(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return cheThreeHDao.selectCheThreeMonitorReceive(objs.getString("carNo"));
    }

}
