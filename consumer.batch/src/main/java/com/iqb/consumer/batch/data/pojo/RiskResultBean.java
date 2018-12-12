/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年1月3日 上午11:24:26
 * @version V1.0
 */
package com.iqb.consumer.batch.data.pojo;

import com.alibaba.fastjson.JSONObject;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class RiskResultBean {

    private String orderId;// 订单号
    private int riskStatus;// 风控状态
    private String content;
    private String sendUrl;
    private String type;
    private int status;// 状态
    private int reqTimes;// 状态
    private String flag;// null:发往读脉 1:发给风控
    private String reportNo;

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendUrl() {
        return sendUrl;
    }

    public void setSendUrl(String sendUrl) {
        this.sendUrl = sendUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(int riskStatus) {
        this.riskStatus = riskStatus;
    }

    public int getReqTimes() {
        return reqTimes;
    }

    public void setReqTimes(int reqTimes) {
        this.reqTimes = reqTimes;
    }

    public RiskResultBean(JSONObject objs) {
        this.orderId = objs.getString("orderId");
        this.riskStatus = objs.getIntValue("riskStatus");
        this.status = objs.getIntValue("status");
    }

    public RiskResultBean() {

    }

}
