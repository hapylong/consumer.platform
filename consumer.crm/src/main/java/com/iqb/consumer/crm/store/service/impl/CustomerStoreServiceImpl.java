package com.iqb.consumer.crm.store.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.crm.base.CrmReturnInfo;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.bean.IqbCustomerStoreInfoEntity;
import com.iqb.consumer.crm.customer.bean.pojo.CustomerInfoResponsePojo;
import com.iqb.consumer.crm.customer.bean.pojo.IqbCustomerInfoPojo;
import com.iqb.consumer.crm.customer.bean.pojo.UpdateCustomerInfoRequestPojo;
import com.iqb.consumer.crm.store.biz.CustomerStoreBiz;
import com.iqb.consumer.crm.store.service.ICustomerStoreService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.SysUserSession;

import jodd.util.StringUtil;

/**
 * 
 * Description: 门店信息维护service实现
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
@Service
public class CustomerStoreServiceImpl implements ICustomerStoreService {
    private static Logger logger = LoggerFactory.getLogger(CustomerStoreServiceImpl.class);

    @Autowired
    private CustomerStoreBiz customerStoreBiz;

    @Autowired
    private SysUserSession userSession;

    @Override
    public PageInfo getCustomerStoreInfoList(JSONObject objs) throws IqbException {
        objs.put("customerCode", userSession.getOrgCode());
        return new PageInfo<>(customerStoreBiz.getCustomerStoreInfoList(objs));
    }

    @Override
    public CustomerInfoResponsePojo getCustomerStoreInfoByCode(JSONObject objs) throws GenerallyException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        CustomerBean cb = this.customerStoreBiz.getCustomerStoreInfoByCode(objs);
        if (cb == null) {
            throw new GenerallyException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.A);
        }
        CustomerInfoResponsePojo cirp = new CustomerInfoResponsePojo();

        List<IqbCustomerStoreInfoEntity> ics = new LinkedList<>();
        IqbCustomerStoreInfoEntity icsi = new IqbCustomerStoreInfoEntity();
        icsi.createMasterEntity(cb);
        ics.add(icsi);
        if (!StringUtil.isEmpty(cb.getCreditorOtherInfo())) {
            try {
                IqbCustomerInfoPojo icip = JSONObject.parseObject(cb.getCreditorOtherInfo(), IqbCustomerInfoPojo.class);
                ics.addAll(icip.getRequestMessage());
            } catch (Exception e) {
                logger.error("其他债权人信息 转 json 异常:", e);
            }
        }
        cirp.setCb(cb);
        cirp.setIcsi(ics);
        return cirp;
    }

    @Override
    public void updateCustomerStoreInfo(JSONObject objs) throws GenerallyException {
        IqbCustomerInfoPojo icip = null;
        try {
            icip = JSONObject.toJavaObject(objs, IqbCustomerInfoPojo.class);
        } catch (Throwable e) {
            logger.error("CustomerStoreServiceImpl.updateCustomerStoreInfo error:", e);
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        if (icip == null || icip.getRequestMessage() == null || icip.getRequestMessage().size() == 0) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
        }
        IqbCustomerStoreInfoEntity ic = new IqbCustomerStoreInfoEntity();
        List<IqbCustomerStoreInfoEntity> list = new LinkedList<>();
        for (IqbCustomerStoreInfoEntity icsi : icip.getRequestMessage()) {
            if (icsi.getFlag() == IqbCustomerStoreInfoEntity.FLAG_MASTER) {
                ic.copy(icsi, icip);
            } else {
                list.add(icsi);
            }
        }
        IqbCustomerInfoPojo icip2 = new IqbCustomerInfoPojo();
        icip2.setRequestMessage(list);
        ic.setCreditorOtherInfo(JSONObject.toJSONString(icip2));
        CustomerBean dbBean = this.customerStoreBiz.getCustomerStoreDbInfoByCode(objs);
        if (dbBean == null) {
            this.customerStoreBiz.insertCustomerStoreInfo(ic);
            return;
        }
        this.customerStoreBiz.updateCustomerStoreInfo(ic);
    }

    @Override
    public void updateCustomerStoreAuditInfo(JSONObject objs) throws IqbException {
        /** 校验传递信息是否为空 **/
        if (CollectionUtils.isEmpty(objs)) {
            throw new IqbException(CrmReturnInfo.CRM_CUSTOMER_STORE_01050001);
        }
        this.customerStoreBiz.updateCustomerStoreAuditInfo(objs);
    }

    @Override
    public void updateCustomerInfo(JSONObject objs) throws GenerallyException {
        UpdateCustomerInfoRequestPojo ucir = null;
        try {
            ucir = JSONObject.toJavaObject(objs, UpdateCustomerInfoRequestPojo.class);
        } catch (Throwable e) {
            logger.error("CustomerStoreServiceImpl.updateCustomerInfo error:", e);
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        if (ucir == null || !ucir.checkRequest()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
        }
        customerStoreBiz.updateCustomerInfo(ucir);
    }

    @Override
    public CustomerInfoResponsePojo getCustomerStoreInfoByOid(String orderId) throws GenerallyException {
        if (StringUtil.isEmpty(orderId)) {
            return null;
        }
        JSONObject jo = customerStoreBiz.getCustomerCodeByOid(orderId);
        if (jo.get("customerCode") == null) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        return getCustomerStoreInfoByCode(jo);
    }

    @Override
    public CustomerInfoResponsePojo getCustomerStoreInfoByJysOid(String orderId) throws GenerallyException {
        if (StringUtil.isEmpty(orderId)) {
            return null;
        }
        JSONObject jo = customerStoreBiz.getCustomerCodeByJysOid(orderId);
        if (jo.get("customerCode") == null) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        return getCustomerStoreInfoByCode(jo);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月7日
     */
    @Override
    public IqbCustomerStoreInfoEntity getCreditInfoByOrderId(JSONObject objs) {
        return customerStoreBiz.getCreditInfoByOrderId(objs);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年2月9日
     */
    @Override
    public IqbCustomerStoreInfoEntity getCreditInfoById(JSONObject objs) {
        return customerStoreBiz.getCreditInfoById(objs);
    }

}
