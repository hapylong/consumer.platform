/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2018年1月18日 10:49:41
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.merchant;

import com.iqb.consumer.data.layer.bean.BaseEntity;

@SuppressWarnings("serial")
public class PayConfBean extends BaseEntity {

    private String merchantNo;// 商户号
    private String merchantName;// 商户名称
    private String bizOwner;// 业务主体
    private String payWay;// 支付方式
    private String gateWay;// 支付URL地址
    private String service;// 服务值
    private String vSon;//
    private String merchantId;// 支付商户
    private String key;// 密钥
    private String secId;// 加密ID
    private String certPath;// 公钥地址
    private String prikeyPath;// 私钥地址

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getBizOwner() {
        return bizOwner;
    }

    public void setBizOwner(String bizOwner) {
        this.bizOwner = bizOwner;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getGateWay() {
        return gateWay;
    }

    public void setGateWay(String gateWay) {
        this.gateWay = gateWay;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getvSon() {
        return vSon;
    }

    public void setvSon(String vSon) {
        this.vSon = vSon;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getPrikeyPath() {
        return prikeyPath;
    }

    public void setPrikeyPath(String prikeyPath) {
        this.prikeyPath = prikeyPath;
    }

}
