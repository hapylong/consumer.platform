package com.iqb.consumer.data.layer.bean.order;

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
 * 2018年5月10日上午11:43:14 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class OrderAmtDetailBean {
    /** 订单号 **/
    private String orderId;
    /** 订单金额 **/
    private BigDecimal orderAmt;
    /** 期数 **/
    private int orderItems;
    /** 订单时间 **/
    private String createTime;
    /** 手机号 **/
    private String regId;
    /** 姓名 **/
    private String realName;
    /** 商户名 **/
    private String merchantName;
    /** 产品方案 **/
    private String planFullName;
    /** gps金额 **/
    private BigDecimal gpsAmt;
    /** 商业险 **/
    private BigDecimal businessTaxAmt = BigDecimal.ZERO.setScale(2);
    /** 税费价格 **/
    private BigDecimal taxAmt = BigDecimal.ZERO.setScale(2);
    /** 交强险 **/
    private BigDecimal insAmt = BigDecimal.ZERO.setScale(2);
    /** 其他费用 **/
    private BigDecimal otherAmt = BigDecimal.ZERO.setScale(2);

    /** 支付GPS **/
    private BigDecimal preGpsAmt = BigDecimal.ZERO.setScale(2);
    /** 支付商业险 **/
    private BigDecimal preBusinessTaxAmt = BigDecimal.ZERO.setScale(2);
    /** 购置税 **/
    private BigDecimal preTaxAmt = BigDecimal.ZERO.setScale(2);
    /** 车商服务费 **/
    private BigDecimal serverAmt = BigDecimal.ZERO.setScale(2);
    /** 支付交强险 **/
    private BigDecimal preRiskAmt = BigDecimal.ZERO.setScale(2);
    /** 评估管理费 **/
    private BigDecimal assessMsgAmt = BigDecimal.ZERO.setScale(2);
    /** 考察费 **/
    private BigDecimal inspectionAmt = BigDecimal.ZERO.setScale(2);
    /** 支付其他费用 **/
    private BigDecimal preOtherAmt = BigDecimal.ZERO.setScale(2);

    /** 车秒贷gps费用 **/
    private BigDecimal carGpsAmt = BigDecimal.ZERO.setScale(2);
    /** 车秒贷商业险 **/
    private BigDecimal carBusinessTaxAmt = BigDecimal.ZERO.setScale(2);
    /** 车秒贷交强险 **/
    private BigDecimal carRiskAmt = BigDecimal.ZERO.setScale(2);
    /** 车秒贷购置税 **/
    private BigDecimal carTaxAmt = BigDecimal.ZERO.setScale(2);
    /** 车秒贷服务费 **/
    private BigDecimal carserverAmt = BigDecimal.ZERO.setScale(2);
    /** 车秒贷其他费用 **/
    private BigDecimal carOtherAmt = BigDecimal.ZERO.setScale(2);

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPlanFullName() {
        return planFullName;
    }

    public void setPlanFullName(String planFullName) {
        this.planFullName = planFullName;
    }

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public BigDecimal getBusinessTaxAmt() {
        return businessTaxAmt;
    }

    public void setBusinessTaxAmt(BigDecimal businessTaxAmt) {
        this.businessTaxAmt = businessTaxAmt;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public BigDecimal getInsAmt() {
        return insAmt;
    }

    public void setInsAmt(BigDecimal insAmt) {
        this.insAmt = insAmt;
    }

    public BigDecimal getOtherAmt() {
        return otherAmt;
    }

    public void setOtherAmt(BigDecimal otherAmt) {
        this.otherAmt = otherAmt;
    }

    public BigDecimal getPreGpsAmt() {
        return preGpsAmt;
    }

    public void setPreGpsAmt(BigDecimal preGpsAmt) {
        this.preGpsAmt = preGpsAmt;
    }

    public BigDecimal getPreBusinessTaxAmt() {
        return preBusinessTaxAmt;
    }

    public void setPreBusinessTaxAmt(BigDecimal preBusinessTaxAmt) {
        this.preBusinessTaxAmt = preBusinessTaxAmt;
    }

    public BigDecimal getPreTaxAmt() {
        return preTaxAmt;
    }

    public void setPreTaxAmt(BigDecimal preTaxAmt) {
        this.preTaxAmt = preTaxAmt;
    }

    public BigDecimal getServerAmt() {
        return serverAmt;
    }

    public void setServerAmt(BigDecimal serverAmt) {
        this.serverAmt = serverAmt;
    }

    public BigDecimal getPreRiskAmt() {
        return preRiskAmt;
    }

    public void setPreRiskAmt(BigDecimal preRiskAmt) {
        this.preRiskAmt = preRiskAmt;
    }

    public BigDecimal getAssessMsgAmt() {
        return assessMsgAmt;
    }

    public void setAssessMsgAmt(BigDecimal assessMsgAmt) {
        this.assessMsgAmt = assessMsgAmt;
    }

    public BigDecimal getInspectionAmt() {
        return inspectionAmt;
    }

    public void setInspectionAmt(BigDecimal inspectionAmt) {
        this.inspectionAmt = inspectionAmt;
    }

    public BigDecimal getPreOtherAmt() {
        return preOtherAmt;
    }

    public void setPreOtherAmt(BigDecimal preOtherAmt) {
        this.preOtherAmt = preOtherAmt;
    }

    public BigDecimal getCarGpsAmt() {
        return carGpsAmt;
    }

    public void setCarGpsAmt(BigDecimal carGpsAmt) {
        this.carGpsAmt = carGpsAmt;
    }

    public BigDecimal getCarBusinessTaxAmt() {
        return carBusinessTaxAmt;
    }

    public void setCarBusinessTaxAmt(BigDecimal carBusinessTaxAmt) {
        this.carBusinessTaxAmt = carBusinessTaxAmt;
    }

    public BigDecimal getCarTaxAmt() {
        return carTaxAmt;
    }

    public void setCarTaxAmt(BigDecimal carTaxAmt) {
        this.carTaxAmt = carTaxAmt;
    }

    public BigDecimal getCarRiskAmt() {
        return carRiskAmt;
    }

    public void setCarRiskAmt(BigDecimal carRiskAmt) {
        this.carRiskAmt = carRiskAmt;
    }

    public BigDecimal getCarserverAmt() {
        return carserverAmt;
    }

    public void setCarserverAmt(BigDecimal carserverAmt) {
        this.carserverAmt = carserverAmt;
    }

    public BigDecimal getCarOtherAmt() {
        return carOtherAmt;
    }

    public void setCarOtherAmt(BigDecimal carOtherAmt) {
        this.carOtherAmt = carOtherAmt;
    }
}
