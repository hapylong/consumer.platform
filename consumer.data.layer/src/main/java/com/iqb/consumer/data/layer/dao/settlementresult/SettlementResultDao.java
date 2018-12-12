/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月23日下午6:07:33 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.data.layer.dao.settlementresult;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.settlementresult.SettlementResultBean;

/**
 * @author haojinlong
 * 
 */
public interface SettlementResultDao {
    /**
     * 
     * Description:保存inst_settlementResult
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    long insertSettlementResult(List<SettlementResultBean> list);

    /**
     * 
     * Description:更新inst_settlementResult
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    long updateSettlementResult(JSONObject objs);

    /**
     * 
     * Description: 通过 tradeNo 更新 表
     * 
     * @param
     * @return void
     * @throws @Author adam Create Date: 2017年6月26日 下午5:09:06
     */
    void updateSRBByTN(SettlementResultBean srb);

    /**
     * 
     * Description:查询自动划扣账单信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月15日
     */
    List<SettlementResultBean> listSettlementResult(JSONObject objs);
}
