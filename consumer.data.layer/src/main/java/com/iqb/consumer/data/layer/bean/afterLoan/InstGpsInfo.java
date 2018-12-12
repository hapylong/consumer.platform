package com.iqb.consumer.data.layer.bean.afterLoan;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年5月4日下午5:34:40 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstGpsInfo {
    private String orderId;
    private String gpsStatus;
    private String disposalScheme;
    private String remark;
    private String createTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(String gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public String getDisposalScheme() {
        return disposalScheme;
    }

    public void setDisposalScheme(String disposalScheme) {
        this.disposalScheme = disposalScheme;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
