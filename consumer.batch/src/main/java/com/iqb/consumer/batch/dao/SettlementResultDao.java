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
package com.iqb.consumer.batch.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.batch.data.pojo.SettlementResultBean;

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
     * Description:根据条件查询记录
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    public List<SettlementResultBean> selectSettlementResultByParams(Map<String, String> params);

    /**
     * 
     * Description:获取每日划扣失败记录数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    public List<SettlementResultBean> selectFailSettlementResultByParams(Map<String, String> params);

}
