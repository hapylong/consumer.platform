package com.iqb.consumer.batch.data.pojo;

/**
 * 风控表bean
 */
public class RiskInfoBean {

    private String id;// 主键id
    private String regId;// 注册号主要是手机号
    private Integer riskType;// 商户类型 商户类型：
    private String checkInfo;// 风控信息，需要进行压缩json格式
    private String step1;// 风控采集第一步信息
    private String step2;// 风控采集第二步信息
    private String step3;// 风控采集第仨步信息
    private String step4;// 风控采集第四步信息

    public Integer getRiskType() {
        return riskType;
    }

    public void setRiskType(Integer riskType) {
        this.riskType = riskType;
    }

    public String getStep1() {
        return step1;
    }

    public void setStep1(String step1) {
        this.step1 = step1;
    }

    public String getStep2() {
        return step2;
    }

    public void setStep2(String step2) {
        this.step2 = step2;
    }

    public String getStep3() {
        return step3;
    }

    public void setStep3(String step3) {
        this.step3 = step3;
    }

    public String getStep4() {
        return step4;
    }

    public void setStep4(String step4) {
        this.step4 = step4;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

}
