package com.iqb.consumer.batch.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.batch.dao.InstAssetStockDao;
import com.iqb.consumer.batch.data.pojo.InstAssetStockBean;
import com.iqb.etep.common.base.biz.BaseBiz;

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
 * 2018年8月2日下午2:22:41 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class InstAssetStockBiz extends BaseBiz {
    @Autowired
    private InstAssetStockDao instAssetStockDao;

    /**
     * 
     * 批量插入每日资产存量数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月2日
     */
    public int batchInsertInstAssetStock(List<InstAssetStockBean> recordList) {
        setDb(0, super.MASTER);
        return instAssetStockDao.batchInsertInstAssetStock(recordList);
    }
}
