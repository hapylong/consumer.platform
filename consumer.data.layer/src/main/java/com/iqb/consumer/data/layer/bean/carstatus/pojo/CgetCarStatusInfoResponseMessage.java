package com.iqb.consumer.data.layer.bean.carstatus.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class CgetCarStatusInfoResponseMessage {

    /** inst_orderinfo **/
    private String orderId;// 订单号
    private String regId;// 手机号
    private BigDecimal orderAmt; // 借款金额
    private Integer orderItems;// 总期数
    private BigDecimal monthInterest; // 月供
    private String orderName;// 品牌 + 车系
    private Date createTime; // 时间

    /** inst_user **/
    private String realName; // 姓名

    /** inst_authoritycard **/
    private String carNo;// 车架号；
    private String plate;// 车牌号
    /** inst_managecar_info **/
    private Long id;
    private String status;
    private Date intoGarageDate;
    private String trailerReason;
    private String intoGarageRemark;
    private String lostContactReason;
    private String afterLoanDate;
    /** inst_merchantinfo **/
    private String merchantShortName;
    private String merchantId;
    /** inst_gpsinfo FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪 **/
    private String gpsStatus;
    private String remark;
    private String disposalScheme;
    private String billStatus;// 账单状态
    private int curItems;// 当前账单
    private String processStatus;// 贷后状态

    public String getAfterLoanDate() {
        return afterLoanDate;
    }

    public void setAfterLoanDate(String afterLoanDate) {
        this.afterLoanDate = afterLoanDate;
    }

    public String getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(String gpsStatus) {
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Date getIntoGarageDate() {
        return intoGarageDate;
    }

    public void setIntoGarageDate(Date intoGarageDate) {
        this.intoGarageDate = intoGarageDate;
    }

    public String getTrailerReason() {
        return trailerReason;
    }

    public void setTrailerReason(String trailerReason) {
        this.trailerReason = trailerReason;
    }

    public String getIntoGarageRemark() {
        return intoGarageRemark;
    }

    public void setIntoGarageRemark(String intoGarageRemark) {
        this.intoGarageRemark = intoGarageRemark;
    }

    public String getLostContactReason() {
        return lostContactReason;
    }

    public void setLostContactReason(String lostContactReason) {
        this.lostContactReason = lostContactReason;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public int getCurItems() {
        return curItems;
    }

    public void setCurItems(int curItems) {
        this.curItems = curItems;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

}
