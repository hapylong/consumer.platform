/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月14日 上午10:25:27
 * @version V1.0
 */
package com.iqb.consumer.manage.front.api;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.config.RBParamConfig;
import com.iqb.consumer.common.constant.FinanceConstant;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.data.layer.bean.api.PicInformationPojo;
import com.iqb.consumer.data.layer.bean.merchant.MerchantKeypair;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.schedule.http.ApiRequestMessage;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantKeypairBiz;
import com.iqb.consumer.data.layer.biz.paylog.PaymentLogBiz;
import com.iqb.consumer.data.layer.biz.schedule.ScheduleTaskAnalysisAllot;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.consumer.manage.front.api.util.PayUtils;
import com.iqb.consumer.manage.front.api.util.WhiteListUtils;
import com.iqb.consumer.service.layer.api.AssetApiService;
import com.iqb.consumer.service.layer.api.dto.CarAssetDto;
import com.iqb.consumer.service.layer.api.dto.CreditLoanDto;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.consumer.service.layer.business.service.impl.OrderService;
import com.iqb.consumer.service.layer.common.CalculateService;
import com.iqb.consumer.service.layer.pay.PayService;
import com.iqb.consumer.service.layer.wfservice.CombinationQueryService;
import com.iqb.consumer.service.layer.xfpay.XFPayService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.ucf.sdk.CoderException;
import com.ucf.sdk.UcfForOnline;

/**
 * 服务于业务层进件API接口,类下开放接口全部通过发放商户密钥进行加密传输
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@RestController
@RequestMapping("/api")
public class IntoAssetController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String MERCHANTNO = "merchantNo";

    /**
     * 这块后续随着后续业务增加，可以通过表配置业务对应的服务和方法。
     */
    protected static final String PGYMORTGAGE = "pgymortgage";// 蒲公英收益权抵押类
    protected static final String CARASSET = "carAsset";// 车类资产
    protected static final String DIDILOAN = "DIDILOAN";// 滴滴贷款
    protected static final String THREEYARDS = "threeYards"; // 三码鉴权
    protected static final String FOURYARDS = "fourYards"; // 四码鉴权
    protected static final String HTC = "htc";// 滴滴贷款

    @Resource
    private EncryptUtils encryptUtils;
    @Resource
    private MerchantKeypairBiz merchantKeypairBiz;
    @Resource
    private AssetApiService assetApiService;
    @Resource
    private RBParamConfig rbParamConfig;
    @Resource
    private OrderService orderService;
    @Resource
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private ParamConfig paramConfig;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Resource
    private BillInfoService billInfoService;
    @Resource
    private PaymentLogBiz paymentLogBiz;
    @Autowired
    private PayService payService;
    @Resource
    private XFPayService xfPayService;
    @Resource
    private CombinationQueryService combinationQueryService;
    @Autowired
    private ScheduleTaskAnalysisAllot scheduleTaskAnalysisAllot;
    @Resource
    private CalculateService calculateService;

    /**
     * 资产进件API接口,资产进件包含[1,用户信息(含银行卡) 2,订单信息(车) 3,风控信息]
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/into{type}Asset", method = RequestMethod.POST)
    public Object intoAsset(@PathVariable("type") String type, @RequestBody String obj, HttpServletRequest request) {
        logger.info("商户:{}发送的数据为:{}", type, obj);
        Map<String, Object> result = new HashMap<>();
        String decrypData;
        try {
            JSONObject parseObj = null;
            try {
                parseObj = JSONObject.parseObject(obj);
            } catch (Exception e) {
                throw new IqbException(ApiReturnInfo.API_BIZANALYTIC_20000000);
            }
            decrypData = receiveData(request, parseObj, result);
        } catch (IqbException e1) {
            result.put("result_code", e1.getRetInfo().getRetCode());
            result.put("result_msg", e1.getRetInfo().getRetUserInfo());
            return result;
        }
        // 处理业务逻辑,不同业务分发不同的Service
        if (PGYMORTGAGE.equalsIgnoreCase(type)) {
            CreditLoanDto creditLoanDto = null;
            try {
                creditLoanDto = JSONObject.parseObject(decrypData, CreditLoanDto.class);
            } catch (Exception e) {
                logger.error("业务类型:{},数据:{}为标准Bean发送异常", type, decrypData, e);
                result.put("result_code", ApiReturnInfo.API_BIZANALYTIC_20000000.getRetCode());
                result.put("result_msg", ApiReturnInfo.API_BIZANALYTIC_20000000.getRetUserInfo());
                return result;
            }
            // 最终处理逻辑
            try {
                result = assetApiService.intoCreditLoan(creditLoanDto);
            } catch (IqbException e) {
                logger.error("业务逻辑处理happen异常", e);
                result.put("result_code", e.getRetInfo().getRetCode());
                result.put("result_msg", e.getRetInfo().getRetUserInfo());
                return result;
            }
        } else if (DIDILOAN.equalsIgnoreCase(type)) {
            // 滴滴贷款业务
        } else if (CARASSET.equalsIgnoreCase(type)) {
            // 车类贷款业务
            CarAssetDto carAssetDto = null;
            try {
                carAssetDto = JSONObject.parseObject(decrypData, CarAssetDto.class);
            } catch (Exception e) {
                logger.error("业务类型:{},数据:{}为标准Bean发送异常", type, decrypData, e);
                result.put("result_code", ApiReturnInfo.API_BIZANALYTIC_20000000.getRetCode());
                result.put("result_msg", ApiReturnInfo.API_BIZANALYTIC_20000000.getRetUserInfo());
                return result;
            }
            // 逻辑处理类
            try {
                result = assetApiService.intoCarAsset(carAssetDto);
            } catch (IqbException e) {
                logger.error("业务逻辑处理happen异常", e);
                result.put("result_code", e.getRetInfo().getRetCode());
                result.put("result_msg", e.getRetInfo().getRetUserInfo());
                return result;
            }
        } else {
            result.put("result_code", ApiReturnInfo.API_NOTSUPPORTBIZ_90000000.getRetCode());
            result.put("result_msg", ApiReturnInfo.API_NOTSUPPORTBIZ_90000000.getRetUserInfo());
            return result;
        }
        return result;
    }

    /**
     * 用户鉴权
     * 
     * @param type
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = {"/auth{type}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object auth(@PathVariable("type") String type, @RequestBody String obj, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String decrypData;
        try {
            JSONObject parseObj = null;
            try {
                parseObj = JSONObject.parseObject(obj);
            } catch (Exception e) {
                throw new IqbException(ApiReturnInfo.API_BIZANALYTIC_20000000);
            }
            decrypData = receiveData(request, parseObj, result);
        } catch (IqbException e1) {
            result.put("result_code", e1.getRetInfo().getRetCode());
            result.put("result_msg", e1.getRetInfo().getRetUserInfo());
            return result;
        }
        AuthDto authDto = JSONObject.parseObject(decrypData, AuthDto.class);
        Map<String, String> params = new HashMap<>();
        params.put("card_no", authDto.getCardNo()); // 卡号
        params.put("owner", authDto.getOwner()); // 姓名
        params.put("cert_no", authDto.getCertNo()); // 证件号
        params.put("member_id", authDto.getMemberId());
        Map<?, ?> map = null;
        if (THREEYARDS.equalsIgnoreCase(type)) {// 三码鉴权
            map = IdentifyOperation.checkThreeElements(params, rbParamConfig);
        } else if (FOURYARDS.equalsIgnoreCase(type)) { // 四码鉴权

        } else {
            result.put("result_code", "9999");
            result.put("result_msg", "不支持的鉴权方式");
        }
        return map;
    }

    /**
     * API接口通过商户查询计划列表
     * 
     * @param obj
     * @param response
     * @return
     */
    @RequestMapping(value = "/selPlanByMerch", method = RequestMethod.POST)
    public Object selPlanByMerchantNo(@RequestBody JSONObject obj, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String decrypData;
        try {
            decrypData = receiveData(request, obj, result);
        } catch (IqbException e1) {
            result.put("result_code", e1.getRetInfo().getRetCode());
            result.put("result_msg", e1.getRetInfo().getRetUserInfo());
            return result;
        }
        List<PlanBean> planBeans = null;
        try {
            JSONObject jsonObject = validateParams(decrypData, "selPlanByMerchantNo");
            logger.info("查询计划参数:{}", jsonObject);
            planBeans = orderService.getPlanByMerAndBType(jsonObject);
        } catch (IqbException e) {
            result.put("result_code", e.getRetInfo().getRetCode());
            result.put("result_msg", e.getRetInfo().getRetUserInfo());
            return result;
        }
        result.put("result_code", ApiReturnInfo.API_GET_SUCCESS_80000000.getRetCode());
        result.put("result_msg", planBeans);
        return result;
    }

    /**
     * 根据金额和计划ID计算各明细
     * 
     * @param obj
     * @param request
     * @return
     */
    @RequestMapping(value = "/recalAmt", method = RequestMethod.POST)
    public Object recalAmt(@RequestBody JSONObject obj, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String decrypData;
        try {
            decrypData = receiveData(request, obj, result);
        } catch (IqbException e1) {
            result.put("result_code", e1.getRetInfo().getRetCode());
            result.put("result_msg", e1.getRetInfo().getRetUserInfo());
            return result;
        }
        Map<String, BigDecimal> detailAmt = null;
        try {
            JSONObject jsonObject = validateParams(decrypData, "recalAmt");
            logger.info("重算金额参数:{}", jsonObject);
            PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(Long.parseLong(jsonObject.getString("planId")));// 获取订单计划
            if (planBean != null) {
                detailAmt = calculateService.calculateAmt(planBean, new BigDecimal(jsonObject.getString("orderAmt")));// getDetail(new
                                                                                                                      // BigDecimal(jsonObject.getString("orderAmt")),
                                                                                                                      // planBean);
            } else {
                result.put("result_code", ApiReturnInfo.API_PLAN_NOEXIT_40000004.getRetCode());
                result.put("result_msg", ApiReturnInfo.API_PLAN_NOEXIT_40000004.getRetUserInfo());
                return result;
            }
        } catch (IqbException e) {
            result.put("result_code", e.getRetInfo().getRetCode());
            result.put("result_msg", e.getRetInfo().getRetUserInfo());
            return result;
        } catch (Exception e) {
            logger.error("计算:{}明细发生错误", obj, e);
            result.put("result_code", ApiReturnInfo.API_RECAL_ERROR_40000006.getRetCode());
            result.put("result_msg", ApiReturnInfo.API_RECAL_ERROR_40000006.getRetUserInfo());
            return result;
        }
        result.put("result_code", ApiReturnInfo.API_GET_SUCCESS_80000000.getRetCode());
        result.put("result_msg", detailAmt);
        return result;
    }

    private JSONObject validateParams(String decrypData, String method) throws IqbException {
        JSONObject parseObject = null;
        try {
            parseObject = JSONObject.parseObject(decrypData);
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZANALYTIC_20000000);
        }
        if ("selPlanByMerchantNo".equals(method)) { // API接口通过商户查询计划列表

            if (StringUtils.isBlank(parseObject.getString("merchantNo"))) {
                // 商户号
                throw new IqbException(ApiReturnInfo.API_MERCHANT_NULL_40000002);
            } else if (StringUtils.isBlank(parseObject.getString("bizType"))) {
                // 商户类型
                throw new IqbException(ApiReturnInfo.API_BIZTYPE_NULL_40000003);
            }
        } else if ("recalAmt".equals(method)) {

        } else if ("getLastThreeOrderInfo".equals(method)) {
            if (StringUtils.isBlank(parseObject.getString("merchantNo"))) {
                // 商户号
                throw new IqbException(ApiReturnInfo.API_MERCHANT_NULL_40000002);
            } else if (StringUtils.isBlank(parseObject.getString("bizType"))) {
                // 商户类型
                throw new IqbException(ApiReturnInfo.API_BIZTYPE_NULL_40000003);
            } else if (StringUtils.isBlank(parseObject.getString("regId"))) {
                // 手机号码
                throw new IqbException(ApiReturnInfo.API_BIZTYPE_NULL_40000004);
            } else if (StringUtils.isBlank(parseObject.getString("orderId"))) {
                // 订单ID
                throw new IqbException(ApiReturnInfo.API_BIZTYPE_NULL_40000005);
            }
        } else if ("getAllOrderInfo".equals(method)) {
            if (StringUtils.isBlank(parseObject.getString("orderId"))) {
                // 订单ID
                throw new IqbException(ApiReturnInfo.API_BIZTYPE_NULL_40000005);
            }
        }
        if ("sendPaySms".equals(method)) { // API接口通过商户查询计划列表
            if (StringUtils.isBlank(parseObject.getString("regId"))) {
                // 手机号码
                throw new IqbException(ApiReturnInfo.API_PAY_PARAMS_NULL_40000007);
            } else if (StringUtils.isBlank(parseObject.getString("bankCardNo"))) {
                // 银行卡号
                throw new IqbException(ApiReturnInfo.API_PAY_PARAMS_NULL_40000008);
            } else if (StringUtils.isBlank(parseObject.getString("amount"))) {
                // 金额
                throw new IqbException(ApiReturnInfo.API_PAY_PARAMS_NULL_40000009);
            } else if (StringUtils.isBlank(parseObject.getString("traceNo"))) {
                // 订单号
                throw new IqbException(ApiReturnInfo.API_PAY_PARAMS_NULL_400000012);
            } else if (StringUtils.isBlank(parseObject.getString("flag"))) {
                // 还款类别
                throw new IqbException(ApiReturnInfo.API_PAY_PARAMS_NULL_400000013);
            } else if (StringUtils.isBlank(parseObject.getString("callbackUrl"))) {
                // 回调地址
                throw new IqbException(ApiReturnInfo.API_PAY_PARAMS_NULL_400000019);
            }

        } else if ("PCpayConfirm".equals(method)) { // API接口通过商户查询计划列表
            if (StringUtils.isBlank(parseObject.getString("paymentId"))) {
                // 支付ID
                throw new IqbException(ApiReturnInfo.API_PAY_PARAMS_NULL_400000014);
            } else if (StringUtils.isBlank(parseObject.getString("smsCode"))) {
                // 验证码
                throw new IqbException(ApiReturnInfo.API_PAY_PARAMS_NULL_400000010);
            } else if (StringUtils.isBlank(parseObject.getString("traceNo"))) {
                // 订单号
                throw new IqbException(ApiReturnInfo.API_PAY_PARAMS_NULL_400000012);
            }
        }
        return parseObject;
    }

    /**
     * 通用解密方法
     * 
     * @param request
     * @param result
     * @return
     * @throws IqbException
     */
    private String receiveData(HttpServletRequest request, JSONObject parseObj, Map<String, Object> result)
            throws IqbException {
        MerchantKeypair merchantKeypair = null;
        // 获取商户和商户对应的密钥对
        String merchantNo = null;
        try {
            merchantNo = parseObj.getString(MERCHANTNO);
            logger.info("商户:{}请求获取密钥", merchantNo);
            merchantKeypair = merchantKeypairBiz.queryKeyPair(merchantNo);
            if (merchantKeypair == null) {
                throw new IqbException(ApiReturnInfo.API_GETNULLKEY_10000000);
            }
        } catch (IqbException iqb) {
            logger.error("商户:{}请求获取密钥发生异常:{}", merchantNo, iqb);
            throw new IqbException(ApiReturnInfo.API_GETKEY_10000001);
        }

        // 验证白名单
        String whiteList = merchantKeypair.getWhiteList();
        if (StringUtils.isBlank(whiteList)) { // 白名单
            throw new IqbException(ApiReturnInfo.API_NOTINWHITELIST_90000001);
        } else {
            String[] list = whiteList.split(",");
            boolean whiteFlag = WhiteListUtils.valid(request, Arrays.asList(list));
            if (!whiteFlag) { // 在白名单内
                throw new IqbException(ApiReturnInfo.API_NOTINWHITELIST_90000001);
            }
        }
        // 开始解密数据
        String decrypData = null;
        try {
            decrypData = encryptUtils.decode(parseObj, merchantKeypair.getPublicKey());
            logger.info("商户:{},解密后的数据为:{}", merchantNo, decrypData);
        } catch (IqbException e) {
            logger.error("商户:{},解密发生异常:{}", merchantNo, e);
            throw new IqbException(ApiReturnInfo.API_DECRYPTION_10000002);
        }
        return decrypData;
    }

    /**
     * Description:根据regId orderId merchantNo bizType获取最近三期待还款账单
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getLastThreeOrderInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getLastThreeOrderInfo(@RequestBody JSONObject obj, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String decrypData;
        try {
            decrypData = receiveData(request, obj, result);
        } catch (IqbException e1) {
            result.put("result_code", e1.getRetInfo().getRetCode());
            result.put("result_msg", e1.getRetInfo().getRetUserInfo());
            return result;
        }
        try {
            JSONObject jsonObject = validateParams(decrypData, "getLastThreeOrderInfo");
            logger.info("获取最近三期待还款账单参数:{}", jsonObject);
            result = assetApiService.getLastThreeOrderInfo(jsonObject);
        } catch (IqbException e) {
            result.put("result_code", e.getRetInfo().getRetCode());
            result.put("result_msg", e.getRetInfo().getRetUserInfo());
            return result;
        }
        return result;
    }

    /**
     * Description:根据orderId获取全部账单
     * 
     * @param objs
     * @param request
     * @return
     */
    @RequestMapping(value = "/getAllOrderInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getAllOrderInfo(@RequestBody JSONObject obj, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String decrypData;
        try {
            decrypData = receiveData(request, obj, result);
        } catch (IqbException e1) {
            result.put("result_code", e1.getRetInfo().getRetCode());
            result.put("result_msg", e1.getRetInfo().getRetUserInfo());
            return result;
        }
        try {
            JSONObject jsonObject = validateParams(decrypData, "getAllOrderInfo");
            logger.info("获取获取全部账单参数:{}", jsonObject);
            result = assetApiService.getAllOrderInfo(jsonObject);
        } catch (IqbException e) {
            result.put("result_code", e.getRetInfo().getRetCode());
            result.put("result_msg", e.getRetInfo().getRetUserInfo());
            return result;
        }
        return result;
    }

    /**
     * 
     * Description:短信验证码发送
     * 
     * @param objs
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendPaySms", method = RequestMethod.POST)
    public Object sendPaySms(@RequestBody JSONObject obj, HttpServletRequest request) {
        logger.info("调用先锋接口进行短信验证码发送--开始:{}", obj);
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> xfResult = new HashMap<>();
        result.put("result_code", "100000");// 默认预支付发生异常

        String decrypData;
        try {
            decrypData = receiveData(request, obj, result);
        } catch (IqbException e1) {
            result.put("result_code", e1.getRetInfo().getRetCode());
            result.put("result_msg", e1.getRetInfo().getRetUserInfo());
            return result;
        }
        JSONObject jsonObject;
        try {
            jsonObject = validateParams(decrypData, "sendPaySms");
            logger.info("短信验证码发送参数:{}", jsonObject);
        } catch (IqbException e) {
            result.put("result_code", e.getRetInfo().getRetCode());
            result.put("result_msg", e.getRetInfo().getRetUserInfo());
            return result;
        }

        PayConfBean payConfBean = null;
        try {
            String flag = jsonObject.getString("flag"); // 还款类别 1 预付款 2 正常分期
            String traceNo = jsonObject.getString("traceNo"); // 订单号
            String regId = jsonObject.getString("regId"); // 手机号
            String bankCardNo = jsonObject.getString("bankCardNo"); // 银行卡号
            String amount = jsonObject.getString("amount"); // 交易金额
            String callbackUrl = jsonObject.getString("callbackUrl"); // 回调地址

            // 1.查询订单
            Map<String, Object> params = new HashMap<>();
            params.put("orderId", traceNo);
            OrderBean orderBean = combinationQueryService.selectOne(params);
            payConfBean = payService.getPayChannelBy(orderBean.getPayOwnerId(), orderBean.getMerchantNo());
            // 2.查询用户信息
            UserBean ub = xfPayService.getUserInfo(Long.parseLong(orderBean.getUserId()));

            String cardNo = ub.getIdNo(); // 身份证号码
            String realName = ub.getRealName(); // 手机号

            // 金额扩大100倍
            amount = BigDecimalUtil.expand(new BigDecimal(amount)).toString();

            Map<String, String> param = new HashMap<>();

            // 对订单号增加6位随机数处理,先锋支付队同一个订单号只能执行一次交易
            traceNo = traceNo + "_" + String.valueOf(getRandNum(1, 999999));
            // 还款类别处理 1 预支付 2 正常还款
            String keyOrderId = "";
            try {
                if (flag.equals("1")) {
                    keyOrderId = "preAmount_" + traceNo;
                    assetApiService.prePayment(keyOrderId, jsonObject);
                    param.put("noticeUrl", paramConfig.getIntoXFQuickPreNoticeUrl());// 回调通知后台----------------------
                } else if (flag.equals("2")) {
                    keyOrderId = "normalAmount_" + traceNo;
                    assetApiService.normalPayment(keyOrderId, jsonObject);
                    param.put("noticeUrl", paramConfig.getIntoXFQuickNormalNoticeUrl());// 回调通知后台----------------------
                }
            } catch (IqbException e1) {
                result.put("result_code", e1.getRetInfo().getRetCode());
                result.put("result_msg", e1.getRetInfo().getRetUserInfo());
                return result;
            }
            param.put("amount", amount.substring(0, amount.indexOf(".")));
            param.put("traceNo", traceNo);
            param.put("merchantId", payConfBean.getMerchantId());
            param.put("regId", regId);// 预留手机号

            param.put("bankCardNo", bankCardNo);// 银行卡号
            param.put("cardNo", cardNo);
            param.put("bankCode", "");
            param.put("merchantKey", payConfBean.getKey());
            param.put("gateway", payConfBean.getGateWay());
            param.put("flag", flag); // 还款类别
            param.put("repayNo", jsonObject.getString("repayNo")); // 支付期数
            param.put("realName", realName);
            param.put("callbackUrl", callbackUrl); // 支付成功第三方回调地址

            xfResult = PayUtils.prePay(param);
            String okResult = JSONObject.toJSONString(xfResult, true);
            logger.info("--短信验证码发送成功返回信息 {}", okResult);
            if (xfResult.get("result_code").equals("00")) {
                String key = "preResult_" + traceNo;
                redisPlatformDao.setKeyAndValue(key, okResult);

                result.put("traceNo", xfResult.get("outOrderId"));
                result.put("paymentId", xfResult.get("paymentId"));
                result.put("result_code", ApiReturnInfo.API_SUCCESS_00000000.getRetCode());
                result.put("result_msg", xfResult.get("result_msg"));
            } else {
                result.put("result_msg", xfResult.get("result_msg"));
            }
        } catch (Exception e) {
            result.put("result_msg", e.getMessage());
            logger.error("sendPaySms occured error:{}", e.getMessage());
        }
        logger.info("调用先锋接口进行短信验证码发送--结束:{} ", result);
        return result;
    }

    /**
     * 
     * Description:先锋接口-代扣确认支付
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/iqbPay", method = RequestMethod.POST)
    public Object PCpayConfirm(@RequestBody JSONObject obj, HttpServletRequest request) {
        logger.info("调用先锋接口进行代扣确认支付--开始:{}", obj);
        Map<String, Object> result = new HashMap<>();
        result.put("result_code", "100000");// 默认预支付发生异常

        String decrypData;
        try {
            decrypData = receiveData(request, obj, result);
        } catch (IqbException e1) {
            result.put("result_code", e1.getRetInfo().getRetCode());
            result.put("result_msg", e1.getRetInfo().getRetUserInfo());
            return result;
        }
        JSONObject jsonObject;
        try {
            jsonObject = validateParams(decrypData, "PCpayConfirm");
            logger.info("确认支付发送参数:{}", jsonObject);
        } catch (IqbException e) {
            result.put("result_code", e.getRetInfo().getRetCode());
            result.put("result_msg", e.getRetInfo().getRetUserInfo());
            return result;
        }

        PayConfBean payChannel = null;
        try {
            payChannel = payService.getPayChannelBy(null, null);
            String traceNo = jsonObject.getString("traceNo"); // 订单号
            String smsCode = jsonObject.getString("smsCode"); // 手机验证码
            String paymentId = jsonObject.getString("paymentId"); // 支付ID

            Map<String, String> param = new HashMap<>();
            // 预支付返回信息
            String key = "preResult_" + traceNo;
            Map<String, String> resultParam = null;

            String okResult = redisPlatformDao.getValueByKey(key);
            resultParam = (Map<String, String>) JSONObject.parse(okResult);

            param.put("merchantId", payChannel.getMerchantId());
            param.put("memberUserId", resultParam.get("memberUserId"));// 预付款返回值
            param.put("smsCode", smsCode);
            param.put("mobileNo", resultParam.get("regId"));// 绑定手机号非注册号
            param.put("outOrderId", resultParam.get("outOrderId"));

            // 添加判断金额不一致
            Map<String, Object> valResult = assetApiService.validateAmount(traceNo, paymentId);
            if (!valResult.get("result_code").equals(ApiReturnInfo.API_SUCCESS_00000000.getRetCode())) {
                return valResult;
            }
            param.put("amount", resultParam.get("amount"));
            param.put("realName", resultParam.get("realName"));
            param.put("cardNo", resultParam.get("cardNo"));
            param.put("bankCardNo", resultParam.get("bankCardNo"));
            param.put("bankName", resultParam.get("bankName"));// 预付款返回值
            param.put("bankCode", resultParam.get("bankCode"));// 预付款返回值
            param.put("paymentId", resultParam.get("paymentId"));// 预付款返回值
            param.put("tradeNo", resultParam.get("tradeNo"));// 预付款返回值
            param.put("payChannel", resultParam.get("payChannel"));

            param.put("merchantKey", payChannel.getKey());
            param.put("gateway", payChannel.getGateWay());

            result = PayUtils.confirmPay(param);
        } catch (Exception e) {
            result.put("result_msg", e.getMessage());
            logger.error("PCpayConfirm occured error:{}", e.getMessage());
        }
        logger.info("调用先锋接口进行代扣确认支付--结束:{} ", result);
        return result;
    }

    /**
     * 先锋还款成功异步回调
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "/intoNotice{method}Result", method = RequestMethod.POST)
    public Object intoNoticeResult(@PathVariable("method") String method, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        logger.debug("---支付成功接收先锋支付的异步通知------------------------------------------------------");// 成功
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("errno", "1");
        returnMap.put("errorMsg", "验签不过");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        logger.debug("***************页面通知-接收到先锋支付返回报文：");
        // 打印接收到的数据
        Map<String, String> signParameters = new HashMap<>();
        Map parameters = request.getParameterMap();
        Iterator paiter = parameters.keySet().iterator();
        String signValue = "";
        while (paiter.hasNext()) {
            String key = paiter.next().toString();
            String[] values = (String[]) parameters.get(key);
            signParameters.put(key, values[0]);
            if ("sign".equals(key)) {
                signValue = values[0];
            }
        }
        logger.debug("回调内容为" + signParameters);
        // 开始验签
        boolean verifyResult = true;
        try {
            signParameters.remove("sign");
            PayConfBean payConfBean = payService.getPayChannelBy(null, null);
            verifyResult = UcfForOnline.verify(payConfBean.getKey(), "sign", signValue, signParameters, "RSA");
        } catch (GeneralSecurityException e) {
            logger.error("先锋支付异步回调验签发生GeneralSecurityException异常", e);
        } catch (CoderException e) {
            logger.error("先锋支付异步回调验签发生CoderException异常", e);
        }

        /**
         * 从支付结果中取出订单预留信息outOrderId，此字段是在提交支付信息时存入的，存的值是用户的regId,即注册号.
         */
        String info_order = signParameters.get("outOrderId");// 订单号
        String billInfoJson = redisPlatformDao.getValueByKey("preResult_" + info_order);
        /**
         * 从redis中取出账单信息，用于用户还款时获取账单总金额
         */
        logger.debug("redis存储的账单信息为:" + billInfoJson);
        if (billInfoJson == null) {
            logger.debug("回调支付超时,获取key为" + "preResult_" + info_order + "的redis丢失");
            return returnMap;
        }
        Map<String, String> preResultParam = (Map<String, String>) JSONObject.parse(billInfoJson);
        // 支付成功后调用第三方接口,通知支付成功
        String callbackUrl = preResultParam.get("callbackUrl");

        if (verifyResult
                && ("00".equalsIgnoreCase(signParameters.get("payStatus"))
                        || "S".equalsIgnoreCase(signParameters.get("orderStatus"))
                        || "00".equalsIgnoreCase(signParameters.get("orderStatus"))
                        || "S".equalsIgnoreCase(signParameters.get("status"))
                        || "0".equalsIgnoreCase(signParameters.get("payStatus")) || "00"
                            .equalsIgnoreCase(signParameters.get("status")))) {

            String orderId = signParameters.get("outOrderId");
            orderId = orderId.substring(0, orderId.lastIndexOf("_"));

            // 开始处理核心逻辑
            if ("PrePayment".equalsIgnoreCase(method)) {// 预付款
                logger.debug("----标准接口--支付预付款--记录支付流水--");
                try {

                    // 回调通知
                    logger.debug("支付成功调用第三方接口 地址{}", callbackUrl);
                    callBackPay(orderId, callbackUrl);

                    PaymentLogBean payLog = xfPayService.getPayLogByTradeNo(signParameters.get("tradeNo"));

                    if (payLog == null) {
                        // 插入代偿日志
                        insertQuickPayLog(1, signParameters, "api代偿预支付");
                        // 回写订单预支付状态为1,风控状态修改为0
                        updateOrderStatus(orderId);
                        returnMap.put("errno", "0");
                        returnMap.put("errorMsg", "交易成功");
                        return returnMap;
                    } else {
                        logger.debug("先锋已回调多次");
                        returnMap.put("errno", "0");
                        returnMap.put("errorMsg", "交易成功");
                        return returnMap;
                    }
                } catch (Exception e) {
                    logger.error("支付预付款异常", e);
                    returnMap.put("errno", "1");
                    returnMap.put("errorMsg", "交易不成功");
                }
            } else if ("NormalPayment".equalsIgnoreCase(method)) {// 正常还款
                logger.debug("----标准接口--支付分期付款--记录支付流水--");
                // 进行还款操作 防止重复还款
                PaymentLogBean payLog = xfPayService.getPayLogByTradeNo(signParameters.get("tradeNo"));

                if (payLog == null) {
                    boolean payFlag = goToRepay(orderId, signParameters, preResultParam);
                    if (!payFlag) {
                        returnMap.put("errno", "1");
                        returnMap.put("errorMsg", "交易不成功");
                        return returnMap;
                    }
                    // 记录代偿支付日志
                    insertQuickPayLog(2, signParameters, "api正常支付");
                    returnMap.put("errno", "0");
                    returnMap.put("errorMsg", "交易成功");
                } else {
                    logger.debug("先锋已回调多次");
                    returnMap.put("errno", "0");
                    returnMap.put("errorMsg", "交易成功");
                    return returnMap;
                }
            }
        }
        return returnMap;
    }

    /**
     * 支付订单
     * 
     * @param orderId
     * @return
     */
    @SuppressWarnings("unused")
    private boolean goToRepay(String orderId, Map<String, String> signParameters, Map<String, String> preResultParam) {
        logger.info("----标准接口--正常分期-平账---开始--{}", signParameters);
        // 1.查询订单
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        OrderBean orderBean = combinationQueryService.selectOne(params);
        // 2.查询账单
        UserBean ub = xfPayService.getUserInfo(Long.parseLong(orderBean.getUserId()));

        String repayNos = preResultParam.get("repayNo");
        Map<String, Object> billMap = billInfoService.getBillInfo(orderId,
                ub.getRegId(), repayNos);
        billMap.put("sumAmt", preResultParam.get("amount"));

        if (billMap == null) {
            logger.debug("订单{}异步还款未查询到账单", orderId);
            return false;
        }
        // 3.平账
        signParameters.put("repayType", "22");
        Map<String, Object> retMap2 = assetApiService.repay(billMap, signParameters);

        if (retMap2 != null && FinanceConstant.SUCCESS.equals(retMap2.get("retCode"))) {
            return true;
        }
        logger.info("----标准接口--正常分期-平账---结束--{}", retMap2);
        return false;
    }

    /**
     * 快捷支付插入日志
     * 
     * @param signParameters
     */
    private void insertQuickPayLog(int flag, Map<String, String> params, String remark) {
        logger.info("----标准接口--支付-流水记录--开始--{}", params);
        if (flag == 1 || flag == 2) {// 支付
            String orderId = params.get("outOrderId");
            orderId = orderId.substring(0, orderId.lastIndexOf("_"));
            params.put("merchantNo", params.get("merchantId"));
            params.put("remark", remark);
            params.put("orderId", orderId);
            params.put("amount", params.get("amount"));
            if (flag == 1) {
                params.put("flag", "12");
            } else {
                params.put("flag", "22");
            }
            params.put("orderStatus", params.get("status"));
            // 查询订单信息
            Map<String, Object> orderParam = new HashMap<>();
            orderParam.put("orderId", orderId);
            OrderBean ob = xfPayService.getOrderInfo(orderParam);
            // 查询用户信息
            UserBean ub = xfPayService.getUserInfo(Long.parseLong(ob.getUserId()));
            params.put("regId", ub.getRegId());
            params.put("merchantNo", ob.getMerchantNo());
        } else if (flag == 3 || flag == 4) {// 快捷支付
            String outOrderId = params.get("outOrderId");
            String orderId = getOrderId(outOrderId);
            params.put("remark", remark);
            params.put("orderId", orderId);
            // 查询订单信息
            Map<String, Object> orderParam = new HashMap<>();
            orderParam.put("orderId", orderId);
            OrderBean ob = xfPayService.getOrderInfo(orderParam);
            // 查询用户信息
            UserBean ub = xfPayService.getUserInfo(Long.parseLong(ob.getUserId()));
            params.put("regId", ub.getRegId());
            params.put("merchantNo", ob.getMerchantNo());
            if (flag == 3) {
                // 11.用户支付预付款 12.商户代偿预付款 13.线下平账预付款 21.用户支付分期还款 22.商户代偿分期还款
                // 23.线下平账分期付款
                params.put("flag", "12");
            } else {
                // 11.用户支付预付款 12.商户代偿预付款 13.线下平账预付款 21.用户支付分期还款 22.商户代偿分期还款
                // 23.线下平账分期付款
                params.put("flag", "22");
            }
        }
        xfPayService.insertPaymentLog(params);
        logger.info("----标准接口--支付-流水记录--结束--");
    }

    /**
     * 回写订单预支付状态为1,风控状态修改为0
     */
    private void updateOrderStatus(String orderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);// 订单号
        params.put("riskStatus", 0);// 通过
        params.put("preAmtStatus", 1);// 已支付
        xfPayService.updatePreStatusByNO(params);
    }

    /**
     * 获取订单号
     * 
     * @param outOrderId
     * @return
     */
    private String getOrderId(String outOrderId) {
        String orderId = null;
        if (outOrderId.startsWith("2") && outOrderId.indexOf("-") > 7) {
            orderId = outOrderId.substring(0, 15);
        } else {
            orderId = outOrderId.substring(0, outOrderId.indexOf("-"));
        }
        return orderId;
    }

    /**
     * 
     * Description:生成随机数
     * 
     * @param objs
     * @param request
     * @return
     */
    public static int getRandNum(int min, int max) {
        int randNum = min + (int) (Math.random() * ((max - min) + 1));
        return randNum;
    }

    /**
     * 
     * Description:支付成功调用第三方接口进行通知
     * 
     * @param objs
     * @param request
     * @return
     */
    private void callBackPay(String orderId, String callbackUrl) {
        ApiRequestMessage data = new ApiRequestMessage();
        data.setOrderId(orderId);
        data.setStatus(2);

        scheduleTaskAnalysisAllot.send(data.toString(), callbackUrl, ScheduleTaskAnalysisAllot.MODULE_A,
                orderId, true);
    }

    /**
     * 
     * Description: FINANCE-1374 惠掏车以租代购数据查询FINANCE-1375 客户信息
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年6月28日 下午2:56:36
     */
    @ResponseBody
    @RequestMapping(value = "/getInfoByRegId", method = RequestMethod.POST)
    public Object getInfoByRegId(@RequestBody JSONObject obj, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String decrypData;
        try {
            decrypData = receiveData(request, obj, result);
        } catch (IqbException e) {
            result.put("result_code", e.getRetInfo().getRetCode());
            result.put("result_msg", e.getRetInfo().getRetUserInfo());
            return result;
        } catch (Exception e) {
            logger.error("解密异常", e);
            result.put("result_code", "10000001");
            result.put("result_msg", "解密异常");
            return result;
        }
        try {
            JSONObject jo = JSONObject.parseObject(decrypData);
            String regId = jo.getString("regId");
            if (StringUtil.isEmpty(regId)) {
                result.put("result_code", ApiReturnInfo.API_BIZTYPE_NULL_40000004.getRetCode());
                result.put("result_msg", ApiReturnInfo.API_BIZTYPE_NULL_40000004.getRetUserInfo());
            }
            result.put("result_code", ApiReturnInfo.API_GET_SUCCESS_80000000.getRetCode());
            result.put("result_msg", assetApiService.getInfoByRegId(regId));
            return result;
        } catch (Exception e) {
            result.put("result_msg", e.getMessage());
            logger.error("sendPaySms occured error:{}", e.getMessage());
            return result;
        }
    }

    /**
     * 
     * Description:FINANCE-1554 通过订单查询影像车车辆信息
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年7月19日 下午1:53:55
     */
    @ResponseBody
    @RequestMapping(value = "/getInfoByOid", method = RequestMethod.POST)
    public Object getInfoByOid(@RequestBody JSONObject obj, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String decrypData;
        try {
            decrypData = receiveData(request, obj, result);
        } catch (IqbException e) {
            result.put("result_code", e.getRetInfo().getRetCode());
            result.put("result_msg", e.getRetInfo().getRetUserInfo());
            return result;
        } catch (Exception e) {
            logger.error("解密异常", e);
            result.put("result_code", "10000001");
            result.put("result_msg", "解密异常");
            return result;
        }
        try {
            JSONObject jo = JSONObject.parseObject(decrypData);
            String orderId = jo.getString("orderId");
            // String merchantNo = jo.getString(MERCHANTNO);
            if (StringUtil.isEmpty(orderId)) {
                result.put("result_code", ApiReturnInfo.API_BIZTYPE_NULL_40000005.getRetCode());
                result.put("result_msg", ApiReturnInfo.API_BIZTYPE_NULL_40000005.getRetUserInfo());
            }

            PicInformationPojo pip = assetApiService.getPIPByOid(orderId);
            String url = request.getRequestURL().toString();
            String uri = request.getRequestURI();
            String baseUrl = url.replace(uri, "") + "/uploadData";
            pip.setBaseUrl(baseUrl);
            result.put("result_code", ApiReturnInfo.API_GET_SUCCESS_80000000.getRetCode());
            result.put("result_msg", pip);
            return result;
        } catch (Exception e) {
            result.put("result_msg", e.getMessage());
            logger.error("sendPaySms occured error:{}", e.getMessage());
            return result;
        }
    }
}
