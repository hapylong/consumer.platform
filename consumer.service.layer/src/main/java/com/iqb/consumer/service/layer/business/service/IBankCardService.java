/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午8:53:14
 * @version V1.0
 */
package com.iqb.consumer.service.layer.business.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface IBankCardService {

    /**
     * 通过用户Id查询开户绑卡信息
     * 
     * @param userId
     * @return
     */
    BankCardBean getOpenBankCardByRegId(String userId);

    /**
     * 通过商户号简拼查询商户的ID
     */
    String getMerchanIdByMerchanNo(String merchanNo);

    // 保存报告内容
    void saveReprotContent(JSONObject objs);

    Map getCodeAndKeyByMerchanNo(String merchantNo);
}
