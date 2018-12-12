package com.iqb.consumer.data.layer.bean.user.pojo;

import com.iqb.consumer.common.utils.StringUtil;

public class GetUserRiskInfoResponsePojo {
    private String addProvince; // 地址
    private String contactName1; // 紧急联系人 1
    private String contactName2; // 紧急联系人2
    private String contactPhone1;// 联系人1 手机号
    private String contactPhone2;// 联系人2 手机号
    private String marriedStatus;// 婚姻状况
    private String idCard; // 身份证号

    public String getAddProvince() {
        return addProvince;
    }

    public void setAddProvince(String addProvince) {
        this.addProvince = addProvince;
    }

    public String getContactName1() {
        return contactName1;
    }

    public void setContactName1(String contactName1) {
        this.contactName1 = contactName1;
    }

    public String getContactName2() {
        return contactName2;
    }

    public void setContactName2(String contactName2) {
        this.contactName2 = contactName2;
    }

    public String getContactPhone1() {
        return contactPhone1;
    }

    public void setContactPhone1(String contactPhone1) {
        this.contactPhone1 = contactPhone1;
    }

    public String getContactPhone2() {
        return contactPhone2;
    }

    public void setContactPhone2(String contactPhone2) {
        this.contactPhone2 = contactPhone2;
    }

    public String getMarriedStatus() {
        return marriedStatus;
    }

    public void setMarriedStatus(String marriedStatus) {
        this.marriedStatus = marriedStatus;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public boolean checkResponse() {
        if (StringUtil.isEmpty(addProvince) ||
                StringUtil.isEmpty(contactName1) ||
                StringUtil.isEmpty(contactName2) ||
                StringUtil.isEmpty(contactPhone1) ||
                StringUtil.isEmpty(contactPhone2) ||
                StringUtil.isEmpty(marriedStatus)) {
            return false;
        }
        return true;
    }

}
