package com.iqb.consumer.manage.front.exchange;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * Created by ckq.
 */
public class ListPushVo {

    private String listName;

    private String listNumber;

    private String assetNumber;

    @JSONField(format = "yyyy-MM-dd")
    private Date publishTime;

    private String issuerName;

    @JSONField(format = "yyyy-MM-dd")
    private Date recruitStart;

    @JSONField(format = "yyyy-MM-dd")
    private Date recruitEnd;

    @JSONField(format = "yyyy-MM-dd")
    private Date interestStart;

    @JSONField(format = "yyyy-MM-dd")
    private Date interestEnd;

    private Integer refundMethod;

    private Integer listCount;

    private Double totalMoney;

    private String rate;

    private String currency;

    private String investKind;

    private List<AmountSetting> amountSetting;

    private List<ExpectRefund> refundDetail;

    private List<QuestionVo> questionList;

    private List<RiskVo> riskList;

    private List<BidVo> bidList;

    public String getInvestKind() {
        return investKind;
    }

    public void setInvestKind(String investKind) {
        this.investKind = investKind;
    }

    public List<AmountSetting> getAmountSetting() {
        return amountSetting;
    }

    public void setAmountSetting(List<AmountSetting> amountSetting) {
        this.amountSetting = amountSetting;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListNumber() {
        return listNumber;
    }

    public void setListNumber(String listNumber) {
        this.listNumber = listNumber;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public Date getRecruitStart() {
        return recruitStart;
    }

    public void setRecruitStart(Date recruitStart) {
        this.recruitStart = recruitStart;
    }

    public Date getRecruitEnd() {
        return recruitEnd;
    }

    public void setRecruitEnd(Date recruitEnd) {
        this.recruitEnd = recruitEnd;
    }

    public Date getInterestStart() {
        return interestStart;
    }

    public void setInterestStart(Date interestStart) {
        this.interestStart = interestStart;
    }

    public Date getInterestEnd() {
        return interestEnd;
    }

    public void setInterestEnd(Date interestEnd) {
        this.interestEnd = interestEnd;
    }

    public Integer getRefundMethod() {
        return refundMethod;
    }

    public void setRefundMethod(Integer refundMethod) {
        this.refundMethod = refundMethod;
    }

    public Integer getListCount() {
        return listCount;
    }

    public void setListCount(Integer listCount) {
        this.listCount = listCount;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<QuestionVo> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionVo> questionList) {
        this.questionList = questionList;
    }

    public List<RiskVo> getRiskList() {
        return riskList;
    }

    public void setRiskList(List<RiskVo> riskList) {
        this.riskList = riskList;
    }

    public List<BidVo> getBidList() {
        return bidList;
    }

    public void setBidList(List<BidVo> bidList) {
        this.bidList = bidList;
    }

    public List<ExpectRefund> getRefundDetail() {
        return refundDetail;
    }

    public void setRefundDetail(List<ExpectRefund> refundDetail) {
        this.refundDetail = refundDetail;
    }
}
