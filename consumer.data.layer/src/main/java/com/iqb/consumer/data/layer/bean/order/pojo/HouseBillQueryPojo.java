package com.iqb.consumer.data.layer.bean.order.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class HouseBillQueryPojo {
    /** eatep_house_order **/
    private String channelUser; // 渠道经纪人
    private String orderNo; // 订单号
    private Date createTime; // 订单时间
    private BigDecimal realInstAmt; // 已分期金额
    private Integer applyTerm; // 借款期数
    private BigDecimal receivableAmt; // 保证金
    private String userName; // 姓名
    private String mobile; // 手机号
    private BigDecimal approvalAmt; // 核准金额 实际借借款金额
    private String businessType; // 业务类型 （数字）

    /** iqb_sys_organization_info **/
    private String orgShortName; // 渠道名称

    /** iqb_sys_dict_item **/
    private String businessName; // 业务类型 （汉字）

    /** inst_plan **/
    private Integer takePayment; // 是否上收月供
    private String planFullName; // 产品方案

    private Date sbTime;// 上标时间

    public String getChannelUser() {
        return channelUser;
    }

    public void setChannelUser(String channelUser) {
        this.channelUser = channelUser;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getRealInstAmt() {
        return realInstAmt;
    }

    public void setRealInstAmt(BigDecimal realInstAmt) {
        this.realInstAmt = realInstAmt;
    }

    public Integer getApplyTerm() {
        return applyTerm;
    }

    public void setApplyTerm(Integer applyTerm) {
        this.applyTerm = applyTerm;
    }

    public BigDecimal getReceivableAmt() {
        return receivableAmt;
    }

    public void setReceivableAmt(BigDecimal receivableAmt) {
        this.receivableAmt = receivableAmt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getApprovalAmt() {
        return approvalAmt;
    }

    public void setApprovalAmt(BigDecimal approvalAmt) {
        this.approvalAmt = approvalAmt;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getOrgShortName() {
        return orgShortName;
    }

    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(Integer takePayment) {
        this.takePayment = takePayment;
    }

    public String getPlanFullName() {
        return planFullName;
    }

    public void setPlanFullName(String planFullName) {
        this.planFullName = planFullName;
    }

    public Date getSbTime() {
        return sbTime;
    }

    public void setSbTime(Date sbTime) {
        this.sbTime = sbTime;
    }
}
