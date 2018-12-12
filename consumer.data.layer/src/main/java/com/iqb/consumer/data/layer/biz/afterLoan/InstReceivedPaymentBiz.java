package com.iqb.consumer.data.layer.biz.afterLoan;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iqb.consumer.data.layer.bean.afterLoan.InstReceivedPaymentBean;
import com.iqb.consumer.data.layer.dao.afterLoan.InstReceivedPaymentDao;
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
 * 2018年7月17日下午5:49:43 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Repository
public class InstReceivedPaymentBiz extends BaseBiz {
    @Autowired
    private InstReceivedPaymentDao instReceivedPaymentDao;

    /**
     * 
     * 批量保存法务回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    public int batchSaveInstReceivedPayment(List<InstReceivedPaymentBean> list) {
        setDb(0, super.MASTER);
        return instReceivedPaymentDao.batchSaveInstReceivedPayment(list);
    }

    /**
     * 
     * 根据订单号查询法务回款列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    public List<InstReceivedPaymentBean> selectInstReceivedPaymentList(String orderId, String caseId) {
        setDb(1, super.MASTER);
        return instReceivedPaymentDao.selectInstReceivedPaymentList(orderId, caseId);
    }

    /**
     * 
     * 根据caseId删除法务回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月30日
     */
    public int deleteReceivedPayInfoByCaseId(@Param("caseId") String caseId) {
        setDb(0, super.MASTER);
        return instReceivedPaymentDao.deleteReceivedPayInfoByCaseId(caseId);
    }
}
