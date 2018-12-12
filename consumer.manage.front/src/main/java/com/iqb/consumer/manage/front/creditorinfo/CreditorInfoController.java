package com.iqb.consumer.manage.front.creditorinfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.RedisUtils;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.biz.CustomerBiz;
import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.creditorinfo.CreditorInfoBiz;
import com.iqb.consumer.data.layer.biz.wf.OrderOtherInfoBiz;
import com.iqb.consumer.service.layer.creditorinfo.CreditorInfoService;
import com.iqb.consumer.service.layer.merchant.service.IMerchantService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;

@Controller
@RequestMapping("/creditorInfo")
public class CreditorInfoController extends BaseService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(CreditorInfoController.class);

    @Autowired
    private CreditorInfoService creditorInfoService;

    @Autowired
    private CustomerBiz customerBiz;
    @Autowired
    private OrderBiz orderBiz;
    @Autowired
    private CreditorInfoBiz creditorInfoBiz;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Resource
    private YMForeignConfig ymForeignConfig;
    @Autowired
    private OrderOtherInfoBiz orderOtherInfoBiz;
    @Autowired
    private IMerchantService merchantService;
    private static final String YZDS_ORDER_PROJECT_PREFIX = "汽车以租代购项目";// 订单项目名前缀:以租代售
    private static final String DY_ORDER_PROJECT_PREFIX = "爱抵贷";// 订单项目名前缀:抵押
    private static final String ORDER_CARSORTNO_PREFIX = "ORDER_CARSORTNO_";// 订单车辆排序序列号前缀
    private static final String YZDS_ORDER_PROJECT_TYPE = "YZDS"; // 订单项目类型:以租代售
    private static final String DY_ORDER_PROJECT_TYPE = "DY"; // 订单项目类型:抵押
    private static final String ZY_ORDER_PROJECT_TYPE = "ZY"; // 订单项目类型:质押
    private static final String PGYH_ORDER_PROJECT_TYPE = "PGYH"; // 订单项目类型:(PGYH)蒲公英行
    private static final String PGYH_ORDER_PROJECT_NAME = "房优贷"; // 订单项目类型:质押
    private static final String HUAYI_ORDER_PROJECT_TYPE = "HYZZD"; // 订单项目类型:华益周转贷
    private static final String HUAYI_ORDER_PROJECT_NAME = "周转贷"; // 订单项目类型:华益周转贷

    private static final String YZDDS_ORDER_PROJECT_TYPE = "YZDD";
    private static final String YZDDS_ORDER_PROJECT_NAME = "汽车以租代购项目";

    private static final String FRB_ORDER_PROJECT_TYPE = "FRB";
    private static final String FRB_ORDER_PROJECT_NAME = "房融保";

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/getCreditorInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getCreditorInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始获取债权人信息数据...");
            CreditorInfoBean creditorInfoBean = creditorInfoService.selectOneByOrderId(objs);
            logger.debug("IQB信息---查询债权人信息数据完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", creditorInfoBean);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/saveCreditorInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map saveCreditorInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始存储债权人信息数据...");
            int result = creditorInfoService.insertCreditorInfo(objs);
            logger.debug("IQB信息---存储债权人信息数据完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result == 1 ? "success" : "fail");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/updateCreditorInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map updateCreditorInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始修改债权人信息数据...");
            int result = creditorInfoService.updateCreditorInfo(objs);
            logger.debug("IQB信息---修改债权人信息数据完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result == 1 ? "success" : "fail");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 车秒贷拷贝主订单的债权人信息和担保人信息
     * 
     * @param obj
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/copyOrderInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map copyOrderInfo(@RequestBody String obj, HttpServletRequest request) {
        logger.info("订单:{}开始拷贝债权人信息", obj);
        try {
            Map map = JSONObject.parseObject(obj);
            String orderId = (String) map.get("orderId");
            // 查询担保人信息
            CreditorInfoBean creditorInfoBean =
                    creditorInfoBiz.selectOneByOrderId(orderId.substring(0, orderId.length() - 1));
            if (creditorInfoBean != null) {
                // FINANCE-991 车秒贷内控/项目初审审批退回后，到项目信息维护界面，继续下面流程，资产分配项目目信息回调了2次 By yeoman
                CreditorInfoBean infoBean = creditorInfoBiz.selectOneByOrderId(orderId);
                if (infoBean == null) {
                    creditorInfoBean.setOrderId(orderId);
                    creditorInfoBiz.insertCreditorInfo(creditorInfoBean);
                }
            }
            // 查询线上借款人信息
            OrderOtherInfo orderOtherInfo = orderBiz.selectOtherOne(orderId.substring(0, orderId.length() - 1));
            if (orderOtherInfo != null) {
                // FINANCE-991 车秒贷内控/项目初审审批退回后，到项目信息维护界面，继续下面流程，资产分配项目目信息回调了2次 By yeoman
                OrderOtherInfo otherInfo = orderBiz.selectOtherOne(orderId);
                if (otherInfo == null) {
                    // 首次项目信息维护节点回调
                    orderOtherInfo.setOrderId(orderId);
                    orderOtherInfo.setProjectName(orderOtherInfo.getProjectName() + "X");
                    orderOtherInfo.setProjectNo(orderOtherInfo.getProjectNo() + "X");
                    orderBiz.insertOrderOtherInfo(orderOtherInfo);
                }
            }
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", orderOtherInfo != null && creditorInfoBean != null ? "success" : "fail");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/WFReturn/{type}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map WFReturn(@PathVariable("type") String type, @RequestBody String obj, HttpServletRequest request) {
        try {
            logger.info("WFReturn工作流回调内调获得参数：{}", obj);
            Map map = JSONObject.parseObject(obj);
            String orderId = (String) map.get("orderId");
            OrderBean orderBean = orderBiz.selectOne(map);

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
            logger.info("拼接项目信息结果:{}", proMap);
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
            OrderOtherInfo otherInfo = orderBiz.selectOtherOne(orderId);
            int res1 = 0;
            if (otherInfo == null) {
                res1 = orderBiz.insertOrderOtherInfo(orderOtherInfo);
            } else {
                res1 = orderOtherInfoBiz.updateOrderOtherInfo(orderOtherInfo);
            }
            logger.info("修改订单信息结果:{}", res1);
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
            CreditorInfoBean bean = creditorInfoBiz.selectOneByOrderId(orderId);
            int res2 = 0;
            if (bean != null) {
                res2 = creditorInfoBiz.updateCreditorInfo(creditorInfoBean);
            } else {
                res2 = creditorInfoBiz.insertCreditorInfo(creditorInfoBean);
            }
            logger.info("存入债权人信息结果:{}", res2);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", res1 == 1 && res2 == 1 ? "success" : "fail");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("生成项目信息报错",e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }

    }

    /**
     * 规则为“汽车以租代购项目+门店简称拼音大写+年月（yymm）+本机构当月第几辆车（四位）”。如：汽车以租代购项目SYLDFC16090008
     */
    private Map<String, Object> getOrderProjectInfo(OrderBean orderBean, String type) {
        Map<String, Object> proMap = new HashMap<String, Object>();
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
            case YZDS_ORDER_PROJECT_TYPE:
                projectName = YZDS_ORDER_PROJECT_PREFIX + projectNo;
                break;
            case DY_ORDER_PROJECT_TYPE:
                projectName = DY_ORDER_PROJECT_PREFIX + projectNo;
                break;
            case ZY_ORDER_PROJECT_TYPE:
                projectName = ZY_ORDER_PROJECT_TYPE + projectNo;
                break;
            case PGYH_ORDER_PROJECT_TYPE:
                projectName = PGYH_ORDER_PROJECT_NAME + projectNo;
                break;
            case HUAYI_ORDER_PROJECT_TYPE:
                projectName = HUAYI_ORDER_PROJECT_NAME + projectNo;
                break;
            case YZDDS_ORDER_PROJECT_TYPE:
                projectName = YZDDS_ORDER_PROJECT_NAME + projectNo;
                break;
            case FRB_ORDER_PROJECT_TYPE:
                projectName = FRB_ORDER_PROJECT_NAME + projectNo;
                break;
        }

        proMap.put("carSortNo", Integer.parseInt(carSortNo));
        proMap.put("projectNo", projectNo);
        proMap.put("projectName", projectName);
        return proMap;
    }

    /**
     * 
     * 
     * @Description:FINANCE-3192 新增华益项目流程及页面调整FINANCE-3199保存抵押信息接口，返回抵押信息接口
     * @param @param args
     * @return void
     * @author chengzhen
     * @data
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/saveUpdateMortgageInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map saveMortgageInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始保存质押信息数据...");
            int result = creditorInfoService.saveUpdateMortgageInfo(objs);
            logger.debug("IQB信息---开始保存质押信息数据完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result == 1 ? "success" : "fail");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("rawtypes")
    public static void main(String[] args) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", "20161201-702058");
        LinkedHashMap result = SendHttpsUtil
                .postMsg4GetMap("http://localhost:8080/consumer.manage.front/creditorInfo/WFReturn/YZDS", params);
        System.out.println(result);
    }
}
