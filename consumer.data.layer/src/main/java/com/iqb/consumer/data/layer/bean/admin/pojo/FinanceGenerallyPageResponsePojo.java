package com.iqb.consumer.data.layer.bean.admin.pojo;

public class FinanceGenerallyPageResponsePojo<T> {
    private String status;
    private String errorMsg;

    private FinancePageResult<T> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public FinancePageResult getResult() {
        return result;
    }

    public void setResult(FinancePageResult result) {
        this.result = result;
    }
}
