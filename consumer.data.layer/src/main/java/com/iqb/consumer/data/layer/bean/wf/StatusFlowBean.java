/*
 * 软件著作权：北京爱钱帮财富科技有限公司 项目名称：
 * 
 * NAME : StatusFlowBean.java
 * 
 * PURPOSE :
 * 
 * AUTHOR : haojinlong
 * 
 * 
 * 创建日期: 2017年5月16日 HISTORY： 变更日期
 */
package com.iqb.consumer.data.layer.bean.wf;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author haojinlong 流程流转信息实体类
 */
public class StatusFlowBean extends BaseEntity {

    private static final long serialVersionUID = 1L;
    // 业务类型
    private String bizType;
    // 流程处理类型
    private String dealType;
    // 当前流程节点
    private String procCurrTask;
    // 当前流程节点状态
    private int procApprStatus;
    // 工作流状态
    private int wfStatus;
    // 当前订单状态
    private int riskStatus;
    // 目标节点流程状态
    private String goalWfStatus;
    // 目标节点订单状态
    private String goalRiskStatus;
    //
    private int goalStatus;
    // 正则表达式匹配
    private String match;
    // 预支付状态
    private String preAmtStatus;
    // 备注
    private String remark;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getProcCurrTask() {
        return procCurrTask;
    }

    public void setProcCurrTask(String procCurrTask) {
        this.procCurrTask = procCurrTask;
    }

    public int getProcApprStatus() {
        return procApprStatus;
    }

    public void setProcApprStatus(int procApprStatus) {
        this.procApprStatus = procApprStatus;
    }

    public int getWfStatus() {
        return wfStatus;
    }

    public void setWfStatus(int wfStatus) {
        this.wfStatus = wfStatus;
    }

    public int getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(int riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getGoalWfStatus() {
        return goalWfStatus;
    }

    public void setGoalWfStatus(String goalWfStatus) {
        this.goalWfStatus = goalWfStatus;
    }

    public String getGoalRiskStatus() {
        return goalRiskStatus;
    }

    public void setGoalRiskStatus(String goalRiskStatus) {
        this.goalRiskStatus = goalRiskStatus;
    }

    public int getGoalStatus() {
        return goalStatus;
    }

    public void setGoalStatus(int goalStatus) {
        this.goalStatus = goalStatus;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getPreAmtStatus() {
        return preAmtStatus;
    }

    public void setPreAmtStatus(String preAmtStatus) {
        this.preAmtStatus = preAmtStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
