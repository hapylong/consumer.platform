package com.iqb.consumer.crm.customer.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.crm.base.CrmAttr.CrmImgType;
import com.iqb.consumer.crm.base.CrmAttr.CrmPushCommonAttr;
import com.iqb.consumer.crm.base.CrmAttr.CrmPushReceiveAttr;
import com.iqb.consumer.crm.base.CrmAttr.HttpInterMode;
import com.iqb.consumer.crm.base.CrmReturnInfo;
import com.iqb.consumer.crm.base.config.CrmConfig;
import com.iqb.consumer.crm.crmpush.service.ICrmPushService;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.bean.CustomerPushRecord;
import com.iqb.consumer.crm.customer.biz.CustomerBiz;
import com.iqb.consumer.crm.customer.service.ICustomerService;
import com.iqb.consumer.data.layer.bean.ownerloan.MyCheckInfoBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.BeanUtil;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.GenerationUtil;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.common.utils.StringUtil;

/**
 * 
 * Description: 客户信息实现类
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
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class CustomerServiceImpl implements ICustomerService {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerBiz customerBiz;

    @Autowired
    private ICrmPushService crmPushServiceImpl;

    @Autowired
    private CrmConfig crmConfig;

    @Override
    public void saveCustomerServiceFromEtep(Map m) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(m)) {
            throw new IqbException(CrmReturnInfo.ETEP_PUSH_01020001);
        }
        this.customerBiz.saveCustomerServiceFromEtep(m);
    }

    @Override
    public void pushCustomerInfoToXFJR(Map<String, Object> m) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(m)) {
            throw new IqbException(CrmReturnInfo.CUSTOMER_PUSH_01030001);
        }
        CustomerBean customerBean = this.customerBiz.getCustomerInfoByCustmerCode(m);
        /** 校验返回信息 **/
        if (customerBean == null || StringUtil.isEmpty(customerBean.getCustomerCode())) {
            throw new IqbException(CrmReturnInfo.CUSTOMER_PUSH_01030002);
        }
        String riskFlag = m.get("riskFlag").toString();
        String riskCode = null != m.get("riskCode") ? (String) m.get("riskCode") : "";
        customerBean.setRiskFlag(riskFlag.equals("1") ? 1 : 2);
        customerBean.setRiskCode(riskCode);
        this.pushCustomerBean(customerBean);
    }

    private void pushCustomerBean(CustomerBean customerBean) throws IqbException {
        String uuid = GenerationUtil.getUUID();
        if (StringUtil.isEmpty(customerBean.getRequestType())) {
            /** 封装请求类型 **/
            customerBean.setRequestType(CrmPushCommonAttr.CRM_PUSH_TYPE_UPDATE);
            if (StringUtil.isEmpty(customerBean.getPushTime())) {
                customerBean.setRequestType(CrmPushCommonAttr.CRM_PUSH_TYPE_ADD);
            }
        }

        /** 封装记录信息 **/
        CustomerPushRecord customerPushRecord = new CustomerPushRecord();
        customerPushRecord.setCustomerCode(customerBean.getCustomerCode());
        customerPushRecord.setUuid(uuid);
        customerPushRecord.setPushType(Integer.parseInt(CrmPushCommonAttr.CRM_PUSH_TYPE_ADD));
        customerPushRecord.setPushTime(DateTools.getCurrTime());
        customerPushRecord.setReceive(CrmPushReceiveAttr.RECEIVE_XFJR);
        customerPushRecord.setPushStatus(Integer.parseInt(CrmPushCommonAttr.CRM_PUSH_STATUS_START));
        this.customerBiz.insertCustomerPushRecord(customerPushRecord);

        /** 开始推送客户信息 **/
        try {
            synchronized (this) {
                logger.info("推送crm客户信息开始，客户信息" + JSONUtil.objToJson(customerPushRecord));
                String retStr = "";
                if (HttpInterMode.HTTPINTERMODE_HTTP.equals(crmConfig.getHttpInterfaceInteractionMode())) {
                    retStr =
                            this.crmPushServiceImpl.crmPushByPost(crmConfig.getUrlCrmCustomerCfmPush(),
                                    this.CustomerBeanToXFJRMap(customerBean));
                }
                if (HttpInterMode.HTTPINTERMODE_HTTPS.equals(crmConfig.getHttpInterfaceInteractionMode())) {
                    retStr =
                            this.crmPushServiceImpl.crmPushByPostByHttps(crmConfig.getUrlCrmCustomerCfmPush(),
                                    this.CustomerBeanToXFJRMap(customerBean));
                }
                /** 如果返回值为空，则抛出异常 **/
                logger.info("消费金融返回信息：" + retStr);
                if (StringUtil.isEmpty(retStr)) {
                    throw new IqbException(CrmReturnInfo.CUSTOMER_PUSH_01030003);
                }
                JSONObject retMap = JSONUtil.strToJSON(retStr);
                if (!CollectionUtils.isEmpty(retMap)
                        && CrmPushCommonAttr.CRM_PUSH_STATUS_FAIL.equals(retMap.getString("success"))) {
                    throw new IqbException(CrmReturnInfo.CUSTOMER_PUSH_01030003);
                }
                Map iqbResult = (Map) retMap.get("iqbResult");
                if (!CollectionUtils.isEmpty(iqbResult)
                        && !CrmPushCommonAttr.CRM_PUSH_XFJR_SUCC_RESULT.equals(iqbResult.get("result"))) {
                    throw new IqbException(CrmReturnInfo.CUSTOMER_PUSH_01030003);
                }
                this.customerBiz.updatePushTimeByCustmerCode(customerBean);
            }
        } catch (Exception e) {
            /** 推送异常 **/
            logger.error("推送crm客户信息至" + customerPushRecord.getReceive() + "异常", e);
            customerPushRecord.setPushTime(DateTools.getCurrTime());
            customerPushRecord.setPushStatus(Integer.parseInt(CrmPushCommonAttr.CRM_PUSH_STATUS_ERROE));
            this.customerBiz.insertCustomerPushRecord(customerPushRecord);
            throw new IqbException(CrmReturnInfo.CUSTOMER_PUSH_01030003);
        }

        /** 推送结束 **/
        customerPushRecord.setPushTime(DateTools.getCurrTime());
        customerPushRecord.setPushStatus(Integer.parseInt(CrmPushCommonAttr.CRM_PUSH_STATUS_END));
        this.customerBiz.insertCustomerPushRecord(customerPushRecord);
    }

    /**
     * 
     * Description: 将客户信息bean转成消费金融map
     * 
     * @param
     * @return Map
     * @throws
     * @Author wangxinbang Create Date: 2016年9月22日 下午2:28:01
     */
    private Map CustomerBeanToXFJRMap(CustomerBean customerBean) {
        Map retMap = new HashMap();
        retMap.put("id", customerBean.getCustomerCode());
        retMap.put("merchCode", customerBean.getCustomerShortNameCode());
        retMap.put("merchShortName", customerBean.getCustomerShortName());
        retMap.put("merchantFullName", customerBean.getCustomerFullName());
        retMap.put("publicNo", customerBean.getHoldWeixin());
        retMap.put("province", customerBean.getProvince());
        retMap.put("city", customerBean.getCity());
        retMap.put("merchAddr", customerBean.getAddressDetail());
        retMap.put("riskType", customerBean.getRiskManageType());
        retMap.put("parentId", customerBean.getHigherOrgCode());
        retMap.put("level", customerBean.getLayer());
        retMap.put("instalNo", customerBean.getInstallmentPlan());
        retMap.put("overdueRate", customerBean.getOverdueInterestRate());
        retMap.put("overdueFee", customerBean.getOverdueFixedFee());
        retMap.put("overdueType", customerBean.getOverdueInterestModel());
        retMap.put("enabled", customerBean.getIsVirtualMerc());
        retMap.put("actcode", customerBean.getRequestType());
        retMap.put("businessType", customerBean.getBusinessType());
        retMap.put("riskFlag", customerBean.getRiskFlag());
        retMap.put("riskCode", customerBean.getRiskCode());
        return retMap;
    }

    @Override
    public void updateCustomerInfoFromEtep(Map m) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(m)) {
            throw new IqbException(CrmReturnInfo.ETEP_PUSH_01020001);
        }
        this.customerBiz.updateCustomerInfoFromEtep(m);
    }

    @Override
    public PageInfo queryCustomerList(JSONObject objs) throws IqbException {
        return new PageInfo<CustomerBean>(this.customerBiz.queryCustomerList(objs));
    }

    @Override
    public void deleteCustomerInfo(JSONObject objs) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            throw new IqbException(CrmReturnInfo.CUSTOMER_PUSH_01030001);
        }
        CustomerBean customerBean = BeanUtil.mapToBean(objs, CustomerBean.class);
        /** 校验数据完整性 **/
        if (customerBean == null || StringUtil.isEmpty(customerBean.getCustomerCode())) {
            throw new IqbException(CrmReturnInfo.COMMON_01010001);
        }
        CustomerBean dbCustomerBean = this.customerBiz.getCustomerInfoByCustmerCode(objs);
        /** 已推送消费金融的商户，不可删除 2017年12月18日 11:01:41 chengzhen **/
        Integer customerPushRecordCount =
                this.customerBiz.selectCustomerPushRecordByCustomerCode(dbCustomerBean);
        if (customerPushRecordCount > 0) {// 说明该商户已经推送消费金融
            throw new IqbException(CrmReturnInfo.CRM_CUSTOMER_01040007);
        }
        this.customerBiz.deleteCustomerInfo(customerBean.getCustomerCode());
        /** 校验返回信息 **/
        // 修改BY chengzhen 2017年12月18日 11:17:00
        /*
         * if (dbCustomerBean == null || StringUtil.isEmpty(dbCustomerBean.getCustomerCode())) {
         * return; } if (StringUtil.isEmpty(dbCustomerBean.getPushTime())) { return; }
         * customerBean.setRequestType(CrmPushCommonAttr.CRM_PUSH_TYPE_DELETE);
         * this.pushCustomerBean(customerBean);
         */
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertCustomerInfo(JSONObject objs) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            throw new IqbException(CrmReturnInfo.CRM_CUSTOMER_01040001);
        }
        Map m = this.clearEmptyFromMapForCustomer(objs);
        this.customerBiz.insertCustomerBaseInfo(m);
        this.customerBiz.insertCustomerEnterpriseInfo(m);
        this.customerBiz.insertCustomerXFJRInfo(m);
        // 调用推送消费金融接口,将客户信息保存至客户信息表中
        pushCustomerInfoToXFJR(objs);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerInfo(JSONObject objs) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            throw new IqbException(CrmReturnInfo.CRM_CUSTOMER_01040001);
        }
        this.customerBiz.updateCustomerBaseInfo(objs);
        // 调用推送消费金融接口,将客户信息保存至客户信息表中
        pushCustomerInfoToXFJR(objs);
    }

    @Override
    public Map getCustomerInfoByCustomerCode(JSONObject objs) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            throw new IqbException(CrmReturnInfo.CRM_CUSTOMER_01040001);
        }
        CustomerBean bean = this.customerBiz.getCustomerInfoByCustmerCode(objs);
        if (bean == null) {
            throw new IqbException(CrmReturnInfo.CRM_CUSTOMER_01040003);
        }
        return BeanUtil.entity2map(bean);
    }

    /**
     * 
     * Description: 清空map中的空值
     * 
     * @param
     * @return Map
     * @throws
     * @Author wangxinbang Create Date: 2016年9月25日 下午3:13:39
     */
    @SuppressWarnings("unused")
    private Map clearEmptyFromMap(Map m) {
        Map retMap = new HashMap();
        Set<String> keySet = m.keySet();
        for (String key : keySet) {
            if (m.get(key) != null && StringUtil.isNotEmpty((String) m.get(key))) {
                retMap.put(key, m.get(key));
            }
        }
        return retMap;
    }

    /**
     * 用于特殊类型 集合的处理
     * 
     * @param objs
     * @param request
     * @return
     */
    private Map clearEmptyFromMapForCustomer(Map m) {
        Map retMap = new HashMap();
        Set<String> keySet = m.keySet();
        for (String key : keySet) {
            if (key.equals("customerType") || key.equals("businessType")) {
                retMap.put(key, m.get(key));
            } else {
                if (m.get(key) != null && StringUtil.isNotEmpty((String) m.get(key))) {
                    retMap.put(key, m.get(key));
                }
            }

        }
        return retMap;
    }

    @Override
    public List<CustomerBean> getCustomerInfoByCustomerType(JSONObject objs) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            throw new IqbException(CrmReturnInfo.CRM_CUSTOMER_01040001);
        }
        /** 校验客户类型 **/
        if (StringUtil.isEmpty(objs.getString("customerType"))) {
            throw new IqbException(CrmReturnInfo.CRM_CUSTOMER_01040005);
        }
        return this.customerBiz.getCustomerInfoByCustomerType(objs);
    }

    @Override
    public void uploadCustomerImg(JSONObject objs) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            logger.error("客户图片上传异常：传入信息为空");
            return;
        }
        /** 校验客户Id **/
        if (StringUtil.isEmpty(objs.getString("customerCode"))) {
            logger.error("客户图片上传异常：传入客户code为空");
            return;
        }
        /** 校验图片路径 **/
        if (StringUtil.isEmpty(objs.getString("imgUrl"))) {
            logger.error("客户图片上传异常：传入图片路径为空");
            return;
        }
        /** 校验图片类型 **/
        if (StringUtil.isEmpty(objs.getString("imgType"))) {
            logger.error("客户图片上传异常：传入图片类型为空");
            return;
        }
        /** 获取图片类型 1：公司印章 2：法人印章 **/
        String imgType = objs.getString("imgType");
        if (CrmImgType.IMG_TYPE_COMPONY_IMG.equals(imgType)) {
            this.customerBiz.uploadCustomerComponyImg(objs);
        }
        if (CrmImgType.IMG_TYPE_CORPORATE_IMG.equals(imgType)) {
            this.customerBiz.uploadCustomerCorporateImg(objs);
        }
    }

    @Override
    public void deleteCustomerImg(JSONObject objs) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            logger.error("客户图片删除异常：传入信息为空");
            return;
        }
        /** 校验客户Id **/
        if (StringUtil.isEmpty(objs.getString("customerCode"))) {
            logger.error("客户图片删除异常：传入客户code为空");
            return;
        }
        /** 获取图片类型 1：公司印章 2：法人印章 **/
        String imgType = objs.getString("imgType");
        if (CrmImgType.IMG_TYPE_COMPONY_IMG.equals(imgType)) {
            this.customerBiz.deleteCustomerComponyImg(objs.getString("customerCode"));
        }
        if (CrmImgType.IMG_TYPE_CORPORATE_IMG.equals(imgType)) {
            this.customerBiz.deleteCustomerCorporateImg(objs.getString("customerCode"));
        }
    }

    @Override
    public Map getCustomerStoreInfoByCode(JSONObject objs) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            logger.error("门店查询接口：传入查询信息为空");
            throw new IqbException(CrmReturnInfo.CRM_CUSTOMER_STORE_01050001);
        }
        if (StringUtil.isEmpty(objs.getString("customerCode"))) {
            logger.error("门店查询接口：传入查询信息客户编码为空");
            throw new IqbException(CrmReturnInfo.CRM_CUSTOMER_STORE_01050002);
        }
        CustomerBean bean = this.customerBiz.getCustomerStoreInfoByCode(objs);
        if (bean == null) {
            logger.error("门店查询接口：查询结果为空");
            return new HashMap();
        }
        return BeanUtil.entity2map(bean);
    }

    @Override
    public List<Map<String, Object>> getCustomerCityListByProvince(JSONObject objs) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            logger.error("获取市下拉框集合：传入查询信息为空");
            return new ArrayList<Map<String, Object>>();
        }
        if (StringUtil.isEmpty(objs.getString("provinceName"))) {
            logger.error("获取市下拉框集合：传入查询信息客户编码为空");
            return new ArrayList<Map<String, Object>>();
        }
        List<Map<String, Object>> cityList = this.customerBiz.getCustomerCityListByProvince(objs);
        return cityList;
    }

    /**
     * 根据商户code查询债权人信息列表
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月3日
     */
    @Override
    public CustomerBean getCustomerStoreInfoListByCode(String customerCode) {
        return customerBiz.getCustomerStoreInfoListByCode(customerCode);
    }

    /**
     * FINANCE-2784 FINANCE-2690 蒲公英个人客户信息查询
     */
    @Override
    public PageInfo<MyCheckInfoBean> getDandelionCustomerList(JSONObject objs) {
        List<UserBean> dandelionCustomerList = customerBiz.getDandelionCustomerList(objs, true);
        List<MyCheckInfoBean> checkInfoBeanList = new ArrayList<>();
        for (UserBean userBean : dandelionCustomerList) {
            String checkInfo = userBean.getCheckInfo();
            JSONObject jsb = JSONUtil.strToJSON(checkInfo);
            MyCheckInfoBean checkBean = JSONObject.toJavaObject(jsb, MyCheckInfoBean.class);
            checkInfoBeanList.add(checkBean);
        }
        PageInfo<UserBean> urbp = new PageInfo<>(dandelionCustomerList);
        PageInfo<MyCheckInfoBean> urp = new PageInfo<>(checkInfoBeanList);
        urp.setEndRow(urbp.getEndRow());
        urp.setFirstPage(urbp.getFirstPage());
        urp.setHasNextPage(urbp.isHasNextPage());
        urp.setHasPreviousPage(urbp.isHasPreviousPage());
        urp.setIsFirstPage(urbp.isIsFirstPage());
        urp.setIsLastPage(urbp.isIsLastPage());
        urp.setNavigatepageNums(urbp.getNavigatepageNums());
        urp.setNextPage(urbp.getNextPage());
        urp.setOrderBy(urbp.getOrderBy());
        urp.setPageNum(urbp.getPageNum());
        urp.setPages(urbp.getPages());
        urp.setPageSize(urbp.getPageSize());
        urp.setPrePage(urbp.getPrePage());
        urp.setSize(urbp.getSize());
        urp.setStartRow(urbp.getStartRow());
        urp.setTotal(urbp.getTotal());
        return urp;
    }

    /**
     * FINANCE-2784 FINANCE-2690 蒲公英个人客户信息明细查询
     */
    @Override
    public MyCheckInfoBean getDandelionCustomerDetail(JSONObject objs) {
        return customerBiz.getDandelionCustomerDetail(objs);
    }

}
