package com.iqb.consumer.manage.front.api;

/**
 * 鉴权数据
 * 
 * @author Yeoman
 * 
 */
public class AuthDto {
    private String cardNo;// 卡号
    private String owner;// 姓名
    private String certNo;// 证件号
    private String memberId;// 用户会员号
    private String phone;// 用户手机号

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
