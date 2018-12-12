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
 * 2017年8月17日下午5:46:09 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.asset.allocation.assetinfo.bean;

import java.math.BigDecimal;

/**
 * @author haojinlong
 * 
 */
public class HouseOrderBean {
    private long userId; // 用户id
    private String mobile; // 用户手机号码
    private String orderNo; // 订单号
    private String projectName; // 项目名称
    private int applyTerm; // 分期期数
    private String bizType; // 业务类型
    private String wfStatus; // 工作流状态
    private int houseId; // 房屋id
    private String channelCode; // 渠道code
    private int source; // 分配来源
    private int planId; // 计划id
    private int deadline;
    private String userName; // 用户姓名
    private String idNo; // 用户身份证号码
    private String reserveMobile; // 用户预留手机号码
    private String bankCard; // 用户银行卡号
    private String bankName; // 用户银行卡开户行
    private int projectId;
    private BigDecimal applyamt;// 借款金额
    private int checkTime;// 审核通过时间
    private String creditorName;// 债权人姓名
    private String creditorIdNo;// 债权人身份证号
    private String creditorBankNo;// 债权人银行卡号
    private String creditorBankName;// 债权人开户银行
    private String creditorPhone;// 债权人手机号
    private String guaranteeCorporationName;// 担保公司
    private String guaranteeCorporationCorName;// 担保法人
    private String merchantId;// 商户编号
    private String subBizType;// 子业务类型

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getApplyTerm() {
        return applyTerm;
    }

    public void setApplyTerm(int applyTerm) {
        this.applyTerm = applyTerm;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(String wfStatus) {
        this.wfStatus = wfStatus;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReserveMobile() {
        return reserveMobile;
    }

    public void setReserveMobile(String reserveMobile) {
        this.reserveMobile = reserveMobile;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getApplyamt() {
        return applyamt;
    }

    public void setApplyamt(BigDecimal applyamt) {
        this.applyamt = applyamt;
    }

    public int getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(int checkTime) {
        this.checkTime = checkTime;
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

    public String getCreditorPhone() {
        return creditorPhone;
    }

    public void setCreditorPhone(String creditorPhone) {
        this.creditorPhone = creditorPhone;
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSubBizType() {
        return subBizType;
    }

    public void setSubBizType(String subBizType) {
        this.subBizType = subBizType;
    }

}
