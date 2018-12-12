package com.iqb.consumer.data.layer.biz.afterLoan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.afterLoan.InstManageCarReceiveBean;
import com.iqb.consumer.data.layer.dao.afterLoan.InstManageCarReceiveDao;
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
 * 2018年7月11日下午6:16:08 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Repository
public class InstManageCarReceiveBiz extends BaseBiz {
    @Autowired
    private InstManageCarReceiveDao instManageCarReceiveDao;

    /**
     * 
     * 保存贷后车辆回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月11日
     */
    public int saveInstManageCarReceive(InstManageCarReceiveBean bean) {
        setDb(0, super.MASTER);
        return instManageCarReceiveDao.saveInstManageCarReceive(bean);
    }

    /**
     * 
     * 更新贷后车辆回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月11日
     */
    public int updateInstManageCarReceiveByOrderId(JSONObject objs) {
        setDb(0, super.MASTER);
        return instManageCarReceiveDao.updateInstManageCarReceiveByOrderId(objs);
    }

    /**
     * 
     * 根据订单号查询车辆回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月26日
     */
    public InstManageCarReceiveBean getInstManageCarReceiveInfoByOrderId(JSONObject objs) {
        setDb(0, super.SLAVE);
        return instManageCarReceiveDao.getInstManageCarReceiveInfoByOrderId(objs);
    }
}
