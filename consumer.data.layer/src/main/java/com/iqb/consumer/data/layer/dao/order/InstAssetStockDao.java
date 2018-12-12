package com.iqb.consumer.data.layer.dao.order;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.order.InstAssetStockBean;

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
     * 根据条件查询资产存量报表数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月2日
     */
    public List<InstAssetStockBean> selInstAssetStockInfo(JSONObject objs);
}
