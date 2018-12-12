package com.iqb.consumer.data.layer.dao.afterLoan;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.afterLoan.InstReceivedPaymentBean;

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
 * 2018年7月17日下午5:07:22 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstReceivedPaymentDao {
    /**
     * 
     * 批量保存法务回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    int batchSaveInstReceivedPayment(List<InstReceivedPaymentBean> list);

    /**
     * 
     * 根据订单号查询法务回款列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    List<InstReceivedPaymentBean> selectInstReceivedPaymentList(@Param("orderId") String orderId,
            @Param("caseId") String caseId);

    /**
     * 
     * 根据caseId删除法务回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月30日
     */
    int deleteReceivedPayInfoByCaseId(@Param("caseId") String caseId);
}
