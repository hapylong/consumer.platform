package com.iqb.consumer.service.layer.common;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;

@Service
public class CalculateService {

    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private EncryptUtils encryptUtils;

    @SuppressWarnings("unchecked")
    public Map<String, BigDecimal> calculateAmt(PlanBean planBean, BigDecimal orderAmt) {
        // 调用账务系统计算金额
        Map<String, Object> sourceMap = new HashMap<String, Object>();
        sourceMap.put("instPlan", planBean);
        sourceMap.put("amt", orderAmt);
        String resultStr =
                SimpleHttpUtils.httpPost(consumerConfig.getCalculateAmtUrl(),
                        encryptUtils.encrypt(JSONObject.toJSONString(sourceMap)));
        if (resultStr != null) {
            Map<String, Object> result = JSONObject.parseObject(resultStr);
            if ("success".equals(result.get("retCode"))) {
                return (Map<String, BigDecimal>) result.get("result");
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
