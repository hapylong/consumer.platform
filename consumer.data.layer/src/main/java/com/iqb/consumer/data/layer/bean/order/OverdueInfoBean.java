package com.iqb.consumer.data.layer.bean.order;

import java.math.BigDecimal;

import com.iqb.consumer.data.layer.bean.BaseEntity;
import com.iqb.etep.common.utils.StringUtil;

/**
 * Description:客户违约信息
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年3月16日上午11:31:57 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class OverdueInfoBean extends BaseEntity {
    /** 订单号 **/
    private String orderId;
    /** 批次id **/
    private String batchId;
    /** 流程ID **/
    private String processId;
    /** 违约时间 **/
    private String overdueDate;
    /** 违约原因 **/
    private String overdueRemark;
    /** 总保证金 **/
    private BigDecimal sumMarginAmt;
    /** 总结算金额 **/
    private BigDecimal sumSettlement;
    /** 付款时间 **/
    private String repayDate;
    /** 流水号 **/
    private String serialNum;
    /** 结算状态 **/
    private int status;
    /** 流程状态 **/
    private int wfStatus;
    /** 结算时间 **/
    private String settlementDate;
    /** 姓名 **/
    private String realName;
    /** 手机号码 **/
    private String regId;
    /** 产品方案 **/
    private String planName;
    /** 保证金 **/
    private BigDecimal margin;
    /** 商户名称 **/
    private String merchantName;
    /** 业务类型 **/
    private String bizType;
    /** 结算金额 **/
    private BigDecimal settlementAmt;
    /** 订单金额 **/
    private BigDecimal orderAmt;
    /** 结算总笔数 **/
    private int totalNum;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getOverdueDate() {
        return overdueDate;
    }

    public void setOverdueDate(String overdueDate) {
        this.overdueDate = overdueDate;
    }

    public String getOverdueRemark() {
        return overdueRemark;
    }

    public void setOverdueRemark(String overdueRemark) {
        this.overdueRemark = overdueRemark;
    }

    public BigDecimal getSumMarginAmt() {
        return sumMarginAmt;
    }

    public void setSumMarginAmt(BigDecimal sumMarginAmt) {
        this.sumMarginAmt = sumMarginAmt;
    }

    public BigDecimal getSumSettlement() {
        return sumSettlement;
    }

    public void setSumSettlement(BigDecimal sumSettlement) {
        this.sumSettlement = sumSettlement;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(int wfStatus) {
        this.wfStatus = wfStatus;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
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

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public BigDecimal getSettlementAmt() {
        return settlementAmt;
    }

    public void setSettlementAmt(BigDecimal settlementAmt) {
        this.settlementAmt = settlementAmt;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public boolean checkOverdueInfo() {
        if (StringUtil.isNull(batchId) || StringUtil.isNull(repayDate) || StringUtil.isNull(serialNum)) {
            return false;
        } else {
            return true;
        }
    }
}
