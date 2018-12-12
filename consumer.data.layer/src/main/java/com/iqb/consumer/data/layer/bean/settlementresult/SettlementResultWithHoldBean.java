package com.iqb.consumer.data.layer.bean.settlementresult;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;
import com.iqb.consumer.data.layer.bean.pay.SettlementCenterBuckleCallbackRequestMessage;

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
 * 2018年1月7日下午2:57:29 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class SettlementResultWithHoldBean extends BaseEntity {
    private String orderId; // 订单号
    private String merchantNo; // 商户号
    private String tradeNo; // 流水号
    private int repayNo; // 还款期数
    private String openId; // 开户ID
    private BigDecimal curRepayAmt; // 当期还款金额
    private int tradeType; // 交易类型 1,划扣正常月供 2 划扣逾期月供
    private int status; // 划扣状态
    private int number; // 次数
    private String describe;
    private Date lastRepayDate;// 最迟还款日
    private int overdueDays;// 逾期天数
    private BigDecimal monthInterest;// 月供
    private BigDecimal overdueInterest;// 逾期罚息
    private String billStatus;// 账单状态

    private String merchantName;// 商户名称
    private String realName;// 姓名
    private String regId;// 手机号码
    private int orderItems;// 订单总期数

    public static final int NOT_SEND = 1;
    public static final int SEND = 2;
    public static final int HK_SUCCESS = 3;
    public static final int HK_PART_SUCCESS = 4;
    public static final int HK_FAIL = 5;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public BigDecimal getCurRepayAmt() {
        return curRepayAmt;
    }

    public void setCurRepayAmt(BigDecimal curRepayAmt) {
        this.curRepayAmt = curRepayAmt;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public BigDecimal getOverdueInterest() {
        return overdueInterest;
    }

    public void setOverdueInterest(BigDecimal overdueInterest) {
        this.overdueInterest = overdueInterest;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public int getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(int orderItems) {
        this.orderItems = orderItems;
    }

    public void setStatusBySCBC(SettlementCenterBuckleCallbackRequestMessage scbc) {
        switch (scbc.getStatus()) {
            case SettlementCenterBuckleCallbackRequestMessage.SUCCESS_ALL:
                status = SettlementResultWithHoldBean.HK_SUCCESS;
                break;
            case SettlementCenterBuckleCallbackRequestMessage.SUCCESS_PART:
                status = SettlementResultWithHoldBean.HK_PART_SUCCESS;
                break;
            case SettlementCenterBuckleCallbackRequestMessage.FAIL:
                status = SettlementResultWithHoldBean.HK_FAIL;
                break;
            default:
                throw new RuntimeException("Invalid status map, please update procedure.");
        }
    }
}
