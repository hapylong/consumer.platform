package com.iqb.consumer.data.layer.bean.cheegu;

import com.iqb.consumer.data.layer.bean.BaseEntity;

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
 * 2018年5月28日上午10:22:14 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class InstCarPeccancyBean extends BaseEntity {
    /** 订单号 **/
    private String orderId;
    /** 车架号 **/
    private String vin;
    /** 商户名称 **/
    private String merchantName;
    /** 姓名 **/
    private String realName;
    /** 手机号码 **/
    private String regId;
    /** 订单时间 **/
    private String orderTime;
    /** 总期数 **/
    private int orderItems;
    /** 车牌号 **/
    private String licenseNo;
    /** 发动机号 **/
    private String engineNumber;
    /** 总扣分 **/
    private String totalDegree;
    /** 总罚款 **/
    private int totalPeccancyAmt;
    /** 车辆状态 **/
    private String carStatus;
    /** 违章处理时间 **/
    private String processDate;
    /** 处理状态 **/
    private String processStatus;
    /** 违章状态 **/
    private String status;
    /** 当前期数 **/
    private int curItems;
    /** 账单状态 **/
    private String billStatus;
    /** 发送时间 **/
    private String sendTime;
    /** 车e估订单号 **/
    private String cheeguOrderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(int orderItems) {
        this.orderItems = orderItems;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getTotalDegree() {
        return totalDegree;
    }

    public void setTotalDegree(String totalDegree) {
        this.totalDegree = totalDegree;
    }

    public int getTotalPeccancyAmt() {
        return totalPeccancyAmt;
    }

    public void setTotalPeccancyAmt(int totalPeccancyAmt) {
        this.totalPeccancyAmt = totalPeccancyAmt;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCurItems() {
        return curItems;
    }

    public void setCurItems(int curItems) {
        this.curItems = curItems;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getCheeguOrderId() {
        return cheeguOrderId;
    }

    public void setCheeguOrderId(String cheeguOrderId) {
        this.cheeguOrderId = cheeguOrderId;
    }

}
