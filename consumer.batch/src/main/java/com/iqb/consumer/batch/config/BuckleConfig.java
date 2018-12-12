package com.iqb.consumer.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuckleConfig {

    @Value("${IQB_BUCKLE_CHANNAL}")
    private String iqbBuckleChannal; // 业务渠道编码（结算平台提供）

    @Value("${IQB_BUCKLE_PRIVATE_KEY}")
    private String iqbBucklePrivateKey; // 加密用，业务系统加密信息（结算平台提供）

    @Value("${IQB_BUCKLE_URL}")
    private String iqbBuckleUrl; // 结算中心请求 url

    @Value("${IQB_BUCKLE_CERT_PATH}")
    private String certPath; // 加密用，业务系统证书路径（自生成，并上传私钥及密码到结算平台，用来验证数据）

    @Value("${IQB_BUCKLE_APP_SECRET}")
    private String appSecret; // 业务系统安全码（结算平台提供）

    @Value("${CONSUMER.CALLBACK.URL}")
    private String callBackUrl;

    public String getIqbBuckleChannal() {
        return iqbBuckleChannal;
    }

    public void setIqbBuckleChannal(String iqbBuckleChannal) {
        this.iqbBuckleChannal = iqbBuckleChannal;
    }

    public String getIqbBucklePrivateKey() {
        return iqbBucklePrivateKey;
    }

    public void setIqbBucklePrivateKey(String iqbBucklePrivateKey) {
        this.iqbBucklePrivateKey = iqbBucklePrivateKey;
    }

    public String getIqbBuckleUrl() {
        return iqbBuckleUrl;
    }

    public void setIqbBuckleUrl(String iqbBuckleUrl) {
        this.iqbBuckleUrl = iqbBuckleUrl;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
