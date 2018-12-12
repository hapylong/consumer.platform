package com.iqb.consumer.batch.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.batch.data.pojo.SettleApplyBean;

public interface SettleApplyBeanDao {
    /**
     * 
     * Description:更新提前还款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月28日
     */
    public int updateSettleBean(SettleApplyBean sab);

    /**
     * 
     * Description:提前还款代偿查询接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    public List<SettleApplyBean> selectPrepaymentList(JSONObject objs);
}
