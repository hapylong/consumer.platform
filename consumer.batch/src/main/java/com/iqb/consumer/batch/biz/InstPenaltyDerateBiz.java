package com.iqb.consumer.batch.biz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.batch.dao.InstPenaltyDerateDao;
import com.iqb.consumer.batch.data.pojo.SpecialTimeOrderPojo;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年11月29日下午5:32:36 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class InstPenaltyDerateBiz extends BaseBiz {
    @Resource
    private InstPenaltyDerateDao instPenaltyDerateDao;

    /**
     * 
     * Description:查询当天申请中的罚息减免信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月29日
     */
    public List<SpecialTimeOrderPojo> getCurrentInstPenaltyDerateInfo(String currentDate) {
        super.setDb(0, super.SLAVE);
        return this.instPenaltyDerateDao.getCurrentInstPenaltyDerateInfo(currentDate);
    }

    /**
     * 
     * Description:将当天的罚息减免信息状态改为已拒绝
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月29日
     */
    public int updateInstPenaltyDerateByOid(String procInstId) {
        super.setDb(0, super.MASTER);
        return this.instPenaltyDerateDao.updateInstPenaltyDerateByOid(procInstId);
    }
}
