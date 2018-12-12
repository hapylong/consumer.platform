/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @date 2016年9月19日 上午9:46:21
 * @version V1.0
 */
package com.iqb.consumer.manage.front.pay;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.constant.FinanceConstant;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.wf.SettleApplyBeanBiz;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.consumer.service.layer.pay.PayService;
import com.iqb.consumer.service.layer.wfservice.CombinationQueryService;
import com.iqb.consumer.service.layer.xfpay.XFPayService;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.SysUserSession;
import com.ucf.platform.framework.core.util.AESCoder;
import com.ucf.sdk.CoderException;
import com.ucf.sdk.UcfForOnline;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Controller
@RequestMapping("/nr")
public class XFNoticeReturnController {

    protected static final Logger logger = LoggerFactory.getLogger(XFNoticeReturnController.class);

    @Resource
    private ParamConfig paramConfig;
    @Resource
    private XFPayService xfPayService;
    @Resource
    private BillInfoService billInfoService;
    @Resource
    private CombinationQueryService combinationQueryService;
    @Resource
    private SysUserSession sysUserSession;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Resource
    private SettleApplyBeanBiz settleApplyBeanBiz;
    @Autowired
    private OrderBiz orderBiz;
    @Resource
    private PayService payService;
    @Resource
    private EncryptUtils encryptUtils;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private DictService dictService;

    @ResponseBody
    @RequestMapping(value = "/getPayReturn", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getPayReturn(HttpServletRequest request) {
        int orgId = sysUserSession.getOrgId();
        String retJson = redisPlatformDao.getValueByKey("payByQuick" + orgId);
        redisPlatformDao.removeValueByKey("payByQuick" + orgId);
        return retJson;
    }

    /**
     * 同步回调先锋支付方式
     * 
     * @param method
     * @param request
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/return{method}Page", method = {RequestMethod.POST, RequestMethod.GET})
    public void returnPage(@PathVariable("method") String method, HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("同步回调先锋支付方式{}", method);
        Map<String, String> signParameters = new HashMap<String, String>();
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            Map<String, String[]> parameters = request.getParameterMap();
            Iterator<String> paiter = parameters.keySet().iterator();
            String signValue = "";
            while (paiter.hasNext()) {
                String key = paiter.next().toString();
                String[] values = (String[]) parameters.get(key);
                signParameters.put(key, values[0]);
                if ("sign".equals(key)) {
                    signValue = values[0];
                }
            }
            logger.debug("同步回调内容为:{}", signParameters);
        } catch (Exception e) {

        }
        try {
            if ("00".equalsIgnoreCase(signParameters.get("payStatus"))
                    || "S".equalsIgnoreCase(signParameters.get("status"))
                    || "0".equalsIgnoreCase(signParameters.get("payStatus"))
                    || "00".equalsIgnoreCase(signParameters.get("status"))) {
                response.sendRedirect(paramConfig.getSucc2Page());
            } else {
                response.sendRedirect(paramConfig.getFailed2Page());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 异步回调先锋支付方式
     * 
     * @param method
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/notice{method}Result/{payId}", method = RequestMethod.POST)
    public Object noticeResult(@PathVariable("method") String method, @PathVariable("payId") String payId,
            HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("errno", "1");
        result.put("errorMsg", "交易不成功");
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            Map<String, String> signParameters = new HashMap<String, String>();
            Map<String, String[]> parameters = request.getParameterMap();
            Iterator<String> paiter = parameters.keySet().iterator();
            String signValue = "";
            while (paiter.hasNext()) {
                String key = paiter.next().toString();
                String[] values = (String[]) parameters.get(key);
                signParameters.put(key, values[0]);
            }
            logger.info("异步回调内容为:{}", signParameters);
            logger.info("先锋支付异步回调,回调内容为：{},payId为:{}", signParameters, payId);
            // 根据支付主体ID解密数据
            PayConfBean confBean = payService.getPayChannelBy(payId, null);
            String data = signParameters.get("data");
            logger.info("加密回到数据为data:{}", data);
            String decrypt = AESCoder.decrypt(data, confBean.getKey());
            logger.info("解密回调数据为:{}", decrypt);
            signParameters = (Map<String, String>) JSONObject.parse(decrypt);
            signValue = signParameters.get("sign");
            // 开始验签
            boolean verifyResult = true;
            try {
                signParameters.remove("sign");
                verifyResult = UcfForOnline.verify(confBean.getKey(), "sign", signValue, signParameters, "RSA");
            } catch (GeneralSecurityException e) {
                logger.error("先锋支付异步回调验签发生GeneralSecurityException异常", e);
            } catch (CoderException e) {
                logger.error("先锋支付异步回调验签发生CoderException异常", e);
            }
            if (verifyResult
                    && ("00".equalsIgnoreCase(signParameters.get("payStatus"))
                            || "S".equalsIgnoreCase(signParameters.get("orderStatus"))
                            || "00".equalsIgnoreCase(signParameters.get("orderStatus"))
                            || "S".equalsIgnoreCase(signParameters.get("status"))
                            || "0".equalsIgnoreCase(signParameters.get("payStatus")) || "00"
                                .equalsIgnoreCase(signParameters.get("status")))) {
                // 开始处理核心逻辑
                if ("PreCashier".equalsIgnoreCase(method)) {// 收银台支付
                    logger.info("----Deal网银支付预付款----");
                    try {
                        PaymentLogBean payLog = xfPayService.getPayLogByTradeNo(signParameters.get("tradeNo"));
                        if (payLog == null) {
                            // 插入代偿日志
                            insertQuickPayLog(1, signParameters, "网银支付代偿预支付");
                            String orderId = signParameters.get("orderId");
                            // 回写订单预支付状态为1,风控状态修改为0
                            xfPayService.prePay(orderId, new BigDecimal(signParameters.get("amount")));
                            result.put("errno", "0");
                            result.put("errorMsg", "交易成功");
                            return result;
                        }

                    } catch (Exception e) {
                        logger.error("网银支付预付款异常", e);
                        result.put("errno", "1");
                        result.put("errorMsg", "交易不成功");
                    }
                } else if ("Cashier".equalsIgnoreCase(method)) {// 收银台支付
                    logger.info("----Deal网银支付分期付款----");
                    // 进行还款操作 防止重复还款
                    PaymentLogBean payLog = xfPayService.getPayLogByTradeNo(signParameters.get("tradeNo"));
                    /**
                     * FINANCE-3303 划扣部分成功允许继续还款
                     */
                    if (payLog == null) {
                        // 记录代偿支付日志
                        insertQuickPayLog(2, signParameters, "网银支付代偿分期");
                        result.put("errno", "0");
                        result.put("errorMsg", "交易成功");
                    }

                    String outOrderId = signParameters.get("merchantNo");
                    String orderItem = getOrderItem(outOrderId);
                    String orderId = getOrderId(outOrderId);
                    // 查询历史总金额+本次金额是否与月供一致,一致则平账,否则,直接返回成功
                    if (validateIsNeedRefund(orderId, orderItem)) {
                        logger.info("平账期数为:{}", orderItem);
                        boolean payFlag = goToRepay(orderId, signParameters, orderItem);
                        if (!payFlag) {
                            result.put("errno", "1");
                            result.put("errorMsg", "交易不成功");
                            return result;
                        }
                    } else {
                        result.put("errno", "0");
                        result.put("errorMsg", "交易成功");
                        return result;
                    }

                } else if ("PreQuick".equalsIgnoreCase(method)) {
                    logger.info("----Deal快捷支付预付款----");
                    try {
                        // 插入代偿日志
                        insertQuickPayLog(3, signParameters, "快捷支付代偿预支付");
                        String outOrderId = signParameters.get("outOrderId");
                        String orderId = getOrderId(outOrderId);
                        // 查询订单信息,如果支付金额和总预支付相等，则支付完成，否则继续拆分支付。
                        xfPayService.prePay(orderId, new BigDecimal(signParameters.get("amount")));
                        result.put("errno", "0");
                        result.put("errorMsg", "交易成功");
                        return result;
                    } catch (Exception e) {
                        logger.error("快捷预支付异常", e);
                        result.put("errno", "1");
                        result.put("errorMsg", "交易不成功");
                    }
                } else if ("Quick".equalsIgnoreCase(method)) {
                    logger.info("----Deal快捷支付分期付款----");

                    // 进行还款操作 防止重复还款
                    PaymentLogBean payLog = xfPayService.getPayLogByTradeNo(signParameters.get("tradeNo"));
                    String outOrderId = signParameters.get("outOrderId");
                    String orderItem = getOrderItem(outOrderId);
                    String orderId = getOrderId(outOrderId);
                    if (payLog == null) {
                        // 记录代偿支付日志
                        insertQuickPayLog(4, signParameters, "快捷支付代偿分期");
                        // 回写卡号为常用卡
                        Map<String, Object> params = new HashMap<String, Object>();
                        outOrderId = signParameters.get("outOrderId");
                        params.put("id",
                                outOrderId.substring(outOrderId.lastIndexOf("-") - 1, outOrderId.lastIndexOf("-")));
                        params.put("status", 1);
                        xfPayService.unBindCardStatus(params);
                    }
                    // 查询历史总金额+本次金额是否与月供一致,一致则平账,否则,直接返回成功
                    if (validateIsNeedRefund(orderId, orderItem)) {
                        logger.info("平账期数为:{}", orderItem);
                        boolean payFlag = goToRepay(orderId, signParameters, orderItem);
                        if (!payFlag) {
                            result.put("errno", "1");
                            result.put("errorMsg", "交易不成功");
                            return result;
                        }
                    } else {
                        result.put("errno", "0");
                        result.put("errorMsg", "交易成功");
                        return result;
                    }
                } else if ("SettlementQuick".equals(method)) {
                    // 快捷退租
                    logger.info("----SettlementQuick付款----");
                    try {
                        String outOrderId = signParameters.get("outOrderId");
                        String id = getId(outOrderId);
                        SettleApplyBean settleApplyBean = settleApplyBeanBiz.getNeedPayAmt(id);
                        // 插入代偿日志
                        signParameters.put("orderId", settleApplyBean.getOrderId());
                        insertQuickPayLog(6, signParameters, "快捷支付退租");
                        // 查询订单信息,如果支付金额和总预支付相等，则支付完成，否则继续拆分支付。
                        xfPayService.settlePay(settleApplyBean, id, new BigDecimal(signParameters.get("amount")),
                                signParameters, "");
                        result.put("errno", "0");
                        result.put("errorMsg", "交易成功");
                        return result;
                    } catch (Exception e) {
                        logger.error("快捷预支付异常", e);
                        result.put("errno", "1");
                        result.put("errorMsg", "交易不成功");
                    }
                } else if ("SettlementCashier".equals(method)) {
                    // 收银台退租
                    logger.info("----SettlementCashier付款----");
                    try {
                        PaymentLogBean payLog = xfPayService.getPayLogByTradeNo(signParameters.get("tradeNo"));
                        if (payLog == null) {
                            String outOrderId = signParameters.get("merchantNo");
                            String id = getId(outOrderId);
                            SettleApplyBean settleApplyBean = settleApplyBeanBiz.getNeedPayAmt(id);
                            // 插入代偿日志
                            signParameters.put("orderId", settleApplyBean.getOrderId());
                            insertQuickPayLog(5, signParameters, "网银支付退租");
                            // 回写订单预支付状态为1,风控状态修改为0
                            xfPayService.settlePay(settleApplyBean, id, new BigDecimal(signParameters.get("amount")),
                                    signParameters, "");
                            result.put("errno", "0");
                            result.put("errorMsg", "交易成功");
                            return result;
                        }
                    } catch (Exception e) {
                        logger.error("网银支付预付款异常", e);
                        result.put("errno", "1");
                        result.put("errorMsg", "交易不成功");
                    }
                }
                // 校验账单是否已全部还款并且同步修改订单状态
                String outOrderId = signParameters.get("outOrderId");
                if (!StringUtil.isNull(outOrderId)) {
                    logger.debug("---校验账单是否已全部还款并且同步修改订单状态---{}---", outOrderId);
                    String orderId = getOrderId(outOrderId);
                    String orderItem = getOrderItem(outOrderId);
                    validateAndUpdateOrderInfo(orderId, Integer.parseInt(orderItem));
                }

            }
        } catch (Exception e) {
            logger.error("先锋支付异步回调发生异常", e);
        }
        return result;
    }

    private PayConfBean getPayChannel(String method, Map<String, String> signParameters) {
        String orderId = null;
        if ("PreCashier".equalsIgnoreCase(method)) {
            orderId = signParameters.get("orderId");
        } else if ("Cashier".equalsIgnoreCase(method)) {
            String outOrderId = signParameters.get("merchantNo");
            orderId = getOrderId(outOrderId);
        } else if ("PreQuick".equalsIgnoreCase(method)) {
            String outOrderId = signParameters.get("outOrderId");
            orderId = getOrderId(outOrderId);
        } else if ("Quick".equalsIgnoreCase(method)) {
            String outOrderId = signParameters.get("outOrderId");
            orderId = getOrderId(outOrderId);
        } else if ("SettlementQuick".equals(method)) {
            String outOrderId = signParameters.get("outOrderId");
            String id = getId(outOrderId);
            SettleApplyBean settleApplyBean = settleApplyBeanBiz.getNeedPayAmt(id);
            orderId = settleApplyBean.getOrderId();
        } else if ("SettlementCashier".equals(method)) {
            String outOrderId = signParameters.get("merchantNo");
            String id = getId(outOrderId);
            SettleApplyBean settleApplyBean = settleApplyBeanBiz.getNeedPayAmt(id);
            orderId = settleApplyBean.getOrderId();
        }
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        PayConfBean payConfBean =
                payService.getPayChannelBy(orderBean == null ? null : orderBean.getPayOwnerId(), orderBean == null
                        ? null
                        : orderBean.getMerchantNo());
        return payConfBean;
    }

    /**
     * 通过订单号获取还款期数
     * 
     * @param outOrderId
     * @return
     */
    public String getOrderItem(String outOrderId) {
        String orderItem = null;
        if (outOrderId.startsWith("2") && outOrderId.indexOf("-") > 7) {
            orderItem = outOrderId.substring(16, outOrderId.lastIndexOf("-"));
        } else {
            orderItem = outOrderId.substring(outOrderId.indexOf("-") + 1, outOrderId.lastIndexOf("-"));
        }
        return orderItem;
    }

    /**
     * 支付订单
     * 
     * @param orderId
     * @return
     */
    private boolean goToRepay(String orderId, Map<String, String> signParameters, String orderItem) {
        // 1.查询订单
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);
        OrderBean orderBean = combinationQueryService.selectOne(params);
        // 2.查询账单
        UserBean ub = xfPayService.getUserInfo(Long.parseLong(orderBean.getUserId()));
        // 根据期数查询对应的未还款账单
        Map<String, Object> billMap = billInfoService.getAssignBill(orderId, orderItem, ub.getRegId());
        if (billMap == null) {
            logger.debug("订单{}异步还款未查询到账单", orderId);
            return false;
        }
        // 3.平账
        signParameters.put("repayType", "22");
        Map<String, Object> retMap2 = billInfoService.repay(billMap, signParameters);
        if (retMap2 != null && FinanceConstant.SUCCESS.equals(retMap2.get("retCode"))) {
            return true;
        }
        return false;
    }

    /**
     * 
     * Description: 根据返回信息获取需要还款总和
     * 
     * @param
     * @return Float
     * @throws
     * @Author wangxinbang Create Date: 2015年12月30日 下午6:49:54
     */
    @SuppressWarnings("rawtypes")
    protected Float getAmtSum(List<Map> allList) {
        Float amtSum = 0.00f;
        int i = allList.size() - 1;
        amtSum = string2Float((String) allList.get(i).get("currcStmtAmt"))
                + string2Float((String) allList.get(i).get("currLatefeeIn"))
                - string2Float((String) allList.get(i).get("currRepayAmtSumIn"));
        return amtSum;
    }

    /**
     * 
     * Description: 字符串转浮点
     * 
     * @param
     * @return Float
     * @throws
     * @Author wangxinbang Create Date: 2015年12月30日 下午7:09:56
     */
    protected Float string2Float(String indata) {
        Float v;
        if (Float.parseFloat(indata) == 0) {
            v = 0.00f;
        } else {
            v = Float.parseFloat(indata) / 100.00f;
        }
        return v;
    }

    /**
     * 回写订单预支付状态为1,风控状态修改为0
     */
    private void updateOrderStatus(String orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);// 订单号
        params.put("riskStatus", 0);// 通过
        params.put("preAmtStatus", 1);// 已支付
        xfPayService.updatePreStatusByNO(params);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        String key =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrVP33TnZXgK6qW7U+FPqUUO4Bi4VcVHZrAuXNdtaGFMxRL3XY1LGd/98bl5QQbi1qqSzgrYtilwQOI97dD68asrLLrz7s9xCX5+6A3tk8aR0dX/c+1WyWzCKEflQullcdrqrACagBuWt+8G+mV4igrxE3QQ8Pc9rCvuUyipTDzwIDAQAB";
        String data =
                "PYvg07tFA91eUcvYH/ZKzHGb9pi/swsJSqTCBDm8Q1KdAgijaRdykqPgd/3ksazB3xg+cmRbMHidjaNff+6mwTYkChCdWNPsYxDOVmh3r89E/RrjSM/IcL9vNUmGvufceyzVc43QLRFgEKVNogDqFupqd/XmhDMpVBaJ1L29BBXMLPCrr0Y/qyPDk93opqxs3dM93mz55Ppsw+f1xnGZXWwFwsG5DFHwUkVJteHcZUzW/i1EiEaxR7xF4HxUulj7TdHFPR2VdJD363F/4c4isVGvHHttSsXDMtHGRIKzn8u4tOGPTf4xjrjOOlT81fIc+foD+pRAUX6CJBqR9bbzkNM5EWKZBYlJ0yNsq7Rkyr91Gpzfo76Gaz5AtkjuCtzFG+UBLdhzFW9rD71tBPrxaVNAlEnWHijJ5iGbxFqrP0Te4dQNyGBZgxEYAzCOpSiO49/4GUQH8w91gts763/YaGO5qFKuv0fcEVysNfmOQkk16gXADYIFhXvEEHM8D52M2eS8y7AWzIsMyccbia73ng==";
        String decrypt = AESCoder.decrypt(data, key);

        Map<String, String> signParameters = (Map<String, String>) JSONObject.parse(decrypt);
        System.out.println(signParameters);

        String outOrderId = signParameters.get("merchantNo");
        String orderItem = null;
        if (outOrderId.startsWith("2") && outOrderId.indexOf("-") > 7) {
            orderItem = outOrderId.substring(16, outOrderId.lastIndexOf("-"));
        } else {
            orderItem = outOrderId.substring(outOrderId.indexOf("-") + 1, outOrderId.lastIndexOf("-"));
        }
        System.out.println(orderItem);
        String hisRepayAmt = null;
        hisRepayAmt = StringUtil.isNull(hisRepayAmt) ? "0" : hisRepayAmt;
        BigDecimal sumAmt = new BigDecimal("2.2");
        BigDecimal curRealRepayamt = new BigDecimal("2.8");
        BigDecimal subAmt = BigDecimalUtil.sub(sumAmt, new BigDecimal(hisRepayAmt).add(curRealRepayamt));
        System.out.println(subAmt);
    }

    /**
     * 快捷支付插入日志
     * 
     * @param signParameters
     */
    private void insertQuickPayLog(int flag, Map<String, String> params, String remark) {
        if (flag == 1 || flag == 2) {// 网银支付
            String outOrderId = params.get("merchantNo");
            String tradeTime = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse((String) params.get("tradeTime"));
                tradeTime = sdf2.format(date);
            } catch (ParseException e) {
                logger.error("格式化时间异常", e);
            }
            String orderId = getOrderId(outOrderId);
            String orderItem = getOrderItem(outOrderId);
            params.put("tranTime", tradeTime);
            params.put("outOrderId", params.get("merchantNo"));
            params.put("merchantNo", params.get("merchantId"));
            params.put("remark", remark);
            params.put("orderId", orderId);
            if (!StringUtil.isNull(orderItem) && !orderItem.equalsIgnoreCase("null")) {
                params.put("repayNo", orderItem);
            }
            if (flag == 1) {
                params.put("flag", "12");
            } else {
                params.put("flag", "22");
            }
            params.put("orderStatus", params.get("status"));
            // 查询订单信息
            Map<String, Object> orderParam = new HashMap<String, Object>();
            orderParam.put("orderId", orderId);
            OrderBean ob = xfPayService.getOrderInfo(orderParam);
            // 查询用户信息
            UserBean ub = xfPayService.getUserInfo(Long.parseLong(ob.getUserId()));
            params.put("regId", ub.getRegId());
            params.put("merchantNo", ob.getMerchantNo());
        } else if (flag == 5) {
            // 网银支付退租
            // 查询订单信息
            Map<String, Object> orderParam = new HashMap<String, Object>();
            orderParam.put("orderId", params.get("orderId"));
            OrderBean ob = xfPayService.getOrderInfo(orderParam);
            String outOrderId = params.get("merchantNo");
            String tradeTime = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse((String) params.get("tradeTime"));
                tradeTime = sdf2.format(date);
            } catch (ParseException e) {
                logger.error("格式化时间异常", e);
            }
            params.put("tranTime", tradeTime);
            params.put("outOrderId", params.get("merchantNo"));
            params.put("merchantNo", ob.getMerchantNo());
            params.put("remark", remark);
            params.put("orderId", params.get("orderId"));
            params.put("flag", "32");
            params.put("regId", ob.getRegId());
            params.put("orderStatus", params.get("status"));
        } else if (flag == 3 || flag == 4) {// 快捷支付
            String outOrderId = params.get("outOrderId");
            String orderId = getOrderId(outOrderId);
            String orderItem = getOrderItem(outOrderId);
            params.put("remark", remark);
            params.put("orderId", orderId);
            if (!StringUtil.isNull(orderItem) && !orderItem.equalsIgnoreCase("null")) {
                params.put("repayNo", orderItem);
            }
            // 查询订单信息
            Map<String, Object> orderParam = new HashMap<String, Object>();
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
        } else if (flag == 6) {// 快捷支付
            Map<String, Object> orderParam = new HashMap<String, Object>();
            orderParam.put("orderId", params.get("orderId"));
            OrderBean ob = xfPayService.getOrderInfo(orderParam);
            String outOrderId = params.get("outOrderId");
            params.put("merchantNo", ob.getMerchantNo());
            params.put("remark", remark);
            params.put("orderId", params.get("orderId"));
            params.put("regId", ob.getRegId());
            // 11.用户支付预付款 12.商户代偿预付款 13.线下平账预付款 21.用户支付分期还款 22.商户代偿分期还款 31,用户退租支付，32 商户代偿退租
            params.put("flag", "32");
        }
        xfPayService.insertPaymentLog(params);
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

    private String getId(String outOrderId) {
        return outOrderId.substring(0, outOrderId.indexOf("-"));
    }

    /**
     * 
     * Description:账单结清时同步更新订单状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月24日
     */
    private long validateAndUpdateOrderInfo(String orderId, int repayNo) {
        int result = 0;
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        if (orderBean != null) {
            if (Integer.parseInt(orderBean.getOrderItems()) == repayNo) {
                orderBean = new OrderBean();
                orderBean.setOrderId(orderId);
                orderBean.setRiskStatus(10);
                result = orderBiz.updateOrderInfo(orderBean);
            }
        }
        return result;
    }

    /**
     * 校验是否需要平账
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月27日
     */
    private boolean validateIsNeedRefund(String orderId, String repayNo) {
        logger.info("---校验已还款金额是否与应还款总额相等---,参数--orderId:{},订单号:{}", orderId, repayNo);
        BigDecimal sumAmt = BigDecimal.ZERO;// 总还款金额
        BigDecimal curRealRepayamt = BigDecimal.ZERO;// 已还款金额
        // 这个金额需要从账户系统实时获取
        String openId = dictService.getOpenIdByOrderId(orderId);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("orderId", orderId);
        jsonObj.put("repayNo", repayNo);
        jsonObj.put("openId", openId);
        String resStr =
                SimpleHttpUtils.httpPost(consumerConfig.getSelectBillsByRepayNoUrl(), encryptUtils.encrypt(jsonObj));
        logger.info("---校验是否需要平账---{}",resStr);
        if (resStr != null) {
            JSONObject resJson = JSONObject.parseObject(resStr);
            if (resJson.getString("retCode").equals("success")) {
                // 已经放大100倍 缺陷，账务系统出来的数据理论不一样放大100倍的。宣导不到位
                sumAmt = new BigDecimal(resJson.getString("curRepayAmt"));
                curRealRepayamt = new BigDecimal(resJson.getString("curRealRepayamt"));
            } else {
                return false;
            }
        }
        JSONObject json = new JSONObject();
        json.put("orderId", orderId);
        json.put("repayNo", repayNo);
        String hisRepayAmt =
                String.valueOf(xfPayService.selSumAmount(json) != null ? xfPayService.selSumAmount(json) : 0);
        hisRepayAmt = StringUtil.isNull(hisRepayAmt) ? "0" : hisRepayAmt;
        BigDecimal subAmt = BigDecimalUtil.sub(sumAmt, new BigDecimal(hisRepayAmt).add(curRealRepayamt));

        logger.info("---校验已还款金额是否与应还款总额相等---,应还总金额--sumAmt:{},已还金额:{}", sumAmt, hisRepayAmt);
        if (hisRepayAmt != null && Math.abs(subAmt.doubleValue()) <= 0.5) {
            return true;
        }
        return false;
    }
}
