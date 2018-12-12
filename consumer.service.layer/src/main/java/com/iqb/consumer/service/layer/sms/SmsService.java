package com.iqb.consumer.service.layer.sms;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.service.layer.dto.repay.UserPaymentInfoDto;

/**
 * 短信服务
 */
public interface SmsService {

    // 还款发送短信
    @SuppressWarnings("rawtypes")
    List<String> SendSms4Pay(List<Map> list) throws Exception;

    // 预支付发送短信
    @SuppressWarnings("rawtypes")
    List<String> SendSms4Order(List<Map> list) throws Exception;

    // 根据status发送短信内容 1.已还款 2.未还款 3.已逾期
    Object sendSmsByStatus(JSONObject objs) throws Exception;

    // 划扣失败短信通知
    @SuppressWarnings("rawtypes")
    public List<UserPaymentInfoDto> SendSms4Deduct(List<Map> list) throws Exception;
}
