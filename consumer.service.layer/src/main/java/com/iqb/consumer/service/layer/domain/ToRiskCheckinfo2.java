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
public class ToRiskCheckinfo2 {

    @JsonProperty("realname")
    private String realName;

    @JsonProperty("idcard")
    private String idCard;

    @JsonProperty("address")
    private String address;

    @JsonProperty("bankCard")
    private String bankCard;

    @JsonProperty("relation1")
    private String relation1;

    @JsonProperty("rName1")
    private String rName1;

    @JsonProperty("tel1")
    private String tel1;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
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

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

}
