package com.iqb.consumer.data.layer.bean.carstatus.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.iqb.consumer.data.layer.bean.BaseEntity;
import com.iqb.etep.common.utils.StringUtil;

@Table(name = "inst_managecar_info")
public class InstManageCarInfoEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private Integer status;// 1.贷后处置中 2.已失联 3.已入库 4.已出售 5.已转租 6.已返还
    private Date afterLoanDate; // 转贷后时间
    private String trailerReason; // 拖车原因
    private Date intoGarageDate; // 入车库时间
    private String lostContactReason; // 失联原因
    private BigDecimal assessAmt; // 门店评估金额(单位元)
    private BigDecimal hqEvaluatesAmt; // 总部评估价格
    private BigDecimal hqCheckAmt; // 总部核对价格
    private BigDecimal shouldPayAmt; // 应还金额
    private BigDecimal dealAmt; // 处置金额
    private BigDecimal receivedAmt; // 到账金额
    private String serialNumber; // 流水号
    private String intoGarageRemark; // 入库备注
    private String dealerEvaluatesInfo; // 车商评估信息
    private String saleRemark; // 车辆出售备注
    private String evaluatesRemark; // 评估备注
    private String subleaseInfo; // 转租信息
    private String returnInfo; // 返还信息

    private String repaymentFlag; // 还款标识
    private String lostContactFlag; // 车辆是否失联
    private String collectFlag; // 车辆是否收回
    private String intoGarageName; // 车库名称
    private String collectOpinion; // 收车情况

    private String afterLoanOpinion; // 贷后处置意见
    private String checkOpinion; // 核实意见
    private String riskRemark; // 风控备注
    private String gpsArea; // gps信号区域
    private String collectOpinionConfirm;// 收车情况确认
    private String processMethod;// 处理途径 1 转外包 2 转法务

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAfterLoanDate() {
        return afterLoanDate;
    }

    public void setAfterLoanDate(Date afterLoanDate) {
        this.afterLoanDate = afterLoanDate;
    }

    public String getTrailerReason() {
        return trailerReason;
    }

    public void setTrailerReason(String trailerReason) {
        this.trailerReason = trailerReason;
    }

    public Date getIntoGarageDate() {
        return intoGarageDate;
    }

    public void setIntoGarageDate(Date intoGarageDate) {
        this.intoGarageDate = intoGarageDate;
    }

    public String getLostContactReason() {
        return lostContactReason;
    }

    public void setLostContactReason(String lostContactReason) {
        this.lostContactReason = lostContactReason;
    }

    public BigDecimal getAssessAmt() {
        return assessAmt;
    }

    public void setAssessAmt(BigDecimal assessAmt) {
        this.assessAmt = assessAmt;
    }

    public BigDecimal getHqEvaluatesAmt() {
        return hqEvaluatesAmt;
    }

    public void setHqEvaluatesAmt(BigDecimal hqEvaluatesAmt) {
        this.hqEvaluatesAmt = hqEvaluatesAmt;
    }

    public BigDecimal getHqCheckAmt() {
        return hqCheckAmt;
    }

    public void setHqCheckAmt(BigDecimal hqCheckAmt) {
        this.hqCheckAmt = hqCheckAmt;
    }

    public BigDecimal getShouldPayAmt() {
        return shouldPayAmt;
    }

    public void setShouldPayAmt(BigDecimal shouldPayAmt) {
        this.shouldPayAmt = shouldPayAmt;
    }

    public BigDecimal getDealAmt() {
        return dealAmt;
    }

    public void setDealAmt(BigDecimal dealAmt) {
        this.dealAmt = dealAmt;
    }

    public BigDecimal getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(BigDecimal receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIntoGarageRemark() {
        return intoGarageRemark;
    }

    public void setIntoGarageRemark(String intoGarageRemark) {
        this.intoGarageRemark = intoGarageRemark;
    }

    public String getDealerEvaluatesInfo() {
        return dealerEvaluatesInfo;
    }

    public void setDealerEvaluatesInfo(String dealerEvaluatesInfo) {
        this.dealerEvaluatesInfo = dealerEvaluatesInfo;
    }

    public String getSaleRemark() {
        return saleRemark;
    }

    public void setSaleRemark(String saleRemark) {
        this.saleRemark = saleRemark;
    }

    public String getEvaluatesRemark() {
        return evaluatesRemark;
    }

    public void setEvaluatesRemark(String evaluatesRemark) {
        this.evaluatesRemark = evaluatesRemark;
    }

    public String getSubleaseInfo() {
        return subleaseInfo;
    }

    public void setSubleaseInfo(String subleaseInfo) {
        this.subleaseInfo = subleaseInfo;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }

    public String getRepaymentFlag() {
        return repaymentFlag;
    }

    public void setRepaymentFlag(String repaymentFlag) {
        this.repaymentFlag = repaymentFlag;
    }

    public String getLostContactFlag() {
        return lostContactFlag;
    }

    public void setLostContactFlag(String lostContactFlag) {
        this.lostContactFlag = lostContactFlag;
    }

    public String getCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(String collectFlag) {
        this.collectFlag = collectFlag;
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

    public String getAfterLoanOpinion() {
        return afterLoanOpinion;
    }

    public void setAfterLoanOpinion(String afterLoanOpinion) {
        this.afterLoanOpinion = afterLoanOpinion;
    }

    public String getCheckOpinion() {
        return checkOpinion;
    }

    public void setCheckOpinion(String checkOpinion) {
        this.checkOpinion = checkOpinion;
    }

    public String getRiskRemark() {
        return riskRemark;
    }

    public void setRiskRemark(String riskRemark) {
        this.riskRemark = riskRemark;
    }

    public String getGpsArea() {
        return gpsArea;
    }

    public void setGpsArea(String gpsArea) {
        this.gpsArea = gpsArea;
    }

    public String getCollectOpinionConfirm() {
        return collectOpinionConfirm;
    }

    public void setCollectOpinionConfirm(String collectOpinionConfirm) {
        this.collectOpinionConfirm = collectOpinionConfirm;
    }

    public String getProcessMethod() {
        return processMethod;
    }

    public void setProcessMethod(String processMethod) {
        this.processMethod = processMethod;
    }

    public boolean checkEntity() {
        if (StringUtil.isNull(orderId)) {
            return false;
        }
        print();
        return true;
    }

    @Override
    public String toString() {
        return "InstManageCarInfoEntity [orderId=" + orderId + ", status=" + status + ", afterLoanDate="
                + afterLoanDate
                + ", trailerReason=" + trailerReason + ", intoGarageDate=" + intoGarageDate + ", lostContactReason="
                + lostContactReason + ", assessAmt=" + assessAmt + ", hqEvaluatesAmt=" + hqEvaluatesAmt
                + ", hqCheckAmt=" + hqCheckAmt + ", shouldPayAmt=" + shouldPayAmt + ", dealAmt=" + dealAmt
                + ", receivedAmt=" + receivedAmt + ", serialNumber=" + serialNumber + ", intoGarageRemark="
                + intoGarageRemark + ", dealerEvaluatesInfo=" + dealerEvaluatesInfo + ", saleRemark=" + saleRemark
                + ", evaluatesRemark=" + evaluatesRemark + ", subleaseInfo=" + subleaseInfo + ", returnInfo="
                + returnInfo + "]";
    }
}
