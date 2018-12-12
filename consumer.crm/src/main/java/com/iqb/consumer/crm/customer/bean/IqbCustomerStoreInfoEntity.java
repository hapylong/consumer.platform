package com.iqb.consumer.crm.customer.bean;

import javax.persistence.Table;

import com.iqb.consumer.crm.customer.bean.pojo.IqbCustomerInfoPojo;

@Table(name = "iqb_customer_store_info")
public class IqbCustomerStoreInfoEntity {
    public static final int FLAG_MASTER = 1;
    private Integer id;
    private String customerCode; // 客户编码
    private String creditorName; // 债权人姓名
    private String creditorIdNo; // 债权人身份证号
    private String creditorBankNo; // 债权人银行卡号
    private String creditorBankName; // 债权人开户银行
    private String creditorPhone; // 债权人手机号
    private String guaranteeCorporationCode; // 所属担保公司编码
    private String guaranteeCorporationName; // 所属担保公司名称
    private Integer hasAuthed; // 是否已做鉴权
    private String authChannel; // 鉴权渠道
    private Integer authResult; // 鉴权结果
    private Integer status; // 状态
    private Integer deleteFlag; // 删除标识
    private String creditorOtherInfo; // 其他债权人信息

    private Integer flag; // 标识 非表中字段

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorIdNo() {
        return creditorIdNo;
    }

    public void setCreditorIdNo(String creditorIdNo) {
        this.creditorIdNo = creditorIdNo;
    }

    public String getCreditorBankNo() {
        return creditorBankNo;
    }

    public void setCreditorBankNo(String creditorBankNo) {
        this.creditorBankNo = creditorBankNo;
    }

    public String getCreditorBankName() {
        return creditorBankName;
    }

    public void setCreditorBankName(String creditorBankName) {
        this.creditorBankName = creditorBankName;
    }

    public String getCreditorPhone() {
        return creditorPhone;
    }

    public void setCreditorPhone(String creditorPhone) {
        this.creditorPhone = creditorPhone;
    }

    public String getGuaranteeCorporationCode() {
        return guaranteeCorporationCode;
    }

    public void setGuaranteeCorporationCode(String guaranteeCorporationCode) {
        this.guaranteeCorporationCode = guaranteeCorporationCode;
    }

    public String getGuaranteeCorporationName() {
        return guaranteeCorporationName;
    }

    public void setGuaranteeCorporationName(String guaranteeCorporationName) {
        this.guaranteeCorporationName = guaranteeCorporationName;
    }

    public Integer getHasAuthed() {
        return hasAuthed;
    }

    public void setHasAuthed(Integer hasAuthed) {
        this.hasAuthed = hasAuthed;
    }

    public String getAuthChannel() {
        return authChannel;
    }

    public void setAuthChannel(String authChannel) {
        this.authChannel = authChannel;
    }

    public Integer getAuthResult() {
        return authResult;
    }

    public void setAuthResult(Integer authResult) {
        this.authResult = authResult;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCreditorOtherInfo() {
        return creditorOtherInfo;
    }

    public void setCreditorOtherInfo(String creditorOtherInfo) {
        this.creditorOtherInfo = creditorOtherInfo;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public void copy(IqbCustomerStoreInfoEntity icsi, IqbCustomerInfoPojo icip) {
        authChannel = icsi.getAuthChannel();
        authResult = icsi.getAuthResult();
        creditorBankName = icsi.getCreditorBankName();
        creditorBankNo = icsi.getCreditorBankNo();
        creditorIdNo = icsi.getCreditorIdNo();
        creditorName = icsi.getCreditorName();
        creditorOtherInfo = icsi.getCreditorOtherInfo();
        creditorPhone = icsi.getCreditorPhone();
        customerCode = icip.getCustomerCode();
        deleteFlag = icsi.getDeleteFlag();
        flag = icsi.getFlag();
        guaranteeCorporationCode = icip.getGuaranteeCorporationCode();
        guaranteeCorporationName = icip.getGuaranteeCorporationName();
        hasAuthed = icsi.getHasAuthed();
        id = icsi.getId();
        status = icsi.getStatus();
    }

    public void createMasterEntity(CustomerBean cb) {
        authChannel = cb.getAuthChannel();
        authResult = cb.getAuthResult() == null ? null : Integer.parseInt(cb.getAuthResult());
        creditorBankName = cb.getCreditorBankName();
        creditorBankNo = cb.getCreditorBankNo();
        creditorIdNo = cb.getCreditorIdNo();
        creditorName = cb.getCreditorName();
        creditorOtherInfo = cb.getCreditorOtherInfo();
        creditorPhone = cb.getCreditorPhone();
        customerCode = cb.getCustomerCode();
        deleteFlag = cb.getDeleteFlag() == null ? null : Integer.parseInt(cb.getDeleteFlag());
        flag = FLAG_MASTER;
        guaranteeCorporationCode = cb.getGuaranteeCorporationCode();
        guaranteeCorporationName = cb.getGuaranteeCorporationName();
        hasAuthed = cb.getHasAuthed() == null ? null : Integer.parseInt(cb.getHasAuthed());
        status = cb.getCreditorStatus() == null ? null : Integer.parseInt(cb.getCreditorStatus());
    }
}
