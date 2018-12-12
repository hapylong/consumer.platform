package com.iqb.consumer.service.layer.dandelion;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.RedisUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.bean.dandelion.entity.InstCreditInfoEntity;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.PersistDesignPersonRequestPojo;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.RiskInfoBiz;
import com.iqb.consumer.data.layer.biz.bank.BankCardBeanBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.dandelion.DandelionManager;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.api.dto.CreditLoanDto;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.etep.common.utils.https.SendHttpsUtil;

import jodd.util.StringUtil;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年5月14日下午12:01:27 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class HouseLoanServiceImpl implements HouseLoanService {
    protected static Logger logger = LoggerFactory.getLogger(HuayiLoanServiceImpl.class);
    private static final int riskStatus = 2;
    private static final int wfStatus = 42;
    public static final String SUCC = "success";
    /** 初始序号 **/
    private String INITIAL_SEQ = "1";
    /** 序号格式 **/
    private static final String STR_FORMAT = "000";
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private WFConfigBiz wfConfigBiz;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private BankCardBeanBiz bankCardBeanBiz;
    @Autowired
    private RiskInfoBiz riskInfoBiz;
    @Resource
    private YMForeignConfig ymForeignConfig;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Autowired
    private DandelionManager dandelionManager;
    @Autowired
    private SysUserSession sysUserSession;

    /**
     * 
     * Description:启动房融贷工作流
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月14日
     * @throws GenerallyException
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> startHouseLoanProcess(JSONObject objs) throws GenerallyException, IqbException {
        long result = 0;
        Map<String, String> resultMap = new HashMap<>();
        LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
        // 1.保存用户信息
        UserBean ub = saveUserBean(objs.getString("realName"), objs.getString("regId"), objs.getString("idCard"));
        // 2.保存风控信息
        result += saveRiskInfo(objs);
        // 3.生成订单号并保存订单信息
        OrderBean ob = null;
        CreditLoanDto creditLoanDto = JSONUtil.toJavaObject(objs, CreditLoanDto.class);
        String orderId = "";
        try {
            ob = new OrderBean();
            // 生成订单号
            orderId = generateOrderId(creditLoanDto.getMerchantId(), objs.getString("bizType"));
            ob.setOrderId(orderId); // 订单号
            ob.setUserId(ub.getId() + ""); // 用户ID
            ob.setStatus("1"); //
            ob.setWfStatus(wfStatus);// 工作流状态
            ob.setRiskStatus(riskStatus);// 订单状态
            ob.setMerchantNo(creditLoanDto.getMerchantId());// 商户号
            ob.setRegId(ub.getRegId());// 手机号
            ob.setBizType(objs.getString("bizType"));// 业务类型
            ob.setOrderRemark(creditLoanDto.getProType());// 产品类型
            ob.setOrderAmt("0.00");
            ob.setOrderItems("0");
            ob.setOrderNo(objs.getString("orderNo"));
            result += orderBiz.saveOrderInfoForHY(ob);
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_SAVEORDER_ERROR_20000004);
        }
        // 4.保存指派人信息
        objs.put("orderId", orderId);
        persistDesignPersion(objs);
        // 5.启动华益周转贷流程
        try {
            responseMap = prepaymentStartWF(orderId);
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
        }

        if (result > 0 && responseMap.get("retCode").toString().equals("00000000")) {
            resultMap.put("retCode", "1");
            resultMap.put("retMsg", "success");
        } else {
            resultMap.put("retCode", "2");
            resultMap.put("retMsg", responseMap.get("retFactInfo").toString());
        }
        return resultMap;
    }

    /**
     * Description:保存用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月30日
     */
    private UserBean saveUserBean(String realName, String regId, String idCardNo) throws IqbException {
        UserBean ub = null;
        try {
            // 根据手机号查询用户是否已经注册，注册返回存在的用户信息
            ub = userBeanBiz.selectOne(regId);
            if (ub == null) {
                ub = new UserBean();
                ub.setRealName(realName); // 姓名
                ub.setRegId(regId);// 手机号
                ub.setSmsMobile(regId); // 短信手机号
                ub.setIdNo(idCardNo);// 身份证
                ub.setStatus("1");
                ub.setCreateTime(new Date());
                ub.setUpdateTime(new Date());
                userBeanBiz.saveUserBean(ub);
            } else {
                ub.setRealName(realName); // 姓名
                ub.setIdNo(idCardNo);// 身份证
                userBeanBiz.updateUserInfo(ub);
            }
            return ub;
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_SAVEUSER_ERROR_20000001);
        }
    }

    /**
     * 
     * Description:保存风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月30日
     */
    @SuppressWarnings("rawtypes")
    private long saveRiskInfo(JSONObject objs) throws GenerallyException {
        CreditLoanDto creditLoanDto = JSONUtil.toJavaObject(objs, CreditLoanDto.class);
        if (StringUtil.isEmpty(objs.getString("regId"))) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        try {
            RiskInfoBean riskInfo = riskInfoBiz.getRiskInfoByRegId(creditLoanDto.getRegId(), 4 + "");
            if (riskInfo != null) {
                // 修改风控信息
                riskInfo.setCheckInfo(JSONObject.toJSONString(creditLoanDto, SerializerFeature.WriteMapNullValue));
                return riskInfoBiz.updateCheckInfo(riskInfo);
            } else {
                riskInfo = new RiskInfoBean();
                riskInfo.setRegId(objs.getString("regId"));
                riskInfo.setRiskType(4);
                riskInfo.setCheckInfo(JSONObject.toJSONString(creditLoanDto, SerializerFeature.WriteMapNullValue));
                return riskInfoBiz.saveRiskInfo(riskInfo);
            }
        } catch (Exception e) {
            logger.error("---房融贷保存风控信息报错---", e);
            throw new GenerallyException(Reason.UNKNOWN_ERROR, Layer.SERVICE, Location.A);
        }
    }

    /**
     * 
     * Description:根据商户、业务类型获取订单号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月30日
     */
    public String generateOrderId(String merchantNo, String bizType) throws IqbException {
        String seqRedisKey = this.getOrderRedisKey(merchantNo, bizType);
        return seqRedisKey + this.getSeqFromRedis(seqRedisKey, false);
    }

    /**
     * 
     * Description:设置生成订单号的redis-key值
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月30日
     */
    public String getOrderRedisKey(String merchantNo, String bizType) {
        DateFormat sdf = new SimpleDateFormat("yyMMdd");
        String todayStr = sdf.format(new Date());
        return merchantNo.toUpperCase() + bizType + todayStr;
    }

    /**
     * 
     * Description:从redis中获取自增序号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月30日
     */
    private synchronized String getSeqFromRedis(String key, boolean isSub) throws IqbException {

        Redisson redisson =
                RedisUtils.getInstance()
                        .getRedisson(ymForeignConfig.getRedisHost(), ymForeignConfig.getRedisPort());
        DecimalFormat df = null;
        Integer seq = 0;
        try {
            RLock rLock = RedisUtils.getInstance().getRLock(redisson, "getSeqFromRedis");
            try {
                if (rLock.tryLock(15, 15, TimeUnit.SECONDS)) { // 第一个参数代表等待时间，第二是代表超过时间释放锁，第三个代表设置的时间制
                    /** 数字格式化 **/
                    df = new DecimalFormat(STR_FORMAT);
                    /** 从redis中取值 **/
                    String val = this.redisPlatformDao.getValueByKey(key);
                    if (StringUtils.isEmpty(val)) {
                        this.redisPlatformDao.setKeyAndValue(key, this.INITIAL_SEQ);
                        return df.format(Integer.parseInt(this.INITIAL_SEQ));
                    }
                    seq = Integer.parseInt(val);

                    /** 判断是否进行减法操作 **/
                    if (isSub) {
                        seq = seq - 1;
                        this.redisPlatformDao.setKeyAndValue(key, seq.toString());
                    } else {
                        seq = seq + 1;
                        this.redisPlatformDao.setKeyAndValue(key, seq.toString());
                    }
                } else {
                    // 未获取到锁
                    throw new IqbException(ApiReturnInfo.API_BIZ_SAVEORDER_ERROR_20000004);
                }
            } finally {
                rLock.unlock();
            }
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_SAVEORDER_ERROR_20000004);
        }
        // 关闭连接
        try {
            if (redisson != null) {
                RedisUtils.getInstance().closeRedisson(redisson);
            }
        } catch (Exception e) {
            logger.debug("---关闭redisson报错---{}", e);
        }

        return df.format(seq);
    }

    /**
     * 
     * Description:启动房融贷工作流
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月14日
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private LinkedHashMap prepaymentStartWF(String orderId) throws IqbException {
        LinkedHashMap responseMap = null;
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        if (orderBean != null) {
            // 流程摘要：姓名+商户名称+业务类型+手机号
            UserBean userBean = userBeanBiz.selectOne(orderBean.getRegId());
            MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(orderBean.getMerchantNo());
            String procBizMemo =
                    userBean.getRealName() + ";" + merchantBean.getMerchantShortName() + ";" + "房融保" + ";"
                            + userBean.getRegId();// 摘要信息

            WFConfig wfConfig =
                    wfConfigBiz.getConfigByBizType(orderBean.getBizType(), Integer.parseInt(orderBean.getStatus()));

            Map<String, Object> hmProcData = new HashMap<>();
            hmProcData.put("procDefKey", wfConfig.getProcDefKey());

            Map<String, Object> hmVariables = new HashMap<>();
            hmVariables.put("procAuthType", "1");
            hmVariables.put("procTokenUser", wfConfig.getTokenUser());
            hmVariables.put("procTokenPass", wfConfig.getTokenPass());
            hmVariables.put("procTaskUser", userBean.getRegId());
            hmVariables.put("procTaskRole", wfConfig.getTaskRole());
            hmVariables.put("procApprStatus", "1");
            hmVariables.put("procApprOpinion", "同意");
            hmVariables.put("procAssignee", "");
            hmVariables.put("procAppointTask", "");

            Map<String, Object> hmBizData = new HashMap<>();
            hmBizData.put("procBizId", orderBean.getOrderId());
            hmBizData.put("procBizType", "");
            hmBizData.put("procOrgCode", merchantBean.getId() + "");
            hmBizData.put("procBizMemo", procBizMemo);
            hmBizData.put("amount", "");

            Map<String, Map<String, Object>> reqData = new HashMap<>();
            reqData.put("procData", hmProcData);
            reqData.put("variableData", hmVariables);
            reqData.put("bizData", hmBizData);

            String url = wfConfig.getStartWfurl();
            // 发送Https请求
            logger.info("调用工作流接口传入信息：{}", JSONObject.toJSONString(reqData));
            Long startTime = System.currentTimeMillis();
            try {
                responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
            } catch (Exception e) {
                throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用工作流接口返回信息：{}", responseMap);
            logger.info("工作流接口交互花费时间，{}", (endTime - startTime));
        } else {
            throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
        }
        if (responseMap.get("retCode").toString().equals("00000000")) {
            Map<String, String> map = (HashMap<String, String>) responseMap.get("iqbResult");
            String procInstId = map.get("procInstId");

            // 更新流程id
            orderBean = new OrderBean();
            orderBean.setOrderId(orderId);
            orderBean.setProcInstId(procInstId);
            orderBiz.updateOrderInfo(orderBean);
        }
        return responseMap;
    }

    /**
     * 
     * Description:用户信息四码鉴权
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月30日
     */
    public Map<String, String> userInfoAuthByThree(String type, JSONObject objs) {
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = new HashMap<>();
        map.put("bizChannelCode", "zhongge");
        map.put("bankCardNum", objs.getString("bankCard"));
        map.put("userName", objs.getString("realName"));
        map.put("idNum", objs.getString("idCard"));
        if (type.equalsIgnoreCase("three")) {
            map.put("bankReservedPhoneNum", ""); // 银行预留手机号
        } else {
            map.put("bankReservedPhoneNum", objs.getString("bankMobile")); // 银行预留手机号
        }
        String httpStr = null;
        try {
            httpStr = SimpleHttpUtils.httpPost(consumerConfig.getAuthInfoUrl(), map);
        } catch (Exception e) {
            logger.error("调用鉴权接口发生错误参数:{}", objs, e);
        }
        if (SUCC.equals(httpStr)) {
            result.put("retCode", "1");
            result.put("retMsg", "鉴权成功");
        } else {
            result.put("retCode", "2");
            result.put("retMsg", "身份认证失败");
        }
        return result;
    }

    /**
     * 
     * Description:保存指派人信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月14日
     */
    private void persistDesignPersion(JSONObject requestMessage) throws GenerallyException {
        PersistDesignPersonRequestPojo pdpr =
                JSONObject.toJavaObject(requestMessage, PersistDesignPersonRequestPojo.class);

        InstCreditInfoEntity icie = new InstCreditInfoEntity();
        InstCreditInfoEntity i = dandelionManager.getInstCreditInfoEntityByOid(pdpr.getOrderId());
        icie.setOrderId(pdpr.getOrderId());
        icie.setDesignCode(sysUserSession.getUserCode());
        icie.setDesignName(sysUserSession.getRealName());
        icie.setGuarantorNum(1);
        if (i == null) {
            dandelionManager.persistDesignPersion3elements(icie);
        } else {
            dandelionManager.updateDesignPersion3elements(icie);
        }
    }
}
