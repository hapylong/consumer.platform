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
 * 2017年6月23日下午6:44:11 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.batch.service.schedule;

import java.util.List;

import com.iqb.consumer.batch.data.pojo.InstallmentBillPojo;

/**
 * @author haojinlong
 * 
 */
public interface SettlementTaskService {
    /**
     * 
     * Description:保存inst_settlementResult
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    long insertSettlementResult();

    /**
     * 
     * Description:保存inst_settlementresult_withhold-手动代扣
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    long insertSettlementResultFor();

    /**
     * 
     * Description:批量修改订单剩余期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月9日
     */
    long batchUpdateOrderLeftItems();

    /**
     * 
     * Description:每日获取已逾期且逾期天数小于等于5天的账单
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月24日 16:16:20
     */
    long queryBillLastDateThree();

    long insertSettlementResultFor2(List<InstallmentBillPojo> list);

    long queryBillLastBigDateFive();

    /**
     * 
     * Description:划扣失败-短信通知
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月15日
     */
    long failureAfterSms();
}
