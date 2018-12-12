package com.iqb.consumer.data.layer.biz.pushRecord;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.pushRecord.PushRecordBean;
import com.iqb.consumer.data.layer.bean.reqmoney.RequestMoneyBean;
import com.iqb.consumer.data.layer.dao.pushRecord.PushRecordDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 推标记录Biz
 * 
 * @author town
 * 
 */
@Repository
public class PushRecordBiz extends BaseBiz {
    @Autowired
    private PushRecordDao pushRecordDao;

    /**
     * 根据订单号获取推标记录 2018年2月27日 10:47:42 chengzhen
     */
    public List<PushRecordBean> pushRecordByOrderIdList(JSONObject obj) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(obj));
        List<PushRecordBean> resList = pushRecordDao.pushRecordByOrderIdList(obj);
        for (PushRecordBean requestMoneyBean : resList) {
            if (requestMoneyBean.getApplyInstIDay() != 0) {
                requestMoneyBean.setApplyItems(requestMoneyBean.getApplyItems() + 1);
            }
        }
        return resList;
    }

    /**
     * 根据订单号打包拆分状态 2018年2月27日 14:52:41 chengzhen
     */
    public List<Integer> getJSYStateByOrderId(JSONObject objs) {
        setDb(0, super.SLAVE);
        return pushRecordDao.getJSYStateByOrderId(objs);
    }

    /**
     * 保存删除原因 2018年2月27日 15:14:56 chengzhen
     */
    public void insertDelPRRemark(JSONObject objs) {
        setDb(0, super.MASTER);
        pushRecordDao.insertDelPRRemark(objs);
    }

    /**
     * 根据id删除推标记录 2018年2月27日 15:36:14 chengzhen
     */
    public void delPushRecordById(HashMap<String, Long> delIdMap) {
        setDb(0, super.MASTER);
        pushRecordDao.delPushRecordById(delIdMap);
    }

    public void delJSYPushRecordById(HashMap<String, Long> delIdMap) {
        setDb(0, super.MASTER);
        pushRecordDao.delJSYPushRecordById(delIdMap);
    }
}
