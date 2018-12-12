package com.iqb.consumer.data.layer.bean.afterLoan;

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
 * 2018年7月4日下午3:34:22 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class OutSourceOrderBean {
    /** 案件id **/
    private String caseId;
    /** 订单号 **/
    private String orderId;
    /** 手机号码 **/
    private String regId;
    /** 姓名 **/
    private String realName;
    /** 商户名称 **/
    private String merchantName;
    /** 订单金额 **/
    private BigDecimal orderAmt;
    /** 总期数 **/
    private int orderItems;
    /** 月供 **/
    private BigDecimal monthInterest;
    /** 品牌车系 **/
    private String orderName;
    /** 车牌号 **/
    private String plate;
    /** 车架号 **/
    private String carNo;
    /** 发动机号 **/
    private String engine;
    /** gps状态 **/
    private int gpsStatus;
    /** 外包进度 **/
    private String operatorRemark;
    /** 外包时间 **/
    private String processTime;
    /** 是否转法务 1 是 2 否 **/
    private int operateFlag;
    /** 操作人 **/
    private String operator;
    /** 商户id **/
    private String merchantId;
    /** 已还期数 **/
    private int hasRepayNo;
    /** 车辆状态 **/
    private String status;
    /** 是否原告 **/
    private String accuserFlag;

    /** 立案方 **/
    private String register;
    /** 委托机构 **/
    private String associatedAgency;
    /** 委托律师 **/
    private String mandatoryLawyer;
    /** 受理机构 **/
    private String acceptOrg;
    /** 诉讼费 **/
    private BigDecimal legalCost;
    /** 律师费 **/
    private BigDecimal barFee;
    /** 立案时间 **/
    private String registerDate;
    /** 立案备注 **/
    private String registerRemark;
    /** 是否庭前调解 **/
    private String composeFlag;
    /** 和解标识 **/
    private String compromiseFlag;
    /** 调解备注 **/
    private String composeRemark;
    /** 实际账单金额 **/
    private String receivedAmt;
    /** 实际涨到时间 **/
    private String receivedDate;
    /** 案件编号 **/
    private String caseNo;
    /** 案件来源 1 贷后转入 2 法务新增 **/
    private String caseSource;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(int gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public String getOperatorRemark() {
        return operatorRemark;
    }

    public void setOperatorRemark(String operatorRemark) {
        this.operatorRemark = operatorRemark;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public int getOperateFlag() {
        return operateFlag;
    }

    public void setOperateFlag(int operateFlag) {
        this.operateFlag = operateFlag;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public int getHasRepayNo() {
        return hasRepayNo;
    }

    public void setHasRepayNo(int hasRepayNo) {
        this.hasRepayNo = hasRepayNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccuserFlag() {
        return accuserFlag;
    }

    public void setAccuserFlag(String accuserFlag) {
        this.accuserFlag = accuserFlag;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getAssociatedAgency() {
        return associatedAgency;
    }

    public void setAssociatedAgency(String associatedAgency) {
        this.associatedAgency = associatedAgency;
    }

    public String getMandatoryLawyer() {
        return mandatoryLawyer;
    }

    public void setMandatoryLawyer(String mandatoryLawyer) {
        this.mandatoryLawyer = mandatoryLawyer;
    }

    public String getAcceptOrg() {
        return acceptOrg;
    }

    public void setAcceptOrg(String acceptOrg) {
        this.acceptOrg = acceptOrg;
    }

    public BigDecimal getLegalCost() {
        return legalCost;
    }

    public void setLegalCost(BigDecimal legalCost) {
        this.legalCost = legalCost;
    }

    public BigDecimal getBarFee() {
        return barFee;
    }

    public void setBarFee(BigDecimal barFee) {
        this.barFee = barFee;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getRegisterRemark() {
        return registerRemark;
    }

    public void setRegisterRemark(String registerRemark) {
        this.registerRemark = registerRemark;
    }

    public String getComposeFlag() {
        return composeFlag;
    }

    public void setComposeFlag(String composeFlag) {
        this.composeFlag = composeFlag;
    }

    public String getCompromiseFlag() {
        return compromiseFlag;
    }

    public void setCompromiseFlag(String compromiseFlag) {
        this.compromiseFlag = compromiseFlag;
    }

    public String getComposeRemark() {
        return composeRemark;
    }

    public void setComposeRemark(String composeRemark) {
        this.composeRemark = composeRemark;
    }

    public int getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(int orderItems) {
        this.orderItems = orderItems;
    }

    public String getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(String receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getCaseSource() {
        return caseSource;
    }

    public void setCaseSource(String caseSource) {
        this.caseSource = caseSource;
    }

}
