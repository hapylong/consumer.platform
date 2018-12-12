package com.iqb.consumer.data.layer.bean.afterLoan;

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
 * 2018年7月17日下午5:08:28 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@SuppressWarnings("serial")
public class InstReceivedPaymentBean extends BaseEntity {
    private String caseId;
    /** 订单号 **/
    private String orderId;
    /** 法务回款 **/
    private String receivedPayment;
    /** 法务回款日期 **/
    private String receivedPaymentDate;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReceivedPayment() {
        return receivedPayment;
    }

    public void setReceivedPayment(String receivedPayment) {
        this.receivedPayment = receivedPayment;
    }

    public String getReceivedPaymentDate() {
        return receivedPaymentDate;
    }

    public void setReceivedPaymentDate(String receivedPaymentDate) {
        this.receivedPaymentDate = receivedPaymentDate;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

}
