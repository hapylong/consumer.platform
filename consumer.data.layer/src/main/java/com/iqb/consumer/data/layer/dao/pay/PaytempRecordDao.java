package com.iqb.consumer.data.layer.dao.pay;

import com.iqb.consumer.data.layer.bean.pay.PayRecordBean;

public interface PaytempRecordDao {

    /**
     * 查询支付中间记录
     * 
     * @param payRecordBean
     * @return
     */
    PayRecordBean queryPayRecordByOrderId(String orderId);

    void delPayRecord(String orderId);

    int insertPayRecord(PayRecordBean payRecordBean);
}
