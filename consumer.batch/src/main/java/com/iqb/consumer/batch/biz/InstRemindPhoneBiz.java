package com.iqb.consumer.batch.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.batch.dao.InstRemindPhoneDao;
import com.iqb.consumer.batch.data.pojo.InstRemindPhoneBean;
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
 * 2018年4月27日下午5:07:35 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class InstRemindPhoneBiz extends BaseBiz {
    @Autowired
    private InstRemindPhoneDao instRemindPhoneDao;

    /**
     * 
     * Description:保存inst_settlementResult
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    public long insertInstRemindPhoneResult(List<InstRemindPhoneBean> list) {
        setDb(0, super.MASTER);
        return instRemindPhoneDao.insertInstRemindPhoneResult(list);
    }

    /**
     * 
     * Description:查询完成处理的电话提醒记录列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月27日
     */
    public List<InstRemindPhoneBean> selectInstRemindPhoneList(JSONObject objs) {
        setDb(1, super.MASTER);
        return instRemindPhoneDao.selectInstRemindPhoneList(objs);
    }

    /**
     * 
     * Description:根据订单号期数修改电话提醒信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月27日
     */
    public long updateInstRemindPhoneResult(JSONObject objs) {
        setDb(0, super.MASTER);
        return instRemindPhoneDao.updateInstRemindPhoneResult(objs);
    }

    /**
     * 
     * Description:根据订单号当前期数查询电话提醒信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月2日
     */
    public InstRemindPhoneBean selectInstRemindPhone(JSONObject objs) {
        setDb(1, super.MASTER);
        return instRemindPhoneDao.selectInstRemindPhone(objs);
    }

    /**
     * 
     * Description:根据订单号查询贷后车辆信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    public InstRemindPhoneBean selectInstManageCarInfo(String orderId) {
        setDb(1, super.MASTER);
        return instRemindPhoneDao.selectInstManageCarInfo(orderId);
    }
}
