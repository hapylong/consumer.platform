package com.iqb.consumer.crm.customer.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.data.layer.bean.ownerloan.CheckInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.MyCheckInfoBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.etep.common.exception.IqbException;

/**
 * 
 * Description: 客户信息服务接口
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
@SuppressWarnings({"rawtypes"})
public interface ICustomerService {

    /**
     * 
     * Description: 保存etep推送过来的客户信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月19日 下午7:23:15
     */
    public void saveCustomerServiceFromEtep(Map m) throws IqbException;

    /**
     * 
     * Description: 推送客户信息到消费金融
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月21日 下午7:51:19
     */
    public void pushCustomerInfoToXFJR(Map<String, Object> m) throws IqbException;

    /**
     * 
     * Description: 更新来自etep的客户信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月22日 下午3:15:21
     */
    public void updateCustomerInfoFromEtep(Map m) throws IqbException;

    /**
     * 
     * Description: 查询客户信息列表
     * 
     * @param
     * @return PageInfo
     * @throws
     * @Author wangxinbang Create Date: 2016年9月22日 下午3:51:49
     */
    public PageInfo queryCustomerList(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 删除客户信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月22日 下午4:16:28
     */
    public void deleteCustomerInfo(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: deleteCustomerInfo
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月23日 下午2:02:54
     */
    public void insertCustomerInfo(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 更新客户信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年9月23日 下午2:24:15
     */
    public void updateCustomerInfo(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 根据客户code获取客户信息
     * 
     * @param
     * @return Map
     * @throws
     * @Author wangxinbang Create Date: 2016年9月23日 下午3:15:45
     */
    public Map getCustomerInfoByCustomerCode(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 根据客户类型获取客户信息
     * 
     * @param
     * @return List<CustomerBean>
     * @throws
     * @Author wangxinbang Create Date: 2016年9月27日 下午6:03:50
     */
    public List<CustomerBean> getCustomerInfoByCustomerType(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 上传图片信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年10月8日 下午2:33:27
     */
    public void uploadCustomerImg(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 删除图片信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年10月8日 下午2:45:17
     */
    public void deleteCustomerImg(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 根据客户编码查询门店信息
     * 
     * @param
     * @return Map
     * @throws
     * @Author wangxinbang Create Date: 2016年10月11日 下午2:12:04
     */
    public Map getCustomerStoreInfoByCode(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 获取市下拉框集合
     * 
     * @param
     * @return Map
     * @throws
     * @Author wangxinbang Create Date: 2016年10月14日 下午12:09:47
     */
    public List<Map<String, Object>> getCustomerCityListByProvince(JSONObject objs) throws IqbException;

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
     * @Description: FINANCE-2784 FINANCE-2690 蒲公英个人客户信息查询
     * @param @param objs
     * @param @return
     * @return List<Map<String,Object>>
     * @author chengzhen
     * @data 2018年1月8日 15:15:26
     */
    public PageInfo<MyCheckInfoBean> getDandelionCustomerList(JSONObject objs);

    /**
     * 
     * @Description: FINANCE-2784 FINANCE-2690 蒲公英个人客户信息明细
     * @param @param objs
     * @param @return
     * @return CheckInfoBean
     * @author chengzhen
     * @data 2018年1月8日 17:38:53
     */
    public MyCheckInfoBean getDandelionCustomerDetail(JSONObject objs);

}
