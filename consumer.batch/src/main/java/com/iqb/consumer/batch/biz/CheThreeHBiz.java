package com.iqb.consumer.batch.biz;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.iqb.consumer.batch.dao.CheThreeHDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 
 * Description: 车300灰名单biz服务
 * 
 * @author chneyong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2018年8月16日    chenyong       1.0        1.0 Version 
 * </pre>
 */
@Component
public class CheThreeHBiz extends BaseBiz {

    @Autowired
    private CheThreeHDao cheThreeHDao;

    /**
     * 
     * Description: 灰名单待停止监控列表查询
     * 
     * @param
     * @return List<JSONObject>
     * @throws
     * @Author chenyong
     */
    public List<Map<String, Object>> selectCheThreeHWaitStopList(int pageSize) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(1, pageSize);
        return this.cheThreeHDao.selectCheThreeHWaitStopList();
    }

    /**
     * 
     * Description: 灰名单待停止监控列表查询
     * 
     * @param
     * @return List<String>
     * @throws
     * @Author chenyong
     */
    public int stopMonitorOrderLoanrisk(String orderId, Integer assetsId) {
        super.setDb(0, super.MASTER);
        return this.cheThreeHDao.stopMonitorOrderLoanrisk(orderId, assetsId);
    }

}
