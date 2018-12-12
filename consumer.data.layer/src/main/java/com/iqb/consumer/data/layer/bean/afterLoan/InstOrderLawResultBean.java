package com.iqb.consumer.data.layer.bean.afterLoan;

import java.util.Date;

/**
 * Description:
 * 
 * @author chenyong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年7月18日10:08:00 	chenyong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstOrderLawResultBean {

    /** 主键ID **/
    private Integer id;
    /** 订单号 **/
    private String orderId;
    /** 庭审结果 **/
    private String caseResult;
    /** 是否执行标识 1 是 2 否 **/
    private Integer executeFlag;
    /** 庭审备注 **/
    private String courtRemark;
    /** 操作人手机号码 **/
    private String operatorRegId;
    /** 版本号 **/
    private Integer version;
    /** 创建时间 **/
    private Date createTime;
    /** 最后修改时间 **/
    private Date updateTime;
    /** 操作人手机号码 **/
    private String caseId;

    private String realName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCaseResult() {
        return caseResult;
    }

    public void setCaseResult(String caseResult) {
        this.caseResult = caseResult;
    }

    public Integer getExecuteFlag() {
        return executeFlag;
    }

    public void setExecuteFlag(Integer executeFlag) {
        this.executeFlag = executeFlag;
    }

    public String getCourtRemark() {
        return courtRemark;
    }

    public void setCourtRemark(String courtRemark) {
        this.courtRemark = courtRemark;
    }

    public String getOperatorRegId() {
        return operatorRegId;
    }

    public void setOperatorRegId(String operatorRegId) {
        this.operatorRegId = operatorRegId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

}
