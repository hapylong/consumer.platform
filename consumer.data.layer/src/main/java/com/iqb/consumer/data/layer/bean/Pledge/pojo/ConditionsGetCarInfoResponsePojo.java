package com.iqb.consumer.data.layer.bean.Pledge.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class ConditionsGetCarInfoResponsePojo {
    private String id; // 主键
    private String merchantNo; // 商户名称
    private String carBrand; // 品牌
    private String carDetail; // 型号及配置
    private Integer carType; // 车辆类型
    private String plate; // 车牌号
    private String carNo; // 车架号
    private String engine; // 发动机号
    private Date registDate; // 初次登记日期
    private String registAdd; // 上牌所在地
    private BigDecimal assessPrice; // 评估价
    private BigDecimal applyAmt; // 融资金额
    private Integer status; // 状态private String

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarDetail() {
        return carDetail;
    }

    public void setCarDetail(String carDetail) {
        this.carDetail = carDetail;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public String getRegistAdd() {
        return registAdd;
    }

    public void setRegistAdd(String registAdd) {
        this.registAdd = registAdd;
    }

    public BigDecimal getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(BigDecimal assessPrice) {
        this.assessPrice = assessPrice;
    }

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
