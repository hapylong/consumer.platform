package com.iqb.consumer.batch.data.pojo;

/**
 * 
 * Description: 订单信息扩展
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年12月14日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class OrderOtherInfo extends BaseEntity {

    /** 订单id **/
    private String orderId;
    /** 商户号 **/
    private String merchantNo;
    /** 评估价格 **/
    private String assessPrice;
    /** 项目名称 **/
    private String projectName;
    /** 项目编号 **/
    private String projectNo;
    /** 担保人(公司名) **/
    private String guarantee;
    /** 担保人法人姓名 **/
    private String guaranteeName;
    /** 车辆排序编号(用于辅助PROJECTNO 项目编号) **/
    private int carSortNo;
    /** gps备注 **/
    private String gpsRemark;
    /** 到账金额 **/
    private String receiveAmt;
    /** 备注 **/
    private String remark;

    public String getGpsRemark() {
        return gpsRemark;
    }

    public void setGpsRemark(String gpsRemark) {
        this.gpsRemark = gpsRemark;
    }

    public String getReceiveAmt() {
        return receiveAmt;
    }

    public void setReceiveAmt(String receiveAmt) {
        this.receiveAmt = receiveAmt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCarSortNo() {
        return carSortNo;
    }

    public void setCarSortNo(int carSortNo) {
        this.carSortNo = carSortNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAssessPrice() {
        return assessPrice;
    }

    public void setAssessPrice(String assessPrice) {
        this.assessPrice = assessPrice;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public String getGuaranteeName() {
        return guaranteeName;
    }

    public void setGuaranteeName(String guaranteeName) {
        this.guaranteeName = guaranteeName;
    }

}
