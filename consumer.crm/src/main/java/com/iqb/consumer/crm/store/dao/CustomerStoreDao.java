package com.iqb.consumer.crm.store.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.bean.IqbCustomerStoreInfoEntity;
import com.iqb.consumer.crm.customer.bean.pojo.UpdateCustomerInfoRequestPojo;

/**
 * 
 * Description: 门店信息dao
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
public interface CustomerStoreDao {

    /**
     * 
     * Description: 获取门店信息列表
     * 
     * @param
     * @return PageInfo
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 上午10:22:01
     */
    public List<CustomerBean> getCustomerStoreInfoList(JSONObject objs);

    /**
     * 
     * Description: 根据客户编码查询门店信息
     * 
     * @param
     * @return CustomerBean
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 上午11:36:02
     */
    public CustomerBean getCustomerStoreInfoByCode(JSONObject objs);

    /**
     * 
     * Description: 更新门店信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午2:56:20
     */
    public Integer updateCustomerStoreInfo(IqbCustomerStoreInfoEntity ic);

    /**
     * 
     * Description: 更新门店审核信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午3:53:17
     */
    public Integer updateCustomerStoreAuditInfo(CustomerBean customerBean);

    /**
     * 
     * Description: 新增门店信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午6:14:31
     * @UpdateAuthor adam
     */
    public Integer insertCustomerStoreInfo(IqbCustomerStoreInfoEntity icsi);

    /**
     * 
     * Description: 根据用户编码查询用户存储信息
     * 
     * @param
     * @return CustomerBean
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午6:33:51
     */
    public CustomerBean getCustomerStoreDbInfoByCode(JSONObject objs);

    public void updateCustomerInfo(UpdateCustomerInfoRequestPojo ucir);

    /**
     * 
     * Description: 通过orderid 获取 customerCode
     * 
     * @param
     * @return JSONObject
     * @throws
     * @Author adam Create Date: 2017年6月12日 上午11:15:30
     */
    public JSONObject getCustomerCodeByOid(String orderId);

    public JSONObject getCustomerCodeByJysOid(String orderId);

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
