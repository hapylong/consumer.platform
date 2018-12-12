package com.iqb.consumer.data.layer.bean.jys;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * Description:交易所债权人信息表
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年11月3日下午6:09:09 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class JYSCreditInfoBean extends BaseEntity {
    /**
     * 债权人姓名
     */
    private String orderId;
    /**
     * 债权人姓名
     */
    private String creditName;

    /**
     * 债权人身份证号
     */
    private String creditCardNo;

    /**
     * 债权人银行卡号
     */
    private String creditBankCard;

    /**
     * 债权人开户银行
     */
    private String creditBank;

    /**
     * 债权人手机号
     */
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

}
