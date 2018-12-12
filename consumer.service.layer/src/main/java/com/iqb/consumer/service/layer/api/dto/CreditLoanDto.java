package com.iqb.consumer.service.layer.api.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class CreditLoanDto {
    private String merchantId;
    private String realName;
    private String regId;
    private String orderId;
    private String proType;
    private String idCard;
    private String bankCard;
    private String address;
    private String companyName;
    private String companyAddress;
    private String companyPhone;
    private String colleagues1 = "";
    private String colleagues2 = "";
    private String tel1 = "";
    private String tel2 = "";
    private String relation1 = "";
    @JSONField(name = "rName1")
    private String rName1;
    private String sex1 = "";
    private String phone1;
    private String relation2 = "";
    @JSONField(name = "rName2")
    private String rName2;
    private String sex2 = "";
    private String phone2;
    private String relation3 = "";
    @JSONField(name = "rName3")
    private String rName3;
    private String sex3 = "";
    private String phone3;
    private String creditNo; // 征信账号
    private String creditPasswd; // 征信密码
    private String creditCode;// 征信验证码

    private String bankMobile; // 银行预留手机号码
    private String optPasswd; // 运营商密码
    private String zmCreditScore; // 芝麻信用分

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getColleagues1() {
        return colleagues1;
    }

    public void setColleagues1(String colleagues1) {
        this.colleagues1 = colleagues1;
    }

    public String getColleagues2() {
        return colleagues2;
    }

    public void setColleagues2(String colleagues2) {
        this.colleagues2 = colleagues2;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getRelation1() {
        return relation1;
    }

    public void setRelation1(String relation1) {
        this.relation1 = relation1;
    }

    public String getRName1() {
        return rName1;
    }

    public void setRName1(String rName1) {
        this.rName1 = rName1;
    }

    public String getSex1() {
        return sex1;
    }

    public void setSex1(String sex1) {
        this.sex1 = sex1;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getRelation2() {
        return relation2;
    }

    public void setRelation2(String relation2) {
        this.relation2 = relation2;
    }

    public String getRName2() {
        return rName2;
    }

    public void setRName2(String rName2) {
        this.rName2 = rName2;
    }

    public String getSex2() {
        return sex2;
    }

    public void setSex2(String sex2) {
        this.sex2 = sex2;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getRelation3() {
        return relation3;
    }

    public void setRelation3(String relation3) {
        this.relation3 = relation3;
    }

    public String getRName3() {
        return rName3;
    }

    public void setRName3(String rName3) {
        this.rName3 = rName3;
    }

    public String getSex3() {
        return sex3;
    }

    public void setSex3(String sex3) {
        this.sex3 = sex3;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public String getCreditPasswd() {
        return creditPasswd;
    }

    public void setCreditPasswd(String creditPasswd) {
        this.creditPasswd = creditPasswd;
    }

    public String getrName1() {
        return rName1;
    }

    public void setrName1(String rName1) {
        this.rName1 = rName1;
    }

    public String getrName2() {
        return rName2;
    }

    public void setrName2(String rName2) {
        this.rName2 = rName2;
    }

    public String getrName3() {
        return rName3;
    }

    public void setrName3(String rName3) {
        this.rName3 = rName3;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }

    public String getOptPasswd() {
        return optPasswd;
    }

    public void setOptPasswd(String optPasswd) {
        this.optPasswd = optPasswd;
    }

    public String getZmCreditScore() {
        return zmCreditScore;
    }

    public void setZmCreditScore(String zmCreditScore) {
        this.zmCreditScore = zmCreditScore;
    }

}
