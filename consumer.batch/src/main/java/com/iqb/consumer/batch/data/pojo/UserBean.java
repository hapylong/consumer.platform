package com.iqb.consumer.batch.data.pojo;

import java.util.Date;

/**
 * 
 * Description:普通用户Bean
 */
@SuppressWarnings("serial")
public class UserBean extends BaseEntity {

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

}
