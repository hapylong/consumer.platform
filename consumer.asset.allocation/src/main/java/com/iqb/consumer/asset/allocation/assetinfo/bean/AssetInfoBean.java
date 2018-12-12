package com.iqb.consumer.asset.allocation.assetinfo.bean;

/**
 * Description: 资产分配bean
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------
 * 2016年9月29日    wangxinbang       1.0        1.0 Version
 * </pre>
 */
public class AssetInfoBean {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 项目名称
     */
    private String proName;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 借款人
     */
    private String borrower;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private String orderAmt;

    /**
     * 订单日期
     */
    private String orderDate;

    /**
     * 产品方案
     */
    private String productPlan;

    /**
     * 订单期数
     */
    private String orderItems;

    /**
     * 剩余期数
     */
    private String surplusItems;

    /**
     * 资金来源
     */
    private String fundSource;

    /**
     * 订单日期查询开始
     */
    private String queryStartOrderTime;

    /**
     * 订单日期查询结束
     */
    private String queryEndOrderTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getProductPlan() {
        return productPlan;
    }

    public void setProductPlan(String productPlan) {
        this.productPlan = productPlan;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public String getSurplusItems() {
        return surplusItems;
    }

    public void setSurplusItems(String surplusItems) {
        this.surplusItems = surplusItems;
    }

    public String getFundSource() {
        return fundSource;
    }

    public void setFundSource(String fundSource) {
        this.fundSource = fundSource;
    }

    public String getQueryStartOrderTime() {
        return queryStartOrderTime;
    }

    public void setQueryStartOrderTime(String queryStartOrderTime) {
        this.queryStartOrderTime = queryStartOrderTime;
    }

    public String getQueryEndOrderTime() {
        return queryEndOrderTime;
    }

    public void setQueryEndOrderTime(String queryEndOrderTime) {
        this.queryEndOrderTime = queryEndOrderTime;
    }

}
