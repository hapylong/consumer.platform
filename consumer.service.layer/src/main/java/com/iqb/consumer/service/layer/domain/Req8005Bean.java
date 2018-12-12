package com.iqb.consumer.service.layer.domain;

/**
 * 8005接口系统请求参数bean
 * 
 * @author IQB-gxy
 * 
 */
public class Req8005Bean {
    /**
     * 应用类型, 渠道信息，iOS、Android等
     */
    private String appType;

    /**
     * 版本, 渠道版本信息
     */
    private String version;

    /**
     * 登录网点代码
     */
    private String lgnMerchCode;

    /**
     * 登录操作员代码
     */
    private String lgnUserCode;

    /**
     * 请求类型
     */
    private String rtype;

    /**
     * 动作代码 0-新增 1-查询 2-修改 3-删除
     */
    private String actCode;

    /**
     * 系统跟踪号 保证当日不重复，建议值YYYYMMDD-XXXXXX（8位年月日-6位数字）
     */
    private String traceNo;

    /**
     * 商户号
     */
    private String merchCode;

    /**
     * 商户简称
     */
    private String merchShortName;

    /**
     * 商户地址
     */
    private String merchAddr;

    /**
     * 商户地址
     */
    private String passwd;

    /**
     * 分期计划
     */
    private String instalNo;

    /**
     * 逾期利率
     */
    private String overdueRate;

    /**
     * 逾期手续费
     */
    private String overdueFee;

    /**
     * 逾期利率模式
     */
    private String overdueType;

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

    public String getLgnMerchCode() {
        return lgnMerchCode;
    }

    public void setLgnMerchCode(String lgnMerchCode) {
        this.lgnMerchCode = lgnMerchCode;
    }

    public String getLgnUserCode() {
        return lgnUserCode;
    }

    public void setLgnUserCode(String lgnUserCode) {
        this.lgnUserCode = lgnUserCode;
    }

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getMerchCode() {
        return merchCode;
    }

    public void setMerchCode(String merchCode) {
        this.merchCode = merchCode;
    }

    public String getMerchShortName() {
        return merchShortName;
    }

    public void setMerchShortName(String merchShortName) {
        this.merchShortName = merchShortName;
    }

    public String getMerchAddr() {
        return merchAddr;
    }

    public void setMerchAddr(String merchAddr) {
        this.merchAddr = merchAddr;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getInstalNo() {
        return instalNo;
    }

    public void setInstalNo(String instalNo) {
        this.instalNo = instalNo;
    }

    public String getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(String overdueRate) {
        this.overdueRate = overdueRate;
    }

    public String getOverdueFee() {
        return overdueFee;
    }

    public void setOverdueFee(String overdueFee) {
        this.overdueFee = overdueFee;
    }

    public String getOverdueType() {
        return overdueType;
    }

    public void setOverdueType(String overdueType) {
        this.overdueType = overdueType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
