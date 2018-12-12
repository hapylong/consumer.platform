/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月14日 下午2:32:19
 * @version V1.0
 */
package com.iqb.consumer.service.layer.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.bank.BankCardTypeBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.bean.xfpay.PayInfoBean;
import com.iqb.consumer.service.layer.xfpay.XFPayService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-init-test.xml", "classpath:spring-config-test.xml"})
public class TestXFPayService {

    @Resource
    private XFPayService xfPayService;

    @Test
    public void testInsertPayInfo() {
        PayInfoBean payInfoBean = new PayInfoBean(1, "2", "农行银行", "6226021456256222312", "1232", "认证");
        xfPayService.insertPayInfo(payInfoBean);
    }

    @Test
    public void testGetByOrgId() {
        List<PayInfoBean> list = xfPayService.getByOrgId(1);
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    public void testGetByCardNo() {
        xfPayService.getByCardNo("362204198703287412", "1");
    }

    @Test
    public void testGetAllBankType() {
        List<BankCardTypeBean> list = xfPayService.getAllBankType();
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    public void testGetUserInfo() {
        UserBean ub = xfPayService.getUserInfo(20);
        System.out.println(ub.getRealName() + "---" + ub.getRealName());
    }

    @Test
    public void testGetPayInfoBeanById() {
        Map<String, Object> orgMap = new HashMap<String, Object>();
        orgMap.put("id", 5);
        orgMap.put("userId", 1);
        PayInfoBean pib = xfPayService.getByIdAndOrgId(orgMap);
        System.out.println(pib.getBankType());
    }

    @Test
    public void testUnBindCardStatus() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", 5);
        params.put("status", 0);
        xfPayService.unBindCardStatus(params);
    }

    @Test
    public void testUpdateBindCardInfo() {
        PayInfoBean payInfoBean = new PayInfoBean();
        payInfoBean.setId(62L);
        payInfoBean.setRealName("小三");
        payInfoBean.setMobileNo(null);
        payInfoBean.setCardNo(null);
        payInfoBean.setBankNo(null);
        xfPayService.updateBindCardInfo(payInfoBean);
    }

    @Test
    public void testDelBindCardInfo() {
        xfPayService.delBindCardInfo(62);
    }

    @Test
    public void testInsertPaymentLog() {
        String text =
                "{\"tranTime\":\"2016-09-20 12:15:06\",\"tradeNo\":\"201609201213401031610006658522\",\"bankName\":\"中国建设银行股份有限公司\",\"merchantId\":\"M200001523\",\"orderStatus\":\"00\",\"bankCardNo\":\"6236683090001169886\",\"bankId\":\"CCB\",\"outOrderId\":\"20160920-678715_yianjia\"}";
        Map<String, Object> params = JSONObject.parseObject(text);
        Map<String, String> lastParams = new HashMap<String, String>();
        Set<Entry<String, Object>> set = params.entrySet();
        for (Entry<String, Object> e : set) {
            lastParams.put(e.getKey(), (String) e.getValue());
        }
        insertQuickPayLog(lastParams);
    }

    private void insertQuickPayLog(Map<String, String> params) {
        params.put("merchantNo", params.get("merchantId"));
        String outOrderId = (String) params.get("outOrderId");
        String orderId = outOrderId.substring(0, 15);
        params.put("remark", "快捷支付代偿");
        params.put("orderId", orderId);
        params.put("orderNo", orderId);
        // 查询订单信息
        Map<String, Object> orderParam = new HashMap<String, Object>();
        orderParam.put("orderId", orderId);
        OrderBean ob = xfPayService.getOrderInfo(orderParam);
        // 查询用户信息
        UserBean ub = xfPayService.getUserInfo(Long.parseLong(ob.getUserId()));
        params.put("regId", ub.getRegId());
        xfPayService.insertPaymentLog(params);
    }

    /**
     * 修改支付状态
     */
    @Test
    public void testUpdatePreStatus() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderNo", "20160926-571473");// 订单号
        params.put("riskStatus", 0);// 通过
        params.put("preAmountStatus", 1);// 已支付
        xfPayService.updatePreStatusByNO(params);
    }

    @Test
    public void testQueryByOrderId() {
        List<PaymentLogBean> plb = xfPayService.getPayLogByOrderId("20161014-671263", null);
    }

    @Test
    public void testGetMersPaymentLogList() {
        JSONObject objs = new JSONObject();
        objs.put("regId", "");
        PageInfo<PaymentLogBean> list = xfPayService.getMersPaymentLogList(objs);
        System.out.println(JSONObject.toJSONString(list));
    }
}
