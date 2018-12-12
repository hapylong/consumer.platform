package com.iqb.consumer.manage.front.carstatus;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.iqb.consumer.manage.front.test.penalty.derate.util.HttpClientUtil;

public class TestCarStatusCenterController {

    private static final String BASEURL = "http://localhost:8080/consumer.manage.front/unIntcpt-carstatus";
    protected static final Logger logger = LoggerFactory.getLogger(TestCarStatusCenterController.class);

    /**
     * 
     * Description:
     * {"success":1,"retDatetype":1,"retCode":"00000000","retUserInfo":"处理成功","retFactInfo"
     * :"处理成功","iqbResult"
     * :{"result":{"pageNum":1,"pageSize":10,"size":1,"orderBy":null,"startRow":1,
     * "endRow":1,"total":
     * 1,"pages":1,"list":[{"orderId":"CDHTC2002170630007","regId":"15311464963","orderAmt"
     * :60000.00,
     * "orderItems":36,"monthInterest":1765.33,"orderName":"测试测试","createTime":1498808792000
     * ,"realName"
     * :"常雪剑","carNo":"254354","plate":"2543546","id":null,"status":null}],"firstPage":1,"prePage"
     * :0,"nextPage":0,"lastPage":1,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,
     * "hasNextPage":false,"navigatePages":8,"navigatepageNums":[1]}}}
     * 
     * @param
     * @return void
     * @throws @Author adam Create Date: 2017年7月11日 上午11:33:35
     */
    @Test
    public void testcgetInfoList() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("channal", 2);
        params.put("status", 1);
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/cget_info_list", json);
        logger.info("返回结果:{}", result);
    }

    /**
     * 
     * Description:
     * {"success":1,"retDatetype":1,"retCode":"00000000","retUserInfo":"处理成功","retFactInfo"
     * :"处理成功","iqbResult":{"result":"success"}}
     * 
     * @param
     * @return void
     * @throws @Author adam Create Date: 2017年7月11日 下午1:50:58
     */
    @Test
    public void testpersisitImcit() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", "CDHTC2002170630008");
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/persisit_imci", json);
        logger.info("返回结果:{}", result);
    }

    @Test
    public void testcgetInfo() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/cget_info", json);
        logger.info("返回结果:{}", result);
    }
}
