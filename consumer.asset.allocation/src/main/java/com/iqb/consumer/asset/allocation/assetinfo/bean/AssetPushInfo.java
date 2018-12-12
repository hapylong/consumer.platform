package com.iqb.consumer.asset.allocation.assetinfo.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class AssetPushInfo implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int projectId;// 项目Id
    private String orderId;// 订单Id
    private String projectCode;// 项目编号
    private String projectName;// 项目名称
    private String idCard;// 实际借款人身份证号
    private String guarantee;// 担保公司名称
    private String guaranteeName;// 担保公司法人
    private String userName;// 实际借款人姓名
    private String loanName;// 债权人
    private String loanIdcard;// 债权人身份证号
    private int checkTime;// 审核通过时间
    private BigDecimal applyAmt;// 借款金额
    private String projectType = "10";// 项目类型 以租代售
    private int deadline;// 标的结束时间
    private int applyItems;// 申请期数
    private String receive;// 接收平台
    private String cardNum;// 银行卡号
    private String bankName;// 银行名称
    private String phone;// 手机号
    private String merchantNo;// 商户号
    private int responseId;// 推送返回Id
    private int isWithholding;// 是否放款代扣
    private int isPublic;// 是否公开
    private int isPushff;// 是否推送饭饭
    private int planId; // 分期计划ID
    private String orderName;// 订单名称
    private String bizType; // 业务类型
    private String userId; // 用户id
    private int wfStatus; // 工作流状态
    private int riskStatus;
    private String merchantNoS;
    private long creditorId;// 债权人id
    // 修改by chengzhen 2018年3月6日 16:10:29
    private String cf_requestmoney_id;// 对应保存过后的cf_requestmoney表ID

    private int applyInstIDay;// 借款天数
    private String txDate;// 交易日期
    private String txTime;// 交易时间
    private String merchantCode; // 机构编码
    private String bizTypeName;// 业务类型名称
    private String subBizType;// 子业务类型

    public String getCf_requestmoney_id() {
        return cf_requestmoney_id;
    }

    public void setCf_requestmoney_id(String cf_requestmoney_id) {
        this.cf_requestmoney_id = cf_requestmoney_id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public String getGuaranteeName() {
        return guaranteeName;
    }

    public void setGuaranteeName(String guaranteeName) {
        this.guaranteeName = guaranteeName;
    }

    public int getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(int checkTime) {
        this.checkTime = checkTime;
    }

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public int getApplyItems() {
        return applyItems;
    }

    public void setApplyItems(int applyItems) {
        this.applyItems = applyItems;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoanIdcard() {
        return loanIdcard;
    }

    public void setLoanIdcard(String loanIdcard) {
        this.loanIdcard = loanIdcard;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public int getIsWithholding() {
        return isWithholding;
    }

    public void setIsWithholding(int isWithholding) {
        this.isWithholding = isWithholding;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getIsPushff() {
        return isPushff;
    }

    public void setIsPushff(int isPushff) {
        this.isPushff = isPushff;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(int wfStatus) {
        this.wfStatus = wfStatus;
    }

    public int getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(int riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getMerchantNoS() {
        return merchantNoS;
    }

    public void setMerchantNoS(String merchantNoS) {
        this.merchantNoS = merchantNoS;
    }

    public long getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(long creditorId) {
        this.creditorId = creditorId;
    }

    public int getApplyInstIDay() {
        return applyInstIDay;
    }

    public void setApplyInstIDay(int applyInstIDay) {
        this.applyInstIDay = applyInstIDay;
    }

    public String getTxDate() {
        return txDate;
    }

    public void setTxDate(String txDate) {
        this.txDate = txDate;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getBizTypeName() {
        return bizTypeName;
    }

    public void setBizTypeName(String bizTypeName) {
        this.bizTypeName = bizTypeName;
    }

    public String getSubBizType() {
        return subBizType;
    }

    public void setSubBizType(String subBizType) {
        this.subBizType = subBizType;
    }

}
