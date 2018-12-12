package com.iqb.consumer.service.layer.pledge.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.Pledge.PledgeInfoBean;
import com.iqb.consumer.data.layer.biz.pledge.PledgeInquiryBiz;
import com.iqb.consumer.service.layer.pledge.IPledgeInquiryService;

import jodd.util.StringUtil;

/**
 * 
 * @author 质押车逻辑处理
 * 
 */
@Service
public class PledgeInquiryServiceImpl implements IPledgeInquiryService {

    @Autowired
    private PledgeInquiryBiz pledgeInquiryBiz;

    /**
     * 2017-05-23 质押车车辆评估查询接口变更，新需求将不再关联表inst_orderinfo
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年5月23日
     */
    @Override
    public Object getPledgeOrderInfo(JSONObject objs) {
        PledgeInfoBean bean = pledgeInquiryBiz.getPledgeOrderInfo(objs);
        /*
         * String orderName = bean.getOrderName(); String[] names = orderName.split("-"); if
         * (names.length > 1) { bean.setBrand(names[0]); bean.setModel(names[1]); }
         */
        return bean;
    }

    @Override
    @Transactional
    public void updateOrderInfo(JSONObject object) {
        // pledgeInquiryBiz.updateOrderInfo(object);
        pledgeInquiryBiz.updatePledgeInfo(object);
    }

    @Override
    public void updateOrderInfoRiskStatus(Map<String, String> params) {
        pledgeInquiryBiz.updateOrderInfoRiskStatus(params);
    }

    @Override
    public Object getPledgeOrderInfoByOid(String orderId) throws Exception {
        if (StringUtil.isEmpty(orderId)) {
            throw new Exception("orderId 为空");
        }
        String uuid = pledgeInquiryBiz.getUuidByOid(orderId);
        if (StringUtil.isEmpty(uuid)) {
            throw new Exception("uuid 为空");
        }
        JSONObject jo = new JSONObject();
        jo.put("orderId", uuid);
        PledgeInfoBean bean = pledgeInquiryBiz.getPledgeOrderInfo(jo);
        return bean;
    }

}
