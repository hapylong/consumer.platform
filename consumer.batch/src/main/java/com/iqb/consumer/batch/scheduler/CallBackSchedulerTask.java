/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 账户业务调度
 * @date 2016年7月22日 下午2:35:06
 * @version V1.0
 */

package com.iqb.consumer.batch.scheduler;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.batch.service.schedule.ScheduleTaskService;
import com.iqb.etep.common.utils.DateUtil;

/**
 * 
 * Description: AIP call back center
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月6日    adam       1.0        1.0 Version 
 * </pre>
 */
@Component
public class CallBackSchedulerTask {

    protected static final Logger logger = LoggerFactory.getLogger(CallBackSchedulerTask.class);

    @Autowired
    private ScheduleTaskService scheduleTaskServiceImpl;

    /**
     * 
     * Description:
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年6月6日 下午5:08:45
     */

    // @Scheduled(cron = "0/15 * * * * ?")
    @Scheduled(cron = "0 0/15 * * * ?")
    public void callBackScheduleTask() {
        try {
            Map<String, Object> result = scheduleTaskServiceImpl.callBackScheduleTask();
            logger.info(" 成功: " + result.get("succ"));
            logger.info(" 失败: " + result.get("fail"));
            logger.info(" 消息: " + result.get("msg"));
        } catch (Throwable e) {
            logger.error("CallBackSchedulerTask error :", e);
        }
    }

    /**
     * 
     * Description:电话提醒-获取所有待还款账单 0 0 5 * * ?
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月27日
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void batchSelectLatelyThreeDaysBill() {
        logger.info("获取所有待还款账单调度开始时间:{}", new Date().getTime());
        long result = scheduleTaskServiceImpl.batchSelectLatelyThreeDaysBill();
        logger.info("获取所有待还款账单记录数:{}", result);
        logger.info("获取所有待还款账单结束时间:{}", new Date().getTime());
    }

    /**
     * 
     * Description:资产存量数据每日入库 0 0 6 * * ?
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月1日
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void everyDayAssetStock() {
        logger.info("资产存量数据每日入库开始时间:{}", DateUtil.toString(new Date(), 53));
        JSONObject objs = new JSONObject();
        long result = scheduleTaskServiceImpl.everyDayAssetStock(objs);
        logger.info("资产存量数据每日入库记录数:{}", result);
        logger.info("资产存量数据每日入库结束时间:{}", DateUtil.toString(new Date(), 53));
    }
}
