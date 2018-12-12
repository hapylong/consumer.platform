/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年8月16日下午1:54:04 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.asset.allocation.assetinfo.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author haojinlong
 * 
 */
public class HouseAssetBean {
    private String channelName; // 渠道名称
    private String projectName; // 项目名称
    private String orderNo; // 订单号
    private String userName; // 借款人
    private BigDecimal applyMoney; // 借款金额
    private BigDecimal restAmt; // 剩余金额
    private int applyTerm; // 期数
    private Date allotDate; // 资产分配时间
    private String planName; // 产品方案
    private Date orderDate; // 订单日期
    private String source; // 资金来源
    private String idCard; // 借款人身份证号码
    private String creditorName; // 债权人姓名
    private String creditorIdNo; // 债权人身份证号码
    private String creditorBankNo; // 债权人银行卡号
    private String creditorBankName; // 债权人开户行
    private String creditorPhone; // 债权人手机号码
    private String bizTypeName; // 业务类型
    private String mobile; // 借款人手机号码
    private int planId;
    private String guaranteeCorporationName; // 担保人姓名
    private String guaranteeCorporationCorName; // 担保人公司名称

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }

    public BigDecimal getRestAmt() {
        return restAmt;
    }

    public void setRestAmt(BigDecimal restAmt) {
        this.restAmt = restAmt;
    }

    public int getApplyTerm() {
        return applyTerm;
    }

    public void setApplyTerm(int applyTerm) {
        this.applyTerm = applyTerm;
    }

    public Date getAllotDate() {
        return allotDate;
    }

    public void setAllotDate(Date allotDate) {
        this.allotDate = allotDate;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorIdNo() {
        return creditorIdNo;
    }

    public void setCreditorIdNo(String creditorIdNo) {
        this.creditorIdNo = creditorIdNo;
    }

    public String getCreditorBankNo() {
        return creditorBankNo;
    }

    public void setCreditorBankNo(String creditorBankNo) {
        this.creditorBankNo = creditorBankNo;
    }

    public String getCreditorBankName() {
        return creditorBankName;
    }

    public void setCreditorBankName(String creditorBankName) {
        this.creditorBankName = creditorBankName;
    }

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName;
    }

    public String getCreditorPhone() {
        return creditorPhone;
    }

    public void setCreditorPhone(String creditorPhone) {
        this.creditorPhone = creditorPhone;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getGuaranteeCorporationName() {
        return guaranteeCorporationName;
    }

    public void setGuaranteeCorporationName(String guaranteeCorporationName) {
        this.guaranteeCorporationName = guaranteeCorporationName;
    }

    public String getGuaranteeCorporationCorName() {
        return guaranteeCorporationCorName;
    }

    public void setGuaranteeCorporationCorName(String guaranteeCorporationCorName) {
        this.guaranteeCorporationCorName = guaranteeCorporationCorName;
    }

}
