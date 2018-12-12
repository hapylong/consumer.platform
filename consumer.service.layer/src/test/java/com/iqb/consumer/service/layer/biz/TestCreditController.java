/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月19日 下午3:21:52
 * @version V1.0
 */
package com.iqb.consumer.service.layer.biz;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.iqb.consumer.service.layer.credit.CreditService;

import util.HttpClientUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class TestCreditController extends AbstractService {

    protected static final Logger logger = LoggerFactory.getLogger(TestCreditController.class);

    @Resource
    private CreditService creditService;

    @Test
    public void testGetUserPayment() throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("pageNum", "2");
        String json = JSON.toJSONString(param);
        String url = "http://127.0.0.1:8080/consumer.manage.front/credit/repayment";
        // String result = HttpsClientUtil.getInstance().doPost2(url, json, "utf-8");
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }
}
