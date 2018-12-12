/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @date 2016年9月27日 下午3:56:20
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.authoritycard;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.iqb.consumer.common.constant.IsEmptyCheck;
import com.iqb.consumer.common.constant.IsEmptyCheck.CheckGroup;
import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@SuppressWarnings("serial")
@Table(name = "inst_authoritycard")
public class AuthorityCardBean extends BaseEntity {

    private String orderId;// 订单号
    private String carNo;// 车架号

    @IsEmptyCheck(groupCode = CheckGroup.A)
    private String plate;// 车牌号
    private String plateType;// 车型号 "01":"大型汽车"，"02":"小型汽车"，"15":"挂车"
    private String engine;// 发动机型号
    private int hasCar;// 是否交车 1,是 2,否
    private Date getCarDate;// 交车时间
    private Date checkDate;// 年检时间
    private Date gpsInstDate;// gps安装时间
    private Date insuranceStart;// 交强险开始
    private Date insuranceEnd;// 交强险结束
    private String lineGpsNo;// 有线gps识别号
    private String nolineGpsNo;// 无线GPS识别号
    private Date bizRisksStart;// 商业险开始
    private Date bizRisksEnd;// 商业险结束
    private int uploadStatus;// 权证资料状态:1.待上传 2.已上传
    private int status;// 审批结果 1,通过 2 拒绝
    private int mortgageFlag;// 车辆状态 1是2否
    private Date mortgageDate;// 抵押时间

    /**
     * @add column
     * @author adam
     */
    private String procInstId; // 流程ID
    private String lineGpsInstAdd; // 有线GPS安装位置
    private String noLineGpsInstAdd; // 无线GPS安装位置

    private String carConfig;// 车辆配置
    private String carAge;// 车辆年龄
    private String carColor;// 车辆颜色
    private String carEmissions;// 汽车排量
    private String passengerNum;// 核定载客
    private String mileage;// 里程数
    private BigDecimal firstBuyAmt;// 首次购买价格
    private String regOrg;// 注册机构
    private String firstRegDate;// 首次登记时间
    private String transferNum;// 转移次数
    private String gpsDeviceNum;// 设备号
    private String gpsDeviceAddress;// 安装位置
    private int gpsNum;// 安装套数

    private String carBrand;// 品牌
    private String carDetail;// 车辆配置
    private String mortgageCompany;// 抵押公司
    private String buyWays;// 购买方式
    private BigDecimal assessPrice;// 评估价格
    // chengzhen 2017年12月22日 11:35:34 FINANCE-2689 以租代购：添加 驾驶证编号
    private String driverLicenseNum;
    private String carKeyFlag;// 是否有车钥匙 1 有 2没有

    public String getDriverLicenseNum() {
        return driverLicenseNum;
    }

    public void setDriverLicenseNum(String driverLicenseNum) {
        this.driverLicenseNum = driverLicenseNum;
    }

    public String getGpsDeviceNum() {
        return gpsDeviceNum;
    }

    public void setGpsDeviceNum(String gpsDeviceNum) {
        this.gpsDeviceNum = gpsDeviceNum;
    }

    public String getGpsDeviceAddress() {
        return gpsDeviceAddress;
    }

    public void setGpsDeviceAddress(String gpsDeviceAddress) {
        this.gpsDeviceAddress = gpsDeviceAddress;
    }

    public int getGpsNum() {
        return gpsNum;
    }

    public void setGpsNum(int gpsNum) {
        this.gpsNum = gpsNum;
    }

    public int getMortgageFlag() {
        return mortgageFlag;
    }

    public void setMortgageFlag(int mortgageFlag) {
        this.mortgageFlag = mortgageFlag;
    }

    public Date getMortgageDate() {
        return mortgageDate;
    }

    public void setMortgageDate(Date mortgageDate) {
        this.mortgageDate = mortgageDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public int getHasCar() {
        return hasCar;
    }

    public void setHasCar(int hasCar) {
        this.hasCar = hasCar;
    }

    public Date getGetCarDate() {
        return getCarDate;
    }

    public void setGetCarDate(Date getCarDate) {
        this.getCarDate = getCarDate;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public Date getGpsInstDate() {
        return gpsInstDate;
    }

    public void setGpsInstDate(Date gpsInstDate) {
        this.gpsInstDate = gpsInstDate;
    }

    public Date getInsuranceStart() {
        return insuranceStart;
    }

    public void setInsuranceStart(Date insuranceStart) {
        this.insuranceStart = insuranceStart;
    }

    public Date getInsuranceEnd() {
        return insuranceEnd;
    }

    public void setInsuranceEnd(Date insuranceEnd) {
        this.insuranceEnd = insuranceEnd;
    }

    public String getLineGpsNo() {
        return lineGpsNo;
    }

    public void setLineGpsNo(String lineGpsNo) {
        this.lineGpsNo = lineGpsNo;
    }

    public String getNolineGpsNo() {
        return nolineGpsNo;
    }

    public void setNolineGpsNo(String nolineGpsNo) {
        this.nolineGpsNo = nolineGpsNo;
    }

    public Date getBizRisksStart() {
        return bizRisksStart;
    }

    public void setBizRisksStart(Date bizRisksStart) {
        this.bizRisksStart = bizRisksStart;
    }

    public Date getBizRisksEnd() {
        return bizRisksEnd;
    }

    public void setBizRisksEnd(Date bizRisksEnd) {
        this.bizRisksEnd = bizRisksEnd;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getLineGpsInstAdd() {
        return lineGpsInstAdd;
    }

    public void setLineGpsInstAdd(String lineGpsInstAdd) {
        this.lineGpsInstAdd = lineGpsInstAdd;
    }

    public String getNoLineGpsInstAdd() {
        return noLineGpsInstAdd;
    }

    public void setNoLineGpsInstAdd(String noLineGpsInstAdd) {
        this.noLineGpsInstAdd = noLineGpsInstAdd;
    }

    public String getCarConfig() {
        return carConfig;
    }

    public void setCarConfig(String carConfig) {
        this.carConfig = carConfig;
    }

    public String getCarAge() {
        return carAge;
    }

    public void setCarAge(String carAge) {
        this.carAge = carAge;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarEmissions() {
        return carEmissions;
    }

    public void setCarEmissions(String carEmissions) {
        this.carEmissions = carEmissions;
    }

    public BigDecimal getFirstBuyAmt() {
        return firstBuyAmt;
    }

    public void setFirstBuyAmt(BigDecimal firstBuyAmt) {
        this.firstBuyAmt = firstBuyAmt;
    }

    public String getRegOrg() {
        return regOrg;
    }

    public void setRegOrg(String regOrg) {
        this.regOrg = regOrg;
    }

    public String getFirstRegDate() {
        return firstRegDate;
    }

    public void setFirstRegDate(String firstRegDate) {
        this.firstRegDate = firstRegDate;
    }

    public String getPassengerNum() {
        return passengerNum;
    }

    public void setPassengerNum(String passengerNum) {
        this.passengerNum = passengerNum;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getTransferNum() {
        return transferNum;
    }

    public void setTransferNum(String transferNum) {
        this.transferNum = transferNum;
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

    public String getMortgageCompany() {
        return mortgageCompany;
    }

    public void setMortgageCompany(String mortgageCompany) {
        this.mortgageCompany = mortgageCompany;
    }

    public String getBuyWays() {
        return buyWays;
    }

    public void setBuyWays(String buyWays) {
        this.buyWays = buyWays;
    }

    public BigDecimal getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(BigDecimal assessPrice) {
        this.assessPrice = assessPrice;
    }

    public String getCarKeyFlag() {
        return carKeyFlag;
    }

    public void setCarKeyFlag(String carKeyFlag) {
        this.carKeyFlag = carKeyFlag;
    }

}
