package com.iqb.consumer.batch.dao;

import java.util.List;

import com.iqb.consumer.batch.data.pojo.SpecialTimeOrderPojo;

/**
 * Description:罚息减免dao
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2017年11月29日下午5:14:06 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstPenaltyDerateDao {
    /**
     * 
     * Description:查询当天申请中的罚息减免信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月29日
     */
    public List<SpecialTimeOrderPojo> getCurrentInstPenaltyDerateInfo(String currentDate);

    /**
     * 
     * Description:将当天的罚息减免信息状态改为已拒绝
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月29日
     */
    public int updateInstPenaltyDerateByOid(String procInstId);
}
