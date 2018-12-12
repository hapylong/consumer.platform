/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午8:24:47
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.admin.entity.IqbCustomerPermissionEntity;
import com.iqb.consumer.data.layer.bean.admin.pojo.MerchantTreePojo;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface MerchantBeanDao {
    /**
     * 查询所有加盟商
     * 
     * @param city
     * @return
     */
    List<MerchantBean> getMerListByCity(@Param("city") String city);

    /**
     * 查询所有加盟商
     * 
     * @param id
     * @return
     */
    List<MerchantBean> getAllMerByID(@Param("id") String id);

    List<String> getAllMerchantNosByOrgCode(@Param("id") String id);

    /**
     * 通过商户们查询加盟商
     * 
     * @param merchantNo
     * @return
     */
    List<MerchantBean> getAllMerByMerNo(@Param("merchantNo") String merchantNo);

    /**
     * 根据ID查询商户信息
     * 
     * @param id
     * @return
     */
    MerchantBean getMerByID(@Param("id") String id);

    /**
     * 根据商户号查询商户信息
     * 
     * @param merchantNo
     * @return
     */
    MerchantBean getMerByMerNo(@Param("merchantNo") String merchantNo);

    /**
     * 新增商户
     * 
     * @param bean
     * @return
     */
    int insertMerchantInfo(MerchantBean bean);

    /**
     * 修改商户信息
     * 
     * @param bean
     */
    void updateMerchantInfo(MerchantBean bean);

    /**
     * 删除商户
     * 
     * @param bean
     */
    void delMerchantInfo(MerchantBean bean);

    /**
     * 根据商户简称查询商户号
     * 
     * @param merchantShortName
     * @return
     */
    String getMerCodeByMerSortName(@Param("merchantShortName") String merchantShortName);

    /**
     * 新版 根据商户简称列表 查询 商户号列表
     * 
     * @param merchantShortName
     * @return
     */
    List<MerchantBean> getMerCodeByMerSortNameList(@Param("merchNames") String[] merchNames);

    List<String> getAllMerchantNosByNames(@Param("merchNames") String[] mns);

    /**
     * 获取说有商户No
     * 
     * @return
     */
    public List<String> getAllMerchantNoList();

    /**
     * 根据商户号查询商户信息
     * 
     * @param merchantNo
     * @return
     */
    MerchantBean getMerByMerName(@Param("merchantShortName") String merchantShortName);

    IqbCustomerPermissionEntity getICPEByOrgCode(String orgCode);

    List<MerchantBean> getMBListByATList(List<MerchantTreePojo> authorityTreeList);

}
