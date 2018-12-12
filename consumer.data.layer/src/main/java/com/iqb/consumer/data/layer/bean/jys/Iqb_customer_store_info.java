/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月15日 下午2:55:30
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.jys;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class Iqb_customer_store_info {

    private String id;// 债权人姓名
    private String text;// 所属担保公司名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
