/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月13日 上午10:20:36
 * @version V1.0
 */
package com.iqb.consumer.service.layer.xfpay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.bank.BankCardTypeBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.bean.xfpay.PayInfoBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface XFPayService {

    /**
     * 查询用户信息
     * 
     * @param map
     * @return
     */
    UserBean getUserInfo(long id);

    /**
     * 预支付逻辑处理
     * 
     * @param orderId
     * @param amount
     */
    void prePay(String orderId, BigDecimal amount);

    /**
     * 退租支付逻辑处理
     * 
     * @param orderId
     * @param amount
     */
    void settlePay(SettleApplyBean settleApplyBean, String orderId, BigDecimal amount, Map<String, String> params,
            String merchantNo);

    /**
     * 查询订单信息
     * 
     * @param params
     * @return
     */
    OrderBean getOrderInfo(Map<String, Object> params);

    /**
     * 不存在就插入
     */
    void insertIfNotExists(PayInfoBean payInfoBean);

    /**
     * 
     * @param id
     * @return
     */
    PayInfoBean getByIdAndOrgId(Map<String, Object> params);

    /**
     * 根据管理后台维护唯一orgId查询
     * 
     * @param orgId
     * @return
     */
    List<PayInfoBean> getByOrgId(int orgId);

    /**
     * 通过身份证查询PayInfoBean
     * 
     * @param cardNo
     * @return
     */
    PayInfoBean getByCardNo(String cardNo, String orgId);

    /**
     * 添加快捷支付卡
     * 
     * @param payInfoBean
     * @return
     */
    long insertPayInfo(PayInfoBean payInfoBean);

    /**
     * 查询所有卡类型
     * 
     * @return
     */
    List<BankCardTypeBean> getAllBankType();

    /**
     * 修改支付卡信息状态
     * 
     * @param params
     */
    void unBindCardStatus(Map<String, Object> params);

    /**
     * 添加支付日志
     * 
     * @param paymentLogBean
     * @return
     */
    long insertPaymentLog(Map<String, String> params);

    /**
     * 修改订单信息
     * 
     * @param orderBean
     * @return
     */
    int updateOrderInfo(OrderBean orderBean);

    /**
     * 通过流水查询支付日志
     * 
     * @param tradeNo
     * @return
     */
    PaymentLogBean getPayLogByTradeNo(String tradeNo);

    /**
     * 通过订单号查询支付日志
     * 
     * @param orderId
     * @return
     */
    List<PaymentLogBean> getPayLogByOrderId(String orderId, String flag);

    /**
     * 通过orderNo修改订单状态和预付款状态
     * 
     * @param params
     * @return
     */
    int updatePreStatusByNO(Map<String, Object> params);

    /**
     * 修改支付卡信息
     * 
     * @param payInfoBean
     * @return
     */
    int updateBindCardInfo(PayInfoBean payInfoBean);

    /**
     * 删除卡信息操作
     * 
     * @param bankId
     */
    void delBindCardInfo(int bankId);

    /**
     * 根据商户名称、开始日期、结束日期、订单号、手机号、还款金额 开始、还款金额 结束获取支付日志列表
     * 
     * @param
     */
    PageInfo<PaymentLogBean> getMersPaymentLogList(JSONObject objs);

    /**
     * 根据商户名称、开始日期、结束日期、订单号、手机号、还款金额 开始、还款金额 结束获取条数、金额
     * 
     * @param merchName,startDate,endDate,orderId,regId,startAmount,endAmount
     * @return
     */
    public Map<String, Object> getMersPaymentLogNumAmt(JSONObject objs);

    /**
     * 根据商户名称、开始日期、结束日期、订单号、手机号、还款金额 开始、还款金额 结束导出支付日志列表
     * 
     * @param
     */
    void getMersPaymentLogListForSave(JSONObject objs, HttpServletResponse response);

    /**
     * 查询总金额
     * 
     * @param params
     * @return
     */
    Integer selSumAmount(JSONObject objs);
}
