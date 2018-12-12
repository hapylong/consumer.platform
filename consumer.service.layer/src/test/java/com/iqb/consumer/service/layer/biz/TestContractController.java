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

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class TestContractController {

    protected static final Logger logger = LoggerFactory
            .getLogger(TestContractController.class);

    @Test
    public void testMakeContract() throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("orderId", "20161015-839014");
        // param.put("bizId", "aaa");
        param.put("orgCode", "1006");
        param.put("bizType", "1001");
        String json = JSON.toJSONString(param);
        String url = "http://127.0.0.1:8080/consumer.manage.front/unIntcpt-contract/makeContract";
        // String result = HttpsClientUtil.getInstance().doPost2(url, json,
        // "utf-8");
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }

    @Test
    public void testSubmitContract() throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("orderId", "20161015-839014");
        param.put("orgCode", "1006");
        // param.put("bizId", "1");
        // param.put("bizType", "2");
        // param.put("orgCode", "3");
        String json = JSON.toJSONString(param);
        String url = "http://127.0.0.1:8080/consumer.manage.front/unIntcpt-contract/submitContract";
        // String result = HttpsClientUtil.getInstance().doPost2(url, json,
        // "utf-8");
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }
}
