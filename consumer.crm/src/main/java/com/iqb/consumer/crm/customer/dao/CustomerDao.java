package com.iqb.consumer.crm.customer.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.bean.CustomerPushRecord;

/**
 * 
 * Description: 客户dao接口
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月19日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public interface CustomerDao {

    /**
     * 
     * Description: 保存来自etep推送的信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月21日 下午3:08:14
     */
    public void saveCustomerServiceFromEtep(CustomerBean customerBean);

    /**
     * 
     * Description: 根据客户编码获取客户信息
     * 
     * @param
     * @return CustomerBean
     * @throws
     * @Author wangxinbang Create Date: 2016年9月21日 下午7:57:57
     */
    public CustomerBean getCustomerInfoByCustmerCode(String customerCode);

    /**
     * 
     * Description: 插入客户推送记录
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月21日 下午8:46:31
     */
    public void insertCustomerPushRecord(CustomerPushRecord customerPushRecord);

    /**
     * 
     * Description: 更新来自etep的客户信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月22日 下午3:20:01
     */
    public void updateCustomerInfoFromEtep(CustomerBean customerBean);

    /**
     * 
     * Description: 查询客户信息列表
     * 
     * @param
     * @return List<CustomerBean>
     * @throws
     * @Author wangxinbang Create Date: 2016年9月22日 下午4:00:18
     */
    public List<CustomerBean> queryCustomerList(JSONObject objs);

    /**
     * 
     * Description: 删除客户信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月22日 下午4:21:41
     */
    public void deleteCustomerInfo(String customerCode);

    /**
     * 
     * Description: 插入客户基本信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月23日 下午2:13:32
     */
    public void insertCustomerBaseInfo(CustomerBean customerBean);

    /**
     * 
     * Description: 更新客户基本信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月23日 下午2:26:16
     */
    public void updateCustomerBaseInfo(CustomerBean customerBean);

    /**
     * 
     * Description: 删除客户企业相关信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年9月25日 下午1:48:53
     */
    public Integer deleteCustomerEnterpriseInfo(String customerCode);

    /**
     * 
     * Description: 删除消费金融客户相关信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年9月25日 下午1:50:26
     */
    public Integer deleteCustomerXFJRInfo(String customerCode);

    /**
     * 
     * Description: 插入客户企业相关信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月25日 下午1:52:24
     */
    public Integer insertCustomerEnterpriseInfo(CustomerBean customerBean);

    /**
     * 
     * Description: 插入消费金融客户相关信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年9月25日 下午1:53:06
     */
    public Integer insertCustomerXFJRInfo(CustomerBean customerBean);

    /**
     * 
     * Description: 更新客户企业相关信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月25日 下午2:19:04
     */
    public Integer updateCustomerEnterpriseInfo(CustomerBean customerBean);

    /**
     * 
     * Description: 更新消费金融客户相关信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月25日 下午2:19:04
     */
    public Integer updateCustomerXFJRInfo(CustomerBean customerBean);

    /**
     * 
     * Description: 根据客户编码更新推送时间
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年9月26日 下午3:16:11
     */
    public Integer updatePushTimeByCustmerCode(CustomerBean customerBean);

    /**
     * 
     * Description: 初始化一条消费金融数据
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月26日 下午4:49:39
     */
    public Integer insertXFJRCustomerForEtep(CustomerBean customerBean);

    /**
     * 
     * Description: 上传图片信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年10月8日 下午2:37:00
     */
    public Integer uploadCustomerImg(JSONObject objs);

    /**
     * 
     * Description: 上传公司印章
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年10月9日 上午9:59:41
     */
    public Integer uploadCustomerComponyImg(JSONObject objs);

    /**
     * 
     * Description: 上传法人印章
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年10月9日 上午10:00:34
     */
    public Integer uploadCustomerCorporateImg(JSONObject objs);

    /**
     * 
     * Description: 删除图片信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年10月8日 下午2:37:00
     */
    public Integer deleteCustomerImg(String customerCode);

    /**
     * 
     * Description: 删除公司印章
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年10月9日 上午10:02:21
     */
    public Integer deleteCustomerComponyImg(String customerCode);

    /**
     * 
     * Description: 删除法人印章
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年10月9日 上午10:02:27
     */
    public Integer deleteCustomerCorporateImg(String customerCode);

    /**
     * 
     * Description: 根据客户编码查询门店信息
     * 
     * @param
     * @return CustomerBean
     * @throws
     * @Author wangxinbang Create Date: 2016年10月11日 下午2:18:37
     */
    public CustomerBean getCustomerStoreInfoByCode(JSONObject objs);

    /**
     * 
     * Description: 根据客户编码简称获取客户信息
     * 
     * @param
     * @return CustomerBean
     * @throws
     * @Author wangxinbang Create Date: 2016年10月11日 下午5:00:07
     */
    public CustomerBean getCustomerInfoByCustmerShortNameCode(String customerShortNameCode);

    /**
     * 
     * Description: 获取市下拉框集合
     * 
     * @param
     * @return List<Map<String,Object>>
     * @throws
     * @Author wangxinbang Create Date: 2016年10月14日 下午12:15:26
     */
    public List<Map<String, Object>> getCustomerCityListByProvince(JSONObject objs);

    /**
     * 
     * Description:根据商户code查询债权人信息列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月3日
     */
    public CustomerBean getCustomerStoreInfoListByCode(String customerCode);

    /**
     * 
     * 
     * @Description: 根据商户code查询是否已经推送消费金融
     * @param customerBean
     * @return CustomerPushRecord
     * @author chengzhen
     * @data 2017年12月13日 11:06:16
     */
    public Integer selectCustomerPushRecordByCustomerCode(CustomerBean customerBean);

}
