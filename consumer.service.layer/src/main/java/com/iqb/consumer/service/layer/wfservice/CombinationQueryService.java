/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月18日 上午10:28:23
 * @version V1.0
 */
package com.iqb.consumer.service.layer.wfservice;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.reqmoney.RequestMoneyBean;
import com.iqb.consumer.data.layer.bean.wf.CombinationQueryBean;
import com.iqb.etep.common.exception.IqbException;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface CombinationQueryService {

    /**
     * 通过ID查询工作流展示信息
     * 
     * @param orderId
     * @return
     */
    CombinationQueryBean getByOrderId(String orderId);

    /**
     * 修改订单状态
     * 
     * @param orderId
     * @param status
     */
    void updateStatus(String orderId, String status);

    /**
     * 修改订单信息
     * 
     * @param orderBean
     * @return
     */
    int updateOrderInfo(OrderBean orderBean);

    /**
     * 查询订单信息
     * 
     * @param orderId
     * @return
     */
    OrderBean selectOne(Map<String, Object> params);

    /**
     * 通过商户号查询所有分期计划
     * 
     * @param merchantNo
     * @return
     */
    List<PlanBean> getPlanByMerNo(String merchantNo);

    /**
     * 根据ID查询计划
     * 
     * @param id
     * @return
     */
    PlanBean getPlanByID(long id);

    /**
     * 查询请款信息
     * 
     * @param objs
     * @return
     */
    PageInfo<RequestMoneyBean> getAllRequest(JSONObject objs);

    /**
     * 保存资产分配信息
     * 
     * @param requestMoneyBean
     * @return
     */
    long insertReqMoney(RequestMoneyBean requestMoneyBean);

    /**
     * 更新工作流相关信息
     * 
     * @param orderOtherInfo
     * @return
     */
    int updateOrderOtherInfo(OrderOtherInfo orderOtherInfo);

    /**
     * 
     * Description:查询资产分配详情
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月4日
     */
    public List<RequestMoneyBean> getAllotDetailByOrderId(JSONObject objs);

    /**
     * 查询资产分配明细信息
     * 
     * @param objs
     * @return
     */
    PageInfo<RequestMoneyBean> getAllReqAllotMoney(JSONObject objs);

    void exportAllReqAllotMoney(JSONObject objs, HttpServletResponse response);

    /**
     * 资产分配总个数,总金额
     * 
     * @Description: TODO
     * @param @param objs
     * @param @return
     * @return Map
     * @author chengzhen
     * @data
     */
    Map getAllRequestTotal(JSONObject objs);

    /**
     * 资产分配明细总个数,总金额
     * 
     * @Description: TODO
     * @param @param objs
     * @param @return
     * @return Map
     * @author chengzhen
     * @data
     */
    Map getAllReqAllotMoneyTotal(JSONObject objs);

    /**
     * 查询资产赎回明细信息
     * 
     * @param objs
     * @return
     */
    PageInfo<RequestMoneyBean> geAllotRedemptionInfo(JSONObject objs);

    /**
     * 
     * Description:资产赎回
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月12日
     */
    int allotRedemption(JSONObject objs) throws IqbException;
}
