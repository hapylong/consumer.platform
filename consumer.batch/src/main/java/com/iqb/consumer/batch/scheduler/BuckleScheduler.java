package com.iqb.consumer.batch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iqb.consumer.batch.service.schedule.ScheduleTaskService;

/**
 * 
 * Description: 结算中心
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月26日    adam       1.0        1.0 Version 
 * </pre>
 */
@Component
public class BuckleScheduler {

    protected static final Logger logger = LoggerFactory.getLogger(CallBackSchedulerTask.class);

    @Autowired
    private ScheduleTaskService scheduleTaskServiceImpl;

    /**
     * 
     * Description: 定时划扣功能 从inst_settlementresult(划扣表)获取status为未发送的数据进行划扣
     * 0/30 * * * * ?
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年6月6日 下午5:08:45
     */

    @Scheduled(cron = "0/30 * * * * ?")
    public void buckleScheduleTask() {
        try {
            scheduleTaskServiceImpl.buckleScheduleTask();
        } catch (Throwable e) {
            logger.error("CallBackSchedulerTask error :", e);
        }
    }
}
