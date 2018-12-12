/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午3:39:35
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.paylog;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;
import com.iqb.consumer.data.layer.bean.pay.ChatFinanceToRefundRequestMessage;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class PaymentLogBean extends BaseEntity {

    // 用户注册号
    private String regId;
    // 金额
    private long amount;
    // 交易时间
    private Date tranTime;
    // 银行卡号
    private String bankCardNo;
    // 银行简称
    private String bankId;
    // 银行名称
    private String bankName;
    // 订单状态
    private String orderStatus;
    // 创建时间
    private Date createTime;
    // 商户号
    private String merchantNo;
    // 订单id：若有则为预支付的订单id，没有则为账单
    private String orderId;
    // 流水ID
    private String tradeNo;
    // 先锋outOrderId
    private String outOrderId;
    // 1,手机端支付 2,线下平账 3,代偿 4,代扣
    private int flag;
    // 备注
    private String remark;

    /**************** 前台需要数据 *************/
    // 客户真实姓名
    private String realName;
    // 商户简称
    private String merchantShortName;

    /*************************************/

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getTranTime() {
        return tranTime;
    }

    public void setTranTime(Date tranTime) {
        this.tranTime = tranTime;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void create(ChatFinanceToRefundRequestMessage cft) {
        regId = cft.getRegId();
        amount = (cft.getSumAmt().multiply(new BigDecimal(100)).intValue());
        tranTime = new Date();
        bankCardNo = cft.getBankCardNo();
        bankId = cft.getBankCode();
        bankName = cft.getBankName();
        orderStatus = cft.getRiskStatus() + "";
        createTime = new Date();
        merchantNo = cft.getMerchantNo();
        orderId = cft.getOrderId();
        tradeNo = cft.getTradeNo();
        // outOrderId = ;
        flag = 42;
        remark = "";
    }
}
