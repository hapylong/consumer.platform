package com.iqb.consumer.data.layer.bean.admin.request;

import jodd.util.StringUtil;

public class CGetKeyPairsRequestMessage {
    public static final int KP_PAGE_INFO_LIST = 1;
    public static final int KP_SINGLE = 2;

    private String merchantNo; // 商户号

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public int howToGet() {
        if (StringUtil.isEmpty(merchantNo)) {
            return KP_PAGE_INFO_LIST;
        } else {
            return KP_SINGLE;
        }
    }
}
