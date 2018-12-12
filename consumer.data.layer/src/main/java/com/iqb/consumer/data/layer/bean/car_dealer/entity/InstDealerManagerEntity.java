package com.iqb.consumer.data.layer.bean.car_dealer.entity;

import javax.persistence.Table;

import com.iqb.consumer.data.layer.bean.BaseEntity;

@Table(name = "inst_dealermanage")
public class InstDealerManagerEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String custChannels; // 客户渠道
    private String custChannelsName;// 客户渠道名称
    private String sourceCar;// 车来源
    private String sourceCarName;// 车来源名称
    private String address;// 地址
    private String contactMethod;// 联系方式
    private String maritalStatus;// 婚姻状态
    private String contactName;// 联系名称
    private String contactMobile;// 联系人电话
    private String contactAddr;// 联系人住址
    private Byte status;
    private Integer version;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustChannels() {
        return custChannels;
    }

    public void setCustChannels(String custChannels) {
        this.custChannels = custChannels;
    }

    public String getCustChannelsName() {
        return custChannelsName;
    }

    public void setCustChannelsName(String custChannelsName) {
        this.custChannelsName = custChannelsName;
    }

    public String getSourceCar() {
        return sourceCar;
    }

    public void setSourceCar(String sourceCar) {
        this.sourceCar = sourceCar;
    }

    public String getSourceCarName() {
        return sourceCarName;
    }

    public void setSourceCarName(String sourceCarName) {
        this.sourceCarName = sourceCarName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactMethod() {
        return contactMethod;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactAddr() {
        return contactAddr;
    }

    public void setContactAddr(String contactAddr) {
        this.contactAddr = contactAddr;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

}
