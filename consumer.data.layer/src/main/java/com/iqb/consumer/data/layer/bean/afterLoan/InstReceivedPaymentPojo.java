package com.iqb.consumer.data.layer.bean.afterLoan;

import java.util.List;
import java.util.Map;

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
 * 2018年7月17日下午6:05:08 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstReceivedPaymentPojo {
    private List<Map<String, String>> receivedPaymentList;

    public List<Map<String, String>> getReceivedPaymentList() {
        return receivedPaymentList;
    }

    public void setReceivedPaymentList(List<Map<String, String>> receivedPaymentList) {
        this.receivedPaymentList = receivedPaymentList;
    }

}
