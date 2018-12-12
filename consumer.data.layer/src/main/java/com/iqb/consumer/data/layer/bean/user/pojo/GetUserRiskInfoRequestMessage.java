package com.iqb.consumer.data.layer.bean.user.pojo;

import jodd.util.StringUtil;

public class GetUserRiskInfoRequestMessage {

    private String regId; // 用户手机号
    private Integer riskType; // 风控类型 1旅游,2新氧,3车

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Integer getRiskType() {
        return riskType;
    }

    public void setRiskType(Integer riskType) {
        this.riskType = riskType;
    }

    public boolean checkRequest() {
        if (StringUtil.isEmpty(regId) || riskType == null) {
            return false;
        }
        return true;
    }

}
