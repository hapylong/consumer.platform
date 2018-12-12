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
package com.iqb.consumer.data.layer.biz.settlementresult;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.settlementresult.SettlementResultBean;
import com.iqb.consumer.data.layer.dao.settlementresult.SettlementResultDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author haojinlong
 * 
 */
@Repository
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
    public long insertSettlementResult(List<SettlementResultBean> beanList) {
        setDb(0, super.SLAVE);
        return settlementResultDao.insertSettlementResult(beanList);
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
        setDb(0, super.SLAVE);
        return settlementResultDao.updateSettlementResult(objs);
    }

    public void updateSRBByTN(SettlementResultBean srb) {
        setDb(0, super.MASTER);
        settlementResultDao.updateSRBByTN(srb);
    }

    /**
     * 
     * Description:查询自动划扣账单信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月15日
     */
    public List<SettlementResultBean> listSettlementResult(JSONObject objs) {
        setDb(1, super.MASTER);
        return settlementResultDao.listSettlementResult(objs);
    }
}
