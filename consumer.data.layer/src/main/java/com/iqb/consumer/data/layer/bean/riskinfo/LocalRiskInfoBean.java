/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年1月3日 上午11:24:26
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.riskinfo;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * chengzhen 2018年3月26日 18:04:25
 */
@SuppressWarnings("serial")
public class LocalRiskInfoBean extends BaseEntity {

    private String orderId;// 订单号
    private String tradeNo;// 交易号
    private String reportType;// 报告类型
    private String reportState;// 报告状态
    private String reportContent;// 报告内容
    private String contentCreateTime1;// 个人合同生成时间
    private String contentCreateTime2;// 个人合同生成时间
    private String reportName;// 报告名称
    private String effect;// 1.未失效2.已失效
    private String reportNo;// 报告编号
    private String errCode;// 错误编号
    private String errMsg;// 错误信息
    private String personDetail;// 涉案详情

    public String getPersonDetail() {
        return personDetail;
    }

    public void setPersonDetail(String personDetail) {
        this.personDetail = personDetail;
    }

    public String getContentCreateTime1() {
        return contentCreateTime1;
    }

    public void setContentCreateTime1(String contentCreateTime1) {
        this.contentCreateTime1 = contentCreateTime1;
    }

    public String getContentCreateTime2() {
        return contentCreateTime2;
    }

    public void setContentCreateTime2(String contentCreateTime2) {
        this.contentCreateTime2 = contentCreateTime2;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportState() {
        return reportState;
    }

    public void setReportState(String reportState) {
        this.reportState = reportState;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

}
