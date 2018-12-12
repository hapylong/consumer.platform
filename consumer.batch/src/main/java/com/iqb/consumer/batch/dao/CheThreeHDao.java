package com.iqb.consumer.batch.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CheThreeHDao {

    /**
     * 灰名单待停止监控列表查询
     */
    List<Map<String, Object>> selectCheThreeHWaitStopList();

    /**
     * 更新灰名单监控订单监控状态为取消发送
     */
    int stopMonitorOrderLoanrisk(@Param("orderId") String orderId, @Param("assetsId") Integer assetsId);

}
