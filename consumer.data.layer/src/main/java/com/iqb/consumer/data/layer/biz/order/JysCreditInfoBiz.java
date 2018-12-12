package com.iqb.consumer.data.layer.biz.order;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.jys.JYSCreditInfoBean;
import com.iqb.consumer.data.layer.dao.JysCreditInfoDao;
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
 * 2017年11月3日下午6:25:38 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class JysCreditInfoBiz extends BaseBiz {

    @Resource
    private JysCreditInfoDao jysCreditInfoDao;

    /**
     * 
     * Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月3日
     */
    public long insertJysCreditInfo(JYSCreditInfoBean job) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        jysCreditInfoDao.insertJysCreditInfo(job);
        return job.getId();
    }

    /**
     * 
     * Description:获取交易所债权人信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月3日
     */
    public JYSCreditInfoBean getJysCreditInfo(JSONObject objs) {
        // 设置数据源为从库
        setDb(0, super.SLAVE);
        return jysCreditInfoDao.getJysCreditInfo(objs);
    }
}
