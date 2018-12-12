package com.iqb.consumer.data.layer.bean.carstatus.pojo;

import java.math.BigDecimal;

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
 * 2018年5月4日上午9:53:45 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class AfterLoanBean {
    /** 订单号 **/
    private String orderId;
    /** 手机号码 **/
    private String regId;
    /** 姓名 **/
    private String realName;
    /** 商户名称 **/
    private String merchantName;
    /** 订单金额 **/
    private BigDecimal orderAmt;
    /** 月供 **/
    private BigDecimal monthInterest;
    /** 品牌车系 **/
    private String orderName;
    /** 车牌号 **/
    private String plate;
    /** 车架号 **/
    private String carNo;
    /** gps状态 **/
    private int gpsStatus;
    /** GPS备注 **/
    private String remark;
    /** 处置方案 **/
    private String disposalScheme;
    /** 贷后意见 **/
    private String afterLoanOpinion;
    /** 还款标识 1 是 2 否 **/
    private int repaymentFlag;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    public BigDecimal getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal monthInterest) {
        this.monthInterest = monthInterest;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
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

    public int getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(int gpsStatus) {
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

    public String getAfterLoanOpinion() {
        return afterLoanOpinion;
    }

    public void setAfterLoanOpinion(String afterLoanOpinion) {
        this.afterLoanOpinion = afterLoanOpinion;
    }

    public int getRepaymentFlag() {
        return repaymentFlag;
    }

    public void setRepaymentFlag(int repaymentFlag) {
        this.repaymentFlag = repaymentFlag;
    }

}
