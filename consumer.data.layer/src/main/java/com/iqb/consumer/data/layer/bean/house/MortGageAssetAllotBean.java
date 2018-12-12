/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年8月17日下午2:35:03 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.data.layer.bean.house;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author haojinlong
 * 
 */
public class MortGageAssetAllotBean {
    private int id;
    private String orderId; // 订单id
    private BigDecimal allotAmt; // 资产分配金额
    private int instPeriods; // 期数
    private Date allotDate; // 资产分配时间
    private Date assetEndDate; // 资产到期日
    private String sourcesFunding; // 资金来源
    private String remark; // 备注
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAllotAmt() {
        return allotAmt;
    }

    public void setAllotAmt(BigDecimal allotAmt) {
        this.allotAmt = allotAmt;
    }

    public int getInstPeriods() {
        return instPeriods;
    }

    public void setInstPeriods(int instPeriods) {
        this.instPeriods = instPeriods;
    }

    public Date getAllotDate() {
        return allotDate;
    }

    public void setAllotDate(Date allotDate) {
        this.allotDate = allotDate;
    }

    public Date getAssetEndDate() {
        return assetEndDate;
    }

    public void setAssetEndDate(Date assetEndDate) {
        this.assetEndDate = assetEndDate;
    }

    public String getSourcesFunding() {
        return sourcesFunding;
    }

    public void setSourcesFunding(String sourcesFunding) {
        this.sourcesFunding = sourcesFunding;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
