package com.iqb.consumer.data.layer.bean.carthreehundred;

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
 * 2018年8月14日下午5:59:40 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstOrderloanRiskBean {
    /** 订单号 **/
    private String orderId;
    /** 发送状态 **/
    private String sendStatus;
    /** 车300返回状态 **/
    private String status;
    /** 车300返回错误信息 **/
    private String errorMsg;
    /** 车300返回串号 **/
    private String assetsId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(String assetsId) {
        this.assetsId = assetsId;
    }

}
