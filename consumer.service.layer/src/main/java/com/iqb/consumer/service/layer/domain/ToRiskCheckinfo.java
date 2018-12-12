package com.iqb.consumer.service.layer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Copyright 2016 aTool.org
 */
/**
 * Auto-generated: 2016-05-03 20:24:10
 * 
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class ToRiskCheckinfo {

    @JsonProperty("realname")
    private String realName;

    @JsonProperty("idcard")
    private String idCard;

    @JsonProperty("addprovince")
    private String addProvince;

    @JsonProperty("adddetails")
    private String addDetails;

    @JsonProperty("marriedstatus")
    private String marriedStatus;

    @JsonProperty("bankname")
    private String bankName;

    @JsonProperty("bankno")
    private String bankNo;

    private String job;

    private String company;

    private String income;

    @JsonProperty("incomepic")
    private String incomepic;

    @JsonProperty("otherincome")
    private String otherincome;

    private String education;

    @JsonProperty("mobelPhone")
    private String phone;

    @JsonProperty("serverpwd")
    private String serverpwd;

    @JsonProperty("contactrelation1")
    private String contactrelation1;

    @JsonProperty("contactname1")
    private String contactName1;

    @JsonProperty("contactphone1")
    private String contactPhone1;

    @JsonProperty("contactrelation2")
    private String contactrelation2;

    @JsonProperty("contactname2")
    private String contactName2;

    @JsonProperty("contactphone2")
    private String contactPhone2;

    @JsonProperty("historyname")
    private String historyname;

    @JsonProperty("projectname")
    private String projectname;

    @JsonProperty("insuranceid")
    private String insuranceid;

    @JsonProperty("insurancepwd")
    private String insurancepwd;

    @JsonProperty("fundid")
    private String fundid;

    @JsonProperty("fundpwd")
    private String fundpwd;

    public ToRiskCheckinfo() {}

    public ToRiskCheckinfo(CheckInfoBean checkinfoBean) {
        this.marriedStatus = checkinfoBean.getMarriedStatus();
        this.addProvince = checkinfoBean.getAddProvince();
        this.company = checkinfoBean.getCompany();
        this.income = checkinfoBean.getIncome();
        this.education = checkinfoBean.getCulturalLevel();
        this.serverpwd = checkinfoBean.getServerPWD();
        this.contactName1 = checkinfoBean.getContactName1();
        this.contactPhone1 = checkinfoBean.getContactMobel1();
        this.contactName2 = checkinfoBean.getContactName2();
        this.contactPhone2 = checkinfoBean.getContactMobel2();
    }

    public String getInsuranceid() {
        return insuranceid;
    }

    public void setInsuranceid(String insuranceid) {
        this.insuranceid = insuranceid;
    }

    public String getInsurancepwd() {
        return insurancepwd;
    }

    public void setInsurancepwd(String insurancepwd) {
        this.insurancepwd = insurancepwd;
    }

    public String getFundid() {
        return fundid;
    }

    public void setFundid(String fundid) {
        this.fundid = fundid;
    }

    public String getFundpwd() {
        return fundpwd;
    }

    public void setFundpwd(String fundpwd) {
        this.fundpwd = fundpwd;
    }

    public String getHistoryname() {
        return historyname;
    }

    public void setHistoryname(String historyname) {
        this.historyname = historyname;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public void setRealname(String realname) {
        this.realName = realname;
    }

    public String getRealname() {
        return realName;
    }

    public void setIdcard(String idcard) {
        this.idCard = idcard;
    }

    public String getIdcard() {
        return idCard;
    }

    public void setAddprovince(String addprovince) {
        this.addProvince = addprovince;
    }

    public String getAddprovince() {
        return addProvince;
    }

    public void setAdddetails(String adddetails) {
        this.addDetails = adddetails;
    }

    public String getAdddetails() {
        return addDetails;
    }

    public void setMarriedstatus(String marriedstatus) {
        this.marriedStatus = marriedstatus;
    }

    public String getMarriedstatus() {
        return marriedStatus;
    }

    public void setBankname(String bankname) {
        this.bankName = bankname;
    }

    public String getBankname() {
        return bankName;
    }

    public void setBankno(String bankno) {
        this.bankNo = bankno;
    }

    public String getBankno() {
        return bankNo;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getIncome() {
        return income;
    }

    public void setIncomepic(String incomepic) {
        this.incomepic = incomepic;
    }

    public String getIncomepic() {
        return incomepic;
    }

    public void setOtherincome(String otherincome) {
        this.otherincome = otherincome;
    }

    public String getOtherincome() {
        return otherincome;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducation() {
        return education;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setServerpwd(String serverpwd) {
        this.serverpwd = serverpwd;
    }

    public String getServerpwd() {
        return serverpwd;
    }

    public void setContactrelation1(String contactrelation1) {
        this.contactrelation1 = contactrelation1;
    }

    public String getContactrelation1() {
        return contactrelation1;
    }

    public void setContactname1(String contactname1) {
        this.contactName1 = contactname1;
    }

    public String getContactname1() {
        return contactName1;
    }

    public void setContactphone1(String contactphone1) {
        this.contactPhone1 = contactphone1;
    }

    public String getContactphone1() {
        return contactPhone1;
    }

    public void setContactrelation2(String contactrelation2) {
        this.contactrelation2 = contactrelation2;
    }

    public String getContactrelation2() {
        return contactrelation2;
    }

    public void setContactname2(String contactname2) {
        this.contactName2 = contactname2;
    }

    public String getContactname2() {
        return contactName2;
    }

    public void setContactphone2(String contactphone2) {
        this.contactPhone2 = contactphone2;
    }

    public String getContactphone2() {
        return contactPhone2;
    }

}
