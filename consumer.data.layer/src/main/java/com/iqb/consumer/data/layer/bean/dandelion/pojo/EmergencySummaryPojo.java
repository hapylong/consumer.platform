package com.iqb.consumer.data.layer.bean.dandelion.pojo;

public class EmergencySummaryPojo {

    private String name; // 紧急联系人姓名
    private String phone; // 紧急联系人手机号
    private Integer priority; // 优先级

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
