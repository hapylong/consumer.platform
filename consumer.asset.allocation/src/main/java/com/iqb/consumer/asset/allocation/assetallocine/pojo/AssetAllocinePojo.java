package com.iqb.consumer.asset.allocation.assetallocine.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class AssetAllocinePojo {
    /** jys_breakorderinfo **/
    private long id;
    private String bOrderId; // 期限id - > bOrderId
    private String orderId; // 资产包编号 -> orderId
    private Short orderItems; // 期数
    private BigDecimal bOrderAmt; // 拆分后的订单金额
    private String recordNum; // 备案号
    /** jys_orderinfo **/
    private Double fee; // 手续费率
    private Date expireDate; // 到期日
    private String bizType;
    /** jys_orderinfo add **/
    private String proName; // 项目名称
    private String bakOrgan; // 备案机构
    private String url; // 摘牌网址
    private String proDetail; // 项目概况
    private String tranCondition; // 转让条件transferConditions
    private String safeWay; // SafeguardWay

    /** jys_packinfo **/
    private String raiseInstitutions; // 募集机构
    private String guaranteeInstitution;// 担保机构
    private int breakId;
    private String proBeginDate;// 任务开始时间

    private long jysOrderId;
    private String channel;// 资金来源

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getbOrderId() {
        return bOrderId;
    }

    public void setbOrderId(String bOrderId) {
        this.bOrderId = bOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Short getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Short orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getbOrderAmt() {
        return bOrderAmt;
    }

    public void setbOrderAmt(BigDecimal bOrderAmt) {
        this.bOrderAmt = bOrderAmt;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getRaiseInstitutions() {
        return raiseInstitutions;
    }

    public void setRaiseInstitutions(String raiseInstitutions) {
        this.raiseInstitutions = raiseInstitutions;
    }

    public String getGuaranteeInstitution() {
        return guaranteeInstitution;
    }

    public void setGuaranteeInstitution(String guaranteeInstitution) {
        this.guaranteeInstitution = guaranteeInstitution;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getBakOrgan() {
        return bakOrgan;
    }

    public void setBakOrgan(String bakOrgan) {
        this.bakOrgan = bakOrgan;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProDetail() {
        return proDetail;
    }

    public void setProDetail(String proDetail) {
        this.proDetail = proDetail;
    }

    public String getTranCondition() {
        return tranCondition;
    }

    public void setTranCondition(String tranCondition) {
        this.tranCondition = tranCondition;
    }

    public String getSafeWay() {
        return safeWay;
    }

    public void setSafeWay(String safeWay) {
        this.safeWay = safeWay;
    }

    public int getBreakId() {
        return breakId;
    }

    public void setBreakId(int breakId) {
        this.breakId = breakId;
    }

    public String getProBeginDate() {
        return proBeginDate;
    }

    public void setProBeginDate(String proBeginDate) {
        this.proBeginDate = proBeginDate;
    }

    public long getJysOrderId() {
        return jysOrderId;
    }

    public void setJysOrderId(long jysOrderId) {
        this.jysOrderId = jysOrderId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
