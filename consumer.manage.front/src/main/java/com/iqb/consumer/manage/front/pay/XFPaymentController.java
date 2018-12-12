/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月7日 下午2:45:16
 * @version V1.0
 */
package com.iqb.consumer.manage.front.pay;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucf.platform.framework.core.util.AESCoder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.FinanceConstant;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.CommonUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.HttpUtils;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.bean.xfpay.PayInfoBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.paylog.PaymentLogBiz;
import com.iqb.consumer.data.layer.biz.wf.SettleApplyBeanBiz;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.api.AssetApiService;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.consumer.service.layer.pay.PayService;
import com.iqb.consumer.service.layer.xfpay.XFPayService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.SysUserSession;
import com.ucf.sdk.CoderException;
import com.ucf.sdk.UcfForOnline;
import com.ucf.sdk.util.UnRepeatCodeGenerator;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Controller
@RequestMapping("/pay")
public class XFPaymentController extends BaseService {

    protected static final Logger logger = LoggerFactory
            .getLogger(XFPaymentController.class);

    public final static String FES_RET_SUCC = "000000";
    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";

    @Resource
    private ParamConfig paramConfig;
    @Resource
    private BillInfoService billInfoService;
    @Resource
    private XFPayService xfPayService;
    @Resource
    private SysUserSession sysUserSession;
    @Resource
    private PayService payService;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Resource
    private EncryptUtils encryptUtils;
    @Resource
    private AssetApiService assetApiService;
    @Autowired
    private OrderBiz orderBiz;
    @Autowired
    private PaymentLogBiz paymentLogBiz;
    @Resource
    private AdminService adminServiceImpl;
    @Resource
    private SettleApplyBeanBiz settleApplyBeanBiz;

    public static void main(String[] args) {
        String bankNo = "6217000010061539667";
        char[] bankNos = bankNo.toCharArray();
        String giveBankNo = "";
        for (int i = 0; i < bankNos.length; i++) {
            if ((i >= 0 && i < 4) || (i > bankNos.length - 5)) {
                giveBankNo += bankNos[i];
                continue;
            }
            giveBankNo += "*";
        }
        System.out.println(giveBankNo);
    }

    @ResponseBody
    @RequestMapping(value = "/getBankType", method = {RequestMethod.GET,
            RequestMethod.POST})
    public Object getBankType() {
        return xfPayService.getAllBankType();
    }

    @ResponseBody
    @RequestMapping(value = "/getAlreadyAmt", method = {RequestMethod.POST})
    public Object getAlreadyAmt(@RequestBody JSONObject objs) {
        Integer selSumAmount = paymentLogBiz.selSumAmount(objs);
        LinkedHashMap<String, Object> response = new LinkedHashMap<String, Object>();
        if (selSumAmount == null || selSumAmount == 0) {
            response.put("result", BigDecimalUtil.div100(BigDecimal.ZERO));
        } else {
            response.put("result", BigDecimalUtil.div100(new BigDecimal(selSumAmount)));
        }
        return returnSuccessInfo(response);
    }

    /**
     * 查询登录用户绑卡信息
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBindBankCard", method = {RequestMethod.GET,
            RequestMethod.POST})
    public Object getBindBankCard() {
        int orgId = sysUserSession.getOrgId();// 用户唯一ID
        List<PayInfoBean> payInfoBeanS = xfPayService.getByOrgId(orgId);
        for (PayInfoBean payInfoBean : payInfoBeanS) {
            String bankNo = payInfoBean.getBankNo();
            String realName = payInfoBean.getRealName();
            char[] reals = realName.toCharArray();
            for (int i = 0; i < reals.length; i++) {
                if (i == 0) {
                    realName = realName.toCharArray()[i] + "";
                    continue;
                }
                realName += "*";
            }
            char[] bankNos = bankNo.toCharArray();
            String giveBankNo = "";
            for (int i = 0; i < bankNos.length; i++) {
                if ((i >= 0 && i < 4) || (i > bankNos.length - 5)) {
                    giveBankNo += bankNos[i];
                    continue;
                }
                giveBankNo += "*";
            }

            String mobileNo = payInfoBean.getMobileNo();
            String cardNo = payInfoBean.getCardNo();
            payInfoBean.setCardNo(cardNo.substring(0, 4) + "**********"
                    + cardNo.substring(14));
            payInfoBean.setMobileNo(mobileNo.substring(0, 3) + "****"
                    + mobileNo.substring(7, 11));
            payInfoBean.setRealName(realName);
            payInfoBean.setBankNo(giveBankNo);
        }
        return payInfoBeanS;
    }

    @SuppressWarnings("unused")
    @ResponseBody
    @RequestMapping(value = "/delBindBankCard", method = {RequestMethod.GET,
            RequestMethod.POST})
    public Object delBindBankCard(@RequestBody JSONObject objs) {
        int orgId = sysUserSession.getOrgId();// 用户唯一ID
        Map<String, String> result = new HashMap<String, String>();
        try {
            int bankId = Integer.parseInt(objs.getString("bankId"));
            xfPayService.delBindCardInfo(bankId);
            result.put("retCode", SUCCESS);
        } catch (Exception e) {
            logger.error("删除卡发生异常", e);
            result.put("retCode", FAILED);
        }
        return result;
    }

    @SuppressWarnings("unused")
    @ResponseBody
    @RequestMapping(value = "/updateBindBankCard", method = {RequestMethod.GET,
            RequestMethod.POST})
    public Object updateBindBankCard(@RequestBody JSONObject objs) {
        logger.info("修改卡信息....", objs);
        Map<String, String> result = new HashMap<String, String>();
        try {
            int orgId = sysUserSession.getOrgId();// 用户唯一ID
            String bankId = objs.getString("bankId");
            String bankNo = objs.getString("bankNo").contains("*")
                    ? null
                    : objs.getString("bankNo");
            String realName = objs.getString("realName").contains("*")
                    ? null
                    : objs.getString("realName");
            String mobileNo = objs.getString("mobileNo").contains("*")
                    ? null
                    : objs.getString("mobileNo");
            String cardNo = objs.getString("cardNo").contains("*")
                    ? null
                    : objs.getString("cardNo");
            PayInfoBean payInfoBean = new PayInfoBean();
            payInfoBean.setId(Long.parseLong(bankId));
            payInfoBean.setBankNo(bankNo);
            payInfoBean.setRealName(realName);
            payInfoBean.setMobileNo(mobileNo);
            payInfoBean.setCardNo(cardNo);
            xfPayService.updateBindCardInfo(payInfoBean);
            result.put("retCode", SUCCESS);
        } catch (Exception e) {
            logger.error("添加卡发生异常", e);
            result.put("retCode", FAILED);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/addBindBankCard", method = {RequestMethod.GET,
            RequestMethod.POST})
    public Object addBindBankCard(@RequestBody JSONObject objs) {
        logger.info("添加新支付新卡....", objs);
        Map<String, String> result = new HashMap<String, String>();
        try {
            int orgId = sysUserSession.getOrgId();// 用户唯一ID
            // 填充参数
            String realName = objs.getString("realName").trim();// 真实姓名
            String cardNo = objs.getString("cardNo").trim();// 身份证
            String bankType = objs.getString("bankType").trim();// 银行卡号
            String bankNo = objs.getString("bankNo").trim();// 银行卡号
            String mobileNo = objs.getString("mobileNo").trim();// 手机号
            PayInfoBean payInfoBean = new PayInfoBean(orgId, mobileNo, bankType,
                    bankNo, cardNo, realName);
            xfPayService.insertPayInfo(payInfoBean);
            result.put("retCode", SUCCESS);
        } catch (Exception e) {
            logger.error("添加卡发生异常", e);
            result.put("retCode", FAILED);
        }
        return result;
    }

    /**
     * 收银台支付
     * 
     * @param request
     * @param response
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/payByCashierDesk", method = RequestMethod.GET)
    public void payByCashierDesk(HttpServletRequest request,
            HttpServletResponse response) {
        int orgId = sysUserSession.getOrgId();
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
        } catch (IOException e) {
            logger.debug("支付表单提交发送异常", e);
        }
        Map<String, String> param = new HashMap<String, String>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paraName = paramNames.nextElement();
            String para;
            try {
                para = URLDecoder.decode(request.getParameter(paraName),
                        "UTF-8");
                param.put(paraName, para.trim());
            } catch (UnsupportedEncodingException e) {

            }
        }
        // 后台提供数据(regId,订单号orderNo,预付款(还款)flag标识)
        String orderId = param.get("orderId");
        // 支付期数
        String orderItem = param.get("orderItem");
        String regId = null;
        OrderBean ob = null;
        UserBean ub = null;
        PayConfBean payConfBean = null;
        try {
            // 查询订单信息
            Map<String, Object> orderParam = new HashMap<String, Object>();
            orderParam.put("orderId", orderId);
            ob = xfPayService.getOrderInfo(orderParam);
            payConfBean = payService.getPayChannelBy(ob.getPayOwnerId(), ob.getMerchantNo());
            // 查询用户信息
            ub = xfPayService.getUserInfo(Long.parseLong(ob.getUserId()));
            regId = ob.getRegId();
        } catch (Exception e) {
            go2Cashier(response, payConfBean, null);
        }

        String amount;// 交易金额
        String realAmount;
        String productInfo;// 产品信息
        String noticeUrl;
        if (ob.getRiskStatus() != 3 && ob.getRiskStatus() != 7) {// 预支付金额
            BigDecimal needAmt = BigDecimalUtil.sub(new BigDecimal(ob.getPreAmt()), ob.getReceivedPreAmt());
            amount = BigDecimalUtil.mul(needAmt, new BigDecimal(100)) + "";
            if (amount.contains(".")) {
                amount = amount.substring(0, amount.indexOf("."));
            }
            productInfo = ob.getOrderName() + "-预付款";
            noticeUrl = paramConfig.getCashierPreNoticeUrl();
        } else {// 分期还款
            productInfo = ob.getOrderName() + "-分期";
            noticeUrl = paramConfig.getCashierNoticeUrl();
            // 查询还款金额
            amount = getRepayAmt(orderId, regId, orderItem);
            // 需要计算是否拆分支付过
            JSONObject objs = new JSONObject();
            objs.put("orderId", orderId);
            objs.put("repayNo", orderItem);
            Integer selSumAmount = paymentLogBiz.selSumAmount(objs);
            amount = BigDecimalUtil.sub(BigDecimalUtil
                    .mul(new BigDecimal(amount), new BigDecimal(100)), selSumAmount == null
                    ? BigDecimal.ZERO
                    : new BigDecimal(selSumAmount)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
            // amount = BigDecimalUtil
            // .mul(new BigDecimal(amount), new BigDecimal(100))
            // .setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        }
        BigDecimal b = new BigDecimal(amount).divide(new BigDecimal(100), 2,
                BigDecimal.ROUND_HALF_UP);
        realAmount = b.toString();
        String merchantNo = param.get("orderId") + "-" + orderItem + "-" + CommonUtil.randomString(6);

        // 写Redis
        Map<String, Object> payRedis = new HashMap<String, Object>();
        payRedis.put("orderNo", merchantNo);
        payRedis.put("productName", productInfo);
        payRedis.put("amount", realAmount);
        payRedis.put("method", "收银台(网银)支付");
        redisPlatformDao.setKeyAndValue("payByQuick" + orgId, JSONObject.toJSONString(payRedis));
        noticeUrl = noticeUrl + File.separator + payConfBean.getId();
        String reqSn = null;
        try {
            reqSn =
                    UnRepeatCodeGenerator.createUnRepeatCode(payConfBean.getMerchantId(), payConfBean.getService(),
                            merchantNo);
            PayBaseInfo payBaseInfo = new PayBaseInfo();
            payBaseInfo.setAmount(amount);// 放大100倍后的金额
            payBaseInfo.setReturnUrl(paramConfig.getCashierReturnUrl());
            payBaseInfo.setNoticeUrl(noticeUrl);
            payBaseInfo.setProductInfo(productInfo);
            payBaseInfo.setOutOrderId(merchantNo);
            String cashierData = getCashierData(payConfBean, payBaseInfo);
            String cashierSign = getCashierSign(payConfBean, merchantNo, cashierData, reqSn);
            Map<String, String> paraMap = new HashMap<String, String>();
            paraMap.put("service", "TO_UCF_CASHIER");
            paraMap.put("secId", "RSA");
            paraMap.put("version", payConfBean.getvSon());
            paraMap.put("reqSn", reqSn);
            paraMap.put("merchantId", payConfBean.getMerchantId());
            paraMap.put("data", cashierData);
            paraMap.put("sign", cashierSign);
            go2Cashier(response, payConfBean, paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRepayAmt(String orderId, String regId, String orderItem) {
        String amount = "0";
        Map<String, Object> retMap = billInfoService.getAssignBill(orderId, orderItem, regId);
        amount = retMap.get("curRepayAmt") + "";
        return amount;
    }

    /**
     * 快捷支付
     * 
     * @param request
     * @param response
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/payByQuick", method = RequestMethod.GET)
    public void payByQuick(HttpServletRequest request,
            HttpServletResponse response) {
        int orgId = sysUserSession.getOrgId();
        // int orgId = 1;
        // 后台提供数据(regId,订单号orderNo,预付款(还款)flag标识)
        String orderId = request.getParameter("orderId");
        // 支付四要素
        String bankId = request.getParameter("bankId");
        // 支付期数
        String orderItem = request.getParameter("orderItem");
        // 查询后台
        Map<String, Object> orgMap = new HashMap<String, Object>();
        orgMap.put("id", bankId);
        orgMap.put("userId", orgId);
        PayInfoBean payInfoBean = xfPayService.getByIdAndOrgId(orgMap);// 需缓存穿透
        // 根据orderNo,查询Order表获取(user_id)
        Map<String, Object> orderParam = new HashMap<String, Object>();
        orderParam.put("orderId", orderId);
        OrderBean ob = xfPayService.getOrderInfo(orderParam);
        PayConfBean payConfBean = payService.getPayChannelBy(ob.getPayOwnerId(), ob.getMerchantNo());
        // 【解绑卡信息】
        try {
            if (1 != payInfoBean.getStatus()) {// 未绑定的卡需解绑
                // 查询身份证下的支付数据,并解绑
                // payInfoBean = xfPayService.getByCardNo(payInfoBean.getCardNo(), orgId + "");
                if (payInfoBean != null) {// 解绑
                    // ----解绑逻辑------
                    // 2017-08-02 银行卡自动解绑

                    Map<String, String> objs = new HashMap<String, String>();
                    objs.put("regId", payInfoBean.getMobileNo());
                    objs.put("bankCardNo", payInfoBean.getBankNo());
                    objs.put("payOwnerId", ob.getPayOwnerId());
                    objs.put("merchantNo", ob.getMerchantNo());
                    Map<String, Object> resultMap = assetApiService.autoSwitchUnBindBankCard(objs);
                    logger.info("---快捷支付---自动解绑卡--结果 --{}", resultMap);
                }
            }
        } catch (Exception e1) {
            logger.info("---快捷支付---自动解绑卡--异常 {}", e1);
        }

        // 人员信息
        UserBean ub = xfPayService.getUserInfo(Long.parseLong(ob.getUserId()));

        String regId = ob.getRegId();
        String outOrderId = ob.getOrderId() + "-" + orderItem + "-"
                + CommonUtil.randomString(4);// 订单号+随机
        String amount;// 支付金额"金额放大100倍"
        String realAmount;
        String productName;// 产品名称 +"预付款/分期"
        String noticeUrl;
        if (ob.getRiskStatus() != 3 && ob.getRiskStatus() != 7) {// 预支付金额
            BigDecimal getReceivedPreAmt = ob.getReceivedPreAmt() == null ? BigDecimal.ZERO : ob.getReceivedPreAmt();
            BigDecimal needAmt = BigDecimalUtil.sub(new BigDecimal(ob.getPreAmt()), getReceivedPreAmt);
            amount = BigDecimalUtil.mul(needAmt, new BigDecimal(100)) + "";
            logger.info("t----amount:{},preAmount:{}", amount, ob.getPreAmt());
            if (amount.contains(".")) {
                amount = amount.substring(0, amount.indexOf("."));
            }
            productName = ob.getOrderName() + "-预付款";
            noticeUrl = paramConfig.getQuickPreNoticeUrl();
        } else {// 分期还款
            productName = ob.getOrderName() + "-分期";
            noticeUrl = paramConfig.getQuickNoticeUrl();
            // 查询还款金额
            amount = getRepayAmt(orderId, regId, orderItem);
            // 需要计算是否拆分支付过
            JSONObject objs = new JSONObject();
            objs.put("orderId", orderId);
            objs.put("repayNo", orderItem);
            Integer selSumAmount = paymentLogBiz.selSumAmount(objs);
            amount = BigDecimalUtil.sub(BigDecimalUtil
                    .mul(new BigDecimal(amount), new BigDecimal(100)), selSumAmount == null
                    ? BigDecimal.ZERO
                    : new BigDecimal(selSumAmount)).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
            // amount = BigDecimalUtil
            // .mul(new BigDecimal(amount), new BigDecimal(100))
            // .setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        }
        BigDecimal b = new BigDecimal(amount).divide(new BigDecimal(100), 2,
                BigDecimal.ROUND_HALF_UP);
        realAmount = b.toString();

        // 保存支付相关信息
        Map<String, Object> payRedis = new HashMap<String, Object>();
        payRedis.put("orderNo", outOrderId);
        payRedis.put("productName", productName);
        payRedis.put("amount", realAmount);
        payRedis.put("method", "快捷支付");
        redisPlatformDao.setKeyAndValue("payByQuick" + orgId, JSONObject.toJSONString(payRedis));
        String returnUrl = paramConfig.getQuickReturnUrl();
        noticeUrl = noticeUrl + File.separator + payConfBean.getId();
        PayBaseInfo payBaseInfo = new PayBaseInfo();
        payBaseInfo.setAmount(amount);// 放大100倍后的金额
        payBaseInfo.setReturnUrl(returnUrl);
        payBaseInfo.setNoticeUrl(noticeUrl);
        payBaseInfo.setOutOrderId(outOrderId);
        // 支付核心逻辑
        Map<String, String> payMap = getPayMap(payBaseInfo, payInfoBean);
        go2Pay(response, payConfBean, payMap);
    }

    /**
     * 提前退租收银台支付
     * 
     * @param request
     * @param response
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/paySettlementByCashierDesk", method = RequestMethod.GET)
    public void paySettlementByCashierDesk(HttpServletRequest request,
            HttpServletResponse response) {
        int orgId = sysUserSession.getOrgId();
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            writer = response.getWriter();
            // writer.print(buildRequest(params, "get", "支付"));
        } catch (IOException e) {
            logger.debug("支付表单提交发送异常", e);
        }
        Map<String, String> param = new HashMap<String, String>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paraName = paramNames.nextElement();
            String para;
            try {
                para = URLDecoder.decode(request.getParameter(paraName),
                        "UTF-8");
                param.put(paraName, para.trim());
            } catch (UnsupportedEncodingException e) {

            }
        }
        String id = param.get("id");
        String regId = null;
        String amount;// 交易金额
        String realAmount;
        String productInfo;// 产品信息
        String noticeUrl;
        // 根据ID查询inst_settleapply信息
        SettleApplyBean settleApplyBean = settleApplyBeanBiz.getNeedPayAmt(id);
        payService.savePaytemRecord(param.get("payList") + "", settleApplyBean.getOrderId(), id);
        param.remove("payList");
        if (!"2".equals(settleApplyBean.getAmtStatus())) {
            throw new RuntimeException("该订单不在待支付状态范围内");
        }
        // 拆分退租支付接口
        BigDecimal getReceivedPreAmt =
                settleApplyBean.getReceiveAmt() == null ? BigDecimal.ZERO : settleApplyBean.getReceiveAmt();
        BigDecimal needAmt = BigDecimalUtil.sub(settleApplyBean.getTotalRepayAmt(), getReceivedPreAmt);
        amount = BigDecimalUtil.mul(needAmt, new BigDecimal(100)) + "";
        if (amount.contains(".")) {
            amount = amount.substring(0, amount.indexOf("."));
        }
        productInfo = settleApplyBean.getOrderId() + "-退租收银台支付";
        noticeUrl = paramConfig.getSettleCashierUrl();
        BigDecimal b = new BigDecimal(amount).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        realAmount = b.toString();
        String service = "TO_UCF_CASHIER";
        String merchantNo = id + "-" + CommonUtil.randomString(6);
        //
        OrderBean orderBean = orderBiz.selByOrderId(settleApplyBean.getOrderId());
        PayConfBean payConfBean = payService.getPayChannelBy(orderBean.getPayOwnerId(), orderBean.getMerchantNo());
        // 写Redis
        Map<String, Object> payRedis = new HashMap<String, Object>();
        payRedis.put("orderNo", merchantNo);
        payRedis.put("productName", productInfo);
        payRedis.put("amount", realAmount);
        payRedis.put("method", "收银台(网银)支付");
        redisPlatformDao.setKeyAndValue("payByQuick" + orgId, JSONObject.toJSONString(payRedis));
        noticeUrl = noticeUrl + File.separator + payConfBean.getId();
        String reqSn = null;
        try {
            reqSn =
                    UnRepeatCodeGenerator.createUnRepeatCode(payConfBean.getMerchantId(), payConfBean.getService(),
                            merchantNo);
            PayBaseInfo payBaseInfo = new PayBaseInfo();
            payBaseInfo.setAmount(amount);// 放大100倍后的金额
            payBaseInfo.setReturnUrl(paramConfig.getCashierReturnUrl());
            payBaseInfo.setNoticeUrl(noticeUrl);
            payBaseInfo.setProductInfo(productInfo);
            payBaseInfo.setOutOrderId(merchantNo);
            String cashierData = getCashierData(payConfBean, payBaseInfo);
            String cashierSign = getCashierSign(payConfBean, merchantNo, cashierData, reqSn);
            Map<String, String> paraMap = new HashMap<String, String>();
            paraMap.put("service", "TO_UCF_CASHIER");
            paraMap.put("secId", "RSA");
            paraMap.put("version", payConfBean.getvSon());
            paraMap.put("reqSn", reqSn);
            paraMap.put("merchantId", payConfBean.getMerchantId());
            paraMap.put("data", cashierData);
            paraMap.put("sign", cashierSign);
            go2Cashier(response, payConfBean, paraMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 提前退租快捷支付
     * 
     * @param request
     * @param response
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/paySettlementByQuick/{flag}", method = RequestMethod.GET)
    public void paySettlementByQuick(@PathVariable("flag") String flag, HttpServletRequest request,
            HttpServletResponse response) {
        int orgId = sysUserSession.getOrgId();
        // id对应退租inst_settlyApply记录
        String id = request.getParameter("id");
        String payList = request.getParameter("payList");
        String bankId = request.getParameter("bankId");
        String repayAmt = request.getParameter("repayAmt");
        String amount;// 支付金额"金额放大100倍"
        String realAmount;
        String productName;// 产品名称 +"预付款/分期"
        String noticeUrl;
        Map<String, Object> orgMap = new HashMap<String, Object>();
        orgMap.put("id", bankId);
        orgMap.put("userId", orgId);
        PayInfoBean payInfoBean = xfPayService.getByIdAndOrgId(orgMap);// 需缓存穿透
        String outOrderId = id + "-" + CommonUtil.randomString(4);// 订单号+随机
        // 根据ID查询inst_settleapply信息
        SettleApplyBean settleApplyBean = settleApplyBeanBiz.getNeedPayAmt(id);
        OrderBean orderBean = orderBiz.selByOrderId(settleApplyBean.getOrderId());
        PayConfBean payConfBean = payService.getPayChannelBy(orderBean.getPayOwnerId(), orderBean.getMerchantNo());
        // 【解绑卡信息】
        try {
            if (1 != payInfoBean.getStatus()) {// 未绑定的卡需解绑
                // 查询身份证下的支付数据,并解绑
                // payInfoBean = xfPayService.getByCardNo(payInfoBean.getCardNo(), orgId + "");
                if (payInfoBean != null) {// 解绑
                    Map<String, String> objs = new HashMap<String, String>();
                    objs.put("regId", payInfoBean.getMobileNo());
                    objs.put("bankCardNo", payInfoBean.getBankNo());
                    objs.put("payOwnerId", orderBean.getPayOwnerId());
                    objs.put("merchantNo", orderBean.getMerchantNo());
                    Map<String, Object> resultMap = assetApiService.autoSwitchUnBindBankCard(objs);
                    logger.info("---快捷支付---自动解绑卡--结果 --{}", resultMap);
                }
            }
        } catch (Exception e1) {
            logger.info("---快捷支付---自动解绑卡--异常 {}", e1);
        }

        payService.savePaytemRecord(payList, settleApplyBean.getOrderId(), id);
        if (!"2".equals(settleApplyBean.getAmtStatus())) {
            throw new RuntimeException("该订单不在待支付状态范围内");
        }
        if ("break".equals(flag)) {
            amount =
                    BigDecimalUtil.mul(repayAmt == null ? BigDecimal.ZERO : new BigDecimal(repayAmt), new BigDecimal(
                            100))
                            + "";
            logger.info("t----amount:{},TotalAmount:{}", amount, settleApplyBean.getTotalRepayAmt());
            if (amount.contains(".")) {
                amount = amount.substring(0, amount.indexOf("."));
            }
            productName = settleApplyBean.getOrderId() + "-退租拆分支付";
            noticeUrl = paramConfig.getSettleQuickUrl();
        } else {
            // 拆分退租支付接口
            BigDecimal getReceivedPreAmt =
                    settleApplyBean.getReceiveAmt() == null ? BigDecimal.ZERO : settleApplyBean.getReceiveAmt();
            BigDecimal needAmt = BigDecimalUtil.sub(settleApplyBean.getTotalRepayAmt(), getReceivedPreAmt);
            amount = BigDecimalUtil.mul(needAmt, new BigDecimal(100)) + "";
            logger.info("t----amount:{},TotalAmount:{}", amount, settleApplyBean.getTotalRepayAmt());
            if (amount.contains(".")) {
                amount = amount.substring(0, amount.indexOf("."));
            }
            productName = settleApplyBean.getOrderId() + "-退租拆分支付";
            noticeUrl = paramConfig.getSettleQuickUrl();
        }
        BigDecimal b = new BigDecimal(amount).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        realAmount = b.toString();

        // 保存支付相关信息
        Map<String, Object> payRedis = new HashMap<String, Object>();
        payRedis.put("orderNo", outOrderId);
        payRedis.put("productName", productName);
        payRedis.put("amount", realAmount);
        payRedis.put("method", "快捷支付");
        redisPlatformDao.setKeyAndValue("payByQuick" + orgId, JSONObject.toJSONString(payRedis));
        String returnUrl = paramConfig.getQuickReturnUrl();
        noticeUrl = noticeUrl + File.separator + payConfBean.getId();
        PayBaseInfo payBaseInfo = new PayBaseInfo();
        payBaseInfo.setAmount(amount);// 放大100倍后的金额
        payBaseInfo.setReturnUrl(returnUrl);
        payBaseInfo.setNoticeUrl(noticeUrl);
        payBaseInfo.setOutOrderId(outOrderId);
        // 支付核心逻辑
        Map<String, String> payMap = getPayMap(payBaseInfo, payInfoBean);
        go2Pay(response, payConfBean, payMap);
    }

    @SuppressWarnings("rawtypes")
    protected Float getAmtSum(List<Map> allList) {
        Float amtSum = 0.00f;
        int i = allList.size() - 1;
        amtSum = string2Float((String) allList.get(i).get("currcStmtAmt"))
                + string2Float((String) allList.get(i).get("currLatefeeIn"))
                - string2Float(
                (String) allList.get(i).get("currRepayAmtSumIn"));
        return amtSum * 100;
    }

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
     * 兼容性差
     * 
     * @param sParaTemp
     * @param strMethod
     * @param strButtonName
     * @return
     */
    @SuppressWarnings("unused")
    private String buildRequest(Map<String, String> sParaTemp, String strMethod,
            String strButtonName) {
        // 增加md5签名和AES加密
        List<String> keys = new ArrayList<String>(sParaTemp.keySet());
        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append("<form name=\"alipaysubmit\" action=\""
                + "https://mapi.ucfpay.com/gateway.do" + "\" method=\""
                + strMethod + "\">");
        for (int i = 0; i < keys.size(); i++) {
            String name = keys.get(i);
            String value = sParaTemp.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name
                    + "\" value=\"" + value + "\"/>");
        }
        // submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName
                + "\" style=\"display:none;\"></form>");
        sbHtml.append(
                "<script>document.forms['alipaysubmit'].submit();</script>");
        String str = sbHtml.toString();
        return str;
    }

    private String buildRequest2(Map<String, String> sParaTemp,
            String strMethod, String strButtonName) {
        // 增加md5签名和AES加密
        List<String> keys = new ArrayList<String>(sParaTemp.keySet());
        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append("<html><head><title>xfpay</title>"
                + "<script type=\"text/javascript\">"
                + "function autoSubmit(){ "
                + "document.getElementById(\"alipaysubmit\").submit();}"
                + "</script><body onload=\"autoSubmit();\" >");
        sbHtml.append(
                "<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\""
                        + "https://mapi.ucfpay.com/gateway.do" + "\" method=\""
                        + strMethod + "\">");
        for (int i = 0; i < keys.size(); i++) {
            String name = keys.get(i);
            String value = sParaTemp.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name
                    + "\" value=\"" + value + "\"/>");
        }
        // submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName
                + "\" style=\"display:none;\"></form>");
        sbHtml.append("</body></html>");
        // sbHtml.append("<script>alert('1111');document.forms['alipaysubmit'].submit();</script>");
        String str = sbHtml.toString();
        return str;
    }

    /**
     * 网银支付生成签名
     * 
     * @param objs
     * @param request
     * @return
     */
    @RequestMapping(value = "/genSignature", method = RequestMethod.GET)
    public Object genSignature(@RequestBody JSONObject objs,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        double amount = objs.getDouble("amount");
        amount = amount * 100;// 放大100倍
        String payAmount = amount + "";
        if (payAmount.indexOf(".") != -1) {
            payAmount = payAmount.substring(0, payAmount.indexOf("."));// 支付金额
        }
        String orderId = objs.getString("orderId");// 订单号
        String merId = objs.getString("merId");// yianjia
        String productName = objs.getString("productName");// 产品名称()
        String merchantId = "M200001523";
        String merRsaKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWO6JG+aDscLAXF7LXjJ1R5P/gK0szkNyuA059lYEaHU3tJ+FKGYhdigfNk+ld69bSh3nwlX6fR8fqa/9o8cSzbyz5BDUkj7ZgldBNRRTLP+VyJk3xA09t7PnmtjS+Y8ttLbcZNDYosdYfkwvDxFesJ6ljqOoe/lUO8y1YhVNSpwIDAQAB";
        String service = "TO_UCF_CASHIER";
        String returnUrl = "";
        String noticeUrl = "";
        String merchantNo = orderId + "-" + merId + "-"
                + CommonUtil.randomString(6);
        Map<String, String> params = new HashMap<String, String>();// 参与签名业务字段集合
        params.put("expireTime", "");
        params.put("returnUrl", returnUrl);// 同步返回
        params.put("transCur", "156");
        params.put("memo", "");
        params.put("noticeUrl", noticeUrl);// 异步回调
        params.put("productInfo", "");// 产品信息
        params.put("productType", "W");// 产品类型
        params.put("version", "3.0.0");// 版本
        params.put("secId", "RSA");// 异步回调
        params.put("amount", payAmount);
        params.put("source", "1");
        params.put("service", service);
        params.put("accountType", "");//
        params.put("payerId", "");//
        params.put("merchantId", merchantId);//
        params.put("merchantNo", merchantNo);
        params.put("productName", productName);// ----
        String reqSn = "";
        try {
            reqSn = UnRepeatCodeGenerator.createUnRepeatCode(merchantId,
                    service, merchantNo);
        } catch (CoderException e) {
            logger.error("生成reqSn出现异常", e);
        }
        params.put("reqSn", reqSn);

        String signValue = "";
        try {
            signValue = UcfForOnline.createSign(merRsaKey, "sign", params,
                    "RSA");
        } catch (GeneralSecurityException e) {
            logger.error("生成signValue出现异常1", e);
        } catch (CoderException e) {
            logger.error("生成signValue出现异常2", e);
        }
        result.put("sign", signValue);
        return result;
    }

    /**
     * 校验后台代偿订单
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/verifyPayment", method = {RequestMethod.GET,
            RequestMethod.POST})
    public Object verifyPayment(@RequestBody JSONObject objs,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        logger.debug("校验后台代偿订单获取参数:{}", objs);
        String orderId = objs.getString("orderId");
        String repayNo = objs.getString("repayNo");
        String regId = null;
        try {
            // 查询订单信息
            Map<String, Object> orderParam = new HashMap<String, Object>();
            orderParam.put("orderId", orderId);
            OrderBean ob = xfPayService.getOrderInfo(orderParam);
            regId = ob.getRegId();
            Map<String, Object> billInfo = billInfoService.getDesignBillInfo(orderId, repayNo, regId);
            if (billInfo == null) {
                result.put("retCode", "error");
                result.put("retMsg", "不能跨期还款");
            } else {
                result.put("retCode", "success");
                result.put("retMsg", "可以还款");
            }
            return result;
        } catch (Exception e) {
            logger.error("校验后台代偿订单异常:{}", e);
            result.put("retCode", FinanceConstant.ERROR);
        }
        return result;
    }

    /*
     * Description: 获取支付日志列表
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/getMersPaymentLogList", method = {
            RequestMethod.POST, RequestMethod.GET})
    public Map getMersPaymentLogList(@RequestBody JSONObject objs,
            HttpServletRequest request) {
        try {
            /* 日期校验 */
            adminServiceImpl.getMerchantNos(objs);
            if (null != objs.getDate("startDate")
                    && null != objs.getDate("endDate")) {
                Date startDate = objs.getDate("startDate");
                Date endDate = objs.getDate("endDate");

                if (DateUtil.diffDays(startDate, endDate) < 0) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("success", 2);
                    map.put("retDatetype", 1);
                    map.put("retCode", "00000095");
                    map.put("retUserInfo", "处理失败，开始日期不能大于结束日期");
                    map.put("retFactInfo", "处理失败");
                    return map;
                }
            }
            /* 金额校验 */
            if (null != objs.getString("startAmount")
                    && "" != objs.getString("startAmount")
                    && null != objs.getString("endAmount")
                    && "" != objs.getString("endAmount")) {
                Double dStartAmount = objs.getDoubleValue("startAmount");
                Double dEndAmount = objs.getDoubleValue("endAmount");

                if (dStartAmount > dEndAmount) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("success", 2);
                    map.put("retDatetype", 1);
                    map.put("retCode", "00000096");
                    map.put("retUserInfo", "处理失败，起始金额不能大于结束金额");
                    map.put("retFactInfo", "处理失败");
                    return map;
                }
            }

            /* 单独处理 结束日期 */
            if (null != objs.getDate("endDate")) {
                Date endDate = objs.getDate("endDate");
                endDate = DateUtil.addDays(endDate, 1).getTime();
                objs.put("endDate", endDate);
            }
            /* 单独处理金额 */
            if (null != objs.getString("startAmount")
                    && "" != objs.getString("startAmount")) {
                Double dStartAmount = objs.getDoubleValue("startAmount");
                Long lStartAmount = Math.round(dStartAmount * 100);
                objs.put("startAmount", lStartAmount);
            }
            if (null != objs.getString("endAmount")
                    && "" != objs.getString("endAmount")) {
                Double dEndAmount = objs.getDoubleValue("endAmount");
                Long lEndAmount = Math.round(dEndAmount * 100);
                objs.put("endAmount", lEndAmount);
            }

            logger.debug("IQB信息---获取支付日志列表信息开始...");
            PageInfo<PaymentLogBean> pageInfo = xfPayService
                    .getMersPaymentLogList(objs);
            logger.debug("IQB信息---获取支付日志列表信息结束...");

            logger.debug("IQB信息---获取支付日志总金额 总条数...");
            Map<String, Object> totalMap = xfPayService
                    .getMersPaymentLogNumAmt(objs);
            logger.debug("IQB信息---获取支付日志总金额 总条数结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", pageInfo);
            linkedHashMap.put("totalMap", totalMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(
                    new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 保存支付日志列表
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/getMersPaymentLogListForSave", method = {
            RequestMethod.POST, RequestMethod.GET})
    public Map getMersPaymentLogListForSave(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            Map<String, String> data = new HashMap<String, String>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para;
                para = request.getParameter(paraName);
                data.put(paraName, para.trim());
                logger.info("导出支付日志列表信息获取到的参数:{}", data);
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            /* 日期校验 */
            if (null != objs.getDate("startDate")
                    && null != objs.getDate("endDate")) {
                Date startDate = objs.getDate("startDate");
                Date endDate = objs.getDate("endDate");

                if (DateUtil.diffDays(startDate, endDate) < 0) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("success", 2);
                    map.put("retDatetype", 1);
                    map.put("retCode", "00000095");
                    map.put("retUserInfo", "处理失败，开始日期不能大于结束日期");
                    map.put("retFactInfo", "处理失败");
                    return map;
                }
            }
            /* 金额校验 */
            if (null != objs.getString("startAmount")
                    && !"".equals(objs.getString("startAmount"))
                    && null != objs.getString("endAmount")
                    && !"".equals(objs.getString("endAmount"))) {
                Double dStartAmount = objs.getDoubleValue("startAmount");
                Double dEndAmount = objs.getDoubleValue("endAmount");

                if (dStartAmount > dEndAmount) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("success", 2);
                    map.put("retDatetype", 1);
                    map.put("retCode", "00000096");
                    map.put("retUserInfo", "处理失败，起始金额不能大于结束金额");
                    map.put("retFactInfo", "处理失败");
                    return map;
                }
            }

            /* 单独处理 结束日期 */
            if (null != objs.getDate("endDate")) {
                Date endDate = objs.getDate("endDate");
                endDate = DateUtil.addDays(endDate, 1).getTime();
                objs.put("endDate", endDate);
            }
            /* 单独处理金额 */
            if (null != objs.getString("startAmount")
                    && !"".equals(objs.getString("startAmount"))) {
                Double dStartAmount = objs.getDoubleValue("startAmount");
                Long lStartAmount = Math.round(dStartAmount * 100);
                objs.put("startAmount", lStartAmount);
            }
            if (null != objs.getString("endAmount")
                    && !"".equals(objs.getString("endAmount"))) {
                Double dEndAmount = objs.getDoubleValue("endAmount");
                Long lEndAmount = Math.round(dEndAmount * 100);
                objs.put("endAmount", lEndAmount);
            }

            logger.debug("IQB信息---导出支付日志列表信息开始...");
            xfPayService.getMersPaymentLogListForSave(objs, response);
            logger.debug("IQB信息---导出支付日志列表信息结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("retCode", "00000000");
            linkedHashMap.put("retUserInfo", "处理成功");
            linkedHashMap.put("retFactInfo", "处理成功");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(
                    new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 收银台第一步,加密业务数据
     * 
     * @throws Exception
     **/
    private String getCashierData(PayConfBean payConfBean, PayBaseInfo payBaseInfo) throws Exception {
        JSONObject objs = new JSONObject();// 参与加密的业务字段
        objs.put("accountType", "");
        objs.put("amount", payBaseInfo.getAmount()); // 支付金额
        objs.put("expireTime", "");
        objs.put("memo", "");
        objs.put("merchantNo", payBaseInfo.getOutOrderId()); // 唯一订单号
        objs.put("noticeUrl", payBaseInfo.getNoticeUrl());// 异步回调地址
        objs.put("payerId", "");
        objs.put("productInfo", "");
        objs.put("productName", payBaseInfo.getProductInfo());// 产品名称
        objs.put("productType", "W");
        objs.put("returnUrl", payBaseInfo.getReturnUrl());// 同步回调地址
        objs.put("source", "1");
        objs.put("transCur", "156");
        logger.info("第一步的数据:{}", objs);
        return AESCoder.encrypt(objs.toString(), payConfBean.getKey());
    }

    /**
     * 收银台第二步,加密业务数据
     * 
     * @throws CoderException
     * @throws GeneralSecurityException
     **/
    private String getCashierSign(PayConfBean payConfBean, String outOrderId, String data, String reqSn)
            throws GeneralSecurityException, CoderException {
        Map<String, String> params = new HashMap<String, String>();// 参与签名业务字段集合
        params.put("secId", "RSA");// 固定值
        params.put("data", data);
        params.put("service", "TO_UCF_CASHIER");// 固定值
        params.put("merchantId", payConfBean.getMerchantId());
        params.put("reqSn", reqSn);
        params.put("version", payConfBean.getvSon());
        logger.info("第一步的数据:{}", params);
        return UcfForOnline.createSign(payConfBean.getKey(), "sign", params, "RSA");
    }

    /** 收银台第三步,加密业务数据 **/
    private void go2Cashier(HttpServletResponse response, PayConfBean payConfBean, Map<String, String> sParaTemp) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            /** 表单提交 **/
            writer = response.getWriter();
            writer.print(buildRequest2(sParaTemp, "post", "submit"));
        } catch (Exception e) {
            logger.error("", e);
            e.printStackTrace();
        } finally {

        }
    }

    /** 第2.1步快捷加密方法 **/
    public void go2Pay(HttpServletResponse response, PayConfBean payConfBean, Map<String, String> payMap) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String gateWay = payConfBean.getGateWay();
            String jsonStr = JSON.toJSONString(payMap);
            String key = payConfBean.getKey();// 分配给商户的RSA公钥
            String data = AESCoder.encrypt(jsonStr, key);// AES算法加密业务数据
            Map<String, String> payData = payData(payConfBean, data);
            logger.info("createOrder, 地址= " + gateWay + ", 提交参数= " + payData);
            // String params = HttpUtils.convertMapToParams(payData);
            // String url = gateWay + "?" + params;
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            /** 表单提交 **/
            PrintWriter writer = response.getWriter();
            writer.print(buildRequest(payData, "get", "支付"));
        } catch (Exception e) {
            logger.error("", e);
            e.printStackTrace();
            out.println(e.getMessage());
        } finally {

        }
    }

    /** 第一步快捷加密方法 **/
    public Map<String, String> getPayMap(PayBaseInfo payBaseInfo, PayInfoBean payInfoBean) {
        Map<String, String> busi_data = new HashMap<String, String>();
        busi_data.put("mobileNo", payInfoBean.getMobileNo());// 支付手机号
        busi_data.put("outOrderId", payBaseInfo.getOutOrderId());// 第三方支付订单号
        busi_data.put("userId", payInfoBean.getMobileNo());// 用户编号
        busi_data.put("realName", payInfoBean.getRealName());// 用户真实姓名
        busi_data.put("cardNo", payInfoBean.getCardNo());// 用户身份证号
        busi_data.put("cardType", "1");
        busi_data.put("amount", payBaseInfo.getAmount());// 放大100倍的支付金额
        busi_data.put("returnUrl", payBaseInfo.getReturnUrl());
        busi_data.put("noticeUrl", payBaseInfo.getNoticeUrl());
        busi_data.put("bankNo", payInfoBean.getBankNo());// 银行卡号
        busi_data.put("bankCode", "");
        busi_data.put("bankName", "");
        return busi_data;
    }

    /** 第2.2步快捷加密方法 **/
    public Map<String, String> payData(PayConfBean payConfBean, String data) throws GeneralSecurityException,
            CoderException {
        Map<String, String> req_params = new HashMap<String, String>();// 请求参数
        req_params.put("service", payConfBean.getService());
        req_params.put("secId", payConfBean.getSecId());
        req_params.put("version", payConfBean.getvSon());
        req_params.put("reqSn", System.currentTimeMillis() + "");// 此处是简单测试，建议商户自行采用更加严谨的算法，保证唯一性
        req_params.put("merchantId", payConfBean.getMerchantId());
        req_params.put("data", data);
        String sign = UcfForOnline.createSign(payConfBean.getKey(), "sign", req_params, "RSA");
        req_params.put("sign", sign);
        return req_params;
    }
}
