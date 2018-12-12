/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月13日 下午2:01:40
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.xfpay;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class PayInfoBean extends BaseEntity {

    private int userId;
    private String mobileNo;// 手机号
    private String bankType;// 银行卡类型
    private String bankNo;// 银行卡号
    private String cardNo;// 身份证
    private String realName;// 真实姓名
    private int status;

    public PayInfoBean() {}

    public PayInfoBean(int userId, String mobileNo, String bankType, String bankNo, String cardNo, String realName) {
        this.userId = userId;
        this.mobileNo = mobileNo;
        this.bankType = bankType;
        this.bankNo = bankNo;
        this.cardNo = cardNo;
        this.realName = realName;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
