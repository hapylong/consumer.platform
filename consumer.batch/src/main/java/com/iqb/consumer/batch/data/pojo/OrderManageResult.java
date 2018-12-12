package com.iqb.consumer.batch.data.pojo;

import java.util.Date;

/**
 * 订单管理结果
 */
@SuppressWarnings("serial")
public class OrderManageResult extends OrderBean {

    private String merchName;// 商户名
    private String userName;// 用户名
    private String planShortName;// 方案(计划简称)
    private String merchCode;// 商户登录名
    private Date applyTime;// 上标时间
    private int uploadStatus;// 权证资料状态:1.待上传 2.待审核 3.已上传
    private String procTaskId;// 当前热本文由流程ID
    private String procOrgCode;// 当前热本文由流程ID

    public String getProcOrgCode() {
        return procOrgCode;
    }

    public void setProcOrgCode(String procOrgCode) {
        this.procOrgCode = procOrgCode;
    }

    public String getProcTaskId() {
        return procTaskId;
    }

    public void setProcTaskId(String procTaskId) {
        this.procTaskId = procTaskId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
