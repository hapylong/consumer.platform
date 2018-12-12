package com.iqb.consumer.service.layer.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.biz.ProductBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.product.service.IProductService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;

@Service
public class ProductService extends BaseService implements IProductService {

    @Autowired
    ProductBiz productBiz;

    /**
     * 新增计划
     * 
     * @param id
     */
    @Override
    public int insertPlan(JSONObject objs) throws IqbException, IqbSqlException {
        return productBiz.insertPlan(objs);
    }

    /**
     * 修改计划
     * 
     * @param planBean
     * @return
     */
    @Override
    public int updatePlan(JSONObject objs) throws IqbException, IqbSqlException {
        return productBiz.updatePlan(objs);
    }

    /**
     * 根据ID删除计划
     * 
     * @param id
     */
    @Override
    public void deletePlanByID(JSONObject objs) throws IqbException, IqbSqlException {
        productBiz.deletePlan(objs);
    }

    /**
     * 根据计划号查询计划
     * 
     * @param id
     * @return
     */
    @Override
    public PlanBean getPlanByID(JSONObject objs) throws IqbException, IqbSqlException {
        return productBiz.getPlanByID(objs);
    }

    /**
     * 根据商户号查询分期计划
     * 
     * @param merchantNo
     * @return
     */
    @Override
    public PageInfo<PlanBean> getPlanByMerNo(JSONObject objs) throws IqbException, IqbSqlException {
        return new PageInfo<PlanBean>(productBiz.getPlanByMerNos(objs, true));
    }

    /**
     * 根据商户号列表查询分期计划
     * 
     * @param objs
     * @return
     */
    @Override
    public PageInfo<PlanBean> getPlanByMerNos(JSONObject objs) throws IqbException, IqbSqlException {
        // 获取商户权限的Objs
        objs = getMerchLimitObject(objs);
        if (objs == null) {
            return null;
        }
        List<PlanBean> list = productBiz.getPlanByMerNos(objs, true);
        PageInfo<PlanBean> p = new PageInfo<PlanBean>(list);
        return p;
    }

    private static final List<String> STATUS = new ArrayList<String>();
    static {
        STATUS.add("1");
        STATUS.add("2");
    }

    @Override
    public int updateStatus(JSONObject objs, String status) {
        List<String> ids = (List<String>) objs.get("id");
        if (ids == null || ids.size() == 0 || !STATUS.contains(status)) {
            return -1;
        }
        return productBiz.updateStatus(ids, status);
    }

    /**
     * Description:不同商户配置不同的支付主体,insert操作 2018年1月18日 10:34:23 chengzhen
     */
    public int addPayConf(JSONObject objs) throws IqbException, IqbSqlException {
        int result = 0;
        if (productBiz.getPayConfByMno2(objs) != null) {
            return result;
        }
        return productBiz.addPayConf(objs);
    }

    /**
     * Description:不同商户配置不同的支付主体,del操作 2018年1月18日 11:14:42 chengzhen
     */
    public void delPayConf(JSONObject objs) {
        productBiz.delPayConf(objs);
    }

    /**
     * Description:不同商户配置不同的支付主体,update操作 2018年1月18日 11:14:42 chengzhen
     */
    public void updatePayConf(JSONObject objs) {
        productBiz.updatePayConf(objs);
    }

    /**
     * Description:不同商户配置不同的支付主体,单操作 2018年1月18日 11:32:33 chengzhen
     */
    public PayConfBean getPayConfByMno(JSONObject objs) {
        return productBiz.getPayConfByMno(objs);
    }

    /**
     * Description:不同商户配置不同的支付主体,列表操作 2018年1月18日 11:38:22 chengzhen
     */
    public PageInfo<PayConfBean> queryPayConfList(JSONObject objs) {
        return new PageInfo<>(productBiz.queryPayConfList(objs));
    }

}
