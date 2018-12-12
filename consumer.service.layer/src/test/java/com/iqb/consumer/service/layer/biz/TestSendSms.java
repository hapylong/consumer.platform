/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月19日 下午3:21:52
 * @version V1.0
 */
package com.iqb.consumer.service.layer.biz;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.service.layer.sms.SmsService;

import util.HttpClientUtil;

public class TestSendSms extends AbstractService {

    protected static final Logger logger = LoggerFactory.getLogger(TestSendSms.class);

    @Resource
    private SmsService smsService;

    @Test
    public void queryBill() throws Exception {
        JSONObject param = new JSONObject();
        param.put("status", "2");
        Object result = smsService.sendSmsByStatus(param);
        System.out.println("result:" + result);
    }

    @Test
    public void testSendSms() throws Exception {
        //
        // String json =
        // "{\"flag\":\"1\",\"list\":[{\"regId\":\"15117923307\",\"currTerm\":3,\"instalTerms\":24,\"stmtDate\":\"20170109\",\"lastDate\":\"20170114\",\"currRepayAmtSumIn\":0,\"currLatefeeIn\":0,\"status\":2,\"lateDate\":0,\"merchCode\":\"cdoy\"},{\"regId\":\"18515262757\",\"currTerm\":2,\"instalTerms\":24,\"stmtDate\":\"20170109\",\"lastDate\":\"20170114\",\"currRepayAmtSumIn\":0,\"currLatefeeIn\":0,\"status\":2,\"lateDate\":0,\"merchCode\":\"syqs\"}]}";
        String json =
                "{\"flag\":\"1\",\"list\":[{\"regId\":\"15117923307\",\"currTerm\":2,\"instalTerms\":24,\"stmtDate\":\"20170109\",\"lastDate\":\"20170114\",\"currRepayAmtSumIn\":6661,\"currLatefeeIn\":66.6,\"status\":3,\"lateDate\":20170214,\"merchCode\":\"syqs\"}]}";
        // String json =
        // "{\"flag\":\"2\",\"list\":[{\"orderId\":\"20160907-643708\",\"userId\":\"11\",\"merchId\":\"19\",\"orderTime\":\"1473259818\",\"orderName\":\"LlGh\",\"orderAmt\":\"211111.00\",\"orderItems\":\"44\",\"preAmount\":\"293670.18\",\"preAmountStatus\":\"0\",\"orderRem\":null,\"clrDate\":\"20160907\",\"orgTranceNo\":null,\"orderNo\":\"20160907-630519\",\"status\":\"1\",\"riskStatus\":4,\"regId\":\"18515262757\",\"margin\":\"21111.10\",\"downPayment\":\"21111.10\",\"serviceFee\":\"21111.10\",\"finalPayment\":\"0.00\",\"qrCodeId\":null,\"takePayment\":0,\"feeYear\":0,\"monthInterest\":24095,\"planId\":\"245\",\"chargeWay\":0,\"feeAmount\":230336.88,\"version\":1,\"createTime\":1473259818000,\"updateTime\":1473260923000,\"merchName\":\"松原全盛\",\"userName\":\"莫愁\",\"planShortName\":null},{\"orderId\":\"20160907-891172\",\"userId\":\"11\",\"merchId\":\"19\",\"orderTime\":\"1473260437\",\"orderName\":\"VvGg\",\"orderAmt\":\"210000.00\",\"orderItems\":\"44\",\"preAmount\":\"292124.70\",\"preAmountStatus\":\"0\",\"orderRem\":null,\"clrDate\":\"20160907\",\"orgTranceNo\":null,\"orderNo\":\"20160907-661938\",\"status\":\"1\",\"riskStatus\":4,\"regId\":\"15117923307\",\"margin\":\"21000.00\",\"downPayment\":\"21000.00\",\"serviceFee\":\"21000.00\",\"finalPayment\":\"0.00\",\"qrCodeId\":null,\"takePayment\":0,\"feeYear\":0,\"monthInterest\":22001,\"planId\":\"245\",\"chargeWay\":0,\"feeAmount\":229124.7,\"version\":1,\"createTime\":1473260437000,\"updateTime\":1473260826000,\"merchName\":\"松原全盛\",\"userName\":\"莫愁\",\"planShortName\":null}]}";
        // String json =
        // "{\"flag\":\"2\",\"list\":[{\"orderId\":\"20160907-891172\",\"userId\":\"11\",\"merchId\":\"19\",\"orderTime\":\"1473260437\",\"orderName\":\"VvGg\",\"orderAmt\":\"210000.00\",\"orderItems\":\"44\",\"preAmount\":\"292124.70\",\"preAmountStatus\":\"0\",\"orderRem\":null,\"clrDate\":\"20160907\",\"orgTranceNo\":null,\"orderNo\":\"20160907-661938\",\"status\":\"1\",\"riskStatus\":4,\"regId\":\"15117923307\",\"margin\":\"21000.00\",\"downPayment\":\"21000.00\",\"serviceFee\":\"21000.00\",\"finalPayment\":\"0.00\",\"qrCodeId\":null,\"takePayment\":0,\"feeYear\":0,\"monthInterest\":22001,\"planId\":\"245\",\"chargeWay\":0,\"feeAmount\":229124.7,\"version\":1,\"createTime\":1473260437000,\"updateTime\":1473260826000,\"merchName\":\"松原全盛\",\"userName\":\"莫愁\",\"planShortName\":null}]}";
        String url = "http://127.0.0.1:8080/consumer.manage.front/SMS/sendSms";
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }
}
