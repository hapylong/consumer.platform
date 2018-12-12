package com.iqb.consumer.data.layer.bean.afterLoan;

import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 贷后客户信息bean
 * 
 * @author chengzhen
 * 
 */
@SuppressWarnings("serial")
public class AfterLoanCustomerInfoBean extends BaseEntity {

    private String orderId;
    private String remark;
    private String optName;
    private Date createTime;
    private String smsMobile;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSmsMobile() {
        return smsMobile;
    }

    public void setSmsMobile(String smsMobile) {
        this.smsMobile = smsMobile;
    }

}
