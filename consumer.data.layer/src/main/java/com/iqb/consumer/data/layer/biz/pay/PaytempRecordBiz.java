package com.iqb.consumer.data.layer.biz.pay;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.data.layer.bean.pay.PayRecordBean;
import com.iqb.consumer.data.layer.dao.pay.PaytempRecordDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Repository
public class PaytempRecordBiz extends BaseBiz {

    @Resource
    private PaytempRecordDao paytempRecordDao;

    public PayRecordBean queryPayRecordByOrderId(String orderId) {
        setDb(0, super.MASTER);
        return paytempRecordDao.queryPayRecordByOrderId(orderId);
    }

    public void delPayRecord(String orderId) {
        setDb(0, super.MASTER);
        paytempRecordDao.delPayRecord(orderId);
    }

    public int insertPayRecord(PayRecordBean payRecordBean) {
        setDb(0, super.MASTER);
        return paytempRecordDao.insertPayRecord(payRecordBean);
    }

}
