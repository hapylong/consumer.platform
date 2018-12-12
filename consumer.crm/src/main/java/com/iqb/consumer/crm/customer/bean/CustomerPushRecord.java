package com.iqb.consumer.crm.customer.bean;

/**
 * 
 * Description: crm推送信息
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月21日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class CustomerPushRecord {

    /**
     * 主键id
     */
    private String id;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 推送类型 1:新增 2:修改 3:删除
     */
    private Integer pushType;

    /**
     * 推送时间
     */
    private Integer pushTime;

    /**
     * 接收端
     */
    private String receive;

    /**
     * 推送状态 1:开始 2:结束 3:异常
     */
    private Integer pushStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public Integer getPushTime() {
        return pushTime;
    }

    public void setPushTime(Integer pushTime) {
        this.pushTime = pushTime;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public Integer getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Integer pushStatus) {
        this.pushStatus = pushStatus;
    }

}
