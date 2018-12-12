package com.iqb.consumer.data.layer.bean.contract;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * 电子合同补录信息
 * 
 * @Description:TODO
 * @author guojuan
 * @date 2017年9月26日 下午3:51:54
 * @version
 */
@SuppressWarnings("serial")
public class ContractInfoBean extends BaseEntity {

    private String orderId;// 订单号
    private String provider;// 供应商
    private String vendor;// 生产厂商
    private String vendorNo;// 厂牌型号
    private int seatNum;// 座位数
    private String carType;// 车型
    private String fuelForm;// 燃油形式
    private String fuelOilNumber;// 燃油标号
    private String engineType;// 发动机型号
    private String carNo;// 车架号
    private String carColor;// 颜色
    private String status;// 签约状态
    private String registrationNo;// 机动车登记证书编号

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendorNo() {
        return vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getFuelForm() {
        return fuelForm;
    }

    public void setFuelForm(String fuelForm) {
        this.fuelForm = fuelForm;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getFuelOilNumber() {
        return fuelOilNumber;
    }

    public void setFuelOilNumber(String fuelOilNumber) {
        this.fuelOilNumber = fuelOilNumber;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

}
