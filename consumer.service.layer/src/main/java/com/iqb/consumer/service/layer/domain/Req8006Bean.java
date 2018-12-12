package com.iqb.consumer.service.layer.domain;

/**
 * 8006接口系统请求参数bean
 * 
 * @author IQB-gxy
 * 
 */
public class Req8006Bean {

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
     * 商户列表
     */
    private String mercodeList;

    /**
     * 账单状态 1:已还款 2:未还款 3:已逾期
     */
    private String status;

    /**
     * 账单日期
     */
    private String stmtDate;

    /**
     * 页码
     */
    private String page;

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

    public String getSign() {
        return sign;
    }

    public String getMercodeList() {
        return mercodeList;
    }

    public void setMercodeList(String mercodeList) {
        this.mercodeList = mercodeList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getStmtDate() {
        return stmtDate;
    }

    public void setStmtDate(String stmtDate) {
        this.stmtDate = stmtDate;
    }
}
