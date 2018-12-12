package com.iqb.consumer.data.layer.bean.schedule.entity;

import java.util.Date;

import javax.persistence.Table;

@Table(name = "inst_schedule_task")
public class InstScheduleTaskEntity {
    public static final int STATE_SUCC = 1;
    public static final int STATE_FAIL = 0;
    private Long id;
    private String orderId; // 订单号
    private String url; // 目标回调url
    private Date nextSendTime; // 下次发送时间
    private Date createTime; // 创建时间
    private Integer sendNum; // 已发送次数
    private Integer sendPolicy; // 发送策略
    private Integer state; // 状态 0:未成功， 1：已成功
    private String jsonData; // 发送的 json 数据

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSendNum() {
        return sendNum;
    }

    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }

    public Integer getSendPolicy() {
        return sendPolicy;
    }

    public void setSendPolicy(Integer sendPolicy) {
        this.sendPolicy = sendPolicy;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Date getNextSendTime() {
        return nextSendTime;
    }

    public void setNextSendTime(Date nextSendTime) {
        this.nextSendTime = nextSendTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
