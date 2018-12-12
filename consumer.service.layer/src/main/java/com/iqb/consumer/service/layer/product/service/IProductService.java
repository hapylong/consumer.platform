package com.iqb.consumer.service.layer.product.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;

public interface IProductService {

    /**
     * 新增计划
     * 
     * @param planBean
     * @return
     */
    int insertPlan(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 修改计划
     * 
     * @param planBean
     * @return
     */

    int updatePlan(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 根据ID删除计划
     * 
     * @param id
     */
    void deletePlanByID(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 根据计划号查询出计划
     * 
     * @param id
     * @return
     */
    PlanBean getPlanByID(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 根据商户号查询分期计划
     * 
     * @param merchantNo
     * @return
     */
    PageInfo<PlanBean> getPlanByMerNo(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 根据商户号列表查询分期计划
     * 
     * @param merchantNos
     * @return
     */
    PageInfo<PlanBean> getPlanByMerNos(JSONObject objs) throws IqbException, IqbSqlException;

    int updateStatus(JSONObject objs, String status);

    /**
     * Description:不同商户配置不同的支付主体,insert操作 2018年1月18日 10:34:23 chengzhen
     */
    int addPayConf(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * Description:不同商户配置不同的支付主体,del操作 2018年1月18日 10:34:23 chengzhen
     */
    void delPayConf(JSONObject objs);

    /**
     * Description:不同商户配置不同的支付主体,update操作 2018年1月18日 10:34:23 chengzhen
     */
    void updatePayConf(JSONObject objs);

    /**
     * Description:不同商户配置不同的支付主体,单查询操作 2018年1月18日 11:32:14 chengzhen
     */
    PayConfBean getPayConfByMno(JSONObject objs);

    /**
     * Description:不同商户配置不同的支付主体,列表查询操作 2018年1月18日 11:32:14 chengzhen
     */
    PageInfo<PayConfBean> queryPayConfList(JSONObject objs);
}
