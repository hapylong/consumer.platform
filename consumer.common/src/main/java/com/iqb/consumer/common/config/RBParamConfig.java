/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年10月11日 下午5:13:27
 * @version V1.0
 */
package com.iqb.consumer.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class RBParamConfig {

    @Value("${RB.PAYURL}")
    private String rbPayUrl;
    @Value("${RB.KEY}")
    private String rbKey;
    @Value("${RB.MERCHANTID}")
    private String rbMerchantId;
    @Value("${RB.PUBKEYURL}")
    private String pubKeyUrl;
    @Value("${RB.PRIKEYURL}")
    private String priKeyUrl;
    @Value("${RB.PRIKEYPWD}")
    private String priKeyPwd;
    @Value("${RB.SELLEREMAIL}")
    private String sellerEmail;
    @Value("${RB.VERSION}")
    private String rbVersion;
    @Value("${RB.NEWPAYURL}")
    private String rbNewPayUrl;

    public String getRbNewPayUrl() {
        return rbNewPayUrl;
    }

    public void setRbNewPayUrl(String rbNewPayUrl) {
        this.rbNewPayUrl = rbNewPayUrl;
    }

    public String getRbPayUrl() {
        return rbPayUrl;
    }

    public void setRbPayUrl(String rbPayUrl) {
        this.rbPayUrl = rbPayUrl;
    }

    public String getRbKey() {
        return rbKey;
    }

    public void setRbKey(String rbKey) {
        this.rbKey = rbKey;
    }

    public String getRbMerchantId() {
        return rbMerchantId;
    }

    public void setRbMerchantId(String rbMerchantId) {
        this.rbMerchantId = rbMerchantId;
    }

    public String getPubKeyUrl() {
        return pubKeyUrl;
    }

    public void setPubKeyUrl(String pubKeyUrl) {
        this.pubKeyUrl = pubKeyUrl;
    }

    public String getPriKeyUrl() {
        return priKeyUrl;
    }

    public void setPriKeyUrl(String priKeyUrl) {
        this.priKeyUrl = priKeyUrl;
    }

    public String getPriKeyPwd() {
        return priKeyPwd;
    }

    public void setPriKeyPwd(String priKeyPwd) {
        this.priKeyPwd = priKeyPwd;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getRbVersion() {
        return rbVersion;
    }

    public void setRbVersion(String rbVersion) {
        this.rbVersion = rbVersion;
    }
}
