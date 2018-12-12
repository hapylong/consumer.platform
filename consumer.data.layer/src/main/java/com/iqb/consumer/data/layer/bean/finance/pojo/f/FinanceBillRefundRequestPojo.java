package com.iqb.consumer.data.layer.bean.finance.pojo.f;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FinanceBillRefundRequestPojo {

    private String orderId;// 订单号
    private String subOrderId;// 订单号
    private String openId;// 开户号
    private String repayModel;// 还款模式
    private String repayType;// 还款方式
    private String bankCardNo;// 还款银行号
    private String bankName;// 还款银行名称
    private String regId;// 手机号
    private String merchantNo;// 商户号
    private String tradeNo;// 流水号
    private Date repayDate;// 还款时间
    private BigDecimal sumAmt;// 总还款金额
    List<FinanceRefundPiece> repayList;// 还款列表

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getRepayModel() {
        return repayModel;
    }

    public void setRepayModel(String repayModel) {
        this.repayModel = repayModel;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public BigDecimal getSumAmt() {
        return sumAmt;
    }

    public void setSumAmt(BigDecimal sumAmt) {
        this.sumAmt = sumAmt;
    }

    public List<FinanceRefundPiece> getRepayList() {
        return repayList;
    }

    public void setRepayList(List<FinanceRefundPiece> repayList) {
        this.repayList = repayList;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

}
