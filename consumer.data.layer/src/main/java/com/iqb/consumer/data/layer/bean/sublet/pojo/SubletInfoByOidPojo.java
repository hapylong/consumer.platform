package com.iqb.consumer.data.layer.bean.sublet.pojo;

import jodd.util.StringUtil;

public class SubletInfoByOidPojo {

    private String regId; // 转租人电话
    private String realName; // 转租人姓名
    private String planShortName; // 产品方案
    private String monthInterest; // 月供
    private Integer orderItems; // 总期数
    private Integer overItems; // 已还期数

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPlanShortName() {
        return planShortName;
    }

    public void setPlanShortName(String planShortName) {
        this.planShortName = planShortName;
    }

    public String getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(String monthInterest) {
        this.monthInterest = monthInterest;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getOverItems() {
        return overItems;
    }

    public void setOverItems(Integer overItems) {
        this.overItems = overItems;
    }

    public boolean checkEntity() {
        if (StringUtil.isEmpty(regId) ||
                StringUtil.isEmpty(realName) ||
                StringUtil.isEmpty(planShortName) ||
                StringUtil.isEmpty(monthInterest) ||
                orderItems == null) {
            return false;
        }
        return true;
    }
}
