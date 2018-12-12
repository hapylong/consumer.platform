package com.iqb.consumer.data.layer.bean.Pledge;

import java.math.BigDecimal;
import java.util.Date;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PledgeInfoBean {
    private static final Logger logger = LoggerFactory.getLogger(PledgeInfoBean.class);
    /** 主键id **/
    private String id;
    /** 订单id **/
    private String orderId;
    /** 车牌号 **/
    private String plate;
    /**
     * 车类型(1,二手车 2，新车)
     **/
    private Integer carType;
    /** 发动机型号 **/
    private String engine;
    /** 上牌地址 */
    private String registAdd;
    /** 还款日 **/
    private Date registDate;
    /** 车架号 **/
    private String carNo;
    /** 公里数 **/
    private BigDecimal mileage;
    /** 版本号 **/
    private Integer version;

    /** 创建时间 **/
    private Date createTime;
    /** 更新时间 **/
    private Date updateTime;
    /** RFID **/
    private String rfid;
    /** 线上融资 / 线下融资 1 ：2 **/
    private Integer selectRZ;
    /** 备注 **/
    private String remark;

    /** 备注 **/
    private String orderName;
    /** 融资金额 **/
    private BigDecimal applyAmt;
    /** 评估价格 **/
    private String assessPrice;
    /** 型号 **/
    private String model;
    /** 品牌 **/
    private String brand;

    /**
     * @describe : 表新增字段
     * @author: adam
     */
    private String merchantNo; // 商户号
    private String carBrand; // 车品牌
    private String carDetail; // 车系
    private String status; // 1,评估中，2已估价3，已退回4，已使用
    private String isLoan; // 是否有贷款：0无，1有
    private String purpose; // 借款用途

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Integer getCarType() {
        return carType;
    }

    public void setCarType(Integer carType) {
        this.carType = carType;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getRegistAdd() {
        return registAdd;
    }

    public void setRegistAdd(String registAdd) {
        this.registAdd = registAdd;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public Integer getSelectRZ() {
        return selectRZ;
    }

    public void setSelectRZ(Integer selectRZ) {
        this.selectRZ = selectRZ;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public String getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(String assessPrice) {
        this.assessPrice = assessPrice;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsLoan() {
        return isLoan;
    }

    public void setIsLoan(String isLoan) {
        this.isLoan = isLoan;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public boolean checkPersistEntity() {
        logger.info("PledgeInfoBean.checkPersistEntity :" + toString());
        if (StringUtil.isEmpty(id) ||
                StringUtil.isEmpty(merchantNo) ||
                StringUtil.isEmpty(carBrand) ||
                StringUtil.isEmpty(carDetail) ||
                null == carType ||
                // StringUtil.isEmpty(plate) ||
                StringUtil.isEmpty(carNo) ||
                StringUtil.isEmpty(engine) ||
                null == registDate ||
                // StringUtil.isEmpty(registAdd) ||
                null == mileage) {
            return false;
        }
        status = "1";
        return true;
    }

    public boolean checkUpdateEntity() {
        logger.info("PledgeInfoBean.checkUpdateEntity :" + toString());
        if (StringUtil.isEmpty(id) ||
                StringUtil.isEmpty(merchantNo) ||
                StringUtil.isEmpty(carBrand) ||
                StringUtil.isEmpty(carDetail) ||
                null == carType ||
                StringUtil.isEmpty(plate) ||
                StringUtil.isEmpty(carNo) ||
                StringUtil.isEmpty(engine) ||
                null == registDate ||
                StringUtil.isEmpty(registAdd) ||
                StringUtil.isEmpty(assessPrice) ||
                null == applyAmt ||
                null == mileage ||
                StringUtil.isEmpty(remark)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PledgeInfoBean [id=" + id + ", orderId=" + orderId + ", plate=" + plate + ", carType=" + carType
                + ", engine=" + engine + ", registAdd=" + registAdd + ", registDate=" + registDate + ", carNo=" + carNo
                + ", mileage=" + mileage + ", version=" + version + ", createTime=" + createTime + ", updateTime="
                + updateTime + ", rfid=" + rfid + ", selectRZ=" + selectRZ + ", remark=" + remark + ", orderName="
                + orderName + ", applyAmt=" + applyAmt + ", assessPrice=" + assessPrice + ", model=" + model
                + ", brand=" + brand + ", merchantNo=" + merchantNo + ", carBrand=" + carBrand + ", carDetail="
                + carDetail + ", status=" + status + ", isLoan=" + isLoan + ", purpose=" + purpose + "]";
    }

}
