package com.iqb.consumer.data.layer.bean.creditorinfo;

import com.iqb.consumer.data.layer.bean.BaseEntity;

@SuppressWarnings("serial")
public class CreditorInfoBean extends BaseEntity {

    private String orderId;// 订单id
    private String creditName;// 债权人姓名
    private String creditCardNo;// 债权人身份证号
    private String creditBankCard;// 债权人银行卡号
    private String creditBank;// 债权人开户银行
    private String creditPhone;// 债权人手机号

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
