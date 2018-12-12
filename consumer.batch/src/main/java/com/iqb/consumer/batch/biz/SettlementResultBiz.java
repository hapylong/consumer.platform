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
 * 2017年6月23日下午6:39:50 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.batch.biz;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.batch.dao.SettlementResultDao;
import com.iqb.consumer.batch.data.pojo.SettlementResultBean;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author haojinlong
 * 
 */
@Component
public class SettlementResultBiz extends BaseBiz {

    @Resource
    private SettlementResultDao settlementResultDao;

    /**
     * 
     * Description:保存inst_settlementResult
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    public long insertSettlementResult(List<SettlementResultBean> list) {
        setDb(0, super.MASTER);
        return settlementResultDao.insertSettlementResult(list);
    }

    /**
     * 
     * Description:更新inst_settlementResult
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    public long updateSettlementResult(JSONObject objs) {
        setDb(0, super.MASTER);
        return settlementResultDao.updateSettlementResult(objs);
    }

    /**
     * 
     * Description:根据条件查询记录
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    public List<SettlementResultBean> selectSettlementResultByParams(Map<String, String> params) {
        setDb(0, super.SLAVE);
        return settlementResultDao.selectSettlementResultByParams(params);
    }

    /**
     * 
     * Description:获取每日划扣失败记录数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    public List<SettlementResultBean> selectFailSettlementResultByParams(Map<String, String> params) {
        setDb(0, super.SLAVE);
        return settlementResultDao.selectFailSettlementResultByParams(params);
    }
}
