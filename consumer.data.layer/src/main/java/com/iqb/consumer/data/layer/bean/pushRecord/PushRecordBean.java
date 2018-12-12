package com.iqb.consumer.data.layer.bean.pushRecord;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 推标Bean
 * 
 * @author chengzhen
 * 
 */
public class PushRecordBean extends BaseEntity {

    // 来自cf_requestmonye表
    private Date applyTime;// 分配时间
    private String orderId;
    private BigDecimal applyAmt;// 金额
    private int applyItems;// 分配期数
    private String sourcesFunding;// 资金来源 1 爱钱帮 2饭饭 3 交易所
    private String creditName;// 债权人姓名
    private String applyName;// 分配人姓名(预留参数)
    private Integer pushId;// 上标ID
    private Date deadLine;// 上标时间
    private Date redemptionDate;// 赎回时间
    private int applyInstIDay;// 分配天数

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getApplyInstIDay() {
        return applyInstIDay;
    }

    public void setApplyInstIDay(int applyInstIDay) {
        this.applyInstIDay = applyInstIDay;
    }

    public Date getRedemptionDate() {
        return redemptionDate;
    }

    public void setRedemptionDate(Date redemptionDate) {
        this.redemptionDate = redemptionDate;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Integer getPushId() {
        return pushId;
    }

    public void setPushId(int pushId) {
        this.pushId = pushId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public int getApplyItems() {
        return applyItems;
    }

    public void setApplyItems(int applyItems) {
        this.applyItems = applyItems;
    }

    public String getSourcesFunding() {
        return sourcesFunding;
    }

    public void setSourcesFunding(String sourcesFunding) {
        this.sourcesFunding = sourcesFunding;
    }

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

}
