/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午8:47:22
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.riskinfo.LocalRiskInfoBean;

/**
 * 用户卡信息
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface BankCardBeanDao {

    /**
     * 通过用户Id查询开户绑卡信息
     * 
     * @param userId
     * @return
     */
    BankCardBean getOpenBankCardByRegId(String userId);

    /**
     * 保存银行卡信息
     * 
     * @param bankCardBean
     * @return
     */
    long saveBankCard(BankCardBean bankCardBean);

    /**
     * 
     * Description:根据userId修改银行卡号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月18日
     */
    long updateBankCardNoByUserId(@Param("userId") String userId, @Param("bankCardNo") String bankCardNo,
            @Param("bankName") String bankName);

    /**
     * 
     * Description:批量插入银行卡信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月12日
     */
    long batchSaveBankCard(List<BankCardBean> list);

    /**
     * 根据用户id 卡状态查询卡信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月1日
     */
    BankCardBean selectBankCardInfoByUserId(@Param("userId") String userId, @Param("status") String status);

    /**
     * 根据商户号简拼查询对应的ID
     * 
     * @Description: TODO
     * @param @param merchanNo
     * @param @return
     * @return Long
     * @author chengzhen
     * @data 2018年3月26日 10:52:59
     */

    String getMerchanIdByMerchanNo(String merchanNo);

    void saveReprotContent(JSONObject objs);

    LocalRiskInfoBean selectLocalInfoByNoAndReportName(@Param("tradeNo") String tradeNo,
            @Param("reportType") String reportType);

    void updateLocalRiskInfoBean(LocalRiskInfoBean localBean);

    Map getCodeAndKeyByMerchanNo(String merchantNo);
}
