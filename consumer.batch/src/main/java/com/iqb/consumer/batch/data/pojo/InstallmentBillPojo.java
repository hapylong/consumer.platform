package com.iqb.consumer.batch.data.pojo;

/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月8日 下午7:27:06
 * @version V1.0
 */

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class InstallmentBillPojo extends BaseEntity {

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
        result = prime * result + repayNo;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        InstallmentBillPojo other = (InstallmentBillPojo) obj;
        if (orderId == null) {
            if (other.orderId != null) return false;
        } else if (!orderId.equals(other.orderId)) return false;
        if (repayNo != other.repayNo) return false;
        return true;
    }

    private static final long serialVersionUID = -8052061778243436750L;
    private String orderId;// 订单id
    private String subOrderId;// 订单id
    private Date orderDate;// 订单时间
    private String merchantNo;// 商户号
    private int repayNo;// 还款顺序
    private long installInfoId;// 分期总表id
    private long installDetailId;// 分期分摊表id
    private String regId;// 注册号
    private String openId;// 账户号-->对应accountinfo主键
    private Date lastRepayDate;// 最后还款日
    private Date delayBeginDate;// 滞纳金计算时间
    private BigDecimal curRepayAmt;// 本期应还总金额
    private Date curRepayDate;// 还款时间
    private Date earliestPayDate;// 最早还款日期
    private BigDecimal curRepayPrincipal;// 本期应还本金
    private BigDecimal curRepayInterest;// 本期应还利息
    private BigDecimal cutInterest;// 减免的利息
    private BigDecimal preInterest;// 日利息
    private BigDecimal curRepayOverdueInterest;// 本期应还逾期金额
    private BigDecimal cutOverdueInterest;// 减免逾期金额
    private BigDecimal preOverdueInterest;// 每天滞纳金
    private BigDecimal fixedOverdueAmt;// 逾期固定收取金额
    private BigDecimal cutFixedOverdueAmt;// 减免违约金
    private int overdueDays;// 逾期天数
    private BigDecimal monthOverdueAmt;// 当月逾期收取金额
    private BigDecimal curRealRepayamt;// 已经还款金额
    private BigDecimal principal;// 本金
    private BigDecimal realPayamt;// 实际分摊金额
    private BigDecimal remainPrincipal;// 剩余本金
    private BigDecimal remainPriandInterest;// 剩余本息
    private BigDecimal installAmt;// 实际分期金额
    private BigDecimal installSumAmt;// 多次分期INSTALLAMT小于实际分期总金额
    private BigDecimal contractAmt;// 合同金额
    private BigDecimal otherAmt;// 其他费用，这个费用需要添加到每个月月供中
    private int installTerms;// 分期期数
    private int prePayment;// 提前还款
    private int partPayment;// 部分还款
    private int instOrder;// 放款顺序
    private int status;// 账单状态
    private long planId;// 计划id

    private BigDecimal overdueAmt; // 违约金
    private int curItems; // 当前期数
    private String expiryDate; // 最后还款日
    private BigDecimal hasRepayAmt; // 已经还款金额
    private BigDecimal repayAmt; // 应还金额
    private int hasRepayNo; // 已还期数
    private BigDecimal offsetAmt; // 转租管理费
    private BigDecimal sumAmt; // 提前结清-总还款数

    private String tradeType;// 交易类型

    private BigDecimal sumCurRepayAmt; // 房贷每期分期金额
    private BigDecimal monthInterest;// 月供

    private String realName;// 姓名
    private String merchantFullName;// 商户号
    private String orderItems;// 期数

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMerchantFullName() {
        return merchantFullName;
    }

    public void setMerchantFullName(String merchantFullName) {
        this.merchantFullName = merchantFullName;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public BigDecimal getRemainPriandInterest() {
        return remainPriandInterest;
    }

    public void setRemainPriandInterest(BigDecimal remainPriandInterest) {
        this.remainPriandInterest = remainPriandInterest;
    }

    public BigDecimal getOtherAmt() {
        return otherAmt;
    }

    public void setOtherAmt(BigDecimal otherAmt) {
        this.otherAmt = otherAmt;
    }

    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
    }

    public int getInstallTerms() {
        return installTerms;
    }

    public void setInstallTerms(int installTerms) {
        this.installTerms = installTerms;
    }

    public BigDecimal getFixedOverdueAmt() {
        return fixedOverdueAmt;
    }

    public void setFixedOverdueAmt(BigDecimal fixedOverdueAmt) {
        this.fixedOverdueAmt = fixedOverdueAmt;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public BigDecimal getInstallSumAmt() {
        return installSumAmt;
    }

    public void setInstallSumAmt(BigDecimal installSumAmt) {
        this.installSumAmt = installSumAmt;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public Date getEarliestPayDate() {
        return earliestPayDate;
    }

    public void setEarliestPayDate(Date earliestPayDate) {
        this.earliestPayDate = earliestPayDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getCurRepayDate() {
        return curRepayDate;
    }

    public void setCurRepayDate(Date curRepayDate) {
        this.curRepayDate = curRepayDate;
    }

    public BigDecimal getRealPayamt() {
        return realPayamt;
    }

    public void setRealPayamt(BigDecimal realPayamt) {
        this.realPayamt = realPayamt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getInstOrder() {
        return instOrder;
    }

    public void setInstOrder(int instOrder) {
        this.instOrder = instOrder;
    }

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public long getInstallInfoId() {
        return installInfoId;
    }

    public void setInstallInfoId(long installInfoId) {
        this.installInfoId = installInfoId;
    }

    public long getInstallDetailId() {
        return installDetailId;
    }

    public void setInstallDetailId(long installDetailId) {
        this.installDetailId = installDetailId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public Date getDelayBeginDate() {
        return delayBeginDate;
    }

    public void setDelayBeginDate(Date delayBeginDate) {
        this.delayBeginDate = delayBeginDate;
    }

    public BigDecimal getCurRepayAmt() {
        return curRepayAmt;
    }

    public void setCurRepayAmt(BigDecimal curRepayAmt) {
        this.curRepayAmt = curRepayAmt;
    }

    public BigDecimal getCurRepayPrincipal() {
        return curRepayPrincipal;
    }

    public void setCurRepayPrincipal(BigDecimal curRepayPrincipal) {
        this.curRepayPrincipal = curRepayPrincipal;
    }

    public BigDecimal getCurRepayInterest() {
        return curRepayInterest;
    }

    public void setCurRepayInterest(BigDecimal curRepayInterest) {
        this.curRepayInterest = curRepayInterest;
    }

    public BigDecimal getPreInterest() {
        return preInterest;
    }

    public void setPreInterest(BigDecimal preInterest) {
        this.preInterest = preInterest;
    }

    public BigDecimal getCurRepayOverdueInterest() {
        return curRepayOverdueInterest;
    }

    public void setCurRepayOverdueInterest(BigDecimal curRepayOverdueInterest) {
        this.curRepayOverdueInterest = curRepayOverdueInterest;
    }

    public BigDecimal getPreOverdueInterest() {
        return preOverdueInterest;
    }

    public void setPreOverdueInterest(BigDecimal preOverdueInterest) {
        this.preOverdueInterest = preOverdueInterest;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public BigDecimal getMonthOverdueAmt() {
        return monthOverdueAmt;
    }

    public void setMonthOverdueAmt(BigDecimal monthOverdueAmt) {
        this.monthOverdueAmt = monthOverdueAmt;
    }

    public BigDecimal getCurRealRepayamt() {
        return curRealRepayamt;
    }

    public void setCurRealRepayamt(BigDecimal curRealRepayamt) {
        this.curRealRepayamt = curRealRepayamt;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getRemainPrincipal() {
        return remainPrincipal;
    }

    public void setRemainPrincipal(BigDecimal remainPrincipal) {
        this.remainPrincipal = remainPrincipal;
    }

    public BigDecimal getInstallAmt() {
        return installAmt;
    }

    public void setInstallAmt(BigDecimal installAmt) {
        this.installAmt = installAmt;
    }

    public int getPrePayment() {
        return prePayment;
    }

    public void setPrePayment(int prePayment) {
        this.prePayment = prePayment;
    }

    public int getPartPayment() {
        return partPayment;
    }

    public void setPartPayment(int partPayment) {
        this.partPayment = partPayment;
    }

    public BigDecimal getCutInterest() {
        return cutInterest;
    }

    public void setCutInterest(BigDecimal cutInterest) {
        this.cutInterest = cutInterest;
    }

    public BigDecimal getCutOverdueInterest() {
        return cutOverdueInterest;
    }

    public void setCutOverdueInterest(BigDecimal cutOverdueInterest) {
        this.cutOverdueInterest = cutOverdueInterest;
    }

    public BigDecimal getCutFixedOverdueAmt() {
        return cutFixedOverdueAmt;
    }

    public void setCutFixedOverdueAmt(BigDecimal cutFixedOverdueAmt) {
        this.cutFixedOverdueAmt = cutFixedOverdueAmt;
    }

    /**
     * @return the overdueAmt
     */
    public BigDecimal getOverdueAmt() {
        return overdueAmt;
    }

    /**
     * @param overdueAmt the overdueAmt to set
     */
    public void setOverdueAmt(BigDecimal overdueAmt) {
        this.overdueAmt = overdueAmt;
    }

    /**
     * @return the curItems
     */
    public int getCurItems() {
        return curItems;
    }

    /**
     * @param curItems the curItems to set
     */
    public void setCurItems(int curItems) {
        this.curItems = curItems;
    }

    /**
     * @return the expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getHasRepayAmt() {
        return hasRepayAmt;
    }

    public void setHasRepayAmt(BigDecimal hasRepayAmt) {
        this.hasRepayAmt = hasRepayAmt;
    }

    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }

    public int getHasRepayNo() {
        return hasRepayNo;
    }

    public void setHasRepayNo(int hasRepayNo) {
        this.hasRepayNo = hasRepayNo;
    }

    public BigDecimal getOffsetAmt() {
        return offsetAmt;
    }

    public void setOffsetAmt(BigDecimal offsetAmt) {
        this.offsetAmt = offsetAmt;
    }

    public BigDecimal getSumAmt() {
        return sumAmt;
    }

    public void setSumAmt(BigDecimal sumAmt) {
        this.sumAmt = sumAmt;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public BigDecimal getSumCurRepayAmt() {
        return sumCurRepayAmt;
    }

    public void setSumCurRepayAmt(BigDecimal sumCurRepayAmt) {
        this.sumCurRepayAmt = sumCurRepayAmt;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    @Override
    public String toString() {
        return "{orderId=" + orderId + ", orderDate=" + orderDate + ", repayNo=" + repayNo
                + ", installInfoId=" + installInfoId + ", installDetailId=" + installDetailId + ", regId=" + regId
                + ", openId=" + openId + ", lastRepayDate=" + lastRepayDate + ", delayBeginDate=" + delayBeginDate
                + ", cutInterest=" + cutInterest + ", cutOverdueInterest=" + cutOverdueInterest
                + ", cutFixedOverdueAmt=" + cutFixedOverdueAmt
                + ", curRepayAmt=" + curRepayAmt + ", curRepayDate=" + curRepayDate + ", curRepayPrincipal="
                + curRepayPrincipal + ", curRepayInterest=" + curRepayInterest + ", preInterest=" + preInterest
                + ", curRepayOverdueInterest=" + curRepayOverdueInterest + ", preOverdueInterest=" + preOverdueInterest
                + ", overdueDays=" + overdueDays + ", curRealRepayamt=" + curRealRepayamt + ", principal=" + principal
                + ", realPayamt=" + realPayamt + ", remainPrincipal=" + remainPrincipal + ", installAmt=" + installAmt
                + ", prePayment=" + prePayment + ", partPayment=" + partPayment + ", instOrder=" + instOrder
                + ", status=" + status + "}";
    }

}
