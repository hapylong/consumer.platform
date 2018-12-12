package com.iqb.consumer.data.layer.bean.credit_product.request;

import com.github.pagehelper.StringUtil;

public class MerchantCertificateRequestMessage {

    private String merchantNo;
    private String bizType;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    private static final String DEFAULT_TYPE = "2300";

    public boolean checkRequest() {
        if (StringUtil.isEmpty(merchantNo)) {
            return false;
        }
        if (StringUtil.isEmpty(bizType)) {
            bizType = DEFAULT_TYPE;
        }
        if (!DEFAULT_TYPE.equals(bizType)) {
            return false;
        }
        return true;
    }
}
