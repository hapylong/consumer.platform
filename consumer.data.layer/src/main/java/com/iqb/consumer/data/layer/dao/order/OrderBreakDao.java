/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月20日 下午2:11:26
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao.order;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.order.OrderBreakInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface OrderBreakDao {

    /**
     * 查询拆分信息
     * 
     * @param orderId
     * @return
     */
    OrderBreakInfo selOrderInfo(@Param("orderId") String orderId);

    /**
     * 插入拆分信息
     * 
     * @param orderBreakInfo
     * @return
     */
    int insertOrderInfo(OrderBreakInfo orderBreakInfo);

    /**
     * 修改拆分信息
     * 
     * @param orderBreakInfo
     * @return
     */
    int updateOrderInfo(OrderBreakInfo orderBreakInfo);
}
