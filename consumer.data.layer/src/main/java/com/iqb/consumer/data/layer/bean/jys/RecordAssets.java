package com.iqb.consumer.data.layer.bean.jys;

import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 备案资产要素
 * 
 * @author Yeoman
 * 
 */
public class RecordAssets extends BaseEntity {

    private String assetNumber;
    private String assetName;
    private String assetKind;
    private String issuerName;
    private String assetFax;
    private String assetUrl;
    private Date interestStart;
    private Date interestEnd;
    private int refundMethod;
    private double totalMoney;
    private String currency;
    private String rate;
    private String debtorName;
    private String debtorIndustryType;
    private String debtorRegAddress;
    private String debtorEconomicNature;
    private String debtorLegalmen;
    private String debtorBusiness;
    private int innerDecision;
    private String promotionLetter;
    private String description;
    private String remarks;
    private int riskLevel;
    private String guaranteedMethod;
    private String guaranteedDetail;
    private String needRiskBearAbility;
    private String refundDetail;

    public int getInnerDecision() {
        return innerDecision;
    }

    public void setInnerDecision(int innerDecision) {
        this.innerDecision = innerDecision;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
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

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetKind() {
        return assetKind;
    }

    public void setAssetKind(String assetKind) {
        this.assetKind = assetKind;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getAssetFax() {
        return assetFax;
    }

    public void setAssetFax(String assetFax) {
        this.assetFax = assetFax;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

    public void setAssetUrl(String assetUrl) {
        this.assetUrl = assetUrl;
    }

    public int getRefundMethod() {
        return refundMethod;
    }

    public void setRefundMethod(int refundMethod) {
        this.refundMethod = refundMethod;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getDebtorIndustryType() {
        return debtorIndustryType;
    }

    public void setDebtorIndustryType(String debtorIndustryType) {
        this.debtorIndustryType = debtorIndustryType;
    }

    public String getDebtorRegAddress() {
        return debtorRegAddress;
    }

    public void setDebtorRegAddress(String debtorRegAddress) {
        this.debtorRegAddress = debtorRegAddress;
    }

    public String getDebtorEconomicNature() {
        return debtorEconomicNature;
    }

    public void setDebtorEconomicNature(String debtorEconomicNature) {
        this.debtorEconomicNature = debtorEconomicNature;
    }

    public String getDebtorLegalmen() {
        return debtorLegalmen;
    }

    public void setDebtorLegalmen(String debtorLegalmen) {
        this.debtorLegalmen = debtorLegalmen;
    }

    public String getDebtorBusiness() {
        return debtorBusiness;
    }

    public void setDebtorBusiness(String debtorBusiness) {
        this.debtorBusiness = debtorBusiness;
    }

    public String getPromotionLetter() {
        return promotionLetter;
    }

    public void setPromotionLetter(String promotionLetter) {
        this.promotionLetter = promotionLetter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getGuaranteedMethod() {
        return guaranteedMethod;
    }

    public void setGuaranteedMethod(String guaranteedMethod) {
        this.guaranteedMethod = guaranteedMethod;
    }

    public String getGuaranteedDetail() {
        return guaranteedDetail;
    }

    public void setGuaranteedDetail(String guaranteedDetail) {
        this.guaranteedDetail = guaranteedDetail;
    }

    public String getNeedRiskBearAbility() {
        return needRiskBearAbility;
    }

    public void setNeedRiskBearAbility(String needRiskBearAbility) {
        this.needRiskBearAbility = needRiskBearAbility;
    }

    public String getRefundDetail() {
        return refundDetail;
    }

    public void setRefundDetail(String refundDetail) {
        this.refundDetail = refundDetail;
    }

}
