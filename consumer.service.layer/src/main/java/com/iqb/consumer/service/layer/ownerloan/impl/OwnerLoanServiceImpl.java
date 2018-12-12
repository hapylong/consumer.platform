package com.iqb.consumer.service.layer.ownerloan.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.data.layer.bean.authoritycard.AuthorityCardBean;
import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.RiskNoticeRequestPojo;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.ownerloan.DeptSignInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.MortgageInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.OwnerLoanBaseInfoBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.ProductBiz;
import com.iqb.consumer.data.layer.biz.authoritycard.AuthorityCardBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.ownerloan.MortgageInfoBiz;
import com.iqb.consumer.service.layer.ownerloan.OwnerLoanService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.https.SimpleHttpUtils;

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
 * 2017年11月10日上午9:43:39 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class OwnerLoanServiceImpl implements OwnerLoanService {
    protected static final Logger logger = LoggerFactory.getLogger(OwnerLoanServiceImpl.class);
    @Resource
    private AuthorityCardBiz authorityCardBiz;
    @Resource
    private MortgageInfoBiz mortgageInfoBiz;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private WFConfigBiz wfConfigBiz;
    @Resource
    private ProductBiz productBiz;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private EncryptUtils encryptUtils;

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月10日
     */
    @Override
    public MortgageInfoBean getCarinfo(JSONObject objs) {
        return mortgageInfoBiz.getCarinfo(objs);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月10日
     */
    @Override
    public MortgageInfoBean selectOneByOrderId(JSONObject objs) {
        return mortgageInfoBiz.selectOneByOrderId(objs);
    }

    /**
     * 车主贷-获取订单 人员 卡信息接口
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月13日
     */
    @Override
    public OwnerLoanBaseInfoBean getBaseInfo(JSONObject objs) {
        return mortgageInfoBiz.getBaseInfo(objs);
    }

    /**
     * 车主贷-更新车辆信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月13日
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class})
    public int updateCarInfo(JSONObject objs) {
        int result = 0;
        // 更新订单表-评估金额评估意见
        OrderBean orderBean = JSON.toJavaObject(objs, OrderBean.class);
        result = orderBiz.updateOrderInfo(orderBean);
        // 更新车辆信息表
        AuthorityCardBean authorityCardBean = JSON.toJavaObject(objs, AuthorityCardBean.class);
        result += authorityCardBiz.updateAuthorityCard(authorityCardBean);
        /**
         * 判断车辆抵押信息是否存在,存在则更新
         */
        MortgageInfoBean mortgageInfoBean = mortgageInfoBiz.selectOneByOrderId(objs);
        if (mortgageInfoBean != null) {
            result += mortgageInfoBiz.updateMortgageInfo(objs);
        } else {
            mortgageInfoBean = JSON.toJavaObject(objs, MortgageInfoBean.class);
            result += mortgageInfoBiz.insertMortgageInfo(mortgageInfoBean);
        }
        return result;
    }

    /**
     * 车主贷-更新车辆抵押信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月13日
     */
    @Override
    public int updateMortgageInfo(JSONObject objs) {
        return mortgageInfoBiz.updateMortgageInfo(objs);
    }

    /**
     * 车主贷-更新放款时间
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月13日
     */
    @Override
    public int updateOrderInfo(JSONObject objs) {
        OrderBean orderBean = JSON.toJavaObject(objs, OrderBean.class);
        long planId = objs.getLongValue("planId");

        if (objs.getLongValue("planId") != 0) {
            objs.put("id", planId);
            PlanBean planBean = productBiz.getPlanByID(objs);
            orderBean.setOrderItems(String.valueOf(planBean.getInstallPeriods()));
        }

        return orderBiz.updateOrderInfo(orderBean);
    }

    /**
     * 车主贷-风控回调接口
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月14日
     */
    @Override
    public int ownerLoanRiskNotice(JSONObject objs) throws IqbException {
        if (CollectionUtils.isEmpty(objs)) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000001);
        }
        RiskNoticeRequestPojo riskNotice = JSONObject.toJavaObject(objs, RiskNoticeRequestPojo.class);

        OrderBean orderBean = orderBiz.selByOrderId(riskNotice.getOrderId());
        MerchantBean mb = merchantBeanBiz.getMerByMerNo(orderBean.getMerchantNo());

        /** 工作流接口交互 **/
        try {
            if (mb.getWfStatus() == 0) {
                WFConfig wfConfig = wfConfigBiz.getConfigByBizType(orderBean.getBizType(), orderBean.getWfStatus());
                String msg = StringUtil.isEmpty(riskNotice.getMessage()) ? "" : riskNotice.getMessage();
                submitDandelionWF(wfConfig, orderBean.getProcInstId(), orderBean.getOrderId(), mb.getId(),
                        orderBean.getOrderAmt(), riskNotice.getRiskStatus(), msg + ":" + riskNotice.getMessageInfo());
            }
        } catch (Throwable e) {
            logger.error("车主贷读脉回调启动工作流异常", e);
            throw new IqbException(ConsumerReturnInfo.CONSUMER_WF_03000002);
        }
        /** 此处只修改风控信息 **/
        OrderBean bean = new OrderBean();
        bean.setOrderId(orderBean.getOrderId());
        bean.setRiskRetRemark(riskNotice.getRiskStatus() + ":" + riskNotice.getMessage() + "-"
                + riskNotice.getMessageInfo());

        Integer i = orderBiz.updateOrderInfo(bean);
        return i;
    }

    /**
     * 
     * Description:读脉审核通过后启动工作流
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月14日
     */
    @SuppressWarnings("rawtypes")
    private void submitDandelionWF(WFConfig wfConfig, String procInstId, String orderId, long orgId, String orderAmt,
            String riskStatus, String messageInfo) throws IqbException {
        logger.info("---车主贷读脉回调启动工作流开始---：{}", orderId);
        JSONObject requestMessage = new JSONObject();
        requestMessage.put("orderId", orderId);
        Map<String, Object> hmProcData = new HashMap<>();
        hmProcData.put("procInstId", procInstId);
        hmProcData.put("procTaskCode", wfConfig.getProcTaskCode());// 等工作流

        Map<String, Object> hmVariables = new HashMap<>();
        hmVariables.put("procAuthType", "1"); // 1：token认证；2：session认证
        hmVariables.put("procTokenUser", wfConfig.getTokenUser());
        hmVariables.put("procTokenPass", wfConfig.getTokenPass());
        hmVariables.put("procTaskUser", wfConfig.getTaskUser());
        hmVariables.put("procTaskRole", wfConfig.getTaskRole());
        hmVariables.put("procApprStatus", "1".equals(riskStatus) ? "1" : "0");
        hmVariables.put("procApprOpinion", "1".equals(riskStatus) ? "同意" : "不同意");

        if ("2".equals(riskStatus)) {
            hmVariables.put("procApprOpinion", messageInfo);
        }
        hmVariables.put("procAssignee", "");
        hmVariables.put("procAppointTask", "");
        hmVariables.put("amount", orderAmt);

        Map<String, Object> hmBizData = new HashMap<>();
        hmBizData.put("procBizId", orderId);
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "" + orgId);
        hmBizData.put("amount", orderAmt);

        Map<String, Map<String, Object>> reqData = new HashMap<>();
        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        String url = wfConfig.getRiskWfUrl();
        // 发送Https请求
        logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
        Long startTime = System.currentTimeMillis();
        LinkedHashMap responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
        Long endTime = System.currentTimeMillis();
        logger.info("调用工作流接口返回信息：{}" + responseMap);
        logger.info("工作流接口交互花费时间，{}" + (endTime - startTime));
    }

    /**
     * 车主贷-更新GPS信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月14日
     */
    @Override
    public int updateGpsInfo(JSONObject objs) {
        int result = 0;
        AuthorityCardBean authorityCardBean = JSON.toJavaObject(objs, AuthorityCardBean.class);
        result = authorityCardBiz.updateAuthorityCard(authorityCardBean);
        return result;
    }

    /**
     * 车主贷-金额计算
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月15日
     */
    @Override
    public Map<String, BigDecimal> recalAmt(JSONObject objs) throws IqbException {
        Map<String, BigDecimal> detailAmt = null;
        Map<String, BigDecimal> tempAmt = null;
        PlanBean planBean = productBiz.getPlanByID(objs);
        if (planBean != null) {
            /** 核准金额 **/
            BigDecimal orderAmt =
                    objs.getBigDecimal("orderAmt") != null ? objs.getBigDecimal("orderAmt") : BigDecimal.ZERO;
            /** gps安装费 **/
            BigDecimal gpsAmt = objs.getBigDecimal("gpsAmt") != null ? objs.getBigDecimal("gpsAmt") : BigDecimal.ZERO;
            /** 预付款 **/
            BigDecimal preAmt = BigDecimal.ZERO;
            /** 首付款 **/
            BigDecimal downPayment = BigDecimal.ZERO;
            /** 保证金 **/
            BigDecimal margin = BigDecimal.ZERO;
            /** 手续费 **/
            BigDecimal serviceFee = BigDecimal.ZERO;
            /** 上标金额 **/
            BigDecimal sbAmt = BigDecimal.ZERO;
            /** 月供 **/
            BigDecimal monthMake = BigDecimal.ZERO;
            /** 合同金额 **/
            BigDecimal contractAmt = BigDecimal.ZERO;
            /** gps流量费 **/
            BigDecimal gpsTrafficFee = BigDecimal.ZERO;
            /** 上收息 **/
            BigDecimal feeAmount = BigDecimal.ZERO;
            /** 上收月供 **/
            BigDecimal monthAmount = BigDecimal.ZERO;
            /** 上浮金额 **/
            BigDecimal floatServiceFee = BigDecimal.ZERO;
            floatServiceFee =
                    BigDecimalUtil.mul(orderAmt, BigDecimal.valueOf(planBean.getFloatMarginRatio())).divide(
                            BigDecimal.valueOf(100), 5,
                            BigDecimal.ROUND_HALF_UP);
            sbAmt = BigDecimalUtil.add(orderAmt, floatServiceFee, gpsAmt);
            tempAmt = calculateAmt(planBean, sbAmt);
            if (!CollectionUtils.isEmpty(tempAmt)) {
                monthMake = tempAmt.get("monthMake");
            }

            /**
             * 将上收保证金比例字段值赋给账务系统上收服务费比例
             */
            planBean.setFloatServiceFeeRatio(planBean.getFloatMarginRatio());
            detailAmt = calculateAmt(planBean, orderAmt);
            if (!CollectionUtils.isEmpty(detailAmt)) {
                downPayment = detailAmt.get("downPayment");
                serviceFee = detailAmt.get("serviceFee");
                margin = detailAmt.get("margin");
                feeAmount = detailAmt.get("feeAmount");
                monthAmount = detailAmt.get("monthAmount");
            }

            /*
             * 金额计算 保证金=核准金额*保证金比例 , 服务费=核准金额*服务费比例 , 上标金额=合同金额=核准金额+上浮金额+GPS安装费用
             * 预付款=服务费+GPS安装费+保证金+首付款+上收息+上收月供, 月供 = 产品方案计算的月供+GPS流量费, GPS流量费 = 上标金额*0.015 四舍五入取整数
             */
            preAmt = BigDecimalUtil.add(gpsAmt, serviceFee, margin, downPayment, feeAmount, monthAmount);

            contractAmt = sbAmt;
            gpsTrafficFee =
                    BigDecimalUtil.mul(sbAmt, new BigDecimal(consumerConfig.getOWNEN_GPS_PER())).setScale(0,
                            BigDecimal.ROUND_HALF_UP);
            monthMake = BigDecimalUtil.add(monthMake, gpsTrafficFee);

            if (detailAmt != null) {
                detailAmt.clear();
                detailAmt.put("preAmt", preAmt);
                detailAmt.put("margin", margin);
                detailAmt.put("serviceFee", serviceFee);
                detailAmt.put("sbAmt", sbAmt);
                detailAmt.put("monthMake", monthMake);
                detailAmt.put("contractAmt", contractAmt);
                detailAmt.put("gpsTrafficFee", gpsTrafficFee);
            } else {
                detailAmt = new HashMap<String, BigDecimal>();
                detailAmt.put("preAmt", preAmt);
                detailAmt.put("margin", margin);
                detailAmt.put("serviceFee", serviceFee);
                detailAmt.put("sbAmt", sbAmt);
                detailAmt.put("monthMake", monthMake);
                detailAmt.put("contractAmt", contractAmt);
                detailAmt.put("gpsTrafficFee", gpsTrafficFee);
            }
        }
        return detailAmt;
    }

    /**
     * 
     * Description:调用账务系统进行金额重算
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月15日
     */
    @SuppressWarnings("unchecked")
    private Map<String, BigDecimal> calculateAmt(PlanBean planBean, BigDecimal orderAmt) throws IqbException {
        // 调用账务系统计算金额
        Map<String, Object> sourceMap = new HashMap<String, Object>();
        sourceMap.put("instPlan", planBean);
        sourceMap.put("amt", orderAmt);
        String resultStr =
                SimpleHttpUtils.httpPost(consumerConfig.getCalculateAmtUrl(),
                        encryptUtils.encrypt(JSONObject.toJSONString(sourceMap)));
        if (resultStr != null) {
            Map<String, Object> result = JSONObject.parseObject(resultStr);
            if ("success".equals(result.get("retCode"))) {
                return (Map<String, BigDecimal>) result.get("result");
            } else {
                return null;
            }
        } else {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_FINANCE_02000007);
        }
    }

    /**
     * 获取门店签约信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月16日
     */
    @Override
    public DeptSignInfoBean getDeptSignInfo(JSONObject objs) throws IqbException {
        DeptSignInfoBean deptSignInfoBean = mortgageInfoBiz.getDeptSignInfo(objs);

        OrderBean orderBean = orderBiz.selByOrderId(objs.getString("orderId"));
        objs.clear();
        objs.put("id", orderBean.getPlanId());

        try {
            PlanBean planBean = productBiz.getPlanByID(objs);
            double floatServiceFeeRatio = 0.00;
            if (planBean != null) {
                floatServiceFeeRatio = planBean.getFloatMarginRatio();
            }
            BigDecimal orderAmt = new BigDecimal(orderBean.getOrderAmt());
            BigDecimal gpsAmt = orderBean.getGpsAmt() != null ? orderBean.getGpsAmt() : BigDecimal.ZERO;
            BigDecimal floatAmt =
                    BigDecimalUtil.mul(orderAmt, BigDecimal.valueOf(floatServiceFeeRatio)).divide(new BigDecimal(100));
            BigDecimal sbAmt = BigDecimalUtil.add(orderAmt, floatAmt, gpsAmt);
            deptSignInfoBean.setSbAmt(sbAmt);
            deptSignInfoBean.setContractAmt(sbAmt);
        } catch (Exception e) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_FINANCE_02000009);
        }
        return deptSignInfoBean;
    }

    /**
     * 获取放款确认信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月16日
     */
    @Override
    public DeptSignInfoBean getLoanInfo(JSONObject objs) throws IqbException {
        DeptSignInfoBean deptSignInfoBean = mortgageInfoBiz.getLoanInfo(objs);
        OrderBean orderBean = orderBiz.selByOrderId(objs.getString("orderId"));
        objs.clear();
        objs.put("id", orderBean.getPlanId());

        try {
            PlanBean planBean = productBiz.getPlanByID(objs);
            double floatServiceFeeRatio = 0.00;
            if (planBean != null) {
                floatServiceFeeRatio = planBean.getFloatMarginRatio();
            }
            BigDecimal orderAmt = new BigDecimal(orderBean.getOrderAmt());
            BigDecimal gpsAmt = orderBean.getGpsAmt() != null ? orderBean.getGpsAmt() : BigDecimal.ZERO;
            BigDecimal floatAmt =
                    BigDecimalUtil.mul(orderAmt, BigDecimal.valueOf(floatServiceFeeRatio)).divide(new BigDecimal(100));
            BigDecimal sbAmt = BigDecimalUtil.add(orderAmt, floatAmt, gpsAmt);
            deptSignInfoBean.setSbAmt(sbAmt);
            deptSignInfoBean.setContractAmt(sbAmt);
        } catch (Exception e) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_FINANCE_02000009);
        }
        return deptSignInfoBean;
    }
}
