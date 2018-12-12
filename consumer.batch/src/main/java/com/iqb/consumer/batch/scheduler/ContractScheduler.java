package com.iqb.consumer.batch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iqb.consumer.batch.service.schedule.ScheduleTaskService;

/**
 * 合同调度
 * 
 * @Description:TODO
 * @author guojuan
 * @date 2017年10月12日 上午11:45:07
 * @version
 */
@Component
public class ContractScheduler {

    protected static final Logger logger = LoggerFactory.getLogger(CallBackSchedulerTask.class);

    @Autowired
    private ScheduleTaskService scheduleTaskServiceImpl;

    /**
     * 
     * @throws
     * @author guojuan 2017年10月12日上午11:45:47
     */
    @Scheduled(cron = "0 0 0/3 * * ?")
    public void buckleScheduleTask() {
        try {
            scheduleTaskServiceImpl.contractScheduleTask();
        } catch (Throwable e) {
            logger.error("CallBackSchedulerTask error :", e);
        }
    }
}
