package com.iqb.consumer.service.layer.domain;

/**
 * 8002接口系统请求参数bean
 * 
 * @author IQB-KF
 * 
 */
public class Req8002Bean {

    /**
     * 应用类型, 渠道信息，iOS、Android等
     */
    private String appType;

    /**
     * 版本, 渠道版本信息
     */
    private String version;

    /**
     * 系统跟踪号 保证当日不重复，建议值YYYYMMDD-XXXXXX（8位年月日-6位数字）
     */
    private String traceNo;

    /**
     * 账单年月 YYYYMM（为空表示最近一期账单）
     */
    private String stmtYearMonth;

    /**
     * 注册号 一般是手机或邮箱
     */
    private String regId;

    /**
     * 签名信息
     */
    private String sign;

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getStmtYearMonth() {
        return stmtYearMonth;
    }

    public void setStmtYearMonth(String stmtYearMonth) {
        this.stmtYearMonth = stmtYearMonth;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
