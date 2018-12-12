package com.iqb.consumer.service.layer.creditorinfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.constant.CommonConstant.DictTypeCodeEnum;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.RedisUtils;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.biz.CustomerBiz;
import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;
import com.iqb.consumer.data.layer.bean.dict.IqbSysDictItemEntity;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.creditorinfo.CreditorInfoBiz;
import com.iqb.consumer.data.layer.biz.dict.DictManager;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;

@Component
public class CreditorInfoServiceImpl implements CreditorInfoService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(CreditorInfoServiceImpl.class);

    @Autowired
    private CreditorInfoBiz creditorInfoBiz;
    @Autowired
    private CustomerBiz customerBiz;
    @Autowired
    private OrderBiz orderBiz;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Resource
    private YMForeignConfig ymForeignConfig;
    @Autowired
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private DictManager dictManager;

    private static final String DY_ORDER_PROJECT_PREFIX = "";// 订单项目名前缀:抵押
    private static final String ORDER_CARSORTNO_PREFIX = "OWNERlOAN_CARSORTNO_";// 订单车辆排序序列号前缀
    private static final String DY_ORDER_PROJECT_TYPE = "DY"; // 订单项目类型:抵押

    @SuppressWarnings("rawtypes")
    @Override
    public int insertCreditorInfo(Map objs) {
        String jsonStr = JSON.toJSONString(objs);
        CreditorInfoBean creditorInfoBean = JSONObject.parseObject(jsonStr, CreditorInfoBean.class);
        return creditorInfoBiz.insertCreditorInfo(creditorInfoBean);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public CreditorInfoBean selectOneByOrderId(Map objs) {
        String orderId = (String) objs.get("orderId");
        return creditorInfoBiz.selectOneByOrderId(orderId);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int updateCreditorInfo(Map objs) {
        String jsonStr = JSON.toJSONString(objs);
        CreditorInfoBean creditorInfoBean = JSONObject.parseObject(jsonStr, CreditorInfoBean.class);
        return creditorInfoBiz.updateCreditorInfo(creditorInfoBean);
    }

    /**
     * 车主贷-风控终审生成项目信息名称 业务类型+商户编码+YYMMDD+001
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月15日
     */
    @Override
    public String createProjectName(Map<String, Object> map) {
        String resultInfo = "success";
        try {
            logger.debug("---车主贷-风控终审生成项目信息名称获得参数：{}", JSONObject.toJSON(map));
            String type = (String) map.get("type");
            String orderId = (String) map.get("orderId");
            OrderBean orderBean = orderBiz.selectOne(map);

            // 1.查询债权人信息, 订单信息
            MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(orderBean.getMerchantNo());
            JSONObject m = new JSONObject();
            m.put("customerCode", merchantBean.getId());
            CustomerBean customerBean = customerBiz.getCustomerStoreInfoByCode(m);
            // 1.1获取业务类型名称
            IqbSysDictItemEntity dictItem =
                    dictManager.getDictByDTCAndDC(DictTypeCodeEnum.business_type, orderBean.getBizType());
            orderBean.setBizType(dictItem.getDictName());
            // 2.获取拼接项目名
            Map<String, Object> proMap = null;
            Redisson redisson =
                    RedisUtils.getInstance()
                            .getRedisson(ymForeignConfig.getRedisHost(), ymForeignConfig.getRedisPort());
            RLock rLock = null;
            try {
                rLock = RedisUtils.getInstance().getRLock(redisson, "getWFReturnSeqFromRedis");
                if (rLock.tryLock(15, 15, TimeUnit.SECONDS)) { // 第一个参数代表等待时间，第二是代表超过时间释放锁，第三个代表设置的时间制
                    proMap = getOrderProjectInfo(orderBean, type);
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
            logger.debug("---拼接项目信息结果:{}", proMap);
            String projectNo = (String) proMap.get("projectNo");// 项目编号
            String projectName = (String) proMap.get("projectName");// 项目名
            int carSortNo = (int) proMap.get("carSortNo");// 本月车辆号码
            // 3.存入order表项目名，项目编号（YLDFC16090008）， 辅助列(本机构第N辆车), 担保人， 担保人法姓名
            OrderOtherInfo orderOtherInfo = new OrderOtherInfo();
            orderOtherInfo.setOrderId(orderId);
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
            creditorInfoBean.setOrderId(orderId);
            int res2 = creditorInfoBiz.insertCreditorInfo(creditorInfoBean);
            logger.debug("存入债权人信息结果:{}", res2);
            resultInfo = res1 == 1 && res2 == 1 ? "success" : "fail";
            return resultInfo;
        } catch (Exception e) {
            logger.error("---生成项目信息报错---{}", e.getMessage());
            return "fail";
        }
    }

    /**
     * 
     * Description:拼接项目信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月15日
     */
    private Map<String, Object> getOrderProjectInfo(OrderBean orderBean, String type) {
        Map<String, Object> proMap = new HashMap<String, Object>();
        String projectNo = null;
        String projectName = null;
        String bizType = orderBean.getBizType();
        // 门店简称拼音大写
        MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(orderBean.getMerchantNo());
        String simpleCustomerName = merchantBean.getMerchantNo().toUpperCase();

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
        projectNo = bizType + simpleCustomerName + orderDate + carSortNo;
        switch (type) {
            case DY_ORDER_PROJECT_TYPE:
                projectName = DY_ORDER_PROJECT_PREFIX + projectNo;
                break;
        }

        proMap.put("carSortNo", Integer.parseInt(carSortNo));
        proMap.put("projectNo", projectNo);
        proMap.put("projectName", projectName);
        return proMap;
    }

    @Override
    public int saveUpdateMortgageInfo(JSONObject objs) {
        return 0;
    }
}
