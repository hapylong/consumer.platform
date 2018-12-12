package com.iqb.consumer.data.layer.bean.carstatus.pojo;

import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年4月25日下午3:56:36 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class InstRemindRecordBean extends BaseEntity {
    /** 订单号 **/
    private String orderId;
    /** 通话结果 **/
    private String telRecord;
    /** 失败原因 **/
    private String failReason;
    /** 处理意见 **/
    private String dealReason;
    /** 当前期数 **/
    private int curItems;
    /** 电催结果 **/
    private String mobileCollection;
    /** 电催处理意见 **/
    private String mobileDealOpinion;
    /** 1,电话提醒 2，电催管理 **/
    private int flag;
    /** 备注 **/
    private String remark;
    /** 操作人 **/
    private String processUser;
    /** 操作时间 **/
    private String processTime;

    private String merchantFullName;// 机构名称
    private String realName;// 姓名
    private String regId;// 手机号
    private String orderAmt;// 借款金额
    private String monthInterest;// 月供
    private String orderItems;// 总期数
    private Date lastrepaydate;// 最迟还款日
    private int billInfoStatus;// 账单状态

    public int getBillInfoStatus() {
        return billInfoStatus;
    }

    public void setBillInfoStatus(int billInfoStatus) {
        this.billInfoStatus = billInfoStatus;
    }

    public String getMerchantFullName() {
        return merchantFullName;
    }

    public void setMerchantFullName(String merchantFullName) {
        this.merchantFullName = merchantFullName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(String monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public Date getLastrepaydate() {
        return lastrepaydate;
    }

    public void setLastrepaydate(Date lastrepaydate) {
        this.lastrepaydate = lastrepaydate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTelRecord() {
        return telRecord;
    }

    public void setTelRecord(String telRecord) {
        this.telRecord = telRecord;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getDealReason() {
        return dealReason;
    }

    public void setDealReason(String dealReason) {
        this.dealReason = dealReason;
    }

    public int getCurItems() {
        return curItems;
    }

    public void setCurItems(int curItems) {
        this.curItems = curItems;
    }

    public String getMobileCollection() {
        return mobileCollection;
    }

    public void setMobileCollection(String mobileCollection) {
        this.mobileCollection = mobileCollection;
    }

    public String getMobileDealOpinion() {
        return mobileDealOpinion;
    }

    public void setMobileDealOpinion(String mobileDealOpinion) {
        this.mobileDealOpinion = mobileDealOpinion;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProcessUser() {
        return processUser;
    }

    public void setProcessUser(String processUser) {
        this.processUser = processUser;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

}
