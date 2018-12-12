package com.iqb.consumer.service.layer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckInfoBean {

    // 基本信息
    private String serverPWD; // 六位服务密码
    private String addProvince; // 常驻地址
    private String marriedStatus;// 婚姻状况
    private String culturalLevel;// 文化水平
    private String regAddr;// 户籍地址
    private String workStatus;// 工作状态
    private String company;// 工作单位
    private String income;// 收入
    private String occupation;// 职务-->旅游特有
    private String sameName;// 同行人-->旅游特有
    private String phoneName;// 同行人电话-->旅游特有
    private String passPort;// 护照号-->旅游特有
    private String bankCard;// 常用卡号-->旅游特有
    private String phoneNum;// 手机号-->旅游特有
    // 通讯信息
    private String contactName1;
    private String contactMobel1;
    private String contactName2;
    private String contactMobel2;
    // 身份证件
    private String idUrl;
    private String idUrl2;
    // 借款人手持身份证照片
    private String imgUrl3;// 手持身份证照片
    private String workIncomingUrl;// 工作收入
    private String otherIncomingUrl;// 其他收入
    private String bankWaterUrl;// 银行流水
    private String bankSrc;// 银行流水-->旅游特有 &隔开
    private String sameSrc;// 同行人关系证明-->旅游特有 &隔开
    // 医美新添
    private String permanentAddress;// 户籍地址

    public String getBankSrc() {
        return bankSrc;
    }

    public void setBankSrc(String bankSrc) {
        this.bankSrc = bankSrc;
    }

    public String getSameSrc() {
        return sameSrc;
    }

    public void setSameSrc(String sameSrc) {
        this.sameSrc = sameSrc;
    }

    public String getCulturalLevel() {
        return culturalLevel;
    }

    public void setCulturalLevel(String culturalLevel) {
        this.culturalLevel = culturalLevel;
    }

    public String getRegAddr() {
        return regAddr;
    }

    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getIdUrl() {
        return idUrl;
    }

    public void setIdUrl(String idUrl) {
        this.idUrl = idUrl;
    }

    public String getIdUrl2() {
        return idUrl2;
    }

    public void setIdUrl2(String idUrl2) {
        this.idUrl2 = idUrl2;
    }

    public String getImgUrl3() {
        return imgUrl3;
    }

    public void setImgUrl3(String imgUrl3) {
        this.imgUrl3 = imgUrl3;
    }

    public String getWorkIncomingUrl() {
        return workIncomingUrl;
    }

    public void setWorkIncomingUrl(String workIncomingUrl) {
        this.workIncomingUrl = workIncomingUrl;
    }

    public String getOtherIncomingUrl() {
        return otherIncomingUrl;
    }

    public void setOtherIncomingUrl(String otherIncomingUrl) {
        this.otherIncomingUrl = otherIncomingUrl;
    }

    public String getBankWaterUrl() {
        return bankWaterUrl;
    }

    public void setBankWaterUrl(String bankWaterUrl) {
        this.bankWaterUrl = bankWaterUrl;
    }

    public String getServerPWD() {
        return serverPWD;
    }

    public void setServerPWD(String serverPWD) {
        this.serverPWD = serverPWD;
    }

    public String getAddProvince() {
        return addProvince;
    }

    public void setAddProvince(String addProvince) {
        this.addProvince = addProvince;
    }

    public String getMarriedStatus() {
        return marriedStatus;
    }

    public void setMarriedStatus(String marriedStatus) {
        this.marriedStatus = marriedStatus;
    }

    public String getContactName1() {
        return contactName1;
    }

    public void setContactName1(String contactName1) {
        this.contactName1 = contactName1;
    }

    public String getContactMobel1() {
        return contactMobel1;
    }

    public void setContactMobel1(String contactMobel1) {
        this.contactMobel1 = contactMobel1;
    }

    public String getContactName2() {
        return contactName2;
    }

    public void setContactName2(String contactName2) {
        this.contactName2 = contactName2;
    }

    public String getContactMobel2() {
        return contactMobel2;
    }

    public void setContactMobel2(String contactMobel2) {
        this.contactMobel2 = contactMobel2;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getSameName() {
        return sameName;
    }

    public void setSameName(String sameName) {
        this.sameName = sameName;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPassPort() {
        return passPort;
    }

    public void setPassPort(String passPort) {
        this.passPort = passPort;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

}
