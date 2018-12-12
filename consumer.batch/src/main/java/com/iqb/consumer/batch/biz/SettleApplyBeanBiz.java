package com.iqb.consumer.batch.biz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.batch.dao.SettleApplyBeanDao;
import com.iqb.consumer.batch.data.pojo.SettleApplyBean;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class SettleApplyBeanBiz extends BaseBiz {

    @Resource
    private SettleApplyBeanDao settleApplyBeanDao;

    /**
     * 
     * Description:更新提前还款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月28日
     */
    public int updateSettleBean(SettleApplyBean sab) {
        setDb(0, super.MASTER);
        return settleApplyBeanDao.updateSettleBean(sab);
    }

    /**
     * 
     * Description:提前还款代偿分页查询接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    public List<SettleApplyBean> selectPrepaymentList(JSONObject objs) {
        setDb(1, super.MASTER);
        return settleApplyBeanDao.selectPrepaymentList(objs);
    }
}
