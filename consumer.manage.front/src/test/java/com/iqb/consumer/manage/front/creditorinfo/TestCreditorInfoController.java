package com.iqb.consumer.manage.front.creditorinfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpClientUtil;

import com.alibaba.fastjson.JSON;
import com.iqb.consumer.manage.front.AbstractController;

public class TestCreditorInfoController extends AbstractController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 测试回调生成车秒贷项目信息
     * 
     * @throws Exception
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Test
    public void testCopyOrderInfo() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", "SYQS2002170503002X");
        String json = JSON.toJSONString(params);
        String result = HttpClientUtil.httpPost(BASEURL + "creditorInfo/copyOrderInfo", json);
        logger.info("返回结果:{}", result);
    }
}
