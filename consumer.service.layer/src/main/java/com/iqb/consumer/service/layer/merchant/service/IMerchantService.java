package com.iqb.consumer.service.layer.merchant.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;

public interface IMerchantService {

    /**
     * 查询所有加盟商
     * 
     * @param city
     * @return
     */
    List<MerchantBean> getMerListByCity(JSONObject objs);

    /**
     * 查询通过商户ID所有子商户包含自己
     * 
     * @param id
     * @return
     */
    List<MerchantBean> getAllMerByID(JSONObject objs);

    /**
     * 通过商户号查询加盟商
     * 
     * @param merchantNo
     * @return
     */
    List<MerchantBean> getAllMerByMerNo(JSONObject objs);

    /**
     * 根据ID查询商户信息
     * 
     * @param id
     * @return
     */
    MerchantBean getMerByID(JSONObject objs);

    /**
     * 根据商户号查询商户信息
     * 
     * @param merchantNo
     * @return
     */
    MerchantBean getMerByMerNo(String merchantNo);

    /**
     * 新增商户
     * 
     * @param bean
     * @return
     */
    int insertMerchantInfo(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 修改商户信息
     * 
     * @param bean
     */
    void updateMerchantInfo(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 删除商户
     * 
     * @param bean
     */
    void delMerchantInfo(JSONObject objs) throws IqbException, IqbSqlException;

    /**
     * 根据商户名称查询商户信息
     * 
     * @param merchantName
     * @return
     */
    MerchantBean getMerByMerName(String merchantName);
}
