package com.iqb.consumer.data.layer.bean.finance.pojo.f;

public class FinanceBillQueryResponsePojo {
    private String retCode;
    private String retMsg;

    private FinanceBillResult result;

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

    public FinanceBillResult getResult() {
        return result;
    }

    public void setResult(FinanceBillResult result) {
        this.result = result;
    }

}
