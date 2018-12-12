/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午6:45:00
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.project;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@SuppressWarnings("serial")
public class ProjectBaseInfoBean extends BaseEntity {
    private String projectName;// 项目名称
    private String projectInfo;// 项目信息
    private int status;// 状态(1,可用 2,不可用)
    private String merchantNo;// 商户号
    private String merchNames;// 商户号(冗余字段)

    public String getMerchNames() {
        return merchNames;
    }

    public void setMerchNames(String merchNames) {
        this.merchNames = merchNames;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(String projectInfo) {
        this.projectInfo = projectInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

}
