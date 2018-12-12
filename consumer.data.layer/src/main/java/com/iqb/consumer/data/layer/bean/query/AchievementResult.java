package com.iqb.consumer.data.layer.bean.query;

import java.util.Date;

public class AchievementResult extends AchievementBean {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String merchName;// 商户名
    private String realName;// 用户名
    private String planShortName;// 方案(计划简称)
    private String merchCode;// 商户登录名
    private int requestMoneyId;// 上标ID
    private String olderIds;
    private String applyTime;
    private String requestPeriod;
    private int requestStatus;
    private Date requestTime;

    public String getMerchCode() {
        return merchCode;
    }

    public void setMerchCode(String merchCode) {
        this.merchCode = merchCode;
    }

    public String getPlanShortName() {
        return planShortName;
    }

    public void setPlanShortName(String planShortName) {
        this.planShortName = planShortName;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getRequestMoneyId() {
        return requestMoneyId;
    }

    public void setRequestMoneyId(int requestMoneyId) {
        this.requestMoneyId = requestMoneyId;
    }

    public String getRequestPeriod() {
        return requestPeriod;
    }

    public void setRequestPeriod(String requestPeriod) {
        this.requestPeriod = requestPeriod;
    }

    public String getOlderIds() {
        return olderIds;
    }

    public void setOlderIds(String olderIds) {
        this.olderIds = olderIds;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

}
