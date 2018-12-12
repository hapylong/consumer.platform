package com.iqb.consumer.data.layer.bean.admin.request;

import com.iqb.consumer.common.constant.IsEmptyCheck;
import com.iqb.consumer.common.constant.IsEmptyCheck.CheckGroup;
import com.iqb.consumer.data.layer.bean.EntityUtil;

public class AuthoritySeekRequestMessage extends EntityUtil {

    private Integer concatId;
    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String merchantNo;
    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String merchantName;

    public Integer getConcatId() {
        return concatId;
    }

    public void setConcatId(Integer concatId) {
        this.concatId = concatId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    @Override
    public String toString() {
        return "AuthoritySeekRequestMessage [concatId=" + concatId + ", merchantNo=" + merchantNo + ", merchantName="
                + merchantName + "]";
    }

}
