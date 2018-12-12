package com.iqb.consumer.data.layer.bean.admin.request;

import jodd.util.StringUtil;

import com.iqb.consumer.data.layer.bean.BaseEntity;

public class ChatToChangePlanResponsePojo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String retCode;
    private String retMsg;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public boolean success() {
        return retCode.equals("success");
    }

    public boolean checkResponse() {
        print();
        if (StringUtil.isEmpty(retCode) || StringUtil.isEmpty(retMsg)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ChatToChangePlanResponsePojo [retCode=" + retCode + ", retMsg=" + retMsg + "]";
    }

}
