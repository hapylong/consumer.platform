/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月19日 下午3:21:52
 * @version V1.0
 */
package com.iqb.consumer.service.layer.biz;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import util.HttpClientUtil;

public class TestBusinessController extends AbstractService {

    protected static final Logger logger = LoggerFactory.getLogger(TestBusinessController.class);

    @Test
    public void testGetOrderList() throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("regId", "");
        // param.put("merchName", "syqs");
        param.put("riskStatus", "2");
        // param.put("startTime", "2016-09-02 00:32:01");
        // param.put("endTime", "2016-09-08 00:52:15");
        // param.put("userName", "二手车");
        String json = JSON.toJSONString(param);
        String url = "http://127.0.0.1:8080/consumer.manage.front/business/getOrderList";
        // String result = HttpsClientUtil.getInstance().doPost2(url, json, "utf-8");
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }

    @Test
    public void testGetOrderInfo() throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("orderId", "20160913-696503");
        String json = JSON.toJSONString(param);
        String url = "http://127.0.0.1:8080/consumer.manage.front/business/getOrderInfo";
        // String result = HttpsClientUtil.getInstance().doPost2(url, json, "utf-8");
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }

    @Test
    public void testGetPreOrderList() throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("regId", "");
        param.put("merchName", "syqs");
        // param.put("riskStatus", "2");
        param.put("startTime", "2016-09-02 00:32:01");
        param.put("endTime", "2016-09-08 00:52:15");
        // param.put("userName", "二手车");
        String json = JSON.toJSONString(param);
        String url = "http://127.0.0.1:8080/consumer.manage.front/business/getPreOrderList";
        // String result = HttpsClientUtil.getInstance().doPost2(url, json, "utf-8");
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }

    @Test
    public void testGetPlanList() throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        // param.put("merchName", "syqs");
        // param.put("riskStatus", "2");
        // param.put("userName", "二手车");
        String json = JSON.toJSONString(param);
        String url = "http://127.0.0.1:8080/consumer.manage.front/business/getPlanList";
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }

}
