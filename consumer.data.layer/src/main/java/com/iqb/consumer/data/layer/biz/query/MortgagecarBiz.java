/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2017年4月25日
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.query;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.query.MortgagecarBean;
import com.iqb.consumer.data.layer.dao.MortgagecarDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="haojinlong@iqianbang.com">haojinlong</a>
 */
@Repository
public class MortgagecarBiz extends BaseBiz {
    @Resource
    private MortgagecarDao mortgagecarDao;

    /**
     * 获取所有符合条件的抵押车业务分成信息
     * 
     * @param JSONObject objs
     * @return List<MortgagecarBean>
     */
    public List<MortgagecarBean> selectMortgagecatList(JSONObject objs) {
        setDb(0, super.SLAVE);
        List<MortgagecarBean> list = mortgagecarDao.selectMortgagecatList(objs);
        return list;
    }
}
