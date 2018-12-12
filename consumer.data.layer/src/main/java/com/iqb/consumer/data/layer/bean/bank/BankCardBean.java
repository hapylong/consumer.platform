package com.iqb.consumer.data.layer.bean.bank;

import com.iqb.consumer.common.constant.IsEmptyCheck;
import com.iqb.consumer.common.constant.IsEmptyCheck.CheckGroup;
import com.iqb.consumer.data.layer.bean.BaseEntity;

@SuppressWarnings("serial")
public class BankCardBean extends BaseEntity {

    private Long userId;// 注册号

    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String bankCardNo;// 银行卡号

    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String bankMobile;// 预留手机号

    private String bankName;// 银行名
    private String bankCode;// 银行编号
    private int status;// 状态 :1,移除卡，2，正常卡 3，激活卡

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
