package com.iqb.consumer.crm.customer.bean.pojo;

import java.util.List;

import com.iqb.consumer.crm.customer.bean.IqbCustomerStoreInfoEntity;

public class IqbCustomerInfoPojo {

    private String customerCode;// 1006001
    private String customerName;//
    private String customerShortName;//
    private String province;// 吉林省
    private String city;// 松原市
    private String higherOrgName;// 消费金融
    private String belongsArea;//
    private String guaranteeCorporationName;// 松原市轮动方程汽车租赁有限公司
    private String guaranteeCorporationCode;//

    private List<IqbCustomerStoreInfoEntity> requestMessage;

    public List<IqbCustomerStoreInfoEntity> getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(List<IqbCustomerStoreInfoEntity> requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerShortName() {
        return customerShortName;
    }

    public void setCustomerShortName(String customerShortName) {
        this.customerShortName = customerShortName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHigherOrgName() {
        return higherOrgName;
    }

    public void setHigherOrgName(String higherOrgName) {
        this.higherOrgName = higherOrgName;
    }

    public String getBelongsArea() {
        return belongsArea;
    }

    public void setBelongsArea(String belongsArea) {
        this.belongsArea = belongsArea;
    }

    public String getGuaranteeCorporationName() {
        return guaranteeCorporationName;
    }

    public void setGuaranteeCorporationName(String guaranteeCorporationName) {
        this.guaranteeCorporationName = guaranteeCorporationName;
    }

    public String getGuaranteeCorporationCode() {
        return guaranteeCorporationCode;
    }

    public void setGuaranteeCorporationCode(String guaranteeCorporationCode) {
        this.guaranteeCorporationCode = guaranteeCorporationCode;
    }

}
