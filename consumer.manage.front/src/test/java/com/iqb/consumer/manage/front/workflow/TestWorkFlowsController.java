package com.iqb.consumer.manage.front.workflow;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.iqb.consumer.manage.front.test.penalty.derate.util.HttpClientUtil;

public class TestWorkFlowsController {

    @Test
    public void testAddAssessPrice() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", "SHXD2100170717001");
        params.put("assessPrice", "1111");
        params.put("gpsPrice", "2222");
        params.put("orderAmt", "3333");
        String json = JSON.toJSONString(params);
        String result =
                HttpClientUtil.httpPost(
                        "http://localhost:9080/consumer.manage.front" + "/workFlow/addzhiyaAssessPrice", json);
        System.out.println(result);
    }

    @Test
    public void testgetAllotDetailByOrderId() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", "RYQC2001170703001");
        String json = JSON.toJSONString(params);
        String result =
                HttpClientUtil.httpPost(
                        "http://localhost:8080/consumer.manage.front" + "/workFlow/getAllotDetailByOrderId", json);
        System.out.println(result);
    }
}
