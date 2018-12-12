package com.iqb.consumer.data.layer.bean.carstatus.pojo;

import java.util.Date;

public class GPSInfo {
    /** inst_gpsinfo FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪 **/
    private String orderId;
    private String gpsStatus;
    private String remark;
    private String disposalScheme;
    private Date createTime;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDisposalScheme() {
        return disposalScheme;
    }

    public void setDisposalScheme(String disposalScheme) {
        this.disposalScheme = disposalScheme;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
