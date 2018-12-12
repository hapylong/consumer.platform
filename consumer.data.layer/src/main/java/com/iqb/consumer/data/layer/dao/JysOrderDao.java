/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月13日 上午11:24:31
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface JysOrderDao {

    /**
     * 通过订单号查询订单相关信息
     * 
     * @param orderId
     * @return
     */
    JYSOrderBean getSingleOrderInfo(JSONObject objs);

    /*
     * 列出交易所订单列表
     */
    List<JYSOrderBean> listJYSOrderInfo(JSONObject objs);

    /**
     * 修改订单
     * 
     * @param objs
     * @return
     */
    int updateJYSOrderInfo(JSONObject objs);

    /**
     * 修改交易所用户信息
     * 
     * @param objs
     * @return
     */
    int updateJYSUserInfo(JSONObject objs);

    /**
     * 交易所需求:修改订单状态
     * 
     * @param status
     * @return
     */
    int updateOrderStatus(@Param("status") String status, @Param("orderId") String orderId);

    /**
     * 删除订单信息(修改状态为0)
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("rawtypes")
    int deleteJYSOrderInfo(List list);

    void updateOrderRiskStatus(JYSOrderBean job);

    /**
     * 
     * Description:插入交易所订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    int insertJysOrder(JYSOrderBean job);

    /**
     * 
     * Description:根据ID获取交易所订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月6日
     */
    JYSOrderBean getOrderInfoById(String orderId);
}
