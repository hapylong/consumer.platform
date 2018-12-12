package com.iqb.consumer.common.exception;

public class DevDefineErrorMsgException extends Exception {
    private static final long serialVersionUID = 1L;

    private String errMsg;

    public DevDefineErrorMsgException(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

}
