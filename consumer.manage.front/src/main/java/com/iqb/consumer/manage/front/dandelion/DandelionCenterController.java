package com.iqb.consumer.manage.front.dandelion;

import java.util.LinkedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.GetDesignedPersionInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.GetInfoByOidResponsePojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.RecalculateAmtPojo;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.api.dto.CreditLoanDto;
import com.iqb.consumer.service.layer.business.service.IOrderService;
import com.iqb.consumer.service.layer.dandelion.DandelionCenterService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;

/**
 * 
 * Description: 蒲公英需求设计
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
@Controller
@RequestMapping("/dandelion")
public class DandelionCenterController extends BasicService {

    protected static final Logger logger = LoggerFactory.getLogger(DandelionCenterController.class);
    private static final int SERVICE_CODE_GET_INFO = 1;
    private static final int SERVICE_CODE_GET_RISK = 2;
    private static final int SERVICE_CODE_PERSIST_DESIGN_PERSON = 3;
    private static final int SERVICE_CODE_GET_DESIGNATED_PERSON_INFO = 4;
    private static final int SERVICE_CODE_ADD_GUARANTEE_NO = 5;
    private static final int SERVICE_CODE_UPDATE_CREDIT_TYPE = 6;
    private static final int SERVICE_CODE_UPDATE_RECALCULATE_AMT = 7;
    private static final int SERVICE_CODE_RISK_CALLBACK = 8;
    private static final int SERVICE_CODE_PERSIST_ORDER_DETAILS = 9;
    private static final int SERVICE_CODE_CHAT_WiTH_RAR = 10;

    private final String ERROR_MSG_ADD_GUARANTEE_NO = "担保人数已达上限.";
    @Autowired
    private DandelionCenterService dandelionCenterServiceImpl;

    @Resource
    private IOrderService orderService;

    /**
     * 
     * Description: 4.3基本信息接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月15日 下午3:03:31
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/get_info"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_INFO)
    public Object getInfoByOid(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            GetInfoByOidResponsePojo result = dandelionCenterServiceImpl
                    .getInfoByOid(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_INFO);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_INFO);
        }
    }

    /**
     * 保存订单金额和订单期数
     * 
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveAmtAndItems", method = RequestMethod.POST)
    public Object saveAmtAndItems(@RequestBody JSONObject objs) {
        logger.info("保存订单金额和期数:{}", objs);
        try {
            orderService.updateAmtAndItems(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "success");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 4.4风控信息(紧急联系人)接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月15日 下午3:35:19
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/get_risk"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_RISK)
    public Object getSubletInfo(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            CreditLoanDto result = dandelionCenterServiceImpl
                    .getCheckInfoByOid(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            System.out.println("--------------" + JSONObject.toJSONString(returnSuccessInfo(linkedHashMap)));
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_RISK);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_RISK);
        }
    }

    /**
     * 
     * Description: 4.6保存指派人信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月16日 上午10:00:52
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/persist_design_person"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_PERSIST_DESIGN_PERSON)
    public Object persistDesignPersion(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            dandelionCenterServiceImpl
                    .persistDesignPersion(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_PERSIST_DESIGN_PERSON);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_PERSIST_DESIGN_PERSON);
        }
    }

    /**
     * 
     * Description: 4.7查询指派人担保人个数,信贷类型，共借人信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月16日 上午10:39:28
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/get_designated_person_info"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_DESIGNATED_PERSON_INFO)
    public Object getDesignedPersionInfo(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            GetDesignedPersionInfoResponseMessage gdpi = dandelionCenterServiceImpl
                    .getInstCreditInfoEntityByOid(requestMessage);
            if (gdpi == null) {
                throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.CONTROLLER, Location.A);
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, gdpi);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_DESIGNATED_PERSON_INFO);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_DESIGNATED_PERSON_INFO);
        }
    }

    /**
     * 
     * Description: 4.8担保人数累加接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午2:37:11
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/add_guarantee_no"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_ADD_GUARANTEE_NO)
    public Object addGuaranteeNo(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            Integer i = dandelionCenterServiceImpl
                    .addGuaranteeNoByOid(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            switch (i) {
                case 1:
                    linkedHashMap.put(KEY_RESULT, StatusEnum.success);
                    break;

                case -1:
                    return generateFailResponseMessage(
                            new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A),
                            SERVICE_CODE_ADD_GUARANTEE_NO, ERROR_MSG_ADD_GUARANTEE_NO);
            }
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_ADD_GUARANTEE_NO);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_ADD_GUARANTEE_NO);
        }
    }

    /**
     * 
     * Description: 4.9保存信用贷类型，共借人等
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午2:21:31
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/update_credit_type"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_UPDATE_CREDIT_TYPE)
    public Object updateCreditType(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            dandelionCenterServiceImpl
                    .updateCreditType(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_CREDIT_TYPE);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_CREDIT_TYPE);
        }
    }

    /**
     * 
     * Description: 4.11重算接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午3:20:25
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/recalculate_amt"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_UPDATE_RECALCULATE_AMT)
    public Object recalculateAmt(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            RecalculateAmtPojo rap = dandelionCenterServiceImpl
                    .recalculateAmt(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, rap);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_RECALCULATE_AMT);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_RECALCULATE_AMT);
        }
    }

    /**
     * 
     * Description: 4.12保存接口信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月17日 下午2:09:53
     */
    @ResponseBody
    @RequestMapping(value = "/persist_order_details", method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_PERSIST_ORDER_DETAILS)
    public Object persistOrderDetails(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            dandelionCenterServiceImpl.persistOrderDetails(requestMessage);
            logger.info("FINANCE-2734 门店签约节点回显可修改开户行名称");
            orderService.saveBankInfo(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_PERSIST_ORDER_DETAILS);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_PERSIST_ORDER_DETAILS);
        }
    }

    /**
     * 
     * Description: 4.13回调业务处理
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午4:27:56
     */
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-risk_callback", method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_RISK_CALLBACK)
    public Object riskCallback(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            dandelionCenterServiceImpl.resolveRiskNotice(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_RISK_CALLBACK);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_RISK_CALLBACK);
        }
    }

    /**
     * 
     * Description: FINANCE-987 蒲公英行---资产分配（饭饭）
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月18日 下午2:13:23
     */
    @ResponseBody
    @RequestMapping(value = "/chat_withRAR", method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_CHAT_WiTH_RAR)
    public Object chatWithRAR(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            dandelionCenterServiceImpl.chatWithRAR(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CHAT_WiTH_RAR);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CHAT_WiTH_RAR);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_DANDELION_CENTER;
    }

    /**
     * 
     * Description: 获取订单返回标识
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月18日 下午2:13:23
     */
    @ResponseBody
    @RequestMapping(value = "/getInstOrderBackFlag", method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_CHAT_WiTH_RAR)
    public Object getInstOrderBackFlag(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            int backFlag = dandelionCenterServiceImpl.getInstOrderBackFlag(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put(KEY_RESULT, backFlag);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CHAT_WiTH_RAR);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CHAT_WiTH_RAR);
        }
    }

    /**
     * 是否是历史订单保存
     * 
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveHistoryOrder", method = RequestMethod.POST)
    public Object saveHistoryOrder(@RequestBody JSONObject objs) {
        logger.info("保存订单金额和期数:{}", objs);
        try {
            orderService.updateOrderProInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "success");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:蒲公英行流程-保存风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月18日
     */
    @ResponseBody
    @RequestMapping(value = "/saveRiskInfo", method = RequestMethod.POST)
    public Object saveRiskInfo(@RequestBody JSONObject objs) {
        logger.info("保存订单金额和期数:{}", objs);
        try {
            dandelionCenterServiceImpl.saveRiskInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "success");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:蒲公英行流程-更新用户信息以及订单表regid
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月18日
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrderInfo", method = RequestMethod.POST)
    public Object saveOrderInfo(@RequestBody JSONObject objs) {
        logger.info("保存订单金额和期数:{}", objs);
        try {
            dandelionCenterServiceImpl.updateOrderInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "success");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
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
    @ResponseBody
    @RequestMapping(value = "/updateOrderInfo", method = RequestMethod.POST)
    public Object updateOrderInfo(@RequestBody JSONObject objs) {
        logger.info("---蒲公英流程---更新订单信息:{}", objs);
        try {
            InstOrderInfoEntity instOrderBean = JSON.toJavaObject(objs, InstOrderInfoEntity.class);
            int result = dandelionCenterServiceImpl.updateDandelionEntityByOid(instOrderBean);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (result > 0) {
                linkedHashMap.put("result", "success");
            } else {
                linkedHashMap.put("result", "fail");
            }

            logger.info("---蒲公英流程---更新订单信息完成:{}", linkedHashMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
    /**
     * 
     * Description: FINANCE-3199 FINANCE-3192 保存抵押信息接口，返回抵押信息接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2018年4月2日 20:32:11
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/saveUpdateMortgageInfo"}, method = {RequestMethod.POST})
    public Object saveUpdateMortgageInfo(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            dandelionCenterServiceImpl
                    .saveUpdateMortgageInfo(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
