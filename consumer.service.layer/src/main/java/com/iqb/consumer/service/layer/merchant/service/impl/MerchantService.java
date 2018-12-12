package com.iqb.consumer.service.layer.merchant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.service.layer.merchant.service.IMerchantService;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;

@Service("merchantService")
public class MerchantService extends BaseService implements IMerchantService {

    @Autowired
    private MerchantBeanBiz merchantBeanBiz;

    @Override
    public List<MerchantBean> getMerListByCity(JSONObject objs) {
        return merchantBeanBiz.getMerListByCity(objs);
    }

    @Override
    public List<MerchantBean> getAllMerByID(JSONObject objs) {
        return merchantBeanBiz.getAllMerByID(objs);
    }

    @Override
    public List<MerchantBean> getAllMerByMerNo(JSONObject objs) {
        return merchantBeanBiz.getAllMerByMerNo(objs);
    }

    @Override
    public MerchantBean getMerByID(JSONObject objs) {
        return merchantBeanBiz.getMerByID(objs);
    }

    @Override
    public MerchantBean getMerByMerNo(String merchantNo) {
        return merchantBeanBiz.getMerByMerNo(merchantNo);
    }

    @Override
    public int insertMerchantInfo(JSONObject objs) throws IqbException, IqbSqlException {
        return merchantBeanBiz.insertMerchantInfo(objs);
    }

    @Override
    public void updateMerchantInfo(JSONObject objs) throws IqbException, IqbSqlException {}

    @Override
    public void delMerchantInfo(JSONObject objs) throws IqbException, IqbSqlException {
        merchantBeanBiz.delMerchantInfo(objs);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年5月26日
     */
    @Override
    public MerchantBean getMerByMerName(String merchantName) {
        return merchantBeanBiz.getMerByMerName(merchantName);
    }

}
