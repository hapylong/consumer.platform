package com.iqb.consumer.batch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iqb.consumer.batch.service.schedule.ScheduleTaskService;

/**
 * 
 * Description:
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年7月5日    adam       1.0        1.0 Version 
 * </pre>
 */
@Component
public class OrderSpecialTimeScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(OrderSpecialTimeScheduleTask.class);

    @Autowired
    private ScheduleTaskService scheduleTaskServiceImpl;

    /**
     * 
     * Description:FINANCE-1442 抵质押节点30天流程终止需求 FINANCE-1443 抵质押节点30天流程终止需求 【以租代售抵质押物估价节点|抵押车车价复评节点】
     * 
     * @param
     * @return void
     * @throws @Author adam Create Date: 2017年6月6日 下午5:08:45
     */

    // @Scheduled(cron = "0 0 9,18 * * ?")
    @Scheduled(cron = "0 30 0 * * ?")
    public void orderSpecialTimeScheduleTask() {
        try {
            scheduleTaskServiceImpl.orderSpecialTimeScheduleTask();
        } catch (Throwable e) {
            log.error("OrderSpecialTimeScheduleTask error :", e);
        }
    }

    /**
     * 
     * Description:罚息减免每日作废任务
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月29日
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void instPenaltyDerateScheduleTask() {
        log.debug("---罚息减免每日作废任务---开始--");
        try {
            scheduleTaskServiceImpl.instPenaltyDerateScheduleTask();
        } catch (Exception e) {
            log.error("OrderSpecialTimeScheduleTask error :", e);
        }
        log.debug("---罚息减免每日作废任务---完成--");
    }

    /**
     * 
     * Description:提前结清每日作废任务 0 0/5 * * * ? 0 30 3 * * ?
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月28日
     */
    @Scheduled(cron = "0 30 3 * * ?")
    public void prepaymentEndScheduleTask() {
        log.debug("---提前结清每日作废任务---开始--");
        try {
            scheduleTaskServiceImpl.prepaymentEndScheduleTask();
        } catch (Exception e) {
            log.error("---提前结清每日作废任务---报错--", e);
        }
        log.debug("---提前结清每日作废任务---完成--");
    }

    /**
     * 
     * Description:已结清订单车辆取消监控任务 0 30 * * * ?
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月16日
     */
    @Scheduled(cron = "0 30 * * * ?")
    public void stopMonitorOrderLoanriskTask() {
        log.info("---已结清订单车辆取消监控任务---开始--");
        try {
            scheduleTaskServiceImpl.stopMonitorOrderLoanrisk(null);
        } catch (Exception e) {
            log.error("---已结清订单车辆取消监控任务---报错--", e);
        }
        log.info("---已结清订单车辆取消监控任务---完成--");
    }
}
