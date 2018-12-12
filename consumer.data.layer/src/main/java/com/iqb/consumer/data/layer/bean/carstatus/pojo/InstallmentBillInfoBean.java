package com.iqb.consumer.data.layer.bean.carstatus.pojo;

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
 * 2018年6月11日上午11:51:26 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstallmentBillInfoBean {
    // 订单id
    private String orderId;
    // 还款顺序
    private int repayNo;
    // 逾期天数
    private int overdueDays;
    // 账单状态
    private int status;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(int repayNo) {
        this.repayNo = repayNo;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
