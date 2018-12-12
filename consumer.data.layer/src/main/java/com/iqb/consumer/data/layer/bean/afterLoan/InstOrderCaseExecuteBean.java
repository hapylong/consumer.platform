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
public class InstOrderCaseExecuteBean {

    /** 主键ID **/
    private Integer id;
    /** 订单号 **/
    private String orderId;
    /** 执行结果 1全部执行 2部分执行 3未执行 4失信被执行人 **/
    private Integer executeResult;
    /** 执行进度 **/
    private String executeRemark;
    /** 操作人手机号码 **/
    private String operatorRegId;
    /** 版本号 **/
    private Integer version;
    /** 创建时间 **/
    private Date createTime;
    /** 最后修改时间 **/
    private Date updateTime;

    /** 操作人-（查询结果） **/
    private String realName;

    /** 创建时间格式化后结果 %Y-%m-%d %H:%i:%S-（查询结果） **/
    private String createTimeFormat;
    private String caseId;

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

    public Integer getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(Integer executeResult) {
        this.executeResult = executeResult;
    }

    public String getOperatorRegId() {
        return operatorRegId;
    }

    public void setOperatorRegId(String operatorRegId) {
        this.operatorRegId = operatorRegId;
    }

    public String getExecuteRemark() {
        return executeRemark;
    }

    public void setExecuteRemark(String executeRemark) {
        this.executeRemark = executeRemark;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCreateTimeFormat() {
        return createTimeFormat;
    }

    public void setCreateTimeFormat(String createTimeFormat) {
        this.createTimeFormat = createTimeFormat;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

}
