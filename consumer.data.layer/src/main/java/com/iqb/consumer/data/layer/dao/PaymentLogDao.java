/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午3:47:11
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface PaymentLogDao {

    /**
     * 插入支付日志
     * 
     * @param paymentLogBean
     * @return
     */
    long insertPaymentLog(Map<String, String> params);

    /**
     * 查询总金额
     * 
     * @param params
     * @return
     */
    Integer selSumAmount(JSONObject objs);

    /**
     * 通过流水ID查询支付日志
     * 
     * @param tradeNo
     * @return
     */
    PaymentLogBean getPayLogByTradeNo(@Param("tradeNo") String tradeNo);

    /**
     * 通过订单Id查询
     * 
     * @param orderId
     * @return
     */
    List<PaymentLogBean> getPayLogByOrderId(@Param("orderId") String orderId, @Param("flag") String flag);

    /**
     * 根据商户名称、开始日期、结束日期、订单号、手机号、还款金额 开始、还款金额 结束获取支付日志列表
     * 
     * @param
     * @return
     */
    List<PaymentLogBean> getMersPaymentLogList(JSONObject objs);

    /**
     * 根据商户名称、开始日期、结束日期、订单号、手机号、还款金额 开始、还款金额 结束获取条数
     * 
     * @param merchName,startDate,endDate,orderId,regId,startAmount,endAmount
     * @return
     */
    public int getMersPaymentLogNum(JSONObject objs);

    /**
     * 根据商户名称、开始日期、结束日期、订单号、手机号、还款金额 开始、还款金额 结束获取金额
     * 
     * @param merchName,startDate,endDate,orderId,regId,startAmount,endAmount
     * @return
     */
    public Long getMersPaymentLogAmt(JSONObject objs);

    void persistLog(PaymentLogBean plb);

    /**
     * 
     * Description: 通过tradeNo查询
     * 
     * @param
     * @return PaymentLogBean
     * @throws @Author adam Create Date: 2017年6月27日 上午10:43:47
     */
    PaymentLogBean getPayLogByTNo(String tradeNo);
}
