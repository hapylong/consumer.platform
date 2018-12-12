package com.iqb.consumer.data.layer.biz.carstatus;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindRecordBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;
import com.iqb.consumer.data.layer.dao.carstatus.InstRemindPhoneDao;
import com.iqb.consumer.data.layer.dao.carstatus.InstRemindRecordDao;
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
 * 2018年4月26日下午4:16:51 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class InstRemindRecordBiz extends BaseBiz {
    @Autowired
    private InstRemindRecordDao instRemindRecordDao;
    @Autowired
    private InstRemindPhoneDao instRemindPhoneDao;

    /**
     * 根据订单号 期数查询电话提醒列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月25日
     */
    public List<InstRemindRecordBean> selectInstRemindRecordList(JSONObject objs) {
        super.setDb(1, super.MASTER);
        if (objs.get("isPageHelper") == null) {
            PageHelper.startPage(getPagePara(objs));
        }
        return instRemindRecordDao.selectInstRemindRecordList(objs);
    }

    /**
     * 
     * 插入电话信息信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月26日
     */
    public int insertInstRemindRecordInfo(InstRemindRecordBean bean) {
        super.setDb(0, super.MASTER);
        return instRemindRecordDao.insertInstRemindRecordInfo(bean);
    }

    /**
     * 
     * 根据订单号 回显贷后客户信息流程页面数据
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月27日 14:11:50
     */
    public InstRemindRecordBean queryCustomerInfo(JSONObject objs) {
        super.setDb(0, super.MASTER);
        return instRemindRecordDao.queryCustomerInfo(objs);
    }

    public List<ManageCarInfoBean> queryManagecarInfo(String orderId) {
        super.setDb(1, super.MASTER);
        return instRemindPhoneDao.queryManagecarInfo(orderId);
    }
}
