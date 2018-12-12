package com.iqb.consumer.service.layer.credit_product;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderBreakInfoEntity;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity.OrderInfoType;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException.Layer;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException.Location;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException.Reason;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.DetailsInfoPojo;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.GuaranteePledgeInfoBridge;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.GuaranteePledgeInfoBridge.StatusEnum;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.PlanDetailsPojo;
import com.iqb.consumer.data.layer.bean.credit_product.request.GetPlanDetailsRequestMessage;
import com.iqb.consumer.data.layer.bean.credit_product.request.GuaranteePledgeInfoRequestMessage;
import com.iqb.consumer.data.layer.bean.credit_product.request.MerchantCertificateRequestMessage;
import com.iqb.consumer.data.layer.bean.order.InstOrderOtherAmtEntity;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.credit_product.CreditProductManager;
import com.iqb.consumer.service.layer.common.CalculateService;
import com.iqb.etep.common.utils.JSONUtil;

@Service
public class CreditProductServiceImpl implements CreditProductService {
    private static final Logger logger = LoggerFactory
            .getLogger(CreditProductServiceImpl.class);
    private static final String ERROR_FORMAT = " ServiceCode[%s] Reason [%s] Layer [%s] Location [%s] Exception [%s]";
    @Autowired
    private CreditProductManager creditProductManager;
    @Resource
    private CalculateService calculateService;
    @Autowired
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Autowired
    private OrderBiz orderBiz;

    @Override
    public boolean merchantCertificate(JSONObject requestMessage) {
        try {
            MerchantCertificateRequestMessage mcrm = JSONUtil.toJavaObject(
                    requestMessage, MerchantCertificateRequestMessage.class);
            if (mcrm == null || !mcrm.checkRequest()) {
                return false;
            }
            return creditProductManager.merchantCertificate(mcrm);
        } catch (Throwable e) {
            return false;
        }

    }

    private static final int CHOICE_OF_CREDIT_LOAN = 1; // 选择信用贷

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class,
            CreditProductlInvalidException.class})
    public boolean guaranteePledgeInfoManager(JSONObject requestMessage,
            int serviceCode)
            throws Throwable {
        try {
            GuaranteePledgeInfoRequestMessage gpir = JSONUtil.toJavaObject(
                    requestMessage, GuaranteePledgeInfoRequestMessage.class);
            logger.info("---GPI_manager--gpir--{}",gpir);
            if (gpir == null || !gpir.checkRequest()) {
                throw new CreditProductlInvalidException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
            }
            GuaranteePledgeInfoBridge gpib = creditProductManager
                    .checkBreakOrderUpdate(gpir.getOrderId(), gpir.getCarAmt());
            logger.info("---GPI_manager--gpib--{}",gpib);
            if (gpib == null) {
                throw new CreditProductlInvalidException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
            }
            
            InstOrderBreakInfoEntity iobie = gpib.getEntity();
            InstOrderInfoEntity ioie = creditProductManager
                    .getInstOrderInfoEntityByOrderId(gpir.getOrderId());
            logger.info("---GPI_manager--ioie--{]",ioie);
            if (ioie == null) {
                throw new CreditProductlInvalidException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.B);
            }
            boolean CHOICE_CREDIT_LOAN = gpir.getUseCreditLoan() == CHOICE_OF_CREDIT_LOAN;
            if (CHOICE_CREDIT_LOAN) {
                MerchantCertificateRequestMessage mcrm = new MerchantCertificateRequestMessage();
                mcrm.setBizType(ioie.getBizType());
                mcrm.setMerchantNo(ioie.getMerchantNo());
                logger.info("---GPI_manager--!creditProductManager.merchantCertificate(mcrm)--{}",!creditProductManager.merchantCertificate(mcrm));
                if (!creditProductManager.merchantCertificate(mcrm)) {
                    throw new CreditProductlInvalidException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.A);
                }
            }
            logger.info("---GPI_manager--gpib.getStatus()--{}",gpib.getStatus());
            logger.info("---GPI_manager--iobie {}-- !iobie.checkEntity()--{}",iobie,gpib.getStatus());
            if (gpib.getStatus() == StatusEnum.FAIL) {
                throw new CreditProductlInvalidException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.B);
            } else if (iobie == null || !iobie.checkEntity()) {
                throw new CreditProductlInvalidException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.C);
            } else if (gpib.getStatus() == StatusEnum.UNVEN || CHOICE_CREDIT_LOAN) {
                /** 增加 || CHOICE_CREDIT_LOAN 判断条件 **/
                BigDecimal orderAmt = gpir.getAssessPrice().add(iobie.getGpsAmt())
                        .add(iobie.getInsAmt()).add(iobie.getOtherAmt())
                        .add(iobie.getTaxAmt()).add(iobie.getBusinessTaxAmt());
                /**
                 * 如果金额修改了，把 preAmt 重新计算，把差值计算出来 FINANCE-2317 以租代购门店预处理节点增加线上收取GPS/保险费等费用
                 * 
                 **/
                InstOrderOtherAmtEntity iooae = orderBiz.getOtherAmtEntity(ioie.getOrderId());
                BigDecimal preAmtDiff = BigDecimal.ZERO;
                if (iooae != null && iooae.getOnline() != null && iooae.getOnline() == 1) {
                    preAmtDiff = iooae.getTotalCost();
                }
                ioie.setOrderAmt(orderAmt);
                PlanDetailsPojo pdp = getPlanDetails(orderAmt, Long.parseLong(gpir.getPlanId())); // 统一计算方式

                if (pdp == null) {
                    throw new CreditProductlInvalidException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.D);
                }
                /** preAmt 重新计算 **/
                pdp.setPreAmt(preAmtDiff.add(pdp.getPreAmt()));
                if (CHOICE_CREDIT_LOAN) {
                    ioie.updateEntityUNVEN(pdp, gpir, CHOICE_CREDIT_LOAN);
                    creditProductManager.updateInstOrderInfoEntityByType(ioie, OrderInfoType.UNVEN);
                    ioie.createEntityX(pdp, gpir);
                    logger.info("生成车秒贷对象:{}", JSONObject.toJSONString(ioie));
                    if (creditProductManager.getInstOrderInfoEntityByOrderId(ioie.getOrderId()) == null) {
                        logger.info("保存save车秒贷订单:{}", ioie.getOrderId());
                        creditProductManager.saveInstOrderInfoEntityX(ioie);
                    } else {
                        ioie.updateEntityUNVEN(pdp, gpir, CHOICE_CREDIT_LOAN);
                        logger.info("保存update车秒贷订单:{}", ioie.getOrderId());
                        creditProductManager.updateInstOrderInfoEntityByType(ioie, OrderInfoType.UNVEN);
                        return true;
                    }
                } else {
                    ioie.updateEntityUNVEN(pdp, gpir, CHOICE_CREDIT_LOAN);
                    creditProductManager.updateInstOrderInfoEntityByType(ioie, OrderInfoType.UNVEN);
                }
                return true;
            } else if (gpib.getStatus() == StatusEnum.EQUAL) {
                ioie.updateEntityEQUAL(gpir);
                creditProductManager.updateInstOrderInfoEntityByType(ioie, OrderInfoType.EQUAL);
                return true;
            } else {
                throw new CreditProductlInvalidException(Reason.UNKNOW_TYPE, Layer.SERVICE, Location.A);
            }
        } catch (CreditProductlInvalidException e) {
            throw e;
        } catch (Throwable e) {
            logger.error(String.format(ERROR_FORMAT, serviceCode,
                    Reason.DB_ERROR, Layer.SERVICE, Location.A, e), e);
            throw new CreditProductlInvalidException(Reason.DB_ERROR, Layer.SERVICE, Location.A);
        }
    }

    @Override
    public PlanDetailsPojo getPlanDetails(BigDecimal orderAmt, long planId) {
        PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(planId);
        Map<String, BigDecimal> calculateAmt = calculateService.calculateAmt(planBean, orderAmt);
        PlanDetailsPojo pdp = new PlanDetailsPojo();
        pdp.setDownPayment(calculateAmt.get("downPayment"));
        pdp.setOrderItems(planBean.getInstallPeriods());
        pdp.setFee(planBean.getFeeRatio());
        pdp.setFeeAmount(calculateAmt.get("feeAmount"));
        pdp.setLeftAmt(calculateAmt.get("leftAmt"));
        pdp.setMargin(calculateAmt.get("margin"));
        pdp.setMonthInterest(calculateAmt.get("monthMake"));
        pdp.setPreAmt(calculateAmt.get("preAmount"));
        pdp.setServiceFee(calculateAmt.get("serviceFee"));
        pdp.setSbAmt(calculateAmt.get("sbAmt"));
        pdp.setContractAmt(calculateAmt.get("contractAmt"));
        pdp.setLoanAmt(calculateAmt.get("loanAmt"));
        if (planBean.getTakePayment() == 0) {
            pdp.setTakePayment(BigDecimal.ZERO);
        } else {
            pdp.setTakePayment(calculateAmt.get("monthAmount"));
        }
        return pdp;
    }

    @Override
    public PlanDetailsPojo getSpePlanDetails(BigDecimal orderAmt, long planId) {
        PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(planId);
        // 由于车秒贷的月供包含服务费即上标金额=分期金额=订单金额+服务费
        // BigDecimal countAmt =
        // BigDecimalUtil.add(orderAmt,
        // BigDecimalUtil.mul(orderAmt, new BigDecimal(planBean.getServiceFeeRatio() / 100)));
        // Map<String, BigDecimal> speAmt = calculateService.calculateAmt(planBean, countAmt);
        Map<String, BigDecimal> calculateAmt = calculateService.calculateAmt(planBean, orderAmt);
        PlanDetailsPojo pdp = new PlanDetailsPojo();
        pdp.setDownPayment(calculateAmt.get("downPayment"));
        pdp.setOrderItems(planBean.getInstallPeriods());
        pdp.setFee(planBean.getFeeRatio());
        pdp.setFeeAmount(calculateAmt.get("feeAmount"));
        pdp.setLeftAmt(calculateAmt.get("leftAmt"));
        pdp.setMargin(calculateAmt.get("margin"));
        pdp.setMonthInterest(calculateAmt.get("monthMake"));
        pdp.setPreAmt(calculateAmt.get("preAmount"));
        pdp.setServiceFee(calculateAmt.get("serviceFee"));
        pdp.setSbAmt(calculateAmt.get("sbAmt"));
        pdp.setContractAmt(calculateAmt.get("contractAmt"));
        pdp.setLoanAmt(calculateAmt.get("loanAmt"));
        if (planBean.getTakePayment() == 0) {
            pdp.setTakePayment(BigDecimal.ZERO);
        } else {
            pdp.setTakePayment(calculateAmt.get("monthAmount"));
        }
        return pdp;
    }

    @Override
    public boolean persistPlanDetailsX(GetPlanDetailsRequestMessage gdrm, int serviceCode)
            throws CreditProductlInvalidException {
        try {
            if (gdrm == null || !gdrm.checkRequestX()) {
                throw new CreditProductlInvalidException(
                        Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
            }
            PlanDetailsPojo pdp = getSpePlanDetails(gdrm.getOrderAmt(),
                    gdrm.getPlanId());
            if (pdp == null) {
                throw new CreditProductlInvalidException(Reason.INVALID_ENTITY,
                        Layer.SERVICE, Location.A);
            }
            InstOrderInfoEntity ioie = new InstOrderInfoEntity();
            ioie.setOrderId(gdrm.getOrderId());
            ioie.updateEntityX(pdp, gdrm);
            creditProductManager.updateInstOrderInfoEntityByType(ioie,
                    OrderInfoType.UPDATE);
            return true;
        } catch (Throwable e) {
            logger.error(String.format(ERROR_FORMAT, serviceCode,
                    Reason.DB_ERROR, Layer.SERVICE, Location.A, e), e);
            throw new CreditProductlInvalidException(Reason.DB_ERROR,
                    Layer.SERVICE, Location.A);
        }
    }

    @Override
    public DetailsInfoPojo getInfoDetailsX(String orderIdx)
            throws CreditProductlInvalidException {
        if (StringUtil.isEmpty(orderIdx)) {
            throw new CreditProductlInvalidException(
                    Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        return creditProductManager.getInfoDetailsX(orderIdx);
    }

    @Override
    public boolean isOverFirstTrial(String orderId) throws CreditProductlInvalidException {
        if (StringUtil.isEmpty(orderId)) {
            throw new CreditProductlInvalidException(
                    Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        if (orderId.endsWith("X")) {
            orderId = orderId.substring(0, orderId.lastIndexOf("X"));
        }
        InstOrderInfoEntity ioie = creditProductManager.getInstOrderInfoEntityByOrderId(orderId);
        if (ioie == null) {
            throw new CreditProductlInvalidException(
                    Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        // 是否完成初审审批，即wfStatus=9，流程结束或者wfStatus=3，已经分期。
        if (ioie.getWfStatus() != null && (9 == ioie.getWfStatus() || 3 == ioie.getWfStatus())) {
            return true;
        }
        return false;
    }

}
