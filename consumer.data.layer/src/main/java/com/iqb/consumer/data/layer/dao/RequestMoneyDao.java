/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月27日 下午4:02:23
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.reqmoney.RequestMoneyBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface RequestMoneyDao {

    /**
     * 查询情况条件
     * 
     * @param objs
     * @return
     */
    List<RequestMoneyBean> getAllRequest(JSONObject objs);

    /**
     * 资产分配保存
     * 
     * @param requestMoneyBean
     * @return
     */
    long insertReqMoney(RequestMoneyBean requestMoneyBean);

    /**
     * 
     * Description:查询资产分配详情
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月4日
     */
    List<RequestMoneyBean> getAllotDetailByOrderId(JSONObject objs);

    /**
     * 
     * Description:资产分配明细查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月5日
     */
    List<RequestMoneyBean> getAllReqAllotMoney(JSONObject objs);

    /**
     * 
     * Description:根据订单号查询资产分配来源 上标id
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月13日
     */
    RequestMoneyBean getRequestMoneyByOrderId(Map<String, Object> params);

    /**
     * 资产分配查询总个数,总金额
     * 
     * @Description: TODO
     * @param @param objs
     * @param @return
     * @return Map
     * @author chengzhen
     * @data
     */
    @SuppressWarnings("rawtypes")
    Map getAllRequestTotal(JSONObject objs);

    /**
     * 资产分配明细查询总个数,总金额
     * 
     * @Description: TODO
     * @param @param objs
     * @param @return
     * @return Map
     * @author chengzhen
     * @data
     */
    @SuppressWarnings("rawtypes")
    Map getAllReqAllotMoneyTotal(JSONObject objs);

    /**
     * 
     * Description:资产赎回查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月12日
     */
    List<RequestMoneyBean> geAllotRedemptionInfo(JSONObject objs);

    /**
     * 
     * Description:更新资产分配信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月12日
     */
    int updateRequestmoneyById(RequestMoneyBean bean);

    /**
     * 
     * Description:根据id查询资产分配信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月12日
     */
    RequestMoneyBean getRequestMoneyById(Integer id);
}
