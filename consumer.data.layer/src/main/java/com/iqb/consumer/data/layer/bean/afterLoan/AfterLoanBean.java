package com.iqb.consumer.data.layer.bean.afterLoan;

import java.math.BigDecimal;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 贷后bean
 * 
 * @author chengzhen
 * 
 */
@SuppressWarnings("serial")
public class AfterLoanBean extends BaseEntity {

    private String merchantShortName;

    private Integer orderItems;
    private String PLANFULLNAME;
    private String riskStatus;

    /** 订单号 **/
    private String orderId;
    /** 手机号码 **/
    private String regId;
    /** 姓名 **/
    private String realName;
    /** 商户名称 **/
    private String merchantName;
    /** 商户名称 **/
    private String merchantNo;
    /** 订单金额 **/
    private BigDecimal orderAmt;
    /** 月供 **/
    private BigDecimal monthInterest;
    /** 品牌车系 **/
    private String orderName;
    /** 车牌号 **/
    private String plate;
    /** 车架号 **/
    private String carNo;
    /** gps状态 **/
    private int gpsStatus;
    /** GPS备注 **/
    private String remark;
    /** 处置方案 **/
    private String disposalScheme;
    /** 贷后意见 **/
    private String afterLoanOpinion;
    /** 还款标识 1 是 2 否 **/
    private String repaymentFlag;
    /** 失联原因 **/
    private String lostContactReason;
    /** 是否失联 **/
    private String lostContactFlag;
    /** 拖车原因 **/
    private String trailerReason;
    /** 核实意见 **/
    private String checkOpinion;
    /** gps信号区域 **/
    private String gpsArea;
    /** 收回标识 1 是 2 否 **/
    private String collectFlag;
    /** 入库时间 **/
    private String intoGarageDate;
    /** 车库名称 **/
    private String intoGarageName;
    /** 收车情况说明 **/
    private String collectOpinion;
    /** 收车情况确认 **/
    private String collectOpinionConfirm;
    /** 风控备注 **/
    private String riskRemark;
    /** 接收短信手机号码 **/
    private String smsMobile;
    /** 车辆颜色 **/
    private String carColor;
    /** 行驶里程 **/
    private String mileage;
    /** 是否有车钥匙 **/
    private int carKeyFlag;
    /** 处理途径 **/
    private String processMethod;
    /** 发动机型号1 **/
    private String engine;
    // 贷后外包回款信息
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
    /** 收款备注 **/
    private String receivedRemark;
    /** 已还期数 **/
    private int hasRepayNo;
    /** 剩余本金 **/
    private BigDecimal remainPrincipal;

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public String getPLANFULLNAME() {
        return PLANFULLNAME;
    }

    public void setPLANFULLNAME(String pLANFULLNAME) {
        PLANFULLNAME = pLANFULLNAME;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public int getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(int gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDisposalScheme() {
        return disposalScheme;
    }

    public void setDisposalScheme(String disposalScheme) {
        this.disposalScheme = disposalScheme;
    }

    public String getAfterLoanOpinion() {
        return afterLoanOpinion;
    }

    public void setAfterLoanOpinion(String afterLoanOpinion) {
        this.afterLoanOpinion = afterLoanOpinion;
    }

    public String getRepaymentFlag() {
        return repaymentFlag;
    }

    public void setRepaymentFlag(String repaymentFlag) {
        this.repaymentFlag = repaymentFlag;
    }

    public String getLostContactReason() {
        return lostContactReason;
    }

    public void setLostContactReason(String lostContactReason) {
        this.lostContactReason = lostContactReason;
    }

    public String getLostContactFlag() {
        return lostContactFlag;
    }

    public void setLostContactFlag(String lostContactFlag) {
        this.lostContactFlag = lostContactFlag;
    }

    public String getTrailerReason() {
        return trailerReason;
    }

    public void setTrailerReason(String trailerReason) {
        this.trailerReason = trailerReason;
    }

    public String getCheckOpinion() {
        return checkOpinion;
    }

    public void setCheckOpinion(String checkOpinion) {
        this.checkOpinion = checkOpinion;
    }

    public String getGpsArea() {
        return gpsArea;
    }

    public void setGpsArea(String gpsArea) {
        this.gpsArea = gpsArea;
    }

    public String getCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(String collectFlag) {
        this.collectFlag = collectFlag;
    }

    public String getIntoGarageDate() {
        return intoGarageDate;
    }

    public void setIntoGarageDate(String intoGarageDate) {
        this.intoGarageDate = intoGarageDate;
    }

    public String getIntoGarageName() {
        return intoGarageName;
    }

    public void setIntoGarageName(String intoGarageName) {
        this.intoGarageName = intoGarageName;
    }

    public String getCollectOpinion() {
        return collectOpinion;
    }

    public void setCollectOpinion(String collectOpinion) {
        this.collectOpinion = collectOpinion;
    }

    public String getCollectOpinionConfirm() {
        return collectOpinionConfirm;
    }

    public void setCollectOpinionConfirm(String collectOpinionConfirm) {
        this.collectOpinionConfirm = collectOpinionConfirm;
    }

    public String getRiskRemark() {
        return riskRemark;
    }

    public void setRiskRemark(String riskRemark) {
        this.riskRemark = riskRemark;
    }

    public String getSmsMobile() {
        return smsMobile;
    }

    public void setSmsMobile(String smsMobile) {
        this.smsMobile = smsMobile;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public int getCarKeyFlag() {
        return carKeyFlag;
    }

    public void setCarKeyFlag(int carKeyFlag) {
        this.carKeyFlag = carKeyFlag;
    }

    public String getProcessMethod() {
        return processMethod;
    }

    public void setProcessMethod(String processMethod) {
        this.processMethod = processMethod;
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

    public int getHasRepayNo() {
        return hasRepayNo;
    }

    public void setHasRepayNo(int hasRepayNo) {
        this.hasRepayNo = hasRepayNo;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getReceivedRemark() {
        return receivedRemark;
    }

    public void setReceivedRemark(String receivedRemark) {
        this.receivedRemark = receivedRemark;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public BigDecimal getRemainPrincipal() {
        return remainPrincipal;
    }

    public void setRemainPrincipal(BigDecimal remainPrincipal) {
        this.remainPrincipal = remainPrincipal;
    }

}
