package com.iqb.consumer.data.layer.bean.jys;

import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * Description:交易所用户Bean
 */
@SuppressWarnings("serial")
public class JYSUserBean extends BaseEntity {

    private String regId;// 注册号
    private String passWord;// 登录密码
    private String smsMobile;// 发送短信使用的手机号默认和regId一致
    private String status;// 账户状态(1,正常 2,注销)
    private String loginIp;// 最后登录IP
    private String realName;// 真实姓名
    private String idNo;// 身份证
    private String openId;// 微信OpenId
    private Date lastLoginTime;// 最后登录时间
    private int autoLogin;// 自动登录
    private String bankCardNo;// 银行卡号
    private String bankName;// 银行名
    /**
     * FINANCE-2283 交易所---债权人信息优化 2017-11-06 增加
     */
    private String creditName;// 债权人姓名
    private String creditCardNo;// 债权人身份证号
    private String creditBankCard;// 债权人银行卡号
    private String creditBank;// 债权人开户银行
    private String creditPhone;// 债权人手机号码

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getSmsMobile() {
        return smsMobile;
    }

    public void setSmsMobile(String smsMobile) {
        this.smsMobile = smsMobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(int autoLogin) {
        this.autoLogin = autoLogin;
    }

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getCreditBankCard() {
        return creditBankCard;
    }

    public void setCreditBankCard(String creditBankCard) {
        this.creditBankCard = creditBankCard;
    }

    public String getCreditBank() {
        return creditBank;
    }

    public void setCreditBank(String creditBank) {
        this.creditBank = creditBank;
    }

    public String getCreditPhone() {
        return creditPhone;
    }

    public void setCreditPhone(String creditPhone) {
        this.creditPhone = creditPhone;
    }

}
