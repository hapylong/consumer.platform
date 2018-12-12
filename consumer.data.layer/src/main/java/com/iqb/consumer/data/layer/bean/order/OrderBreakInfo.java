/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月20日 下午2:05:55
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.order;

import java.math.BigDecimal;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 订单拆分
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class OrderBreakInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private BigDecimal carAmt;
    private BigDecimal gpsAmt;
    private BigDecimal insAmt;
    private BigDecimal taxAmt;
    private BigDecimal otherAmt;

    private BigDecimal orderAmt;

    /** inst_orderinfo **/
    private String employeeID;
    private String employeeName;
    private BigDecimal businessTaxAmt;// 商业险

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getCarAmt() {
        return carAmt;
    }

    public void setCarAmt(BigDecimal carAmt) {
        this.carAmt = carAmt;
    }

    public BigDecimal getGpsAmt() {
        return gpsAmt;
    }

    public void setGpsAmt(BigDecimal gpsAmt) {
        this.gpsAmt = gpsAmt;
    }

    public BigDecimal getInsAmt() {
        return insAmt;
    }

    public void setInsAmt(BigDecimal insAmt) {
        this.insAmt = insAmt;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public BigDecimal getOtherAmt() {
        return otherAmt;
    }

    public void setOtherAmt(BigDecimal otherAmt) {
        this.otherAmt = otherAmt;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public BigDecimal getBusinessTaxAmt() {
        return businessTaxAmt;
    }

    public void setBusinessTaxAmt(BigDecimal businessTaxAmt) {
        this.businessTaxAmt = businessTaxAmt;
    }

    public boolean checkEntity() {
        print();
        if (orderId == null ||
                carAmt == null ||
                gpsAmt == null ||
                insAmt == null ||
                taxAmt == null ||
                otherAmt == null) {
            return false;
        }
        orderAmt = carAmt.add(gpsAmt).add(insAmt).add(taxAmt).add(otherAmt).add(businessTaxAmt);
        return true;
    }

}
