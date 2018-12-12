/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月30日 下午2:52:05
 * @version V1.0
 */
package com.iqb.consumer.manage.front.api.util;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.consumer.common.exception.Code;
import com.iqb.consumer.common.exception.ServiceException;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.HttpUtils;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.etep.common.exception.IqbException;
import com.ucf.sdk.CoderException;
import com.ucf.sdk.UcfForOnline;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("unused")
public class PayUtils {

    protected static final Logger logger = LoggerFactory
            .getLogger(PayUtils.class);

    public static void main(String[] args) {
        String amount = "3.00"; // 交易金额

        // 金额扩大100倍
        amount = BigDecimalUtil.expand(new BigDecimal(amount)).toString();

        Map<String, String> param = new HashMap<String, String>();

        param.put("amount", amount.substring(0, amount.indexOf(".")));
        param.put("traceNo", "SYQS2002170210004");
        // param.put("noticeUrl",
        // "http://api.ishandian.cn:8088/consumer.manage.front/nr/noticePreCashierResult");//
        // 回调通知后台----------------------
        param.put("cardNo", "140621198208115539");
        param.put("bankCode", "");
        param.put("gateway", "https://mapi.ucfpay.com/gateway.do");
        param.put("merchantId", "M200001523");
        param.put("regId", "13681486576");
        param.put("bankCardNo", "6226225200195079");
        param.put(
                "merchantKey",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWO6JG+aDscLAXF7LXjJ1R5P/gK0szkNyuA059lYEaHU3tJ+FKGYhdigfNk+ld69bSh3nwlX6fR8fqa/9o8cSzbyz5BDUkj7ZgldBNRRTLP+VyJk3xA09t7PnmtjS+Y8ttLbcZNDYosdYfkwvDxFesJ6ljqOoe/lUO8y1YhVNSpwIDAQAB");
        param.put("noticeUrl", "http://api.ishandian.cn:8088/consumer.manage.front/nr/returnCashierPage");

        Map<String, Object> map1 = getBindBankCard(param);
        String status = map1.get("status").toString();

        try {
            // Map<String, Object> map = prePay(param);
            // System.out.println("-------------" + map);

            Map<String, String> param1 = new HashMap<String, String>();
            param1.put("merchantId", "M200001523");
            param1.put("cardNo", "");
            param1.put("smsCode", "629801");
            param1.put("mobileNo", "13681486576");
            param1.put("amount", amount);
            param1.put("realName", "");
            param1.put("tradeNo", "201706091216081031610020973420");
            param1.put("paymentId", "201706091216081100210168707554");
            param1.put("memberUserId", "10010260967");
            param1.put("payChannel", "UCFPAY");
            param1.put("bankName", "中国民生银行股份有限公司");
            param1.put("bankCode", "CMBC");
            param1.put("outOrderId", "SYQS2002170210004");

            Map<String, Object> map2 = confirmPay(param);
            System.out.println("......" + map2);
        } catch (GeneralSecurityException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * Description:先锋预支付接口
     * 
     * @param objs
     * @param request
     * @return
     */
    public static Map<String, Object> prePay(Map<String, String> param)
            throws GeneralSecurityException {
        Map<String, Object> result = new HashMap<String, Object>();// 返回结果

        try {
            Map<String, String> reqMap = new HashMap<String, String>();// 使用参数
            reqMap.put("service", "MOBILE_CERTPAY_API_PREPARE_PAY");
            reqMap.put("secId", "RSA");
            reqMap.put("version", "3.0.0");
            UUID uuid = UUID.randomUUID();
            reqMap.put("reqSn", uuid.toString().replaceAll("-", "").toUpperCase());// token
            String t_paltForm = "H5";
            reqMap.put("t_platform", t_paltForm);
            reqMap.put("t_location", "null");//
            reqMap.put("t_edition", "null");//
            reqMap.put("browserName", "null");//
            reqMap.put("browserVersion", "null");
            reqMap.put("amount", param.get("amount"));// 金额
            reqMap.put("outOrderId", param.get("traceNo"));// 订单号
            reqMap.put("merchantId", param.get("merchantId"));// 商户号
            reqMap.put("userId", param.get("regId"));// 用户号
            reqMap.put("mobileNo", param.get("regId"));// 手机号
            reqMap.put("noticeUrl", param.get("noticeUrl"));// 回调url
            reqMap.put("bankCardNo", param.get("bankCardNo"));// 银行账号----------------
            reqMap.put("bankCardType", "1");
            reqMap.put("cardNo", param.get("cardNo"));// 身份证件号 ------------------
            reqMap.put("isbinding", "1");
            reqMap.put("bankCode", param.get("bankCode"));// 银行卡号CMBC
            reqMap.put("productName", "CERTPAY_API");// 产品名称
            // key
            String sign;
            try {
                sign = UcfForOnline.createSign(param.get("merchantKey"), "sign",
                        reqMap, "RSA");
                reqMap.put("sign", sign);
            } catch (CoderException e) {
                e.printStackTrace();
            }
            logger.info("--调用先锋预支付接口上送参数:{}", JSONObject.toJSONString(reqMap));
            String prePayResult = HttpUtils.sendGetRequest(
                    param.get("gateway"), reqMap, "UTF-8");
            logger.info("--调用先锋预支付接口返回结果：{}", JSONObject.toJSONString(prePayResult));
            JSONObject rs = JSON.parseObject(prePayResult);

            if (StringUtil.isEmpty(rs.getString("status"))) {
                throw new ServiceException(rs.getString("resCode"),
                        rs.getString("resMessage"));
            }
            if (!Code.XFSUCCESS.getCode().equals(rs.get("status"))) {
                throw new ServiceException(rs.getString("status"),
                        rs.getString("respMsg"));
            }
            result.put("tradeNo", rs.getString("tradeNo"));
            result.put("paymentId", rs.getString("paymentId"));
            result.put("memberUserId", rs.getString("memberUserId"));
            result.put("payChannel", rs.getString("payChannel"));
            result.put("bankName", rs.getString("bankName"));
            result.put("bankCode", rs.getString("bankCode"));
            result.put("outOrderId", rs.getString("outOrderId"));
            result.put("cardNo", param.get("cardNo"));
            result.put("bankCardNo", param.get("bankCardNo"));
            result.put("amount", param.get("amount"));
            result.put("flag", param.get("flag"));
            result.put("repayNo", param.get("repayNo"));
            result.put("regId", param.get("regId"));
            result.put("realName", param.get("realName"));
            result.put("callbackUrl", param.get("callbackUrl"));

            result.put("result_code", rs.getString("status"));
            result.put("result_msg", rs.getString("respMsg"));
            logger.info("--中阁api预支付调用先锋接口返回结果 {}", rs);
        } catch (ServiceException e) {
            result.put("result_code", e.getCode());
            result.put("result_msg", e.getMsg());
        }
        return result;
    }

    public static Map<String, Object> confirmPay(Map<String, String> param) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();// 返回结果
        Map<String, String> reqMap = new HashMap<String, String>();
        try {

            String orderId = param.get("outOrderId");
            reqMap.put("service", "MOBILE_CERTPAY_API_IMMEDIATE_PAY");
            reqMap.put("secId", "RSA");
            reqMap.put("version", "3.0.0");
            UUID uuid = UUID.randomUUID();
            reqMap.put("reqSn", uuid.toString().replaceAll("-", "")
                    .toUpperCase());
            String t_paltForm = "H5";
            reqMap.put("t_platform", t_paltForm);
            reqMap.put("t_location", "null");
            reqMap.put("t_edition", "null");
            reqMap.put("browserName", "null");
            reqMap.put("browserVersion", "null");
            reqMap.put("merchantId", param.get("merchantId")); // 商户ID
            reqMap.put("memberUserId", param.get("memberUserId")); // 会员ID
            reqMap.put("smsCode", param.get("smsCode")); // 短信验证码
            reqMap.put("mobileNo", param.get("mobileNo")); // 绑定手机号非注册号
            reqMap.put("outOrderId", param.get("outOrderId")); // 外部订单id
            reqMap.put("amount", param.get("amount")); // 金额
            reqMap.put("realName", param.get("realName")); //
            reqMap.put("cardNo", param.get("cardNo")); // 身份证号码
            reqMap.put("bankCardNo", param.get("bankCardNo")); // 银行卡号
            reqMap.put("bankCardType", "1"); // 银行卡类型
            reqMap.put("bankName", param.get("bankName")); // 银行名称
            reqMap.put("bankCode", param.get("bankCode")); // 银行编码
            reqMap.put("paymentId", param.get("paymentId")); // 支付id
            reqMap.put("tradeNo", param.get("tradeNo")); // 流水号
            reqMap.put("payChannel", param.get("payChannel")); // 支付渠道
            reqMap.put("isbinding", "1");
            String sign = UcfForOnline.createSign(param.get("merchantKey"),
                    "sign", reqMap, "RSA");
            reqMap.put("sign", sign);

            logger.info("【调用先锋确认支付】入参 :{}",
                    new Object[] {JSON.toJSONString(reqMap)});
            String payResult = HttpUtils.sendGetRequest(
                    param.get("gateway"), reqMap, "UTF-8");
            logger.info("【调用先锋确认支付】结果 :{}", payResult);
            if (payResult == null) {
                result.put("result_code", ApiReturnInfo.API_GET_FAIL_80000006.getRetCode());
                result.put("result_msg", ApiReturnInfo.API_GET_FAIL_80000006.getRetUserInfo());
                return result;
            }
            JSONObject rs = JSON.parseObject(payResult);
            if (StringUtil.isEmpty(rs.getString("status"))) {
                throw new ServiceException(rs.getString("resCode"),
                        rs.getString("resMessage"));
            }
            String pay_state = Code.I.getCode();
            String verify_state = Code.I.getCode();

            if (paySuccess(rs.getString("status"))) {
                pay_state = Code.S.getCode();
            }
            if (payFail(rs.getString("status"))) {
                pay_state = Code.F.getCode();
            }
            if (authSuccess(rs.getString("authStatus"))) {
                verify_state = Code.S.getCode();
            }
            if (!isSuccess(rs.getString("status"))) {
                throw new ServiceException(rs.getString("status"),
                        rs.getString("respMsg"));
            } else {
                result.put("result_code", ApiReturnInfo.API_SUCCESS_00000000.getRetCode());
                result.put("result_msg", "成功");
            }

        } catch (ServiceException e) {
            result.put("result_code", e.getCode());
            result.put("result_msg", e.getMsg());
        }
        return result;
    }

    /**
     * 
     * Description:解绑银行卡
     * 
     * @param objs
     * @param request
     * @return
     */
    public static Map<String, Object> unBindCard(Map<String, String> param) {
        Map<String, Object> result = new HashMap<String, Object>();// 返回结果
        Map<String, String> reqMap = new HashMap<String, String>();// 使用参数
        reqMap.put("service", "MOBILE_CERTPAY_API_UNBIND_CARD");
        reqMap.put("secId", "RSA");
        reqMap.put("version", "3.0.0");
        UUID uuid = UUID.randomUUID();
        reqMap.put("reqSn", uuid.toString().replaceAll("-", "").toUpperCase());// token
        reqMap.put("merchantId", param.get("merchantId"));// 商户号
        reqMap.put("userId", param.get("regId"));// 用户号
        reqMap.put("bankCardNo", param.get("bankCardNo"));// 银行账号----------------
        String sign = null;
        try {
            sign = UcfForOnline.createSign(param.get("merchantKey"), "sign",
                    reqMap, "RSA");
        } catch (Exception e) {
            logger.error("生成签名失败=", e.getMessage());
            throw new ServiceException("01", "生成签名失败=");
        }
        reqMap.put("sign", sign);
        // get 请求:
        String unResult = SimpleHttpUtils.httpGet("https://mapi.ucfpay.com/gateway.do", reqMap);
        JSONObject rs = JSON.parseObject(unResult);
        result.put("status", rs.getString("status"));// 00 成功
        result.put("respMsg", rs.getString("respMsg"));
        return result;
    }

    /**
     * 
     * Description:根据手机号码查询绑定信息
     * 
     * @param objs
     * @param request
     * @return
     */
    public static Map<String, Object> getBindBankCard(Map<String, String> param) {
        String userId = param.get("regId");
        Map<String, Object> result = new HashMap<String, Object>();// 返回结果
        Map<String, String> reqMap = new HashMap<String, String>();// 使用参数
        reqMap.put("service", "MOBILE_CERTPAY_GETBINDCARDS");
        reqMap.put("secId", "RSA");
        reqMap.put("version", "3.0.0");
        UUID uuid = UUID.randomUUID();
        reqMap.put("reqSn", uuid.toString().replaceAll("-", "").toUpperCase());// token
        reqMap.put("merchantId", "M200001523");// 商户号
        reqMap.put("userId", userId);// 用户号
        String sign = null;
        try {
            sign = UcfForOnline.createSign(param.get("merchantKey"), "sign",
                    reqMap, "RSA");
        } catch (Exception e) {}
        reqMap.put("sign", sign);
        // get 请求:
        String unResult = SimpleHttpUtils.httpGet("https://mapi.ucfpay.com/gateway.do", reqMap);
        JSONObject rs = JSON.parseObject(unResult);
        if (rs.get("status") != null && rs.get("status").equals("00")) {
            JSONArray bankList = rs.getJSONArray("bankList");
            result.put("bankList", bankList);
        } else {
            result.put("bankList", new ArrayList<>());
        }
        result.put("status", rs.getString("status"));// 00 成功
        result.put("respMsg", rs.getString("respMsg"));
        logger.info("调用先锋接口根据手机号查询绑定的银行卡---结束:{} ", result);
        return result;
    }

    private static boolean paySuccess(String payStatus) {
        if (("00".equals(payStatus)) || ("0".equals(payStatus))) {
            return true;
        }
        return false;
    }

    private static boolean payFail(String payStatus) {
        if (("01".equals(payStatus)) || ("1".equals(payStatus))) {
            return true;
        }
        return false;
    }

    private static boolean authSuccess(String authStatus) {
        if ("100001".equals(authStatus)) {
            return true;
        }
        return false;
    }

    private static boolean isSuccess(String status) {
        if (("00".equals(status)) || ("0".equals(status))) {
            return true;
        }
        if (("01".equals(status)) || ("1".equals(status))) {
            return true;
        }
        if (("02".equals(status)) || ("2".equals(status))) {
            return true;
        }
        return false;
    }
}
