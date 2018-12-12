package com.iqb.consumer.asset.allocation.assetinfo.bean;

import java.math.BigDecimal;
import java.util.Calendar;

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
 * 2018年8月2日下午6:46:49 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class AssetObjectInfoBean {
    /** 订单号 **/
    private String orderId;
    /** 项目名称 **/
    private String projectName;
    /** 商户名称 **/
    private String merchantName;
    /** 姓名 **/
    private String realName;
    /** 手机号 **/
    private String regId;
    /** 上标金额 **/
    private BigDecimal applyAmt;
    /** 总期数 **/
    private Integer orderItems;
    /** 分配期数 **/
    private Integer applyItems;
    /** 债权人 **/
    private String creditName;
    /** 资产分配时间 **/
    private String applyTime;
    /** 资产到期日 **/
    private String deadLine;
    /** 资金来源 **/
    private String sourcesFunding;
    /** 资金来源汉字值 **/
    private String sourcesFundingStr;
    /** 备注 **/
    private String remark;
    /** 剩余期数 **/
    private String leftItems;
    /** 资产分配人 **/
    private String allotRealName;
    private Calendar assetDueDate = Calendar.getInstance();// 资产到期日
    private String assetDueDateStr;// 资产到期日-YYYY年MM月DD日格式
    /** 放款主体 **/
    private String lendersSubject;
    /** 放款主体汉字值 **/
    private String lendersSubjectStr;
    /** 预计放款时间 **/
    private String planLendingTime;
    // 推标金额方式 1 按订单金额 2 按剩余未还本金
    private int pushMode;
    /** 订单金额 **/
    private BigDecimal orderAmt;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
        /*
         * if (orderItems != null) { if (!StringUtil.isEmpty(applyTime)) { Calendar c =
         * DateUtil.parseCalendar(applyTime, DateUtil.SHORT_DATE_FORMAT);
         * assetDueDate.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + orderItems,
         * c.get(Calendar.DATE), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)); }
         * 
         * }
         */
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getSourcesFunding() {
        return sourcesFunding;
    }

    public void setSourcesFunding(String sourcesFunding) {
        this.sourcesFunding = sourcesFunding;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLeftItems() {
        return leftItems;
    }

    public void setLeftItems(String leftItems) {
        this.leftItems = leftItems;
    }

    public String getAllotRealName() {
        return allotRealName;
    }

    public void setAllotRealName(String allotRealName) {
        this.allotRealName = allotRealName;
    }

    public Calendar getAssetDueDate() {
        return assetDueDate;
    }

    public void setAssetDueDate(Calendar assetDueDate) {
        this.assetDueDate = assetDueDate;
    }

    public String getLendersSubject() {
        return lendersSubject;
    }

    public void setLendersSubject(String lendersSubject) {
        this.lendersSubject = lendersSubject;
    }

    public String getAssetDueDateStr() {
        return assetDueDateStr;
    }

    public void setAssetDueDateStr(String assetDueDateStr) {
        this.assetDueDateStr = assetDueDateStr;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getApplyItems() {
        return applyItems;
    }

    public void setApplyItems(Integer applyItems) {
        this.applyItems = applyItems;
    }

    public String getSourcesFundingStr() {
        return sourcesFundingStr;
    }

    public void setSourcesFundingStr(String sourcesFundingStr) {
        this.sourcesFundingStr = sourcesFundingStr;
    }

    public String getLendersSubjectStr() {
        return lendersSubjectStr;
    }

    public void setLendersSubjectStr(String lendersSubjectStr) {
        this.lendersSubjectStr = lendersSubjectStr;
    }

    public String getPlanLendingTime() {
        return planLendingTime;
    }

    public void setPlanLendingTime(String planLendingTime) {
        this.planLendingTime = planLendingTime;
    }

    public int getPushMode() {
        return pushMode;
    }

    public void setPushMode(int pushMode) {
        this.pushMode = pushMode;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

}
