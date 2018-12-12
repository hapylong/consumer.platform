package com.iqb.consumer.asset.allocation.assetallocine.db.entity;

import java.util.Date;

import javax.persistence.Table;

@Table(name = "jys_assetallocation")
public class AssetAllocationEntity {
    private long id;
    private String bOrderId; // 拆分后的订单号
    private String orderId; // 原始订单号
    private Short channel; // 分配渠道
    private String proName; // 项目名称
    private Date proBeginDate; // 项目开始时间
    private String bakOrgan; // 备案机构
    private String url; // 摘牌网址
    private String proDetail; // 项目概况
    private String tranCondition; // 转让条件transferConditions
    private String safeWay; // SafeguardWay
    private Integer version; // 版本
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间
    private int deleteFlag;// 资产分配删除标识 0 未删除 1 删除
    private long jysOrderId;// 交易所订单id

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBOrderId() {
        return bOrderId;
    }

    public void setBOrderId(String bOrderId) {
        this.bOrderId = bOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Short getChannel() {
        return channel;
    }

    public void setChannel(Short channel) {
        this.channel = channel;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Date getProBeginDate() {
        return proBeginDate;
    }

    public void setProBeginDate(Date proBeginDate) {
        this.proBeginDate = proBeginDate;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
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

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public long getJysOrderId() {
        return jysOrderId;
    }

    public void setJysOrderId(long jysOrderId) {
        this.jysOrderId = jysOrderId;
    }

}
