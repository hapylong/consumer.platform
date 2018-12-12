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
 * 2018年7月13日下午3:17:00 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstOrderLawBean {
    /** caseId **/
    private String caseId;
    /** 订单号 **/
    private String orderId;
    /** 案由 **/
    private String reason;
    /** 是否原告 **/
    private String accuserFlag;
    /** 诉讼请求 **/
    private String claimRequest;
    /** 立案标识 **/
    private String registerFlag;
    /** 立案方 **/
    private String register;
    /** 缴费标识 **/
    private String payFlag;
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
    /** 到达法务时间 **/
    private String createTime;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAccuserFlag() {
        return accuserFlag;
    }

    public void setAccuserFlag(String accuserFlag) {
        this.accuserFlag = accuserFlag;
    }

    public String getClaimRequest() {
        return claimRequest;
    }

    public void setClaimRequest(String claimRequest) {
        this.claimRequest = claimRequest;
    }

    public String getRegisterFlag() {
        return registerFlag;
    }

    public void setRegisterFlag(String registerFlag) {
        this.registerFlag = registerFlag;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
