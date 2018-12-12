/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年11月22日 下午2:44:22
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.conf;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class SysSmsConfig extends BaseEntity {

    private int wechatNo;
    private String wechatRemark;
    private String smsUrl;
    private String smsName;
    private String smsPswd;
    private int smsNeedStatus;// 1 TRUE 2 FALSE

    public int getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(int wechatNo) {
        this.wechatNo = wechatNo;
    }

    public String getWechatRemark() {
        return wechatRemark;
    }

    public void setWechatRemark(String wechatRemark) {
        this.wechatRemark = wechatRemark;
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public String getSmsName() {
        return smsName;
    }

    public void setSmsName(String smsName) {
        this.smsName = smsName;
    }

    public String getSmsPswd() {
        return smsPswd;
    }

    public void setSmsPswd(String smsPswd) {
        this.smsPswd = smsPswd;
    }

    public int getSmsNeedStatus() {
        return smsNeedStatus;
    }

    public void setSmsNeedStatus(int smsNeedStatus) {
        this.smsNeedStatus = smsNeedStatus;
    }

    @Override
    public String toString() {
        return "[smsName]" + this.smsName + "[smsPswd]" + this.smsPswd + "[wechatNo]"
                + this.wechatNo + "[wechatRemark]" + this.wechatRemark;
    }

}
