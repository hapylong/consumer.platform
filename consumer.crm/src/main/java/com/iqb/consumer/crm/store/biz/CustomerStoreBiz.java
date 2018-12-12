package com.iqb.consumer.crm.store.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.crm.base.CrmBaseBiz;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.bean.IqbCustomerStoreInfoEntity;
import com.iqb.consumer.crm.customer.bean.pojo.UpdateCustomerInfoRequestPojo;
import com.iqb.consumer.crm.store.dao.CustomerStoreDao;
import com.iqb.etep.common.utils.BeanUtil;

/**
 * 
 * Description: 门店信息biz
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
@Component
public class CustomerStoreBiz extends CrmBaseBiz {

    /**
     * 门店信息dao
     */
    @Autowired
    private CustomerStoreDao customerStoreDao;

    /**
     * 
     * Description: 获取门店信息列表
     * 
     * @param
     * @return PageInfo
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 上午10:19:23
     */
    public List<CustomerBean> getCustomerStoreInfoList(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return customerStoreDao.getCustomerStoreInfoList(objs);
    }

    /**
     * 
     * Description: 根据客户编码查询门店信息
     * 
     * @param
     * @return CustomerBean
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 上午11:35:26
     */
    public CustomerBean getCustomerStoreInfoByCode(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        return customerStoreDao.getCustomerStoreInfoByCode(objs);
    }

    /**
     * 
     * Description: 根据用户编码查询用户存储信息
     * 
     * @param
     * @return CustomerBean
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午6:33:24
     */
    public CustomerBean getCustomerStoreDbInfoByCode(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        return customerStoreDao.getCustomerStoreDbInfoByCode(objs);
    }

    /**
     * 
     * Description: 更新门店信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午2:54:50
     */
    public Integer updateCustomerStoreInfo(IqbCustomerStoreInfoEntity ic) {
        super.setDb(0, super.MASTER);
        return this.customerStoreDao.updateCustomerStoreInfo(ic);
    }

    /**
     * 
     * Description: 更新门店审核信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午3:52:29
     */
    public Integer updateCustomerStoreAuditInfo(JSONObject objs) {
        CustomerBean customerBean = BeanUtil.mapToBean(objs, CustomerBean.class);
        super.setDb(0, super.MASTER);
        return this.customerStoreDao.updateCustomerStoreAuditInfo(customerBean);
    }

    /**
     * 
     * Description: 新增门店信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午6:13:52
     */
    public Integer insertCustomerStoreInfo(IqbCustomerStoreInfoEntity ic) {
        super.setDb(0, super.MASTER);
        return this.customerStoreDao.insertCustomerStoreInfo(ic);
    }

    public void updateCustomerInfo(UpdateCustomerInfoRequestPojo ucir) {
        super.setDb(0, super.MASTER);
        customerStoreDao.updateCustomerInfo(ucir);
    }

    public JSONObject getCustomerCodeByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return customerStoreDao.getCustomerCodeByOid(orderId);
    }

    public JSONObject getCustomerCodeByJysOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return customerStoreDao.getCustomerCodeByJysOid(orderId);
    }

    /**
     * 
     * Description:根据订单号查询债权人信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月7日
     */
    public IqbCustomerStoreInfoEntity getCreditInfoByOrderId(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        return customerStoreDao.getCreditInfoByOrderId(objs);
    }

    /**
     * 
     * Description:根据交易所订单表id查询债权人
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月9日
     */
    public IqbCustomerStoreInfoEntity getCreditInfoById(JSONObject objs) {
        super.setDb(1, super.SLAVE);
        return customerStoreDao.getCreditInfoById(objs);
    }
}
