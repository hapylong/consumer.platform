package com.iqb.consumer.data.layer.bean.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFinanceToRefundRequestMessage {
    private static final String REPAY_TYPE_BUCKLE_CENTER = "3"; // 结算中心还款代扣
    private String orderId;// 订单号
    private String openId;// 开户号
    private String repayModel;// 还款模式 part 部分,nornmal 全部
    private String repayType;// 还款方式
    private String bankCardNo;// 还款银行号

    private String bankName;// 还款银行名称
    private String regId;// 手机号
    private String merchantNo;// 商户号
    private String tradeNo;// 流水号
    private Date repayDate;// 还款时间
    private BigDecimal sumAmt;// 总还款金额
    List<RepayListPojo> repayList = new ArrayList<RepayListPojo>();// 还款列表

    private Integer riskStatus;
    private String bankCode; // 银行代号

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

    public List<RepayListPojo> getRepayList() {
        return repayList;
    }

    public void setRepayList(List<RepayListPojo> repayList) {
        this.repayList = repayList;
    }

    public Integer getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Integer riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public void createChatMsg(SettlementCenterBuckleCallbackRequestMessage scbc, String openId2) {
        String repayNo = OverDueBillPojo.getRNByTradeNo(scbc.getTradeNo());
        RepayListPojo rlp = new RepayListPojo();
        rlp.setAmt(scbc.getSuccAmt());
        rlp.setRepayNo(repayNo);
        repayList.add(rlp);
        repayType = REPAY_TYPE_BUCKLE_CENTER;
        tradeNo = scbc.getTradeNo();
        repayDate = new Date();
        sumAmt = scbc.getSuccAmt();
        switch (scbc.getStatus()) {
            case SettlementCenterBuckleCallbackRequestMessage.SUCCESS_ALL:
                repayModel = "normal";
                break;
            case SettlementCenterBuckleCallbackRequestMessage.SUCCESS_PART:
                repayModel = "part";
                break;
            case SettlementCenterBuckleCallbackRequestMessage.FAIL:
                throw new RuntimeException("type not define.");
            default:
                throw new RuntimeException("type not found.");
        }
    }
}
