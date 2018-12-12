/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午2:18:54
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.bank;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class BaseBankInfoBean extends BaseEntity {
    private String bankName;// 银行名称
    private String bankCode;// 银行代码
    private double singleLimit;// 单次支付限额
    private double dayLimit;// 日限额

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public double getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(double singleLimit) {
        this.singleLimit = singleLimit;
    }

    public double getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(double dayLimit) {
        this.dayLimit = dayLimit;
    }

}
