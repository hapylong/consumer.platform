/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月24日 上午11:32:07
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.conf;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class WFConfig extends BaseEntity {
    private String bizType;
    private Integer wfStatus;
    private String procDefKey;
    private String startWfurl;
    private String tokenUser;
    private String tokenPass;
    private String taskUser;
    private String taskRole;
    private String procTaskCode;
    private String riskWfUrl;

    public Integer getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(Integer wfStatus) {
        this.wfStatus = wfStatus;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getStartWfurl() {
        return startWfurl;
    }

    public void setStartWfurl(String startWfurl) {
        this.startWfurl = startWfurl;
    }

    public String getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(String tokenUser) {
        this.tokenUser = tokenUser;
    }

    public String getTokenPass() {
        return tokenPass;
    }

    public void setTokenPass(String tokenPass) {
        this.tokenPass = tokenPass;
    }

    public String getTaskUser() {
        return taskUser;
    }

    public void setTaskUser(String taskUser) {
        this.taskUser = taskUser;
    }

    public String getTaskRole() {
        return taskRole;
    }

    public void setTaskRole(String taskRole) {
        this.taskRole = taskRole;
    }

    public String getProcTaskCode() {
        return procTaskCode;
    }

    public void setProcTaskCode(String procTaskCode) {
        this.procTaskCode = procTaskCode;
    }

    public String getRiskWfUrl() {
        return riskWfUrl;
    }

    public void setRiskWfUrl(String riskWfUrl) {
        this.riskWfUrl = riskWfUrl;
    }

}
