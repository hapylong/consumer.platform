package com.iqb.consumer.data.layer.bean.user;

public class UserRiskInfo {
    private String real_name;// 姓名
    private String reg_id;// 手机号
    private String merchCode;// 商户号
    private String id_card;// 身份证
    private String pay_account;// 银行卡
    private String link_man_first;// 联系人1（亲属）
    private String link_man_first_phone;// 联系人1（亲属）联系方式
    private String link_man_second;// 联系人2（亲属）
    private String link_man_second_phone;// 联系人2（亲属）联系方式
    private String matital_status;// 婚姻状况
    private String permanent_address;// 常驻地址
    private Integer risk_type;// 风控类型
    private String bankName;// 开户银行
    private String bankMobile;// 银行预留手机号
    private String smsMobile; // 接收短信手机号码

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getPay_account() {
        return pay_account;
    }

    public void setPay_account(String pay_account) {
        this.pay_account = pay_account;
    }

    public String getLink_man_first() {
        return link_man_first;
    }

    public void setLink_man_first(String link_man_first) {
        this.link_man_first = link_man_first;
    }

    public String getLink_man_first_phone() {
        return link_man_first_phone;
    }

    public void setLink_man_first_phone(String link_man_first_phone) {
        this.link_man_first_phone = link_man_first_phone;
    }

    public String getLink_man_second() {
        return link_man_second;
    }

    public void setLink_man_second(String link_man_second) {
        this.link_man_second = link_man_second;
    }

    public String getLink_man_second_phone() {
        return link_man_second_phone;
    }

    public void setLink_man_second_phone(String link_man_second_phone) {
        this.link_man_second_phone = link_man_second_phone;
    }

    public String getMatital_status() {
        return matital_status;
    }

    public void setMatital_status(String matital_status) {
        this.matital_status = matital_status;
    }

    public String getPermanent_address() {
        return permanent_address;
    }

    public void setPermanent_address(String permanent_address) {
        this.permanent_address = permanent_address;
    }

    public String getMerchCode() {
        return merchCode;
    }

    public void setMerchCode(String merchCode) {
        this.merchCode = merchCode;
    }

    public Integer getRisk_type() {
        return risk_type;
    }

    public void setRisk_type(Integer risk_type) {
        this.risk_type = risk_type;
    }

    public String getSmsMobile() {
        return smsMobile;
    }

    public void setSmsMobile(String smsMobile) {
        this.smsMobile = smsMobile;
    }

}
