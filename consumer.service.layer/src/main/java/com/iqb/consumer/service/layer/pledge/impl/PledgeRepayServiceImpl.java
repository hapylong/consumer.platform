package com.iqb.consumer.service.layer.pledge.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.utils.encript.RSAUtils;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.biz.pledge.PledgeRepayManager;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.consumer.service.layer.pledge.PledgeRepayService;
import com.iqb.etep.common.utils.https.SimpleHttpUtils;

@Service
public class PledgeRepayServiceImpl implements PledgeRepayService {
    protected static final Logger logger = LoggerFactory.getLogger(PledgeRepayServiceImpl.class);
    @Autowired
    private PledgeRepayManager pledgeRepayManager;

    @Autowired
    private DictService dictService;

    @Autowired
    private ConsumerConfig consumerConfig;

    public final int INVALID_REQUEST = -1;
    public final int UNKNOWN_REPAY_TYPE = -2;
    public final int DB_NOT_FOUND = -3;
    public final int DB_INVALID_ENTITY = -4;
    public final int ENCODE_DATA_EXCEPTION = -5;
    public final int RESPONSE_ERROR_MSG = -6;
    public final int SUCCESS = 0;

    @Override
    public Object repayByType(JSONObject requestMessage) {
        Integer flag = requestMessage.getInteger("flag"); // 1,正常 2 提前全部结清
        if (flag == null) {
            return INVALID_REQUEST;
        }
        switch (flag) {
            case 1:
                return repayNormal(requestMessage);
            case 2:
                return repayAll(requestMessage);
            default:
                return UNKNOWN_REPAY_TYPE;
        }
    }

    private Object repayNormal(JSONObject requestMessage) {
        String orderId = requestMessage.getString("orderId"); // 订单号
        Integer repayNo = requestMessage.getInteger("repayNo") + 1; // 期数 爱钱帮定义 0 为 第一期 ，以此类推
        BigDecimal amt = requestMessage.getBigDecimal("amt"); // 还款金额
        Integer timestamp = requestMessage.getInteger("timestamp"); // 还款时间
        if (StringUtil.isEmpty(orderId) || repayNo == null || amt == null || timestamp == null) {
            return INVALID_REQUEST;
        }
        /** DATA PREPARE **/
        List<Map<String, Object>> c2 = new LinkedList<Map<String, Object>>();
        Map<String, Object> p2 = new HashMap<String, Object>();
        p2.put("repayNo", repayNo); //
        p2.put("amt", amt);
        c2.add(p2);
        InstOrderInfoEntity ioie = pledgeRepayManager.getOrderInfoEntityByOid(orderId);
        if (ioie == null) {
            return DB_NOT_FOUND;
        }
        if (StringUtil.isEmpty(ioie.getBizType())) {
            return DB_INVALID_ENTITY;
        }
        String openId = dictService.getOpenIdByBizType(ioie.getBizType());
        List<Map<String, Object>> container = new LinkedList<Map<String, Object>>();
        Map<String, Object> pojo = new HashMap<String, Object>();
        pojo.put("orderId", orderId);
        pojo.put("openId", StringUtil.isEmpty(openId) ? "" : openId);
        pojo.put("repayModel", "normal");
        pojo.put("regId", StringUtil.isEmpty(ioie.getRegId()) ? "" : ioie.getRegId());
        pojo.put("merchantNo", StringUtil.isEmpty(ioie.getMerchantNo()) ? "" : ioie.getMerchantNo());
        pojo.put("tradeNo", "");
        pojo.put("repayDate", formatTimeByLong(timestamp));
        pojo.put("sumAmt", amt);
        pojo.put("repayList", c2);
        container.add(pojo);

        /** ENCODE **/
        Map<String, Object> data = encodeData(JSONObject.toJSONString(container));
        if (data == null) {
            return ENCODE_DATA_EXCEPTION;
        }
        /** HTTP **/
        String response = SimpleHttpUtils.
                httpPost(
                        consumerConfig.getFinanceBillRefundUrl(),
                        data);
        return responseCode(response, requestMessage.getInteger("flag"));
    }

    private Object responseCode(String response, Integer flag) {
        try {
            JSONObject jo = JSONObject.parseObject(response);
            String retCode = jo.getString("retCode");
            if ("success".equals(retCode)) {
                return SUCCESS;
            }
            else {
                return jo;
            }
        } catch (Exception e) {
            logger.error("PledgeRepayServiceImpl[responseCode] flag [{}] response [{}] ", flag, response);
            return RESPONSE_ERROR_MSG;
        }
    }

    private Object repayAll(JSONObject requestMessage) {
        String orderId = requestMessage.getString("orderId"); // 订单号
        Integer repayNo = requestMessage.getInteger("repayNo") + 1; // 期数
        BigDecimal amt = requestMessage.getBigDecimal("amt"); // 还款金额
        Integer timestamp = requestMessage.getInteger("timestamp"); // 还款时间
        if (StringUtil.isEmpty(orderId) || repayNo == null || amt == null || timestamp == null) {
            return INVALID_REQUEST;
        }
        /** DATA PREPARE **/
        InstOrderInfoEntity ioie = pledgeRepayManager.getOrderInfoEntityByOid(orderId);
        if (ioie == null) {
            return DB_NOT_FOUND;
        }
        if (StringUtil.isEmpty(ioie.getBizType())) {
            return DB_INVALID_ENTITY;
        }
        String openId = dictService.getOpenIdByBizType(ioie.getBizType());

        List<Map<String, Object>> container = new LinkedList<Map<String, Object>>();
        Map<String, Object> pojo = new HashMap<String, Object>();
        pojo.put("orderId", orderId);
        pojo.put("openId", StringUtil.isEmpty(openId) ? "" : openId);
        pojo.put("repayModel", "all");
        pojo.put("regId", StringUtil.isEmpty(ioie.getRegId()) ? "" : ioie.getRegId());
        pojo.put("merchantNo", StringUtil.isEmpty(ioie.getMerchantNo()) ? "" : ioie.getMerchantNo());
        pojo.put("tradeNo", "");
        pojo.put("repayDate", formatTimeByLong(timestamp));
        pojo.put("sumAmt", amt);
        pojo.put("repayList", new ArrayList<Object>());
        container.add(pojo);

        /** ENCODE **/
        Map<String, Object> data = encodeData(JSONObject.toJSONString(container));
        if (data == null) {
            return ENCODE_DATA_EXCEPTION;
        }
        /** HTTP **/
        String response = SimpleHttpUtils.
                httpPost(
                        consumerConfig.getFinanceBillRefundUrl(),
                        data);

        return responseCode(response, requestMessage.getInteger("flag"));
    }

    private Map<String, Object> encodeData(String json) {
        try {
            byte[] data = json.getBytes();
            String PRIVATE_KEY = consumerConfig.getCommonPrivateKey();
            // 私钥加密
            byte[] encodedData = RSAUtils.encryptByPrivateKey(data, PRIVATE_KEY);
            String data1 = encodeBase64(encodedData);
            String sign = RSAUtils.sign(encodedData, PRIVATE_KEY);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("data", data1);
            params.put("sign", sign);
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encodeBase64(source), "UTF-8");
    }

    private static final String format = "yyyyMMdd";

    private static String formatTimeByLong(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = new Date();
        d.setTime(timestamp * 1000);
        return sdf.format(d);
    }

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        long l = c.getTimeInMillis() / 1000;
        System.out.println(l);
        System.out.println((int) l);
        System.err.println(formatTimeByLong((c.getTimeInMillis() / 1000)));
    }

}
