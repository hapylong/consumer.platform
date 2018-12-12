package com.iqb.consumer.data.layer.dao.pay;

/**
 * 支付中间表
 * 
 * @author Yeoman
 * 
 */
public interface PayMiddleDao {

    /**
     * 支付记录中间表，确定表中是否存在记录
     * 
     * @param orderId
     * @return
     */
    int getPayMidCount(String orderId);

    /**
     * 通过orderId删除支付中间表
     * 
     * @param orderId
     * @return
     */
    int delPayMiddle(String orderId);

    /**
     * 插入支付中间记录表
     * 
     * @param orderId
     * @return
     */
    int insertMiddle(String orderId);
}
