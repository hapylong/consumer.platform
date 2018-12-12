/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年1月12日 上午11:11:13
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface OrderOtherInfoDao {

    /**
     * 更新工作流相关信息
     * 
     * @param orderOtherInfo
     * @return
     */
    int updateOrderOtherInfo(OrderOtherInfo orderOtherInfo);

    /**
     * 根据orderId获取订单信息
     */
    public OrderOtherInfo selectOne(Map<String, Object> params);

    /**
     * 批量插入订单相关信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月8日
     */
    public int batchInsertOrderOtherInfo(List<OrderOtherInfo> list);

    /**
     * 
     * Description:根据订单号修改信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月19日
     */
    public int updateOrderOtherInfoByOrderId(OrderOtherInfo orderOtherInfo);
}
