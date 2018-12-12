package com.iqb.consumer.manage.front.credit_product;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException.Layer;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException.Location;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException.Reason;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.DetailsInfoPojo;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.PlanDetailsPojo;
import com.iqb.consumer.data.layer.bean.credit_product.request.GetPlanDetailsRequestMessage;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.service.layer.business.service.IOrderService;
import com.iqb.consumer.service.layer.credit_product.CreditProductService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.JSONUtil;

@Controller
@RequestMapping("/credit_pro")
public class CreditProductController extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(CreditProductController.class);

    @Autowired
    private CreditProductService creditProductServiceImpl;

    @Autowired
    private IOrderService orderService;

    private static final int SUCCESS = 1;
    private static final int FAIL = 2;
    private static final String RESPONSE_KEY = "result";

    private static final String ERROR_FORMAT = " ServiceCode[%s] Reason [%s] Layer [%s] Location [%s] Exception [%s]";

    private static final int SERVICE_CODE_GET_PLAN_DETAILS = 1;
    private static final int SERVICE_CODE_IS_MERCHANT_QUALIFIED_CMD = 2;
    private static final int SERVICE_CODE_GUARANTEE_PLEDGE_INFO_MANAGER = 3;
    private static final int SERVICE_CODE_PLAN_DETAILS_BY_CRITERIA = 4;
    private static final int SERVICE_CODE_PERSIST_PLAN_DETAILS_X = 5;
    private static final int SERVICE_CODE_GET_INFO_DETAILS_X = 6;
    private static final int SERVICE_CODE_IS_OVER_FIRST_TRIAL = 7;

    /**
     * Description: 4.1订单金额重置接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年3月21日 下午9:14:56
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/reset_order_amt"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_PLAN_DETAILS)
    public Object getPlanDetails(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {

        try {
            GetPlanDetailsRequestMessage gdrm = JSONUtil.toJavaObject(
                    requestMessage, GetPlanDetailsRequestMessage.class);
            if (gdrm == null || !gdrm.checkRequest()) {
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090001));
            }
            PlanDetailsPojo result = creditProductServiceImpl.getPlanDetails(
                    gdrm.getOrderAmt(), gdrm.getPlanId());
            if (result != null) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put(RESPONSE_KEY, result);
                return super.returnSuccessInfo(linkedHashMap);
            }
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00010001));
        } catch (Throwable e) {
            logger.error(String.format(ERROR_FORMAT,
                    SERVICE_CODE_GET_PLAN_DETAILS,
                    Reason.UNKNOWN_ERROR, Layer.CONTROLLER, Location.A, e), e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * Description: 4.1订单金额重置接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年3月21日 下午9:14:56
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/SpeResetOrderAmt"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_PLAN_DETAILS)
    public Object getSpePlanDetails(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {

        try {
            GetPlanDetailsRequestMessage gdrm = JSONUtil.toJavaObject(
                    requestMessage, GetPlanDetailsRequestMessage.class);
            if (gdrm == null || !gdrm.checkRequest()) {
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090001));
            }
            PlanDetailsPojo result = creditProductServiceImpl.getSpePlanDetails(
                    gdrm.getOrderAmt(), gdrm.getPlanId());
            if (result != null) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put(RESPONSE_KEY, result);
                return super.returnSuccessInfo(linkedHashMap);
            }
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00010001));
        } catch (Throwable e) {
            logger.error(String.format(ERROR_FORMAT,
                    SERVICE_CODE_GET_PLAN_DETAILS,
                    Reason.UNKNOWN_ERROR, Layer.CONTROLLER, Location.A, e), e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * Description: 4.2商户是否车秒贷业务类型
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年3月22日 上午9:44:21
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/merchant_certificate"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_IS_MERCHANT_QUALIFIED_CMD)
    public Object isMerchantQualifiedCMD(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {

        try {
            boolean result = creditProductServiceImpl
                    .merchantCertificate(requestMessage);
            if (result) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put(RESPONSE_KEY, SUCCESS);
                return super.returnSuccessInfo(linkedHashMap);
            } else {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put(RESPONSE_KEY, FAIL);
                return super.returnSuccessInfo(linkedHashMap);
            }
        } catch (Throwable e) {
            logger.error(String.format(ERROR_FORMAT,
                    SERVICE_CODE_IS_MERCHANT_QUALIFIED_CMD,
                    Reason.UNKNOWN_ERROR, Layer.CONTROLLER, Location.A, e), e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * Description: 4.3抵质押保存数据
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年3月22日 上午9:45:57
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/GPI_manager"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GUARANTEE_PLEDGE_INFO_MANAGER)
    public Object guaranteePledgeInfoManager(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {

        try {
            boolean result = creditProductServiceImpl
                    .guaranteePledgeInfoManager(requestMessage,
                            SERVICE_CODE_GUARANTEE_PLEDGE_INFO_MANAGER);
            if (result) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put(RESPONSE_KEY, SUCCESS);
                return super.returnSuccessInfo(linkedHashMap);
            }
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00010001));
        } catch (CreditProductlInvalidException e) {
            return response(e, SERVICE_CODE_GUARANTEE_PLEDGE_INFO_MANAGER);
        } catch (Throwable e) {
            logger.error(String.format(ERROR_FORMAT,
                    SERVICE_CODE_GUARANTEE_PLEDGE_INFO_MANAGER,
                    Reason.UNKNOWN_ERROR,
                    Layer.CONTROLLER, Location.A, e), e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 4.4商户和子业务类型查询分期计划
     * 
     * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/plan_details_by_criteria"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_PLAN_DETAILS_BY_CRITERIA)
    public Object planDetailsByCriteria(@RequestBody JSONObject objs,
            HttpServletRequest request) {
        try {
            logger.info("query single order info,query params:{}", objs);
            List<PlanBean> list = orderService.getPlanByMerAndBType(objs);
            if (!CollectionUtils.isEmpty(list)) {
                for (PlanBean planBean : list) {
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
                    planBean.setPlanName(planName.toString());
                }
            }
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put(RESPONSE_KEY, list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error(String.format(ERROR_FORMAT,
                    SERVICE_CODE_PLAN_DETAILS_BY_CRITERIA,
                    Reason.UNKNOWN_ERROR, Layer.CONTROLLER, Location.A, e), e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * Description: 4.6保存车秒贷
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年3月22日 下午4:33:48
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/persist_plan_details_x"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_PERSIST_PLAN_DETAILS_X)
    public Object persistPlanDetailsX(@RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        try {
            GetPlanDetailsRequestMessage gdrm = JSONUtil.toJavaObject(
                    requestMessage, GetPlanDetailsRequestMessage.class);
            boolean result = creditProductServiceImpl.persistPlanDetailsX(gdrm, SERVICE_CODE_PERSIST_PLAN_DETAILS_X);
            if (result) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put(RESPONSE_KEY, SUCCESS);
                return super.returnSuccessInfo(linkedHashMap);
            }
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00010001));
        } catch (CreditProductlInvalidException e) {
            return response(e, SERVICE_CODE_PERSIST_PLAN_DETAILS_X);
        } catch (Throwable e) {
            logger.error(String.format(ERROR_FORMAT,
                    SERVICE_CODE_PERSIST_PLAN_DETAILS_X,
                    Reason.UNKNOWN_ERROR,
                    Layer.CONTROLLER, Location.A, e), e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    private static final String KEY_ORDER_ID = "orderId";

    /**
     * Description: 4.9拆分订单展示详情
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年4月18日 上午10:16:20
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/get_info_details_x"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_INFO_DETAILS_X)
    public Object getInfoDetailsX(@RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        try {
            String orderIdx = requestMessage.getString(KEY_ORDER_ID);
            DetailsInfoPojo result = creditProductServiceImpl.getInfoDetailsX(orderIdx);
            if (result == null || !result.checkPojo()) {
                logger.error(String.format(ERROR_FORMAT,
                        SERVICE_CODE_GET_INFO_DETAILS_X,
                        Reason.INVALID_ENTITY, Layer.CONTROLLER, Location.A,
                        JSONObject.toJSONString(result)));
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090004));
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(RESPONSE_KEY, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (CreditProductlInvalidException e) {
            return response(e, SERVICE_CODE_GET_INFO_DETAILS_X);
        } catch (Throwable e) {
            logger.error(String.format(ERROR_FORMAT,
                    SERVICE_CODE_GET_INFO_DETAILS_X,
                    Reason.UNKNOWN_ERROR,
                    Layer.CONTROLLER, Location.A, e), e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: FINANCE-1015 车贷业务项目初审后，车秒贷项目初审才可审核 判断主订单是否完成初审
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月18日 下午1:53:56
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/over_first_trial"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_IS_OVER_FIRST_TRIAL)
    public Object overFirstTrial(@RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        try {
            String orderIdx = requestMessage.getString(KEY_ORDER_ID);
            boolean result = creditProductServiceImpl.isOverFirstTrial(orderIdx);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(RESPONSE_KEY, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (CreditProductlInvalidException e) {
            return response(e, SERVICE_CODE_GET_INFO_DETAILS_X);
        } catch (Throwable e) {
            logger.error(String.format(ERROR_FORMAT,
                    SERVICE_CODE_GET_INFO_DETAILS_X,
                    Reason.UNKNOWN_ERROR,
                    Layer.CONTROLLER, Location.A, e), e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    public Object response(CreditProductlInvalidException e, int serviceCode) {
        switch (e.getReason()) {
            case INVALID_REQUEST_PARAMS:
                logger.error(String.format(ERROR_FORMAT, serviceCode,
                        e.getReason(), e.getLayer(), e.getLocation(), e));
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090004));
            case DB_NOT_FOUND:
                logger.error(String.format(ERROR_FORMAT, serviceCode,
                        e.getReason(), e.getLayer(), e.getLocation(), e));
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090003));
            case INVALID_ENTITY:
                logger.error(String.format(ERROR_FORMAT, serviceCode,
                        e.getReason(), e.getLayer(), e.getLocation(), e));
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090001));
            case UNKNOW_TYPE:
                logger.error(String.format(ERROR_FORMAT, serviceCode,
                        e.getReason(), e.getLayer(), e.getLocation(),
                        serviceCode, e));
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090004));
            case DB_ERROR:
                logger.error(String.format(ERROR_FORMAT, serviceCode,
                        e.getReason(), e.getLayer(), e.getLocation(), e));
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00010001));
            case UNKNOWN_ERROR:
                logger.error(String.format(ERROR_FORMAT, serviceCode,
                        e.getReason(), e.getLayer(), e.getLocation(), e));
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00000002));
            case REPEATED_INJECTION:
                logger.error(String.format(ERROR_FORMAT, serviceCode,
                        e.getReason(), e.getLayer(), e.getLocation(), e));
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00090002));
            default:
                logger.error(String.format(ERROR_FORMAT, serviceCode,
                        Reason.UNKNOWN_ERROR, Layer.CONTROLLER, Location.A, e));
                return super.returnFailtrueInfo(new IqbException(
                        CommonReturnInfo.BASE00000002));
        }
    }

}
