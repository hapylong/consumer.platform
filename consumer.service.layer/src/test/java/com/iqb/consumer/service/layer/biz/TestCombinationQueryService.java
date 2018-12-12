/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月18日 上午10:33:38
 * @version V1.0
 */
package com.iqb.consumer.service.layer.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import util.HttpClientUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.reqmoney.RequestMoneyBean;
import com.iqb.consumer.data.layer.bean.wf.CombinationQueryBean;
import com.iqb.consumer.service.layer.wfservice.CombinationQueryService;
import com.iqb.consumer.service.layer.xfpay.XFPayService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class TestCombinationQueryService extends AbstractService {

    @Autowired
    private CombinationQueryService combinationQueryService;
    @Resource
    private XFPayService xfPayService;

    @Test
    public void test() {
        System.out.println(combinationQueryService);
        CombinationQueryBean cqb = combinationQueryService.getByOrderId("20160908-320211");
        System.out.println(cqb);
    }

    @Test
    public void testGetPlanByMerNo() {
        List<PlanBean> list = combinationQueryService.getPlanByMerNo("syqs");
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    public void testQueryOrder() {
        Map<String, Object> orderParam = new HashMap<String, Object>();
        orderParam.put("orderId", "20160902-871805");
        OrderBean ob = combinationQueryService.selectOne(orderParam);
        System.out.println(ob);
    }

    @Test
    public void testReqMoney() throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        // param.put("merchName", "syqs");
        // param.put("riskStatus", "2");
        // param.put("userName", "二手车");
        String json = JSON.toJSONString(param);
        String url = "http://localhost/consumer.manage.front/workFlow/getAllReqMoney";
        String result = HttpClientUtil.httpPost(url, json);
        System.out.println("result:" + result);
    }

    @Test
    public void testInsertReqMoney() {
        RequestMoneyBean rmb = new RequestMoneyBean();
        rmb.setOrderId("20161008-123564");
        rmb.setApplyItems(36);
        rmb.setApplyTime("20161008122030");
        rmb.setSourcesFunding("10");
        rmb.setStatus(0);
        rmb.setRemark("测试");
        combinationQueryService.insertReqMoney(rmb);
    }
}
