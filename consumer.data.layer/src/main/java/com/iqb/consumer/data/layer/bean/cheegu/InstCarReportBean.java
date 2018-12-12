package com.iqb.consumer.data.layer.bean.cheegu;

import java.math.BigDecimal;
import java.util.List;

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
 * 2018年5月25日上午8:52:44 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class InstCarReportBean extends BaseEntity {
    /** 订单号 **/
    private String orderId;
    /** 车架号 **/
    private String vin;
    /** 车身颜色 **/
    @JSONField(name = "car_color")
    private String carColor;
    /** 车牌号码 **/
    @JSONField(name = "GBNo")
    private String plate;
    /** 发动机号 **/
    @JSONField(name = "EngineNo")
    private String engineNo;
    /** 车主姓名 **/
    @JSONField(name = "OwnerName")
    private String ownerName;
    /** 评估价格 **/
    @JSONField(name = "appraiseprice")
    private BigDecimal appraisePrice;
    /** 出厂日期 **/
    @JSONField(name = "ReleaseDate")
    private String releaseDate;
    /** 登记日期 **/
    @JSONField(name = "RegDate")
    private String regDate;
    /** 当前公里数 **/
    @JSONField(name = "ActualKM")
    private String actualKM;
    /** 排量 **/
    @JSONField(name = "Blowdown")
    private String blowdown;
    /** 功率 **/
    @JSONField(name = "Power")
    private String power;
    /** 车辆类型 **/
    @JSONField(name = "CarType")
    private String carType;
    /** 新车指导价 **/
    @JSONField(name = "ReplacementPrice1")
    private BigDecimal replacementPrice1;
    /** 已使用年限(月) **/
    @JSONField(name = "UsedMonth")
    private int usedMonth;
    /** 折算年限折(月) **/
    @JSONField(name = "ConvertMonth")
    private int convertMonth;
    /** 评估报告地址 **/
    @JSONField(name = "reporturl")
    private String reportUrl;
    /** 不予评估原因 **/
    @JSONField(name = "refuse_msg")
    private String refuseMsg;

    /** 图片信息 **/
    @JSONField(name = "data")
    private List<InstCarReportImageBean> imageList;
    /** 响应信息 **/
    private String msg;

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

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getAppraisePrice() {
        return appraisePrice;
    }

    public void setAppraisePrice(BigDecimal appraisePrice) {
        this.appraisePrice = appraisePrice;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getActualKM() {
        return actualKM;
    }

    public void setActualKM(String actualKM) {
        this.actualKM = actualKM;
    }

    public String getBlowdown() {
        return blowdown;
    }

    public void setBlowdown(String blowdown) {
        this.blowdown = blowdown;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public BigDecimal getReplacementPrice1() {
        return replacementPrice1;
    }

    public void setReplacementPrice1(BigDecimal replacementPrice1) {
        this.replacementPrice1 = replacementPrice1;
    }

    public int getUsedMonth() {
        return usedMonth;
    }

    public void setUsedMonth(int usedMonth) {
        this.usedMonth = usedMonth;
    }

    public int getConvertMonth() {
        return convertMonth;
    }

    public void setConvertMonth(int convertMonth) {
        this.convertMonth = convertMonth;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getRefuseMsg() {
        return refuseMsg;
    }

    public void setRefuseMsg(String refuseMsg) {
        this.refuseMsg = refuseMsg;
    }

    public List<InstCarReportImageBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<InstCarReportImageBean> imageList) {
        this.imageList = imageList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
