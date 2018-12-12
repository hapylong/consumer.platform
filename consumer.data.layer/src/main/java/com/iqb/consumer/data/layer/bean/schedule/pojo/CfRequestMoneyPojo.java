package com.iqb.consumer.data.layer.bean.schedule.pojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.pagehelper.StringUtil;
import com.iqb.consumer.data.layer.bean.schedule.http.FullScaleLendingIdsResponseMessage;

public class CfRequestMoneyPojo {
    private final int STATUS_FINISH = 2; // 融资完毕
    private Date deadLine;
    private Integer pushId;
    private Date loanTime;
    private Integer status;
    private String orderId;
    private Date updateTime;

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Integer getPushId() {
        return pushId;
    }

    public void setPushId(Integer pushId) {
        this.pushId = pushId;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public static CfRequestMoneyPojo init() {
        return new CfRequestMoneyPojo();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    private final DateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    private Date convert(String d) throws ParseException {
        return formatter.parse(d);
    }

    public boolean create(FullScaleLendingIdsResponseMessage fs) {
        if (StringUtil.isEmpty(fs.getFullTime()) || StringUtil.isEmpty(fs.getBorrowId())
                || StringUtil.isEmpty(fs.getCreditTime()) || StringUtil.isEmpty(fs.getOrderId())) {
            return false;
        }
        try {
            orderId = fs.getOrderId();
            deadLine = convert(fs.getFullTime());
            pushId = Integer.parseInt(fs.getBorrowId());
            loanTime = convert(fs.getCreditTime());
            status = STATUS_FINISH;
            updateTime = new Date();
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
