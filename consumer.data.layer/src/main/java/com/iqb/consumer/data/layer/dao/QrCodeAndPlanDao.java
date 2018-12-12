/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年8月16日 上午10:30:56
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface QrCodeAndPlanDao {

    /**
     * 插入计划，返回计划编码
     * 
     * @param planBean
     * @return
     */
    int insertPlan(PlanBean planBean);

    /**
     * 修改计划
     * 
     * @param planBean
     * @return
     */
    int updatePlan(PlanBean planBean);

    /**
     * 根据商户号查询分期计划
     * 
     * @param merchantNo
     * @return
     */
    List<PlanBean> getPlanByMerNo(String merchantNo);

    /**
     * 通过子类型和商户号查询分期计划
     * 
     * @param merchantNo
     * @param bizType
     * @return
     */
    List<PlanBean> getPlByMerAndBType(@Param("merchantNo") String merchantNo, @Param("bizType") String bizType);

    /**
     * 根据商户号列表、期数查询分期计划
     * 
     * @param merchantNos
     * @return
     */
    List<PlanBean> getPlanByMerNos(Map<String, Object> map);

    /**
     * 根据计划号查询出计划
     * 
     * @param id
     * @return
     */
    PlanBean getPlanByID(long id);

    /**
     * 根据ID删除计划
     * 
     * @param id
     */
    void deletePlanByID(long id);

    /**
     * 获取主键id列表
     * 
     * @return
     */
    List<Long> getAllPlanIdList();

    int updateStatus(@Param("ids") List<String> ids, @Param("status") String status);

    /**
     * Description:不同商户配置不同的支付主体,insert操作 2018年1月18日 10:34:23 chengzhen
     */
    int addPayConf(JSONObject objs);

    /**
     * Description:不同商户配置不同的支付主体,判断该id在库中查询 2018年1月18日 10:34:23 chengzhen
     */
    PayConfBean getPayConfByMno(JSONObject objs);

    /**
     * Description:不同商户配置不同的支付主体,判断该商户号在库中查询 2018年1月18日 19:38:13 chengzhen
     */
    PayConfBean getPayConfByMno2(JSONObject objs);

    /**
     * Description:不同商户配置不同的支付主体,del操作 2018年1月18日 10:34:23 chengzhen
     */
    void delPayConf(JSONObject objs);

    /**
     * Description:不同商户配置不同的支付主体,update操作 2018年1月18日 11:23:01 chengzhen
     */
    void updatePayConf(JSONObject objs);

    /**
     * Description:不同商户配置不同的支付主体,列表操作 2018年1月18日 11:40:43 chengzhen
     */
    List<PayConfBean> queryPayConfList(JSONObject objs);

    /**
     * Description:根据字段code 和 子项号 查询对应字典中文 2018年1月18日 15:33:17 chengzhen
     */
    String getChinByCode(@Param("code") String code, @Param("code2") String code2);
}
