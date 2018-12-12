package com.iqb.consumer.service.layer.dandelion;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.credit_product.request.GetPlanDetailsRequestMessage;
import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;
import com.iqb.consumer.data.layer.bean.dandelion.entity.InstCreditInfoEntity;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.GetDesignedPersionInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.GetInfoByOidResponsePojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.PersistDesignPersonRequestPojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.PersistOrderDetailsRequestPojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.RecalculateAmtPojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.RiskNoticeRequestPojo;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.ProductBiz;
import com.iqb.consumer.data.layer.biz.RiskInfoBiz;
import com.iqb.consumer.data.layer.biz.authoritycard.AuthorityCardBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.credit_product.CreditProductManager;
import com.iqb.consumer.data.layer.biz.dandelion.DandelionManager;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.api.dto.CreditLoanDto;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.common.utils.StringUtil;

/**
 * 
 * Description: 蒲公英服务分发中心
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月15日    adam       1.0        1.0 Version 
 * </pre>
 */
@Service
public class DandelionCenterServiceImpl implements DandelionCenterService {
    protected static final Logger logger = LoggerFactory.getLogger(DandelionCenterServiceImpl.class);
    @Autowired
    private DandelionManager dandelionManager;
    @Autowired
    private RiskInfoBiz riskInfoBiz;
    @Autowired
    private OrderBiz orderBiz;
    @Autowired
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private WFConfigBiz wfConfigBiz;
    @Autowired
    private CreditProductManager creditProductManager;
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private AuthorityCardBiz authorityCardBiz;
    @Resource
    private ProductBiz productBiz;
    private final static String PROTYPE_CAR = "车优贷";

    @Override
    public GetInfoByOidResponsePojo getInfoByOid(JSONObject requestMessage) throws GenerallyException {
        String orderId = requestMessage.getString("orderId");
        if (StringUtil.isEmpty(orderId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        return dandelionManager.getInfoByOid(orderId);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public CreditLoanDto getCheckInfoByOid(JSONObject requestMessage) throws GenerallyException {
        String orderId = requestMessage.getString("orderId");
        if (StringUtil.isEmpty(orderId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        OrderBean ioie = orderBiz.selByOrderId(orderId);
        if (ioie == null || StringUtil.isEmpty(ioie.getRegId())) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        RiskInfoBean rib = riskInfoBiz.getRiskInfoByRIAndRT(ioie.getRegId(), RiskInfoBean.RISK_TYPE_4);
        if (rib == null || StringUtil.isEmpty(rib.getCheckInfo())) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.B);
        }
        CreditLoanDto result =
                JSONObject.parseObject(rib.getCheckInfo(), CreditLoanDto.class);
        if (result == null) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.C);
        }
        return result;
    }

    @Override
    public void persistDesignPersion(JSONObject requestMessage) throws GenerallyException {
        PersistDesignPersonRequestPojo pdpr =
                JSONObject.toJavaObject(requestMessage, PersistDesignPersonRequestPojo.class);
        if (pdpr == null || !pdpr.checkRequest()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        InstCreditInfoEntity icie = new InstCreditInfoEntity();
        InstCreditInfoEntity i = dandelionManager.getInstCreditInfoEntityByOid(pdpr.getOrderId());
        if (i == null) {
            icie.createDesignPersion(pdpr);
            dandelionManager.persistDesignPersion3elements(icie);
        } else {
            icie.updateDesignPersion(pdpr);
            dandelionManager.updateDesignPersion3elements(icie);
        }
    }

    @Override
    public GetDesignedPersionInfoResponseMessage getInstCreditInfoEntityByOid(JSONObject requestMessage)
            throws GenerallyException {
        String orderId = requestMessage.getString("orderId");
        if (StringUtil.isEmpty(orderId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        GetDesignedPersionInfoResponseMessage gdpi = new GetDesignedPersionInfoResponseMessage();
        InstCreditInfoEntity icie = dandelionManager.getInstCreditInfoEntityByOid(orderId);
        InstOrderInfoEntity ioie = creditProductManager.getInstOrderInfoEntityByOrderId(orderId);
        // 添加返回债权人信息
        CreditorInfoBean creditorBean = creditProductManager.getCreditorInfoBean(orderId);
        if (ioie == null || icie == null) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        /** 订单金额存 applyAmt上标金额 存 orderAmt，为了 /workFlow/getAllReqMoney 兼容取出正确数据 **/
        RecalculateAmtPojo rap = null;
        PlanBean planBean = null;
        if (ioie.getGpsTrafficFee() != null) {
            requestMessage.put("orderAmt", ioie.getApplyAmt());
            requestMessage.put("planId", ioie.getPlanId());
            requestMessage.put("royaltyRate", ioie.getRoyaltyRate());
            requestMessage.put("gpsTrafficFee", ioie.getGpsTrafficFee());
            rap = recalculateAmtForCar(requestMessage);

        } else {
            rap = dandelionManager.getRecalculateAmtPojo(ioie.getApplyAmt(), ioie.getPlanId(), null, gdpi);
        }
        // 产品方案
        requestMessage.put("id", ioie.getPlanId());
        planBean = productBiz.getPlanByID(requestMessage);
        if (planBean != null) {
            StringBuilder planName = new StringBuilder();
            if (planBean.getDownPaymentRatio() > 0) {
                planName.append("首付");
                planName.append(planBean.getDownPaymentRatio() + "%+");
            }
            if (planBean.getMarginRatio() > 0) {
                planName.append("保证金");
                planName.append(planBean.getMarginRatio() + "%+");
            }
            if (planBean.getServiceFeeRatio() > 0) {
                planName.append("服务费");
                planName.append(planBean.getServiceFeeRatio() + "%+");
            }
            if (planBean.getUpInterestFee() > 0) {
                planName.append("上收息");
                planName.append(planBean.getUpInterestFee() + "%+");
            }
            if (planBean.getTakePayment() == 1) {
                planName.append("上收月供");
            }
            planName.append(planBean.getRemark());
            gdpi.setPlanShortName(planName.toString());
        }

        if (rap == null) {
            rap = new RecalculateAmtPojo();
        }
        rap.setGpsTrafficFee(ioie.getGpsTrafficFee());
        rap.setRoyaltyRate(ioie.getRoyaltyRate());
        rap.setPreAmt(ioie.getPreAmt());
        rap.setApplyLoanAmt(ioie.getApplyLoanAmt());
        rap.setApplyLoanDate(ioie.getApplyLoanDate());
        rap.setUnderAmt(ioie.getUnderAmt());
        gdpi.setRap(rap);
        gdpi.setIcie(icie);
        gdpi.setPlanId(ioie.getPlanId());
        gdpi.setOrderAmt(ioie.getApplyAmt());
        gdpi.setCreditorInfoBean(creditorBean);
        return gdpi;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class,
            GenerallyException.class})
    public Integer addGuaranteeNoByOid(JSONObject requestMessage) throws GenerallyException {
        String orderId = requestMessage.getString("orderId");
        if (StringUtil.isEmpty(orderId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        InstCreditInfoEntity icie = dandelionManager.getInstCreditInfoEntityByOid(orderId);
        if (icie == null) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        if (icie.getGuarantorNum() != null && icie.getGuarantorNum() == InstCreditInfoEntity.MAX_GUARANTOR_NUM) {
            return -1; // 已经到达担保人数上限
        }
        try {
            icie.setGuarantorNum(icie.getGuarantorNum() == null ? 0 : icie.getGuarantorNum() + 1);
            int i = dandelionManager.updateToAddGuaranteeNo(icie);
            if (i != 1) {
                throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
            }
            return i;
        } catch (Throwable e) {
            logger.error("DandelionCenterServiceImpl.addGuaranteeNoByOid error.", e);
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class,
            GenerallyException.class})
    public void updateCreditType(JSONObject requestMessage) throws GenerallyException {
        InstCreditInfoEntity icie = JSONObject.toJavaObject(requestMessage, InstCreditInfoEntity.class);
        if (!StringUtil.isEmpty(requestMessage.getString("mortgageInfo"))) {
            icie.setMortgageInfo(requestMessage.getJSONArray("mortgageInfo").toJSONString());
        }
        if (icie == null || !icie.checkPersistCreditTypeRequestMessage()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        int i = dandelionManager.updateCreditTypeByT(icie);
        if (i != 1) {
            throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
        }
    }

    /**
     * 计算金额
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月15日
     */
    @Override
    public RecalculateAmtPojo recalculateAmt(JSONObject requestMessage) throws GenerallyException {
        GetPlanDetailsRequestMessage gdrm = JSONUtil.toJavaObject(
                requestMessage, GetPlanDetailsRequestMessage.class);
        if (gdrm == null || !gdrm.checkRequest()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        if (requestMessage.getString("proType") != null && requestMessage.getString("proType").equals(PROTYPE_CAR)) {
            return recalculateAmtForCar(requestMessage);
        } else {
            return dandelionManager.getRecalculateAmtPojo(gdrm.getOrderAmt(), gdrm.getPlanId(), null, null);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class,
            GenerallyException.class})
    public void persistOrderDetails(JSONObject requestMessage) throws GenerallyException {
        try {
            PersistOrderDetailsRequestPojo pdrp = JSONUtil.toJavaObject(
                    requestMessage, PersistOrderDetailsRequestPojo.class);
            if (pdrp == null || !pdrp.checkRequest()) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
            }
            PlanBean pb = new PlanBean();
            RecalculateAmtPojo rap = new RecalculateAmtPojo();
            // 根据不同的产品类型调用不同的计算接口
            if (requestMessage.getString("planId") == null) {
                OrderBean orderBean = orderBiz.selByOrderId(requestMessage.getString("orderId"));
                requestMessage.put("planId", orderBean.getPlanId());
                requestMessage.put("royaltyRate", orderBean.getRoyaltyRate());
                requestMessage.put("gpsTrafficFee", orderBean.getGpsTrafficFee());

            }
            String proType = requestMessage.getString("proType");
            if (PROTYPE_CAR.equals(proType)) {
                rap = recalculateAmtForCar(requestMessage);
                pb = productBiz.getPlanByID(requestMessage);
            } else {
                if (requestMessage.getBigDecimal("applyLoanAmt") == null) {
                    rap = dandelionManager.getRecalculateAmtPojo(pdrp.getOrderAmt(), pdrp.getPlanId(), pb, null);
                }
            }

            InstOrderInfoEntity ioie = new InstOrderInfoEntity();
            ioie.updateDandelionEntity(rap, pdrp, pb);

            int i = dandelionManager.updateDandelionEntityByOid(ioie);
            if (i != 1) {
                throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
            }
        } catch (Throwable e) {
            logger.error("DandelionCenterServiceImpl.persistOrderDetails error:", e);
            throw new GenerallyException(Reason.UNKNOWN_ERROR, Layer.SERVICE, Location.A);
        }

    }

    @Override
    public void resolveRiskNotice(JSONObject requestMessage) throws GenerallyException {
        logger.info("---蒲公英风读脉风控返回接口start--");
        RiskNoticeRequestPojo rnrp = JSONObject.toJavaObject(requestMessage, RiskNoticeRequestPojo.class);
        if (rnrp == null || !rnrp.checkRequest()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        OrderBean ob = orderBiz.selByOrderId(rnrp.getOrderId());
        MerchantBean mb = merchantBeanBiz.getMerByMerNo(ob.getMerchantNo());

        /** 工作流接口交互 **/
        try {
            if (mb.getWfStatus() == 0) {
                WFConfig wfConfig = wfConfigBiz.getConfigByBizType(ob.getBizType(), ob.getWfStatus());
                String msg = StringUtil.isEmpty(rnrp.getMessage()) ? "" : rnrp.getMessage();
                submitDandelionWF(wfConfig, ob.getProcInstId(), ob.getOrderId(), mb.getId(),
                        ob.getOrderAmt(), rnrp.getRiskStatus(), msg + ":" + rnrp.getMessageInfo());
            }
        } catch (Throwable e) {
            logger.error("DandelionCenterServiceImpl.resolveRiskNotice error", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
        }

        ob.setRiskRetRemark(rnrp.getRiskStatus() + ":" + rnrp.getMessage() + "-" + rnrp.getMessageInfo());
        logger.info("---蒲公英风读脉风控 更新订单风控信息 --{}", JSONObject.toJSONString(ob));
        Integer i = orderBiz.updateOrderInfoForDandelion(ob);
        if (i != 1) {
            throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
        }
        logger.info("---蒲公英风读脉风控返回接口end--");
    }

    @Override
    public void chatWithRAR(JSONObject requestMessage) throws GenerallyException {
        // TODO Auto-generated method stub

    }

    private void submitDandelionWF(WFConfig wfConfig, String procInstId, String orderId, long orgId, String orderAmt,
            String riskStatus, String messageInfo) throws GenerallyException {
        logger.info("---蒲公英行流程回调启动工作流开始---：{}", orderId);
        JSONObject requestMessage = new JSONObject();
        requestMessage.put("orderId", orderId);
        Map<String, Object> hmProcData = new HashMap<>();
        hmProcData.put("procInstId", procInstId);
        hmProcData.put("procTaskCode", wfConfig.getProcTaskCode());// 等工作流
        // 获取订单返回标识
        int backFlag = this.getInstOrderBackFlag(requestMessage);
        GetInfoByOidResponsePojo orderBean = dandelionManager.getInfoByOid(orderId);
        String proInfo = orderBean.getProInfo();
        String orderFlag = "2";
        if (proInfo != null && !proInfo.equals("")) {
            String[] proInfos = proInfo.split(";");
            orderFlag = proInfos[0];
        } else {

        }
        Map<String, Object> hmVariables = new HashMap<>();
        hmVariables.put("procAuthType", "1"); // 1：token认证；2：session认证
        hmVariables.put("procTokenUser", wfConfig.getTokenUser());
        hmVariables.put("procTokenPass", wfConfig.getTokenPass());
        hmVariables.put("procTaskUser", wfConfig.getTaskUser());
        hmVariables.put("procTaskRole", wfConfig.getTaskRole());
        hmVariables.put("procApprStatus", "1".equals(riskStatus) ? "1" : "0");
        hmVariables.put("procApprOpinion", "1".equals(riskStatus) ? "同意" : "不同意");
        hmVariables.put("backFlag", backFlag);
        hmVariables.put("orderFlag", orderFlag);

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
        hmBizData.put("backFlag", backFlag);
        hmBizData.put("orderFlag", orderFlag);

        Map<String, Map<String, Object>> reqData = new HashMap<>();
        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        String url = wfConfig.getRiskWfUrl();
        // 发送Https请求
        logger.info("submitDandelionWF.request url{} msg ：{}", url, JSONObject.toJSONString(reqData));
        Long startTime = System.currentTimeMillis();
        @SuppressWarnings("unchecked")
        Map<Object, Object> responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
        Long endTime = System.currentTimeMillis();
        logger.info("submitDandelionWF.response msg：" + responseMap);
        logger.info("submitDandelionWF.cost time：" + (endTime - startTime));
    }

    /**
     * 
     * @param params
     * @return
     * @throws GenerallyException
     * @Author haojinlong Create Date: 2017年7月17日
     */
    @Override
    public int getInstOrderBackFlag(JSONObject requestMessage) throws GenerallyException {
        String orderId = requestMessage.getString("orderId");
        if (StringUtil.isEmpty(orderId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        try {
            OrderBean OrderBean = orderBiz.selByOrderId(orderId);
            return OrderBean.getBackFlag();
        } catch (Throwable e) {
            logger.error("DandelionCenterServiceImpl.getInstOrderBackFlag error.", e);
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月18日
     */
    @SuppressWarnings("rawtypes")
    @Override
    public long saveRiskInfo(JSONObject requestMessage) throws GenerallyException {
        // FINANCE-2454 蒲公英外访修改信息后，到读脉的信息为已修改的信息
        // 修改风控信息，更具是否存在风控信息，如果存在修改历史风控信息
        String regId = requestMessage.getString("regId");
        CreditLoanDto creditLoanDto = JSONUtil.toJavaObject(requestMessage, CreditLoanDto.class);
        if (StringUtil.isEmpty(regId)) {
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
                riskInfo.setRegId(regId);
                riskInfo.setRiskType(4);
                riskInfo.setCheckInfo(JSONObject.toJSONString(creditLoanDto, SerializerFeature.WriteMapNullValue));
                return riskInfoBiz.saveRiskInfo(riskInfo);
            }
        } catch (Throwable e) {
            logger.error("DandelionCenterServiceImpl.saveRiskInfo error.", e);
            throw new GenerallyException(Reason.UNKNOWN_ERROR, Layer.SERVICE, Location.A);
        }
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月18日
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    @Override
    public long updateOrderInfo(JSONObject requestMessage) throws GenerallyException {
        String orderId = requestMessage.getString("orderId");
        String realName = requestMessage.getString("realName");
        String regId = requestMessage.getString("regId");
        String idCard = requestMessage.getString("idCard");

        if (StringUtil.isEmpty(regId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        try {
            OrderBean orderBean = new OrderBean();
            orderBean.setOrderId(orderId);
            orderBean.setRegId(regId);
            // 更新orderInfo中regId
            orderBiz.updateOrderInfo(orderBean);

            orderBean = orderBiz.selByOrderId(orderId);
            // 更新inst_userInfo
            UserBean userBean = new UserBean();
            userBean.setId(Long.parseLong(orderBean.getUserId()));
            userBean.setRegId(regId);
            userBean.setRealName(realName);
            userBean.setIdNo(idCard);
            return userBeanBiz.updateUserInfo(userBean);
        } catch (Throwable e) {
            logger.error("DandelionCenterServiceImpl.updateOrderInfo error.", e);
            throw new GenerallyException(Reason.UNKNOWN_ERROR, Layer.SERVICE, Location.A);
        }
    }
    /**
     * 
     * Description:蒲公英流程-车优贷金额重算
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月15日
     */
    private RecalculateAmtPojo recalculateAmtForCar(JSONObject objs) {
        RecalculateAmtPojo rap = new RecalculateAmtPojo();
        objs.put("id", objs.getInteger("planId"));
        PlanBean planBean = productBiz.getPlanByID(objs);
        if (planBean != null) {
            /** 借款金额 **/
            BigDecimal orderAmt =
                    objs.getBigDecimal("orderAmt") != null ? objs.getBigDecimal("orderAmt") : BigDecimal.ZERO;
            /** gps流量费 **/
            BigDecimal gpsTrafficFee =
                    objs.getBigDecimal("gpsTrafficFee") != null ? objs.getBigDecimal("gpsTrafficFee") : BigDecimal.ZERO;
            /** 提成比例 **/
            int royaltyRate = Integer.parseInt(objs.getString("royaltyRate"));
            /** 首付款合计 **/
            // 首付款合计 = 首付款 + 上收息 + 服务费 + 保证金。
            BigDecimal downPaymentToal = BigDecimal.ZERO;
            /** 首付款 **/
            BigDecimal downPayment =
                    orderAmt.multiply(BigDecimal.valueOf(planBean.getDownPaymentRatio()).divide(new BigDecimal(100), 5,
                            BigDecimal.ROUND_HALF_UP));
            /** 上浮保证金 **/
            BigDecimal floatMargin =
                    BigDecimalUtil.mul(orderAmt, BigDecimal.valueOf(planBean.getFloatMarginRatio())).divide(
                            BigDecimal.valueOf(100), 5,
                            BigDecimal.ROUND_HALF_UP);
            /** 保证金 **/
            BigDecimal margin =
                    BigDecimalUtil.mul(orderAmt,
                            BigDecimal.valueOf(planBean.getMarginRatio() + planBean.getFloatMarginRatio())).divide(
                            BigDecimal.valueOf(100), 5,
                            BigDecimal.ROUND_HALF_UP);
            /** 服务费 **/
            BigDecimal serviceFee =
                    BigDecimalUtil.mul(orderAmt, BigDecimal.valueOf(planBean.getServiceFeeRatio())).divide(
                            BigDecimal.valueOf(100), 5,
                            BigDecimal.ROUND_HALF_UP);
            /** 提成金额 **/
            BigDecimal royaltyAmt =
                    BigDecimalUtil.mul(orderAmt, BigDecimal.valueOf(royaltyRate)).divide(BigDecimal.valueOf(100), 5,
                            BigDecimal.ROUND_HALF_UP);
            /** 上标金额 **/
            BigDecimal sbAmt = BigDecimalUtil.add(orderAmt, serviceFee, floatMargin);
            /** 放款金额 **/
            BigDecimal loanAmt = BigDecimalUtil.sub(orderAmt, margin).subtract(royaltyAmt);
            /** 月利息 **/
            BigDecimal ylAmt = sbAmt.multiply(BigDecimal.valueOf(planBean.getFeeRatio())
                    .divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_UP));
            /** 上收息 **/
            BigDecimal feeAmount = BigDecimalUtil.mul(ylAmt, BigDecimal.valueOf(planBean.getFeeYear()));
            /** 月供 **/
            BigDecimal monthMake =
                    BigDecimalUtil.div(sbAmt, BigDecimal.valueOf(planBean.getInstallPeriods())).add(ylAmt)
                            .add(gpsTrafficFee);
            downPaymentToal = BigDecimalUtil.add(downPayment, feeAmount, serviceFee, margin);

            rap.setBzAmt(margin);
            rap.setFkAmt(loanAmt);
            rap.setFwAmt(serviceFee);
            rap.setQs(planBean.getInstallPeriods());
            rap.setSbAmt(sbAmt);
            rap.setSfkAmt(downPayment);
            rap.setSfkTotalAmt(downPaymentToal);
            rap.setSsxAmt(feeAmount);
            rap.setYgAmt(monthMake);
        }
        return rap;
    }

    /**
     * 
     * Description:更新订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月28日
     */
    public int updateDandelionEntityByOid(InstOrderInfoEntity instOrderBean) {
        return dandelionManager.updateDandelionEntityByOid(instOrderBean);
    }

    @Override
    public void saveUpdateMortgageInfo(JSONObject requestMessage) throws GenerallyException {
        InstCreditInfoEntity i =
                dandelionManager.getInstCreditInfoEntityByOid(requestMessage.get("orderId").toString());
        requestMessage.remove("orderId");

        String mortgageInfo = i.getMortgageInfo();
        JSONObject objs = JSONObject.parseObject(mortgageInfo);
        // 更新总

        JSONArray parseArray = objs.parseArray("mortgageInfo");// 获取到了库中的数据
        // 将传来的json数组添加到现有库中
        JSONObject objs2 = JSONObject.parseObject(requestMessage.toJSONString());
        JSONArray parseArray2 = objs2.parseArray("mortgageInfo");
        for (int n = 0; n < parseArray2.size(); n++) {
            parseArray.add(parseArray.getJSONObject(n));
        }
        JSONObject obj3 = new JSONObject();
        obj3.put("mortgageInfo", parseArray);
        // obj3.put("", value);//总信息
        InstCreditInfoEntity icie = JSONObject.toJavaObject(requestMessage, InstCreditInfoEntity.class);

        int i1 = dandelionManager.saveUpdateMortgageInfo(obj3.toJSONString());
        if (i1 != 1) {
            throw new GenerallyException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
        }

    }

    public static void main(String[] args) {

    }
}
