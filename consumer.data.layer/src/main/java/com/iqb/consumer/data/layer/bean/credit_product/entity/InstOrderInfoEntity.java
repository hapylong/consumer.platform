package com.iqb.consumer.data.layer.bean.credit_product.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.iqb.consumer.common.constant.IsEmptyCheck;
import com.iqb.consumer.common.constant.IsEmptyCheck.CheckGroup;
import com.iqb.consumer.data.layer.bean.EntityUtil;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.PlanDetailsPojo;
import com.iqb.consumer.data.layer.bean.credit_product.request.GetPlanDetailsRequestMessage;
import com.iqb.consumer.data.layer.bean.credit_product.request.GuaranteePledgeInfoRequestMessage;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.PersistOrderDetailsRequestPojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.RecalculateAmtPojo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;

@Table(name = "inst_orderinfo")
public class InstOrderInfoEntity extends EntityUtil {
    private final int PRE_AMT_STATUS_PAID = 2; // 已付款

    public enum OrderInfoType {
        EQUAL, UNVEN, UPDATE
    }

    private Long id;

    private String orderId;

    private Long userId;

    private String regId;

    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String merchantNo;

    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String bizType;

    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String orderName;

    private BigDecimal orderAmt;

    @IsEmptyCheck(groupCode = CheckGroup.A)
    private Integer orderItems;

    private BigDecimal preAmt;

    private Integer preAmtStatus;

    private Integer status;

    private Integer wfStatus;

    private Integer riskStatus;

    private Integer refundFlag;

    private BigDecimal margin;

    private BigDecimal downPayment;

    private BigDecimal serviceFee;

    private Long planId;

    private Long qrCodeId;

    private Integer takePayment;

    private Integer feeYear;

    private BigDecimal feeAmount;

    private BigDecimal monthInterest;

    private Integer chargeWay;

    private String procInstId;

    private Double fee;

    private BigDecimal sumMoney;

    @IsEmptyCheck(groupCode = CheckGroup.A)
    private BigDecimal applyAmt;

    private BigDecimal gpsAmt;

    private String assessPrice;

    private String orderNo;

    private String version;

    private Date createTime;

    private Date updateTime;

    private Byte contractStatus;

    private String orderRemark;

    private String riskRetRemark;

    private String loanInterval;

    private Integer backFlag;

    private String employeeID;

    private String employeeName;

    private Integer orderItemsUnit;

    private BigDecimal breachAmt;
    private BigDecimal archivesAmt;
    private BigDecimal registAmt;
    private BigDecimal authenticateAmt;
    private BigDecimal stopAmt;
    private String transctionNo;
    private Integer feeType;

    private BigDecimal receivedPreAmt;
    private BigDecimal loanAmt;
    private String assessSuggest;
    private String finalRiskAdvice;
    private String proInfo;
    private Double gpsTrafficFee;
    private Date specialTime;
    private Date loanDate;
    private Date stageDate;
    private Integer carStatus;

    private String royaltyRate;// 提成比例
    private String applyLoanDate;// 申请放款日期
    private BigDecimal applyLoanAmt;// 申请放款金额
    private BigDecimal underAmt; // 线下费用
    
    public Integer getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(Integer carStatus) {
        this.carStatus = carStatus;
    }

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getBreachAmt() {
        return breachAmt;
    }

    public void setBreachAmt(BigDecimal breachAmt) {
        this.breachAmt = breachAmt;
    }

    public BigDecimal getArchivesAmt() {
        return archivesAmt;
    }

    public void setArchivesAmt(BigDecimal archivesAmt) {
        this.archivesAmt = archivesAmt;
    }

    public BigDecimal getRegistAmt() {
        return registAmt;
    }

    public void setRegistAmt(BigDecimal registAmt) {
        this.registAmt = registAmt;
    }

    public BigDecimal getAuthenticateAmt() {
        return authenticateAmt;
    }

    public void setAuthenticateAmt(BigDecimal authenticateAmt) {
        this.authenticateAmt = authenticateAmt;
    }

    public BigDecimal getStopAmt() {
        return stopAmt;
    }

    public void setStopAmt(BigDecimal stopAmt) {
        this.stopAmt = stopAmt;
    }

    public String getTransctionNo() {
        return transctionNo;
    }

    public void setTransctionNo(String transctionNo) {
        this.transctionNo = transctionNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
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

    public BigDecimal getPreAmt() {
        return preAmt;
    }

    public void setPreAmt(BigDecimal preAmt) {
        this.preAmt = preAmt;
    }

    public Integer getPreAmtStatus() {
        return preAmtStatus;
    }

    public void setPreAmtStatus(Integer preAmtStatus) {
        this.preAmtStatus = preAmtStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(Integer wfStatus) {
        this.wfStatus = wfStatus;
    }

    public Integer getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Integer riskStatus) {
        this.riskStatus = riskStatus;
    }

    public Integer getRefundFlag() {
        return refundFlag;
    }

    public void setRefundFlag(Integer refundFlag) {
        this.refundFlag = refundFlag;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(Long qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public Integer getTakePayment() {
        return takePayment;
    }

    public void setTakePayment(Integer takePayment) {
        this.takePayment = takePayment;
    }

    public Integer getFeeYear() {
        return feeYear;
    }

    public void setFeeYear(Integer feeYear) {
        this.feeYear = feeYear;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public Integer getChargeWay() {
        return chargeWay;
    }

    public void setChargeWay(Integer chargeWay) {
        this.chargeWay = chargeWay;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public BigDecimal getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public String getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(String assessPrice) {
        this.assessPrice = assessPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Byte contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getRiskRetRemark() {
        return riskRetRemark;
    }

    public void setRiskRetRemark(String riskRetRemark) {
        this.riskRetRemark = riskRetRemark;
    }

    public String getLoanInterval() {
        return loanInterval;
    }

    public void setLoanInterval(String loanInterval) {
        this.loanInterval = loanInterval;
    }

    public Integer getBackFlag() {
        return backFlag;
    }

    public void setBackFlag(Integer backFlag) {
        this.backFlag = backFlag;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getOrderItemsUnit() {
        return orderItemsUnit;
    }

    public void setOrderItemsUnit(Integer orderItemsUnit) {
        this.orderItemsUnit = orderItemsUnit;
    }

    public BigDecimal getReceivedPreAmt() {
        return receivedPreAmt;
    }

    public void setReceivedPreAmt(BigDecimal receivedPreAmt) {
        this.receivedPreAmt = receivedPreAmt;
    }

    public String getAssessSuggest() {
        return assessSuggest;
    }

    public void setAssessSuggest(String assessSuggest) {
        this.assessSuggest = assessSuggest;
    }

    public String getFinalRiskAdvice() {
        return finalRiskAdvice;
    }

    public void setFinalRiskAdvice(String finalRiskAdvice) {
        this.finalRiskAdvice = finalRiskAdvice;
    }

    public String getProInfo() {
        return proInfo;
    }

    public void setProInfo(String proInfo) {
        this.proInfo = proInfo;
    }

    public Double getGpsTrafficFee() {
        return gpsTrafficFee;
    }

    public void setGpsTrafficFee(Double gpsTrafficFee) {
        this.gpsTrafficFee = gpsTrafficFee;
    }

    public Date getSpecialTime() {
        return specialTime;
    }

    public void setSpecialTime(Date specialTime) {
        this.specialTime = specialTime;
    }

    public int getPRE_AMT_STATUS_PAID() {
        return PRE_AMT_STATUS_PAID;
    }

    public BigDecimal getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(BigDecimal loanAmt) {
        this.loanAmt = loanAmt;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getStageDate() {
        return stageDate;
    }

    public void setStageDate(Date stageDate) {
        this.stageDate = stageDate;
    }
    
    
    public String getRoyaltyRate() {
        return royaltyRate;
    }

    public void setRoyaltyRate(String royaltyRate) {
        this.royaltyRate = royaltyRate;
    }

    public String getApplyLoanDate() {
        return applyLoanDate;
    }

    public void setApplyLoanDate(String applyLoanDate) {
        this.applyLoanDate = applyLoanDate;
    }

    public BigDecimal getApplyLoanAmt() {
        return applyLoanAmt;
    }

    public void setApplyLoanAmt(BigDecimal applyLoanAmt) {
        this.applyLoanAmt = applyLoanAmt;
    }

    public BigDecimal getUnderAmt() {
        return underAmt;
    }

    public void setUnderAmt(BigDecimal underAmt) {
        this.underAmt = underAmt;
    }


    public static final String SPECIAL_ORDER_IDENTIFIER = "X";
    public static final Long INIT_PLAN_ID = null;
    private static final String INIT_TYPE_X = "2300";
    private static final int INIT_ORDER_ITEMS_X = 0;
    private static final int INIT_WF_STATUS_X = 41;
    private static final int UPDATE_WF_STATUS_X = 5;
    private static final String PREFIX_INIT_ORDER_NAME_X = "车秒贷-";
    private static final String INIT_AMT_LINE_X = null;
    private static final int INVISIBLE_STATUS_X = 2;
    private static final int VISIBLE_STATUS_X = 1;

    public void createEntityX(PlanDetailsPojo pdp, GuaranteePledgeInfoRequestMessage gpir) {
        orderAmt = BigDecimal.ZERO;
        downPayment = BigDecimal.ZERO;
        feeAmount = BigDecimal.ZERO;
        margin = BigDecimal.ZERO;
        monthInterest = BigDecimal.ZERO;
        preAmt = BigDecimal.ZERO;
        serviceFee = BigDecimal.ZERO;
        orderId = orderId + SPECIAL_ORDER_IDENTIFIER;
        assessPrice = gpir.getAssessPrice() + "";
        loanInterval = INIT_AMT_LINE_X;
        createTime = new Date();
        updateTime = new Date();
        status = INVISIBLE_STATUS_X;
        planId = INIT_PLAN_ID;

        bizType = INIT_TYPE_X;
        orderName = PREFIX_INIT_ORDER_NAME_X + orderName;
        orderItems = INIT_ORDER_ITEMS_X;
        procInstId = null;
        wfStatus = INIT_WF_STATUS_X;
    }

    public void updateEntityUNVEN(PlanDetailsPojo pdp,
            GuaranteePledgeInfoRequestMessage gpir, boolean CHOICE_CREDIT_LOAN) {
        downPayment = pdp.getDownPayment();
        feeAmount = pdp.getFeeAmount();
        margin = pdp.getMargin();
        monthInterest = pdp.getMonthInterest();
        preAmt = pdp.getPreAmt();
        serviceFee = pdp.getServiceFee();
        updateTime = new Date();
        assessPrice = gpir.getAssessPrice() + "";

        if (CHOICE_CREDIT_LOAN) {
            loanInterval = gpir.getAmtLines();
        }
    }

    public void updateEntityEQUAL(GuaranteePledgeInfoRequestMessage gpir) {
        assessPrice = gpir.getAssessPrice() + "";
        updateTime = new Date();
    }

    public void updateEntityX(PlanDetailsPojo pdp,
            GetPlanDetailsRequestMessage gdrm) {
        if (gdrm.getOrderAmt().compareTo(BigDecimal.ZERO) > 0) {
            status = VISIBLE_STATUS_X;
            wfStatus = UPDATE_WF_STATUS_X;
        }
        downPayment = pdp.getDownPayment();
        feeAmount = pdp.getFeeAmount();
        margin = pdp.getMargin();
        monthInterest = pdp.getMonthInterest();
        preAmt = pdp.getPreAmt();
        serviceFee = pdp.getServiceFee();
        orderItems = pdp.getOrderItems();
        orderAmt = pdp.getSbAmt();
        // orderAmt = gdrm.getOrderAmt();
        planId = gdrm.getPlanId();
        updateTime = new Date();
    }

    public void updateDandelionEntity(RecalculateAmtPojo rap, PersistOrderDetailsRequestPojo pdrp, PlanBean pb) {
        orderId = pdrp.getOrderId();
        applyAmt = pdrp.getOrderAmt(); // 暂做申请金额
        planId = pdrp.getPlanId();
        bizType = pdrp.getBizType();

        preAmt = rap.getSfkTotalAmt(); // 首付款合计
        orderAmt = rap.getSbAmt(); // 暂做上标金额
        assessPrice = rap.getFkAmt().toString();
        monthInterest = rap.getYgAmt();
        margin = rap.getBzAmt();
        downPayment = rap.getSfkAmt();
        serviceFee = rap.getFwAmt();
        feeAmount = rap.getSsxAmt();
        orderItems = pb.getInstallPeriods();
        takePayment = pb.getTakePayment();
        feeYear = pb.getFeeYear();
        fee = pb.getFeeRatio();
        updateTime = new Date();
    }

    @Override
    public String toString() {
        return "InstOrderInfoEntity [PRE_AMT_STATUS_PAID=" + PRE_AMT_STATUS_PAID + ", id=" + id + ", orderId="
                + orderId
                + ", userId=" + userId + ", regId=" + regId + ", merchantNo=" + merchantNo + ", bizType=" + bizType
                + ", orderName=" + orderName + ", orderAmt=" + orderAmt + ", orderItems=" + orderItems + ", preAmt="
                + preAmt + ", preAmtStatus=" + preAmtStatus + ", status=" + status + ", wfStatus=" + wfStatus
                + ", riskStatus=" + riskStatus + ", refundFlag=" + refundFlag + ", margin=" + margin + ", downPayment="
                + downPayment + ", serviceFee=" + serviceFee + ", planId=" + planId + ", qrCodeId=" + qrCodeId
                + ", takePayment=" + takePayment + ", feeYear=" + feeYear + ", feeAmount=" + feeAmount
                + ", monthInterest=" + monthInterest + ", chargeWay=" + chargeWay + ", procInstId=" + procInstId
                + ", fee=" + fee + ", sumMoney=" + sumMoney + ", applyAmt=" + applyAmt + ", gpsAmt=" + gpsAmt
                + ", assessPrice=" + assessPrice + ", orderNo=" + orderNo + ", version=" + version + ", createTime="
                + createTime + ", updateTime=" + updateTime + ", contractStatus=" + contractStatus + ", orderRemark="
                + orderRemark + ", riskRetRemark=" + riskRetRemark + ", loanInterval=" + loanInterval + ", backFlag="
                + backFlag + ", employeeID=" + employeeID + ", employeeName=" + employeeName + ", orderItemsUnit="
                + orderItemsUnit + ", breachAmt=" + breachAmt + ", archivesAmt=" + archivesAmt + ", registAmt="
                + registAmt + ", authenticateAmt=" + authenticateAmt + ", stopAmt=" + stopAmt + ", transctionNo="
                + transctionNo + ", feeType=" + feeType + ", receivedPreAmt=" + receivedPreAmt + ", loanAmt=" + loanAmt
                + ", assessSuggest=" + assessSuggest + ", finalRiskAdvice=" + finalRiskAdvice + ", proInfo=" + proInfo
                + ", gpsTrafficFee=" + gpsTrafficFee + ", specialTime=" + specialTime + ", loanDate=" + loanDate
                + ", stageDate=" + stageDate + ", carStatus=" + carStatus + "]";
    }
}
