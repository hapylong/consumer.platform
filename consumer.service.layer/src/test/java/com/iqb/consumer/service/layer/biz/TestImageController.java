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
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.service.layer.business.service.IOrderService;

import util.HttpClientUtil;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class TestImageController extends AbstractService {

    protected static final Logger logger = LoggerFactory.getLogger(TestImageController.class);

    @Resource
    private IOrderService orderService;
    @Resource
    private OrderBiz orderBiz;

    @Test
    public void test111() {
        JSONObject objs = new JSONObject();
        objs.put("orderId", "20160926-112233");
        OrderBean orderBean = orderBiz.selectOne(objs);
        orderBean.setChargeWay(1);
        orderBean.setTakePayment(1);
        orderBiz.updateOrderInfo(orderBean);
        objs.put("chargeWay", "0");
        objs.put("takePayment", "0");
        orderService.updateOrderInfo(objs);
    }

    @Test
    public void testInsert() throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        param.put("orderId", "20160926-112233");
        param.put("imgType", "4");
        // param.put("imgName", "dhahsdjkwhkjdhuw");
        // param.put("imgPath", "/opt/aaa/bbb/dhahsdjkwhkjdhuw");

        String json = JSON.toJSONString(param);
        String url = "http://127.0.0.1:8080/consumer.manage.front/image/uploadImage";
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }

    @Test
    public void testGetImageList() throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        // param.put("imgType", "4");
        param.put("orderId", "20160926-112233");
        String json = JSON.toJSONString(param);
        String url = "http://127.0.0.1:8080/consumer.manage.front/image/getImageList";
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }
}
