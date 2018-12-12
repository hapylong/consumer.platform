package com.iqb.consumer.data.layer;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;
import com.iqb.consumer.data.layer.biz.paylog.PaymentLogBiz;

import junit.framework.TestCase;

public class PayLogTest extends TestCase {

    @Resource
    private PaymentLogBiz paymentLogBiz;

    @Test
    public void testGetMersPaymentLogList() {
        JSONObject objs = new JSONObject();
        // objs.put("regId", "");
        List<PaymentLogBean> list = paymentLogBiz.getMersPaymentLogList(objs);
        System.out.println(JSONObject.toJSONString(list));
    }
}
