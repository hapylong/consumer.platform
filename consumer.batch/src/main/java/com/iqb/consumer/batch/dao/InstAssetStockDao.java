package com.iqb.consumer.batch.dao;

import java.util.List;

import com.iqb.consumer.batch.data.pojo.InstAssetStockBean;

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
 * 2018年8月2日下午2:20:56 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstAssetStockDao {
    /**
     * 
     * 批量插入每日资产存量数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月2日
     */
    public int batchInsertInstAssetStock(List<InstAssetStockBean> recordList);
}
