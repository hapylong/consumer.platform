package com.iqb.consumer.data.layer.bean.settlementresult;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年1月9日下午2:50:40 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class BillWithHoldBean {
    private long id;
    /** 电话号码 **/
    private String phone;
    /** 业务类型 **/
    private String businessType;
    /** 客户姓名 **/
    private String customerName;
    /** 身份证号码 **/
    private String idNum;
    /** 开户银行卡号 **/
    private String bankNo;
    /** 开户银行 **/
    private String bankName;
    /** 业务渠道代码（按照结算平台中业务渠道编码上送） **/
    private String businessChannel;
    /** 支行名称 **/
    private String branchName;
    /** 订单金额 **/
    private String orderAmount;
    /** 交易类型 （0：还款；1：首付款划扣；2：放款；） **/
    private String tradeType;
    /** 业务单据号 **/
    private String tradeNo;
    /** 业务渠道通知返回地址 （订单完成后，返回业务渠道系统的通知地址） **/
    private String notifyUrl;

    public Map<String, Object> convertMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("businessChannel", businessChannel);
        map.put("customerName", customerName);
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
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

    public String getBusinessChannel() {
        return businessChannel;
    }

    public void setBusinessChannel(String businessChannel) {
        this.businessChannel = businessChannel;
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

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
