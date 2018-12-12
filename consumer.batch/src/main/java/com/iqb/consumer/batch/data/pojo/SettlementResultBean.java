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
 * 2017年6月23日下午6:09:56 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.batch.data.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author haojinlong
 * 
 */
public class SettlementResultBean {
    private int id;
    private String orderId; // 订单号
    private String tradeNo; // 流水号
    private int repayNo; // 还款期数
    private String openId; // 开户ID
    private BigDecimal curRepayAmt; // 当期还款金额
    private int tradeType; // 交易类型 1,划扣正常月供 2 划扣逾期月供
    private int status; // 状态
    private int number; //
    protected Date createTime = new Date();

    private String merchantNo; // 商户号
    private Date lastRepayDate;// 最迟还款日
    private int overdueDays;// 逾期天数
    private BigDecimal monthInterest;// 月供
    private BigDecimal overdueInterest;// 逾期罚息
    private int billStatus;// 账单状态
    private int flag;// 手动代扣标识 1 是 2 否

    private String regId; // 手机号码-取接收短信手机号码
    private BigDecimal curRepayOverdueInterest;// 本期应还逾期金额
    private int smsFlag;// 是否发送短信 1 是 2 否

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
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

    public int getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(int billStatus) {
        this.billStatus = billStatus;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public BigDecimal getCurRepayOverdueInterest() {
        return curRepayOverdueInterest;
    }

    public void setCurRepayOverdueInterest(BigDecimal curRepayOverdueInterest) {
        this.curRepayOverdueInterest = curRepayOverdueInterest;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public int getSmsFlag() {
        return smsFlag;
    }

    public void setSmsFlag(int smsFlag) {
        this.smsFlag = smsFlag;
    }

}
