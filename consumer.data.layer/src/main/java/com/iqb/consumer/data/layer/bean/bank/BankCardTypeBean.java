/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月19日 下午6:29:36
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.bank;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 对应 cf_banktype
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class BankCardTypeBean extends BaseEntity {

    private String bankType;
    private int status;// 1可用
    private int payType;// 支付类型

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
