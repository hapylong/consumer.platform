package com.iqb.consumer.service.layer.pledge.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.RedisUtils;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.biz.CustomerBiz;
import com.iqb.consumer.data.layer.bean.Pledge.PledgeInfoBean;
import com.iqb.consumer.data.layer.bean.Pledge.pojo.ConditionsGetCarInfoResponsePojo;
import com.iqb.consumer.data.layer.bean.Pledge.pojo.UpdateAmtRequestPojo;
import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.creditorinfo.CreditorInfoBiz;
import com.iqb.consumer.data.layer.biz.pledge.PledgeBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.merchant.service.IMerchantService;
import com.iqb.consumer.service.layer.pledge.IPledgeSerivce;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;

/**
 * 
 * Description: 质押车service服务实现
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月29日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Service
public class PledgeSerivceImpl extends BaseService implements IPledgeSerivce {

    /** 日志 **/
    private static final Logger logger = LoggerFactory.getLogger(PledgeSerivceImpl.class);

    private static final String ORDERID_KEY = "orderId";

    private static final String ZY_ORDER_PROJECT_TYPE = "ZY"; // 订单项目类型:质押

    private static final String ORDER_CARSORTNO_PREFIX = "ORDER_CARSORTNO_";// 订单车辆排序序列号前缀

    @Autowired
    private PledgeBiz pledgeBiz;

    @Autowired
    private OrderBiz orderBiz;

    @Autowired
    private IMerchantService merchantService;

    @Autowired
    private CustomerBiz customerBiz;

    @Autowired
    private CreditorInfoBiz creditorInfoBiz;

    @Resource
    private RedisPlatformDao redisPlatformDao;

    @Resource
    private YMForeignConfig ymForeignConfig;

    @Override
    public Object getPledgeInfo(JSONObject objs) throws IqbException {
        return this.pledgeBiz.getPledgeInfo(objs);
    }

    @Override
    public Integer updatePledgeOrderByWF(OrderBean orderBean) throws IqbException {
        return this.orderBiz.updatePledgeWfStatus(orderBean.getOrderId(), orderBean.getRiskStatus() == null
                ? null
                : Integer.toString(orderBean.getRiskStatus()), Integer.toString(orderBean.getWfStatus()),
                orderBean.getPreAmtStatus(), "", null, null, null);
    }

    @Override
    public void dealRiskWFReturn(String procBizId) throws IqbException {
        /** 将订单id封装进查询map **/
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put(ORDERID_KEY, procBizId);
        OrderBean orderBean = orderBiz.selectOne(reqMap);

        // 1.查询债权人信息, 订单信息
        MerchantBean merchantBean = merchantService.getMerByMerNo(orderBean.getMerchantNo());
        JSONObject m = new JSONObject();
        m.put("customerCode", merchantBean.getId());
        CustomerBean customerBean = customerBiz.getCustomerStoreInfoByCode(m);

        // 2.获取拼接项目名
        Map<String, Object> proMap = null;
        Redisson redisson =
                RedisUtils.getInstance()
                        .getRedisson(ymForeignConfig.getRedisHost(), ymForeignConfig.getRedisPort());
        RLock rLock = null;
        try {
            rLock = RedisUtils.getInstance().getRLock(redisson, "getWFReturnSeqFromRedis");
            if (rLock.tryLock(15, 15, TimeUnit.SECONDS)) { // 第一个参数代表等待时间，第二是代表超过时间释放锁，第三个代表设置的时间制
                proMap = getOrderProjectInfo(orderBean, ZY_ORDER_PROJECT_TYPE);
                logger.debug("拼接项目信息结果:{}", proMap);
            } else {
                throw new IqbException(ConsumerReturnInfo.GET_PROJECT_CREATE_NAME_LOCK);
            }
        } catch (Exception e) {
            throw new IqbException(ConsumerReturnInfo.PROJECT_CREATE_NAME);
        } finally {
            rLock.unlock();
        }

        try {
            if (redisson != null) {
                RedisUtils.getInstance().closeRedisson(redisson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String projectNo = (String) proMap.get("projectNo");// 项目编号
        String projectName = (String) proMap.get("projectName");// 项目名
        int carSortNo = (int) proMap.get("carSortNo");// 本月车辆号码
        // 3.存入order表项目名，项目编号（YLDFC16090008）， 辅助列(本机构第N辆车), 担保人， 担保人法姓名
        OrderOtherInfo orderOtherInfo = new OrderOtherInfo();
        orderOtherInfo.setOrderId(procBizId);
        orderOtherInfo.setMerchantNo(orderBean.getMerchantNo());
        orderOtherInfo.setProjectName(projectName);
        orderOtherInfo.setProjectNo(projectNo);
        orderOtherInfo.setCarSortNo(carSortNo);
        orderOtherInfo.setGuarantee(customerBean.getCustomerName());
        orderOtherInfo.setGuaranteeName(customerBean.getCorporateName());
        int res1 = orderBiz.insertOrderOtherInfo(orderOtherInfo);
        logger.debug("修改订单信息结果:{}", res1);
        // 4.存入债权人信息表相关信息
        JSONObject jo = new JSONObject();
        jo.put("customerCode", merchantBean.getId());
        customerBean = customerBiz.getCustomerStoreInfoByCode(jo);
        CreditorInfoBean creditorInfoBean = new CreditorInfoBean();
        creditorInfoBean.setCreditBank(customerBean.getCreditorBankName());
        creditorInfoBean.setCreditBankCard(customerBean.getCreditorBankNo());
        creditorInfoBean.setCreditCardNo(customerBean.getCreditorIdNo());
        creditorInfoBean.setCreditName(customerBean.getCreditorName());
        creditorInfoBean.setCreditPhone(customerBean.getCreditorPhone());
        creditorInfoBean.setOrderId(procBizId);
        creditorInfoBiz.insertCreditorInfo(creditorInfoBean);
    }

    private Map<String, Object> getOrderProjectInfo(OrderBean orderBean, String type) {
        Map<String, Object> proMap = new HashMap<>();
        String projectNo = null;
        String projectName = null;
        // 门店简称拼音大写
        MerchantBean merchantBean = merchantService.getMerByMerNo(orderBean.getMerchantNo());
        String simpleCustomerName = merchantBean.getMerchantNo().toUpperCase();
        // 1. 获取该机构售出车辆编号
        String carSortNo = redisPlatformDao.getValueByKey(ORDER_CARSORTNO_PREFIX + simpleCustomerName);
        if (carSortNo == null || "".equals(carSortNo)) {
            String prefix = DateUtil.getDateString(orderBean.getCreateTime(), "yyyyMM");
            int no = orderBiz.getMaxCarSortNo(prefix, orderBean.getMerchantNo()) + 1;
            carSortNo = String.format("%04d", no);
            redisPlatformDao.setKeyAndValue(ORDER_CARSORTNO_PREFIX + simpleCustomerName, carSortNo);
        } else {
            int no = Integer.parseInt(carSortNo) + 1;
            carSortNo = String.format("%04d", no);
            redisPlatformDao.setKeyAndValue(ORDER_CARSORTNO_PREFIX + simpleCustomerName, carSortNo);
        }

        String orderDate = DateUtil.getDateString(orderBean.getCreateTime(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        projectNo = simpleCustomerName + orderDate.substring(2, 6) + carSortNo;
        switch (type) {
            case ZY_ORDER_PROJECT_TYPE:
                projectName = ZY_ORDER_PROJECT_TYPE + projectNo;
                break;
        }

        proMap.put("carSortNo", Integer.parseInt(carSortNo));
        proMap.put("projectNo", projectNo);
        proMap.put("projectName", projectName);
        return proMap;
    }

    @Override
    public Integer saveVehicleStorageInfo(JSONObject objs) throws IqbException {
        return this.pledgeBiz.saveVehicleStorageInfo(objs);
    }

    @Override
    public Integer savePledgeInfo(JSONObject objs) {
        return this.pledgeBiz.savePledgeInfo(objs);
    }

    @Override
    public PageInfo<ConditionsGetCarInfoResponsePojo> cgetCarInfo(JSONObject requestMessage) throws GenerallyException {
        getMerchLimitObject(requestMessage);
        List<ConditionsGetCarInfoResponsePojo> list = pledgeBiz.cgetCarInfo(requestMessage);
        return new PageInfo<>(list);
    }

    @Override
    public void persistCarInfo(JSONObject requestMessage) throws GenerallyException {
        PledgeInfoBean pib = JSONObject.toJavaObject(requestMessage, PledgeInfoBean.class);
        if (pib == null || !pib.checkPersistEntity()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        MerchantBean mb = merchantService.getMerByMerName(pib.getMerchantNo());
        if (mb != null) {
            pib.setMerchantNo(mb.getMerchantNo());
        }
        pledgeBiz.persistCarInfo(pib);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {GenerallyException.class})
    public void updateCarInfo(JSONObject requestMessage) throws GenerallyException {
        PledgeInfoBean pib = JSONObject.toJavaObject(requestMessage, PledgeInfoBean.class);
        if (pib == null || !pib.checkUpdateEntity()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        int i = pledgeBiz.updateCarInfo(pib);
        if (i != 1) {
            throw new GenerallyException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.A);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {GenerallyException.class})
    public void updateAmt(JSONObject requestMessage) throws GenerallyException {
        UpdateAmtRequestPojo uarp = JSONObject.toJavaObject(requestMessage, UpdateAmtRequestPojo.class);
        if (uarp == null || !uarp.checkRequest()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        int i = pledgeBiz.updateAmt(uarp);
        if (i != 1) {
            throw new GenerallyException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.A);
        }
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年5月31日
     */
    @Override
    public int updatePledgeStatus(PledgeInfoBean pib) {

        return pledgeBiz.updatePledgeStatus(pib);
    }

}
