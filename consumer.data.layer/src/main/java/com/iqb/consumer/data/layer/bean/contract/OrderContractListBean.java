package com.iqb.consumer.data.layer.bean.contract;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 
 * Description: 电子合同订单列表
 * 
 * @author guojuan
 */
public class OrderContractListBean {
    /** 订单号 **/
    private String orderId;
    /** 关联用户Id **/
    private String userId;
    /** 注册号 **/
    private String regId;
    /** 商户号 **/
    private String merchantNo;
    /** 商户简称 **/
    private String merchantShortName;
    /**
     * 业务类型(2001 以租代售新车 2002以租代售二手车 2100 抵押车 2200 质押车 1100 易安家 1000 医美 1200 旅游)
     **/
    private String bizType;
    /** 订单名称 **/
    private String orderName;
    /** 订单备注 **/
    private String orderRemark;
    /** 工作流状态 **/
    private Integer wfStatus;

    /** 风控状态(0,1,2,3,4) **/
    private Integer riskStatus;

    /** 机构代码 **/
    private String orgCode;

    /** 姓名 **/
    private String realName;

    /** 订单金额 **/
    private BigDecimal orderAmt;

    /** 合同状态 **/
    private int contractStatus;// 合同签约状态1,待手工签约的订单 2，手工签约完毕的订单 3，需电子签约订单 4,不需要电子签约订单

    /** 更新时间 **/
    private Date updateTime;

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
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

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }
    public Integer getWfStatus() {
        return wfStatus;
    }
    public void setWfStatus(Integer wfStatus) {
        this.wfStatus = wfStatus;
    }
    public Integer getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Integer riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public int getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(int contractStatus) {
        this.contractStatus = contractStatus;
    }

}
