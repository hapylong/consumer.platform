package com.iqb.consumer.data.layer.biz.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.order.InstAssetStockBean;
import com.iqb.consumer.data.layer.dao.order.InstAssetStockDao;
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
 * 2018年8月2日下午4:33:14 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class InstAssetStockBiz extends BaseBiz {
    @Autowired
    private InstAssetStockDao instAssetStockDao;

    /**
     * 
     * 根据条件查询资产存量报表数据
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月2日
     */
    public List<InstAssetStockBean> selInstAssetStockInfo(JSONObject objs) {
        setDb(0, super.SLAVE);
        if (objs.get("isPageHelper") == null) {
            PageHelper.startPage(getPagePara(objs));
        }
        return instAssetStockDao.selInstAssetStockInfo(objs);
    }

}
