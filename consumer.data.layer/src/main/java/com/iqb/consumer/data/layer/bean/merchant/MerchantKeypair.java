package com.iqb.consumer.data.layer.bean.merchant;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 商户密钥对
 * 
 * @author Yeoman
 * 
 */
@SuppressWarnings("serial")
public class MerchantKeypair extends BaseEntity {
    /**
     * 商户号
     */
    private String merchantNo;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 白名单列表
     */
    private String whiteList;

    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

}
