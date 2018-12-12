package com.iqb.consumer.data.layer.bean.afterLoan;

import java.math.BigDecimal;

import com.iqb.consumer.data.layer.bean.BaseEntity;

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
 * 2018年7月11日下午6:21:13 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class InstManageCarReceiveBean extends BaseEntity {
    private String caseId;
    /** 订单号 **/
    private String orderId;
    /** 回款金额 **/
    private BigDecimal shouldReceivedAmt;
    /** 回款时间 **/
    private String shouldReceivedDate;
    /** 佣金 **/
    private BigDecimal commision;
    /** 拖车费用 **/
    private BigDecimal trailerAmt;
    /** 停车费 **/
    private BigDecimal stopAmt;
    /** 其他费用 **/
    private BigDecimal otherAmt;
    /** 费用合计 **/
    private BigDecimal totalAmt;
    /** 实际到账金额 **/
    private BigDecimal receivedAmt;
    /** 实际到账时间 **/
    private String receivedDate;
    /** 全额回款标志 1 是 2 否 **/
    private String receivedFlag;
    /** 回款备注 **/
    private String receivedRemark;
    /** 处理途径 1 转外包 2 转法务 3 非外包非法务 **/
    private String processMethod;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getShouldReceivedAmt() {
        return shouldReceivedAmt;
    }

    public void setShouldReceivedAmt(BigDecimal shouldReceivedAmt) {
        this.shouldReceivedAmt = shouldReceivedAmt;
    }

    public String getShouldReceivedDate() {
        return shouldReceivedDate;
    }

    public void setShouldReceivedDate(String shouldReceivedDate) {
        this.shouldReceivedDate = shouldReceivedDate;
    }

    public BigDecimal getCommision() {
        return commision;
    }

    public void setCommision(BigDecimal commision) {
        this.commision = commision;
    }

    public BigDecimal getTrailerAmt() {
        return trailerAmt;
    }

    public void setTrailerAmt(BigDecimal trailerAmt) {
        this.trailerAmt = trailerAmt;
    }

    public BigDecimal getStopAmt() {
        return stopAmt;
    }

    public void setStopAmt(BigDecimal stopAmt) {
        this.stopAmt = stopAmt;
    }

    public BigDecimal getOtherAmt() {
        return otherAmt;
    }

    public void setOtherAmt(BigDecimal otherAmt) {
        this.otherAmt = otherAmt;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public BigDecimal getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(BigDecimal receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getReceivedFlag() {
        return receivedFlag;
    }

    public void setReceivedFlag(String receivedFlag) {
        this.receivedFlag = receivedFlag;
    }

    public String getReceivedRemark() {
        return receivedRemark;
    }

    public void setReceivedRemark(String receivedRemark) {
        this.receivedRemark = receivedRemark;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getProcessMethod() {
        return processMethod;
    }

    public void setProcessMethod(String processMethod) {
        this.processMethod = processMethod;
    }

}
