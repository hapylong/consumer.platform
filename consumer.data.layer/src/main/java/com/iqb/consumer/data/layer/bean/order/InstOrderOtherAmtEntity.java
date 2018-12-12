package com.iqb.consumer.data.layer.bean.order;

import java.math.BigDecimal;

public class InstOrderOtherAmtEntity {

    private String orderId;

    private Integer online; // 1,线上 2线下

    private BigDecimal totalCost; // 费用合计

    private BigDecimal gpsAmt;

    private BigDecimal riskAmt;

    private BigDecimal taxAmt; // 购置税

    private BigDecimal serverAmt; // 车商服务费

    private BigDecimal otherAmt; // 其他费用

    private String assessMsgAmt; // 评估管理费
    private String inspectionAmt;// 考察费
    private String preAmtFlag; // 是否有前期费用 1 是 2 否

    private BigDecimal preBusinessTaxAmt;// 商业险

    public String getAssessMsgAmt() {
        return assessMsgAmt;
    }

    public void setAssessMsgAmt(String assessMsgAmt) {
        this.assessMsgAmt = assessMsgAmt;
    }

    public String getInspectionAmt() {
        return inspectionAmt;
    }

    public void setInspectionAmt(String inspectionAmt) {
        this.inspectionAmt = inspectionAmt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public BigDecimal getRiskAmt() {
        return riskAmt;
    }

    public void setRiskAmt(BigDecimal riskAmt) {
        this.riskAmt = riskAmt;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public BigDecimal getServerAmt() {
        return serverAmt;
    }

    public void setServerAmt(BigDecimal serverAmt) {
        this.serverAmt = serverAmt;
    }

    public BigDecimal getOtherAmt() {
        return otherAmt;
    }

    public void setOtherAmt(BigDecimal otherAmt) {
        this.otherAmt = otherAmt;
    }

    public String getPreAmtFlag() {
        return preAmtFlag;
    }

    public void setPreAmtFlag(String preAmtFlag) {
        this.preAmtFlag = preAmtFlag;
    }

    public BigDecimal getPreBusinessTaxAmt() {
        return preBusinessTaxAmt;
    }

    public void setPreBusinessTaxAmt(BigDecimal preBusinessTaxAmt) {
        this.preBusinessTaxAmt = preBusinessTaxAmt;
    }

    public void checkInfo() {
        if (assessMsgAmt == null || assessMsgAmt.equals("")) {
            assessMsgAmt = "0";
        }
        if (inspectionAmt == null || inspectionAmt.equals("")) {
            inspectionAmt = "0";
        }
        if (otherAmt != null) {
            if (otherAmt.compareTo(BigDecimal.ZERO) <= 0) {
                otherAmt = BigDecimal.ZERO;
            }
        } else {
            otherAmt = BigDecimal.ZERO;
        }
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月27日
     */
    @Override
    public String toString() {
        return "InstOrderOtherAmtEntity [orderId=" + orderId + ", online=" + online + ", totalCost=" + totalCost
                + ", gpsAmt=" + gpsAmt + ", riskAmt=" + riskAmt + ", taxAmt=" + taxAmt + ", serverAmt=" + serverAmt
                + ", otherAmt=" + otherAmt + ", assessMsgAmt=" + assessMsgAmt + ", inspectionAmt=" + inspectionAmt
                + ", preAmtFlag=" + preAmtFlag + "]";
    }

}
