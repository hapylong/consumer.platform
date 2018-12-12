/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年1月12日 上午11:12:38
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.wf;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.dao.OrderOtherInfoDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class OrderOtherInfoBiz extends BaseBiz {

    @Resource
    private OrderOtherInfoDao orderOtherInfoDao;

    public int updateOrderOtherInfo(OrderOtherInfo orderOtherInfo) {
        setDb(0, super.MASTER);
        return orderOtherInfoDao.updateOrderOtherInfo(orderOtherInfo);
    }

    public OrderOtherInfo selectOne(Map<String, Object> params) {
        setDb(1, super.MASTER);
        return orderOtherInfoDao.selectOne(params);
    }

    /**
     * 批量插入订单相关信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月8日
     */
    public int batchInsertOrderOtherInfo(List<OrderOtherInfo> list) {
        setDb(0, super.MASTER);
        return orderOtherInfoDao.batchInsertOrderOtherInfo(list);
    }

    /**
     * 
     * Description:根据订单号修改信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月19日
     */
    public int updateOrderOtherInfoByOrderId(OrderOtherInfo orderOtherInfo) {
        setDb(0, super.MASTER);
        return orderOtherInfoDao.updateOrderOtherInfoByOrderId(orderOtherInfo);
    }
}
