package com.iqb.consumer.data.layer.bean.ownerloan;

import java.math.BigDecimal;

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
 * 2017年11月13日上午10:59:15 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class OwnerLoanBaseInfoBean {
    /** 订单号 **/
    private String orderId;
    /** 订单名称 **/
    private String orderName;
    /** 订单金额 **/
    private BigDecimal orderAmt;
    /** 分期期数 **/
    private int orderItems;
    /** 状态 **/
    private int status;
    /** 业务类型名称 **/
    private String bizTypeName;
    /** 姓名 **/
    private String realName;
    /** 手机号码 **/
    private String regId;
    /** 身份证号码 **/
    private String idNo;
    /** 银行卡号 **/
    private String bankCardNo;
    /** 商户名称 **/
    private String merchantName;
    /** 车牌号 **/
    private String plate;
    /** 车龄 **/
    private int carAge;
    /** 品牌 **/
    private String carBrand;
    /** 车辆型号 **/
    private String carDetail;
    /** 风控信息 **/
    private String checkInfo;
    /** 性别 **/
    private String sex;
    /** 年龄 **/
    private int age;

    /** GPS安装日期 **/
    private String gpsInstDate;
    /** 设备号 **/
    private String gpsDeviceNum;
    /** 安装位置 **/
    private String gpsDeviceAddress;
    /** GPS安装套数 **/
    private int gpsNum;
    /** GPS安装费 **/
    private BigDecimal gpsAmt;
    /** GPS流量费 **/
    private BigDecimal gpsTrafficFee;
    /** 保证金 **/
    private BigDecimal margin;
    /** 预付款 **/
    private BigDecimal preAmt;
    /** 服务费 **/
    private BigDecimal serviceFee;
    /** 月供 **/
    private BigDecimal monthInterest;
    /** 预付款状态 **/
    private int preAmtStatus;
    /** 风控信息 **/
    private CheckInfoBean checkInfoBean;
    /** 商户号 **/
    private String merchantNo;
    /** 业务类型 **/
    private String bizType;
    /** 风控返回信息 **/
    private String riskRetRemark;
    /** 风控状态 **/
    private String riskStatus;
    /** 风控信息 **/
    private String riskMessage;
    /** 开户行名称 **/
    private String bankName;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public int getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(int orderItems) {
        this.orderItems = orderItems;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName;
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

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getCarAge() {
        return carAge;
    }

    public void setCarAge(int carAge) {
        this.carAge = carAge;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarDetail() {
        return carDetail;
    }

    public void setCarDetail(String carDetail) {
        this.carDetail = carDetail;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

    public CheckInfoBean getCheckInfoBean() {
        return checkInfoBean;
    }

    public void setCheckInfoBean(CheckInfoBean checkInfoBean) {
        this.checkInfoBean = checkInfoBean;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getGpsInstDate() {
        return gpsInstDate;
    }

    public void setGpsInstDate(String gpsInstDate) {
        this.gpsInstDate = gpsInstDate;
    }

    public String getGpsDeviceNum() {
        return gpsDeviceNum;
    }

    public void setGpsDeviceNum(String gpsDeviceNum) {
        this.gpsDeviceNum = gpsDeviceNum;
    }

    public String getGpsDeviceAddress() {
        return gpsDeviceAddress;
    }

    public void setGpsDeviceAddress(String gpsDeviceAddress) {
        this.gpsDeviceAddress = gpsDeviceAddress;
    }

    public int getGpsNum() {
        return gpsNum;
    }

    public void setGpsNum(int gpsNum) {
        this.gpsNum = gpsNum;
    }

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public BigDecimal getGpsTrafficFee() {
        return gpsTrafficFee;
    }

    public void setGpsTrafficFee(BigDecimal gpsTrafficFee) {
        this.gpsTrafficFee = gpsTrafficFee;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public int getPreAmtStatus() {
        return preAmtStatus;
    }

    public void setPreAmtStatus(int preAmtStatus) {
        this.preAmtStatus = preAmtStatus;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getRiskRetRemark() {
        return riskRetRemark;
    }

    public void setRiskRetRemark(String riskRetRemark) {
        this.riskRetRemark = riskRetRemark;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getRiskMessage() {
        return riskMessage;
    }

    public void setRiskMessage(String riskMessage) {
        this.riskMessage = riskMessage;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

}
