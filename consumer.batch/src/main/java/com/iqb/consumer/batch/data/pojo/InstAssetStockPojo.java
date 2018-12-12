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
 * 2018年8月6日下午2:25:13 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public class InstAssetStockPojo {
    private List<InstAssetStockBean> recordList;

    public List<InstAssetStockBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<InstAssetStockBean> recordList) {
        this.recordList = recordList;
    }

}
