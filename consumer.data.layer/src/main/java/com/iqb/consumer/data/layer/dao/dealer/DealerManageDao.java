package com.iqb.consumer.data.layer.dao.dealer;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.data.layer.bean.dealer.DealerManage;

/**
 * 车商管理Dao
 * 
 * @author Yeoman
 * 
 */
public interface DealerManageDao {

    /**
     * 保存车商数据信息
     * 
     * @return
     */
    int saveDealerManageInfo(DealerManage dealerManage);

    /**
     * 查询黑名单信息By客户渠道名称
     * 
     * @param params
     * @return
     */
    int judgeBlackInfo(Map<String, Object> params);

    /**
     * 修改车商信息
     * 
     * @param dealerManage
     * @return
     */
    int updateDealerManageInfo(DealerManage dealerManage);

    /**
     * 通过条件查询车商信息
     * 
     * @param orderId
     * @return
     */
    DealerManage getDealerInfo(Map<String, Object> params);

    /**
     * 根据车商id批量修改车商状态(黑名单)
     * 
     * @param objs
     * @param request
     * @return
     */
    int batchUpdateDealerManageInfoStatus(Map<String, Object> params);

    /**
     * 通过车商名称查询订单列表
     * 
     * @param sourceCarName
     * @return
     */
    List<String> getAllOrderIds(String sourceCarName);
}
