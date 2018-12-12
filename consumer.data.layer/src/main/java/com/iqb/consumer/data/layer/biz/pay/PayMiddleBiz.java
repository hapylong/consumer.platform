package com.iqb.consumer.data.layer.biz.pay;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.data.layer.dao.pay.PayMiddleDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Repository
public class PayMiddleBiz extends BaseBiz {

    @Resource
    private PayMiddleDao payMiddleDao;

    /**
     * 支付记录中间表，确定表中是否存在记录
     * 
     * @param orderId
     * @return
     */
    public int getPayMidCount(String orderId) {
        setDb(0, super.SLAVE);
        return payMiddleDao.getPayMidCount(orderId);
    }

    /**
     * 通过orderId删除支付中间表
     * 
     * @param orderId
     * @return
     */
    public int delPayMiddle(String orderId) {
        setDb(0, super.MASTER);
        return payMiddleDao.delPayMiddle(orderId);
    }

    /**
     * 插入支付中间记录表
     * 
     * @param orderId
     * @return
     */
    public int insertMiddle(String orderId) {
        setDb(0, super.MASTER);
        return payMiddleDao.insertMiddle(orderId);
    }
}
