package com.iqb.consumer.data.layer.bean.cheegu;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.iqb.consumer.data.layer.bean.BaseEntity;

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
 * 2018年5月28日上午10:22:25 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class InstCarPeccancyDetailBean extends BaseEntity {
    /** 车e估订单号 **/
    private String cOrderId;
    /** 订单号 **/
    private String orderId;
    /** 车架号 **/
    private String vin;
    @JSONField(name = "Time")
    private String peccancyTime;
    /** 违章地点 **/
    @JSONField(name = "Location")
    private String location;
    /** 违章原因 **/
    @JSONField(name = "Reason")
    private String reason;
    /** 违章罚款金额 **/
    @JSONField(name = "count")
    private BigDecimal peccancyAmt;
    /** 违章记录状态，0：末处理 1： 己处理 **/
    @JSONField(name = "status")
    private String status;
    /** 违章采集机关 **/
    @JSONField(name = "department")
    private String department;
    /** 违章扣分 **/
    @JSONField(name = "Degree")
    private String degree;
    /** 违章代码 **/
    @JSONField(name = "Code")
    private String code;
    /** 违章项文书编号 **/
    @JSONField(name = "Archive")
    private String archive;
    /** 联系电话 **/
    @JSONField(name = "Telephone")
    private String telephone;
    /** 违章处理地址 **/
    @JSONField(name = "Excutelocation")
    private String exLocation;
    /** 执行部门 **/
    @JSONField(name = "Excutedepartment")
    private String exDepartment;
    /** 违章分类类别 **/
    @JSONField(name = "Category")
    private String category;
    /** 滞纳金 **/
    @JSONField(name = "Latefine")
    private String latefine;
    /** 处罚依据 **/
    @JSONField(name = "Punishmentaccording")
    private String punishmentAccording;
    /** 违法条款 **/
    @JSONField(name = "Illegalentry")
    private String illegalentry;
    /** 违章归属地点名 **/
    @JSONField(name = "LocationName")
    private String locationName;
    /** 备注 **/
    private String remark;
    /** 操作人 **/
    private String operateUser;
    /** 尾行处理时间 **/
    private String processDate;

    private String excutelocation;
    private String excutedepartment;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPeccancyTime() {
        return peccancyTime;
    }

    public void setPeccancyTime(String peccancyTime) {
        this.peccancyTime = peccancyTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getPeccancyAmt() {
        return peccancyAmt;
    }

    public void setPeccancyAmt(BigDecimal peccancyAmt) {
        this.peccancyAmt = peccancyAmt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLatefine() {
        return latefine;
    }

    public void setLatefine(String latefine) {
        this.latefine = latefine;
    }

    public String getPunishmentAccording() {
        return punishmentAccording;
    }

    public void setPunishmentAccording(String punishmentAccording) {
        this.punishmentAccording = punishmentAccording;
    }

    public String getIllegalentry() {
        return illegalentry;
    }

    public void setIllegalentry(String illegalentry) {
        this.illegalentry = illegalentry;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    public String getcOrderId() {
        return cOrderId;
    }

    public void setcOrderId(String cOrderId) {
        this.cOrderId = cOrderId;
    }

    public String getExLocation() {
        return exLocation;
    }

    public void setExLocation(String exLocation) {
        this.exLocation = exLocation;
    }

    public String getExDepartment() {
        return exDepartment;
    }

    public void setExDepartment(String exDepartment) {
        this.exDepartment = exDepartment;
    }

    public String getExcutelocation() {
        return this.getExLocation();
    }

    public void setExcutelocation(String excutelocation) {
        this.excutelocation = excutelocation;
    }

    public String getExcutedepartment() {
        return this.getExDepartment();
    }

    public void setExcutedepartment(String excutedepartment) {
        this.excutedepartment = excutedepartment;
    }

}
