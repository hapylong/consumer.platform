package com.iqb.consumer.batch.data.pojo;

import java.util.HashMap;
import java.util.Map;

import com.iqb.etep.common.utils.StringUtil;

/**
 * 
 * Description: 结算中心 代扣接口请求消息
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月20日    adam       1.0        1.0 Version 
 * </pre>
 */
public class SettlementCenterBuckleRequestMessage {
    public static final String TYPE_REFUND = "0"; // 还款

    private String businessChannel; // 业务渠道代码（按照结算平台中业务渠道编码上送）

    /** inst_user **/
    private String customerName; // 客户名称
    private String idNum; // 证件号码
    /** inst_bankcard **/
    private String bankNo; // 银行卡号
    private String bankName; // 银行名称 （按照结算平台中，银行行号管理中获取上送银行名称）
    /** inst_orderinfo **/
    private String phone; // 手机号码
    private String businessType;// 业务类型
    private String branchName; // 支行名称
    private String orderAmount; // 订单金额 （单位元，例如：1.00）
    private String tradeType; // 交易类型 （0：还款；1：首付款划扣；2：放款；）
    private String tradeNo; // 业务单据号
    private String notifyUrl; // 业务渠道通知返回地址 （订单完成后，返回业务渠道系统的通知地址）

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessChannel() {
        return businessChannel;
    }

    public void setBusinessChannel(String businessChannel) {
        this.businessChannel = businessChannel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Object check() {
        if (StringUtil.isEmpty(businessChannel)) {
            return "业务渠道编码不能为空";
        }

        if (StringUtil.isEmpty(bankName)) {
            return "银行名称不能为空";
        }

        if (StringUtil.isEmpty(bankNo)) {
            return "银行卡号不能为空";
        }

        if (StringUtil.isEmpty(customerName)) {
            return "客户名称不能为空";
        }

        if (StringUtil.isEmpty(idNum)) {
            return "证件号码不能为空";
        }

        if (StringUtil.isEmpty(tradeNo)) {
            return "业务交易号不能为空";
        }

        if (StringUtil.isEmpty(tradeType)) {
            return "交易类型不能为空";
        }

        if (StringUtil.isEmpty(orderAmount)) {
            return "订单金额不能为空";
        }

        if (StringUtil.isEmpty(phone)) {
            return "手机号不能为空";
        }

        if (StringUtil.isEmpty(notifyUrl)) {
            return "返回通知地址不能为空";
        }
        return true;
    }

    public Map<String, Object> convertMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("businessChannel", businessChannel);
        map.put("customerName", customerName);
        // FINANCE-2617 中阁推送至结算平台的信息中，增加“业务类型”项
        map.put("businessType", businessType);
        map.put("bankNo", bankNo);
        map.put("bankName", bankName);
        map.put("branchName", branchName);
        map.put("orderAmount", orderAmount);
        map.put("tradeType", tradeType);
        map.put("tradeNo", tradeNo);
        map.put("idNum", idNum);
        map.put("phone", phone);
        map.put("notifyUrl", notifyUrl);
        return map;
    }

    @Override
    public String toString() {
        return "SettlementCenterBuckleRequestMessage [businessChannel=" + businessChannel + ", customerName="
                + customerName + ", idNum=" + idNum + ", bankNo=" + bankNo + ", bankName=" + bankName + ", phone="
                + phone + ", branchName=" + branchName + ", orderAmount=" + orderAmount + ", tradeType=" + tradeType
                + ", tradeNo=" + tradeNo + ", notifyUrl=" + notifyUrl + "]";
    }

}
