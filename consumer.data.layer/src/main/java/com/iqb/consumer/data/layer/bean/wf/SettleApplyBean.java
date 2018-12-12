package com.iqb.consumer.data.layer.bean.wf;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class SettleApplyBean extends BaseEntity {

    // 提前结清审批
    private String orderId; // 订单号
    private Long userId; // 用户ID
    private BigDecimal overdueAmt; // 违约金
    private BigDecimal cutOverdueAmt;// 减免违约金
    private BigDecimal finalOverdueAmt = BigDecimal.ZERO;// 减免后的违约金
    private String cutOverdueRemark;// 违约金减免原因
    private BigDecimal payAmt; // 已还款金额
    private BigDecimal payPrincipal;// 已还本金
    private BigDecimal surplusPrincipal;// 剩余本金

    private BigDecimal margin;// 保证金
    private BigDecimal feeAmount;// 上收息金额
    private Integer settleStatus;// 申请状态:1,正在申请 2,审批通过 3,审批拒绝
    private BigDecimal refundAmt;// 应退上收息
    private BigDecimal refundMargin;// 应退保证金
    private BigDecimal shouldRepayAmt;// 应该还款金额=月供+未还本金
    private BigDecimal totalRepayAmt;// 累计应该还款金额
    private BigDecimal needPayAmt;// 待支付金额
    
    private BigDecimal receiveAmt;// 已收到金额/实际付款金额
    private String recieveDate;// 实际付款时间
    private String amtStatus;// 支付状态 1,已付款 2,待付款
    private String hiddenFee;// 是否隐藏费用明细
    private Integer curItems;// 当前期数
    private String reason;// 原因
    private String remark;// 备注
    private Date expiryDate; // 到期日
    private String procInstId; // 工作流ID
    private String payMethod;// 付款方式 1 线上 2 线下
    private BigDecimal totalOverdueInterest = BigDecimal.ZERO;// 总罚息
    private BigDecimal remainInterest;// 未还利息
    private String cutOverdueFlag;// 是否减免违约金
    // 用户信息
    private String realName;// 真实姓名
    // 订单信息
    private Integer orderItems;// 总期数
    private BigDecimal monthInterest;// 月供
    private BigDecimal monthPrincipal;// 月供本金
    private BigDecimal orderAmt; // 借款金额
    private String merchantName; // 商户名称
    private BigDecimal totalRepayAmtOriginal;// 原始的累计应还金额
    private String overItems; // 逾期期数

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getOverdueAmt() {
        return overdueAmt;
    }

    public void setOverdueAmt(BigDecimal overdueAmt) {
        this.overdueAmt = overdueAmt;
    }

    public BigDecimal getCutOverdueAmt() {
        return cutOverdueAmt;
    }

    public void setCutOverdueAmt(BigDecimal cutOverdueAmt) {
        this.cutOverdueAmt = cutOverdueAmt;
    }

    public BigDecimal getFinalOverdueAmt() {
        return finalOverdueAmt;
    }

    public void setFinalOverdueAmt(BigDecimal finalOverdueAmt) {
        this.finalOverdueAmt = finalOverdueAmt;
    }

    public String getCutOverdueRemark() {
        return cutOverdueRemark;
    }

    public void setCutOverdueRemark(String cutOverdueRemark) {
        this.cutOverdueRemark = cutOverdueRemark;
    }

    public BigDecimal getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(BigDecimal payAmt) {
        this.payAmt = payAmt;
    }

    public BigDecimal getPayPrincipal() {
        return payPrincipal;
    }

    public void setPayPrincipal(BigDecimal payPrincipal) {
        this.payPrincipal = payPrincipal;
    }

    public BigDecimal getSurplusPrincipal() {
        return surplusPrincipal;
    }

    public void setSurplusPrincipal(BigDecimal surplusPrincipal) {
        this.surplusPrincipal = surplusPrincipal;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(Integer settleStatus) {
        this.settleStatus = settleStatus;
    }

    public BigDecimal getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(BigDecimal refundAmt) {
        this.refundAmt = refundAmt;
    }

    public BigDecimal getRefundMargin() {
        return refundMargin;
    }

    public void setRefundMargin(BigDecimal refundMargin) {
        this.refundMargin = refundMargin;
    }

    public BigDecimal getShouldRepayAmt() {
        return shouldRepayAmt;
    }

    public void setShouldRepayAmt(BigDecimal shouldRepayAmt) {
        this.shouldRepayAmt = shouldRepayAmt;
    }

    public BigDecimal getTotalRepayAmt() {
        return totalRepayAmt;
    }

    public void setTotalRepayAmt(BigDecimal totalRepayAmt) {
        this.totalRepayAmt = totalRepayAmt;
    }

    public BigDecimal getReceiveAmt() {
        return receiveAmt;
    }

    public void setReceiveAmt(BigDecimal receiveAmt) {
        this.receiveAmt = receiveAmt;
    }

    public String getRecieveDate() {
        return recieveDate;
    }

    public void setRecieveDate(String recieveDate) {
        this.recieveDate = recieveDate;
    }

    public String getAmtStatus() {
        return amtStatus;
    }

    public void setAmtStatus(String amtStatus) {
        this.amtStatus = amtStatus;
    }

    public String getHiddenFee() {
        return hiddenFee;
    }

    public void setHiddenFee(String hiddenFee) {
        this.hiddenFee = hiddenFee;
    }

    public Integer getCurItems() {
        return curItems;
    }

    public void setCurItems(Integer curItems) {
        this.curItems = curItems;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public BigDecimal getTotalOverdueInterest() {
        return totalOverdueInterest;
    }

    public void setTotalOverdueInterest(BigDecimal totalOverdueInterest) {
        this.totalOverdueInterest = totalOverdueInterest;
    }

    public BigDecimal getRemainInterest() {
        return remainInterest;
    }

    public void setRemainInterest(BigDecimal remainInterest) {
        this.remainInterest = remainInterest;
    }

    public String getCutOverdueFlag() {
        return cutOverdueFlag;
    }

    public void setCutOverdueFlag(String cutOverdueFlag) {
        this.cutOverdueFlag = cutOverdueFlag;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public BigDecimal getMonthPrincipal() {
        return monthPrincipal;
    }

    public void setMonthPrincipal(BigDecimal monthPrincipal) {
        this.monthPrincipal = monthPrincipal;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    public BigDecimal getTotalRepayAmtOriginal() {
        return totalRepayAmtOriginal;
    }

    public void setTotalRepayAmtOriginal(BigDecimal totalRepayAmtOriginal) {
        this.totalRepayAmtOriginal = totalRepayAmtOriginal;
    }

    public String getOverItems() {
        return overItems;
    }

    public void setOverItems(String overItems) {
        this.overItems = overItems;
    }

    public BigDecimal getNeedPayAmt() {
        return needPayAmt;
    }

    public void setNeedPayAmt(BigDecimal needPayAmt) {
        this.needPayAmt = needPayAmt;
    }
    
}
