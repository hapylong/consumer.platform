/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午3:39:35
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.merchant;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@SuppressWarnings("serial")
public class MerchantBizTypeBean extends BaseEntity {

    private String merchantNo;// 商户号
    private String bizType;// 业务类型
    private String bizName;// 业务名称

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

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

}
