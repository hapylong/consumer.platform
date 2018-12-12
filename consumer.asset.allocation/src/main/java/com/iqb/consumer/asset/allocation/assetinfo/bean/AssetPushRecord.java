package com.iqb.consumer.asset.allocation.assetinfo.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class AssetPushRecord implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String projectCode;
    private String projectName;
    private Integer pushTime;
    private BigDecimal applyAmt;
    private Integer pushStatus;
    private String receiveType;
    private Integer applyItems;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPushTime() {
        return pushTime;
    }

    public void setPushTime(Integer pushTime) {
        this.pushTime = pushTime;
    }

    public Integer getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Integer pushStatus) {
        this.pushStatus = pushStatus;
    }

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public Integer getApplyItems() {
        return applyItems;
    }

    public void setApplyItems(Integer applyItems) {
        this.applyItems = applyItems;
    }

}
