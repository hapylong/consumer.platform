package com.iqb.consumer.batch.data.pojo;

public class CallBackResponsePojo {
    public static final String CODE_SUCCESS = "00";
    private String result_code;
    private String result_msg;

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }
}
