/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月12日 下午2:35:41
 * @version V1.0
 */
package com.iqb.consumer.service.layer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Configuration
public class ServiceParaConfig {

    @Value("${FES.URL.8002}")
    private String url_8002;
    @Value("${FES.URL.8003}")
    private String url_8003;
    @Value("${FES.URL.8005}")
    private String url_8005;
    @Value("${FES.URL.8006}")
    private String url_8006;
    @Value("${FES.PRIKEY.PATH}")
    private String priKeyPath;
    /******************************** 短信接口的相关配置 ************************************************************/
    @Value("${smsUrl}")
    private String smsUrl;
    @Value("${smsNameInfo}")
    private String smsNameInfo;
    @Value("${smsPswdInfo}")
    private String smsPswdInfo;
    @Value("${smsNeedStatusInfo}")
    private String smsNeedStatusInfo;
    @Value("${sendSms_overdue}")
    private String sendSms_overdue;
    @Value("${sendSms_repay}")
    private String sendSms_repay;
    @Value("${sendSms_preAmount_urge}")
    private String sendSms_preAmount_urge;
    @Value("${sendSms_deduct}")
    private String sendSms_deduct;

    public String getSendSms_repay() {
        return sendSms_repay;
    }

    public void setSendSms_repay(String sendSms_repay) {
        this.sendSms_repay = sendSms_repay;
    }

    public String getSendSms_preAmount_urge() {
        return sendSms_preAmount_urge;
    }

    public void setSendSms_preAmount_urge(String sendSms_preAmount_urge) {
        this.sendSms_preAmount_urge = sendSms_preAmount_urge;
    }

    public String getUrl_8003() {
        return url_8003;
    }

    public void setUrl_8003(String url_8003) {
        this.url_8003 = url_8003;
    }

    public String getUrl_8002() {
        return url_8002;
    }

    public void setUrl_8002(String url_8002) {
        this.url_8002 = url_8002;
    }

    public String getUrl_8005() {
        return url_8005;
    }

    public void setUrl_8005(String url_8005) {
        this.url_8005 = url_8005;
    }

    public String getUrl_8006() {
        return url_8006;
    }

    public void setUrl_8006(String url_8006) {
        this.url_8006 = url_8006;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public String getSmsNameInfo() {
        return smsNameInfo;
    }

    public void setSmsNameInfo(String smsNameInfo) {
        this.smsNameInfo = smsNameInfo;
    }

    public String getSmsPswdInfo() {
        return smsPswdInfo;
    }

    public void setSmsPswdInfo(String smsPswdInfo) {
        this.smsPswdInfo = smsPswdInfo;
    }

    public String getSmsNeedStatusInfo() {
        return smsNeedStatusInfo;
    }

    public void setSmsNeedStatusInfo(String smsNeedStatusInfo) {
        this.smsNeedStatusInfo = smsNeedStatusInfo;
    }

    public String getSendSms_overdue() {
        return sendSms_overdue;
    }

    public void setSendSms_overdue(String sendSms_overdue) {
        this.sendSms_overdue = sendSms_overdue;
    }

    public String getSendSms_deduct() {
        return sendSms_deduct;
    }

    public void setSendSms_deduct(String sendSms_deduct) {
        this.sendSms_deduct = sendSms_deduct;
    }

}
