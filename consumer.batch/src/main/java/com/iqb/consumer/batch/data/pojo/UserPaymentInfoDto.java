package com.iqb.consumer.batch.data.pojo;

import java.util.List;

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
 * 2018年6月25日下午3:06:53 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class UserPaymentInfoDto {
    private List<UserPaymentInfoBean> result;

    public List<UserPaymentInfoBean> getResult() {
        return result;
    }

    public void setResult(List<UserPaymentInfoBean> result) {
        this.result = result;
    }

}
