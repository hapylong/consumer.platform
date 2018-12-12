/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月14日 下午3:24:36
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.jys;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 交易所需求:字典表
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class JYSDictInfo extends BaseEntity {

    private String key;
    private int code;
    private String value;
    private int enable;
    private String remark;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
