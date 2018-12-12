package com.iqb.consumer.crm.store.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.crm.customer.bean.IqbCustomerStoreInfoEntity;
import com.iqb.consumer.crm.customer.bean.pojo.CustomerInfoResponsePojo;
import com.iqb.etep.common.exception.IqbException;

/**
 * 
 * Description: 门店信息维护service
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年10月12日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@SuppressWarnings({"rawtypes"})
public interface ICustomerStoreService {

    /**
     * 
     * Description: 获取门店信息列表
     * 
     * @param
     * @return PageInfo
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 上午10:15:16
     */
    public PageInfo getCustomerStoreInfoList(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 根据客户编码获取门店信息
     * 
     * @param
     * @return Map
     * @throws GenerallyException
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 上午11:32:39
     */
    public CustomerInfoResponsePojo getCustomerStoreInfoByCode(JSONObject objs) throws GenerallyException;

    /**
     * 
     * Description: 更新门店信息
     * 
     * @param
     * @return Map
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午2:52:32
     */
    public void updateCustomerStoreInfo(JSONObject objs) throws GenerallyException;

    /**
     * 
     * Description: 更新门店审核信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午3:51:32
     */
    public void updateCustomerStoreAuditInfo(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: FINANCE-1204 质押车项目终审债权人信息可修改 FINANCE-1221 债权人修改
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年6月9日 下午6:40:22
     */
    public void updateCustomerInfo(JSONObject objs) throws GenerallyException;

    /**
     * 
     * Description: FINANCE-1204 质押车项目终审债权人信息可修改 ， 通过orderId 查找 债权人信息
     * 
     * @param
     * @return CustomerInfoResponsePojo
     * @throws
     * @Author adam Create Date: 2017年6月12日 上午11:09:11
     */
    public CustomerInfoResponsePojo getCustomerStoreInfoByOid(String orderId) throws GenerallyException;

    CustomerInfoResponsePojo getCustomerStoreInfoByJysOid(String orderId) throws GenerallyException;

    /**
     * 
     * Description:根据订单号查询债权人信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月7日
     */
    public IqbCustomerStoreInfoEntity getCreditInfoByOrderId(JSONObject objs);

    /**
     * 
     * Description:根据交易所订单表id查询债权人信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月9日
     */
    public IqbCustomerStoreInfoEntity getCreditInfoById(JSONObject objs);
}
