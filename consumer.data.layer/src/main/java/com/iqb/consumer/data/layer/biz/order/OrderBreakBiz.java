/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月20日 下午2:48:55
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.order;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.order.OrderBreakInfo;
import com.iqb.consumer.data.layer.dao.order.OrderBreakDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class OrderBreakBiz extends BaseBiz {

    @Resource
    private OrderBreakDao orderBreakDao;

    /**
     * 查询拆分信息
     * 
     * @param orderId
     * @return
     */
    public OrderBreakInfo selOrderInfo(String orderId) {
        setDb(0, super.SLAVE);
        return orderBreakDao.selOrderInfo(orderId);
    }

    /**
     * 插入拆分信息
     * 
     * @param orderBreakInfo
     * @return
     */
    public int insertOrderInfo(OrderBreakInfo orderBreakInfo) {
        setDb(0, super.MASTER);
        return orderBreakDao.insertOrderInfo(orderBreakInfo);
    }

    /**
     * 修改拆分信息
     * 
     * @param orderBreakInfo
     * @return
     */
    public int updateOrderInfo(OrderBreakInfo orderBreakInfo) {
        setDb(0, super.MASTER);
        return orderBreakDao.updateOrderInfo(orderBreakInfo);
    }
}
