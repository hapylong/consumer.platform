/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 抵押车业务分成表接口
 * @date 2017年4月25日
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.query.MortgagecarBean;

/**
 * @author <a href="haojinlong@iqianbang.com">haojinlong</a>
 */
public interface MortgagecarDao {
    /**
     * 获取所有符合条件的抵押车业务分成信息
     * 
     * @param objs
     * @return
     */
    public List<MortgagecarBean> selectMortgagecatList(JSONObject objs);
}
