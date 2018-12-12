package com.iqb.consumer.data.layer.bean.dealer;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 车商管理实体Bean对应inst_dealermanage
 * 
 * @author Yeoman
 * 
 */
@SuppressWarnings("serial")
public class DealerManage extends BaseEntity {
    private String orderId;// 订单号
    private String custChannels; // 客户渠道
    private String custChannelsName; // 客户渠道名称
    private String sourceCar;// 车商
    private String sourceCarName;// 车商名称
    private String address;// 地址
    private String contactMethod;// 联系方式
    private String maritalStatus;// 婚姻状态
    private String contactName;// 联系名称
    private String contactMobile;// 联系电话
    private String contactAddr;// 联系地址
    private String status;// 状态
    private String overdueFlag;// 逾期状态

    public String getOverdueFlag() {
        return overdueFlag;
    }

    public void setOverdueFlag(String overdueFlag) {
        this.overdueFlag = overdueFlag;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
