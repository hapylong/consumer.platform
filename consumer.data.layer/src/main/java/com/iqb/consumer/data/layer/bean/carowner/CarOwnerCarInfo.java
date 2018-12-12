package com.iqb.consumer.data.layer.bean.carowner;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.util.StringUtil;

public class CarOwnerCarInfo {
    private static final Logger log = LoggerFactory.getLogger(CarOwnerCarInfo.class);
    /** inst_authoritycard **/
    private String carNo;// 车架号
    private String plate;// 车牌号
    private Date firstRegDate;// 初次登记日期

    /** inst_mortgageinfo **/
    private String mortgageType; // 抵押类型
    private String mortgageCompany; // 抵押机构

    /** inst_orderinfo **/
    private BigDecimal assessPrice;// 车辆评估价格
    private BigDecimal orderAmt; // 核准金额 && 借款金额
    private String orderId;// 订单号
    private String regId; // 手机号
    private Date createTime; // 订单时间
    private String orderName;// 品牌-型号

    /** inst_user **/
    private String realName; // 姓名
    private String idNo; // 根据身份证号解析【年龄 & 性别】

    /** inst_merchantinfo **/
    private String merchantName; // 商户

    private String pinPai; // 品牌
    private String xingHao; // 型号
    private Integer age; // 年龄
    private String gender; // 性别

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Date getFirstRegDate() {
        return firstRegDate;
    }

    public void setFirstRegDate(Date firstRegDate) {
        this.firstRegDate = firstRegDate;
    }

    public String getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType;
    }

    public String getMortgageCompany() {
        return mortgageCompany;
    }

    public void setMortgageCompany(String mortgageCompany) {
        this.mortgageCompany = mortgageCompany;
    }

    public BigDecimal getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(BigDecimal assessPrice) {
        this.assessPrice = assessPrice;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
        if (!StringUtil.isEmpty(idNo)) {
            this.age = com.iqb.consumer.common.utils.StringUtil.getAgeByIdCardNo(idNo);
            this.gender = com.iqb.consumer.common.utils.StringUtil.getGenderByIdCardNo(idNo);
        }
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
        if (!StringUtil.isEmpty(orderName)) {
            String[] ons = orderName.split("-");
            this.pinPai = getPinPai(ons);
            this.xingHao = getXingHao(ons);
        }
    }

    public String getPinPai() {
        return pinPai;
    }

    public String getXingHao() {
        return xingHao;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    private String getPinPai(String[] orderName) {
        try {
            return orderName[0];
        } catch (Exception e) {
            log.error("getPinPai :", e);
            return "";
        }
    }

    private String getXingHao(String[] orderName) {
        try {
            return orderName[1];
        } catch (Exception e) {
            log.error("getXingHao ：", e);
            return "";
        }
    }

}
