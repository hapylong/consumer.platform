package com.iqb.consumer.data.layer.bean.ownerloan;

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
 * 2017年11月10日上午10:23:23 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class MortgageInfoBean extends BaseEntity {
    private String orderId;// 订单号
    private String mortgageType;// 抵押类型
    private String mortgageCompany;// 抵押公司
    private BigDecimal instAmt;
    private BigDecimal instMonthInterest;// 分期月供
    private String instItems;// 分期期数
    private String instRepayedItems;// 分期已还期数
    private BigDecimal suggestAmt;// 建议额度
    private String suggestItems;// 建议期限
    private String storeRiskAdvice;// 门店风控意见
    private String trialRiskAdvice;// 风控初审意见
    private int riskStatus;// 风控状态
    private String riskRetRemark;// 风控意见

    private String carConfig; // 车辆配置
    private String carColor;// 车辆颜色
    private String carNo;// 车架号
    private String carEmissions;// 排量
    private int passengerNum;// 核定载客
    private int mileage;// 公里数
    private BigDecimal firstBuyAmt;// 首次购买价格
    private String regOrg;// 注册机构
    private String firstRegDate;// 首次登记时间
    private int transferNum;// 转移次数
    private int mortgageFlag;// 抵押标识 1是2否
    private BigDecimal assessPrice; // 评估价格
    private String assessSuggest;// 评估意见

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType;
    }

    public String getMortgageCompany() {
        return mortgageCompany;
    }

    public void setMortgageCompany(String mortgageCompany) {
        this.mortgageCompany = mortgageCompany;
    }

    public BigDecimal getInstAmt() {
        return instAmt;
    }

    public void setInstAmt(BigDecimal instAmt) {
        this.instAmt = instAmt;
    }

    public BigDecimal getInstMonthInterest() {
        return instMonthInterest;
    }

    public void setInstMonthInterest(BigDecimal instMonthInterest) {
        this.instMonthInterest = instMonthInterest;
    }

    public String getInstItems() {
        return instItems;
    }

    public void setInstItems(String instItems) {
        this.instItems = instItems;
    }

    public String getInstRepayedItems() {
        return instRepayedItems;
    }

    public void setInstRepayedItems(String instRepayedItems) {
        this.instRepayedItems = instRepayedItems;
    }

    public BigDecimal getSuggestAmt() {
        return suggestAmt;
    }

    public void setSuggestAmt(BigDecimal suggestAmt) {
        this.suggestAmt = suggestAmt;
    }

    public String getSuggestItems() {
        return suggestItems;
    }

    public void setSuggestItems(String suggestItems) {
        this.suggestItems = suggestItems;
    }

    public String getStoreRiskAdvice() {
        return storeRiskAdvice;
    }

    public void setStoreRiskAdvice(String storeRiskAdvice) {
        this.storeRiskAdvice = storeRiskAdvice;
    }

    public String getTrialRiskAdvice() {
        return trialRiskAdvice;
    }

    public void setTrialRiskAdvice(String trialRiskAdvice) {
        this.trialRiskAdvice = trialRiskAdvice;
    }

    public int getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(int riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getRiskRetRemark() {
        return riskRetRemark;
    }

    public void setRiskRetRemark(String riskRetRemark) {
        this.riskRetRemark = riskRetRemark;
    }

    public String getCarConfig() {
        return carConfig;
    }

    public void setCarConfig(String carConfig) {
        this.carConfig = carConfig;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarEmissions() {
        return carEmissions;
    }

    public void setCarEmissions(String carEmissions) {
        this.carEmissions = carEmissions;
    }

    public int getPassengerNum() {
        return passengerNum;
    }

    public void setPassengerNum(int passengerNum) {
        this.passengerNum = passengerNum;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public BigDecimal getFirstBuyAmt() {
        return firstBuyAmt;
    }

    public void setFirstBuyAmt(BigDecimal firstBuyAmt) {
        this.firstBuyAmt = firstBuyAmt;
    }

    public String getRegOrg() {
        return regOrg;
    }

    public void setRegOrg(String regOrg) {
        this.regOrg = regOrg;
    }

    public String getFirstRegDate() {
        return firstRegDate;
    }

    public void setFirstRegDate(String firstRegDate) {
        this.firstRegDate = firstRegDate;
    }

    public int getTransferNum() {
        return transferNum;
    }

    public void setTransferNum(int transferNum) {
        this.transferNum = transferNum;
    }

    public int getMortgageFlag() {
        return mortgageFlag;
    }

    public void setMortgageFlag(int mortgageFlag) {
        this.mortgageFlag = mortgageFlag;
    }

    public BigDecimal getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(BigDecimal assessPrice) {
        this.assessPrice = assessPrice;
    }

    public String getAssessSuggest() {
        return assessSuggest;
    }

    public void setAssessSuggest(String assessSuggest) {
        this.assessSuggest = assessSuggest;
    }

}
