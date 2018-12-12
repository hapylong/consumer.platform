package com.iqb.consumer.crm.customer.bean.pojo;

import com.iqb.consumer.common.utils.StringUtil;

public class UpdateCustomerInfoRequestPojo {

    private String orderId;
    private String creditName;
    private String creditCardNo;
    private String creditBankCard;
    private String creditBank;
    private String creditPhone;

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

    public boolean checkRequest() {
        System.out.println(toString());
        if (StringUtil.isEmpty(orderId) ||
                StringUtil.isEmpty(creditName) ||
                StringUtil.isEmpty(creditCardNo) ||
                StringUtil.isEmpty(creditBankCard) ||
                StringUtil.isEmpty(creditBank) ||
                StringUtil.isEmpty(creditPhone)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UpdateCustomerInfoRequestPojo [orderId=" + orderId + ", creditName=" + creditName + ", creditCardNo="
                + creditCardNo + ", creditBankCard=" + creditBankCard + ", creditBank=" + creditBank + ", creditPhone="
                + creditPhone + "]";
    }
}
