/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 账户业务调度
 * @date 2016年7月22日 下午2:35:06
 * @version V1.0
 */

package com.iqb.consumer.batch.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iqb.consumer.batch.biz.RiskInfoBiz;
import com.iqb.consumer.batch.config.ParamConfig;
import com.iqb.consumer.batch.service.schedule.ScheduleTaskService;
import com.iqb.consumer.batch.service.schedule.SettlementTaskService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class BusinessScheduler {

    protected static final Logger logger = LoggerFactory.getLogger(BusinessScheduler.class);
    @Resource
    private RiskInfoBiz riskInfoBiz;
    @Autowired
    private ParamConfig paramConfig;

    @Autowired
    private ScheduleTaskService scheduleTaskServiceImpl;
    @Resource
    private SettlementTaskService settlementTaskServiceImpl;

    /**
     * 
     * Description: 风控回调
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2017年1月17日 下午2:05:35
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void dealRiskResultSchedule() {
        logger.info("风控回调结果处理调度开始时间:{}", new Date().getTime());
        Object objs = null;
        try {
            objs = this.riskInfoBiz.dealRiskResultSchedule(10, paramConfig.getIdumai_riskcontrol_notice_url());
        } catch (Exception e) {
            logger.info("风控回调异常", e);
            logger.error("风控回调异常", e);
        }
        logger.info("成功处理风控回调数量:{}", objs);
        logger.info("风控回调结果处理调度结束时间:{}", new Date().getTime());
    }

    /**
     * Description: 爱质贷：4.3.2满标放款接口
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年4月6日 上午9:58:12
     */
    private final String SERVICE_MARK = "BusinessScheduler[fullScaleLendingScheduleTask] ";

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void fullScaleLendingScheduleTask() {
        Date start = new Date();
        logger.info(SERVICE_MARK + " start time:{}", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(start));
        int result = scheduleTaskServiceImpl.fullScaleLendingScheduleTask();
        switch (result) {
            case -1:
                logger.error(SERVICE_MARK + "UNKNOWN_ERROR");
                break;
            case -2:
                logger.error(SERVICE_MARK + "ERROR_RESPONSE_1");
                break; // ERROR_RESPONSE_1
            case -3:
                logger.error(SERVICE_MARK + "ERROR_RESPONSE_2");
                break;// ERROR_RESPONSE_2
            case -5:
                logger.error(SERVICE_MARK + "ERROR_SYMPHONY");
                break;// ERROR_SYMPHONY
            case -6:
                logger.error(SERVICE_MARK + "ERROR_RESPONSE_1_EMPTY_LIST");
                break;//
            default:
                logger.info(SERVICE_MARK + "SUCCESS deal [{}]", result);
                break;
        }
        logger.info(SERVICE_MARK + " cost:{} seconds", ((new Date().getTime() - start.getTime()) / 1000));
    }

    /**
     * 自动划扣数据入库 0 0 10,17 * * ? 每天上午9点，下午18点
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    @Scheduled(cron = "0 0 10,17 * * ?")
    public void batchInsertSettlementResult() {
        logger.info("获取每日到期账单调度开始时间:{}", new Date().getTime());
        long result = settlementTaskServiceImpl.insertSettlementResult();
        logger.info("获取每日到期账单中记录数:{}", result);
        logger.info("获取每日到期账单调度结束时间:{}", new Date().getTime());
    }

    /**
     * 
     * 手动划扣数据入库
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void batchInsertSettlementResultFor() {
        logger.info("获取每日到期代扣账单调度开始时间:{}", new Date().getTime());
        long result = settlementTaskServiceImpl.insertSettlementResultFor();
        logger.info("获取每日到期代扣账单中记录数:{}", result);
        logger.info("获取每日到期代扣账单调度结束时间:{}", new Date().getTime());
    }

    /**
     * 
     * Description:批量修改订单表中所有订单剩余期数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月9日
     */
    public void batchUpdateOrderLeftItems() {
        logger.info("批量修改订单表中所有订单剩余期数开始时间:{}", new Date().getTime());
        long result = settlementTaskServiceImpl.batchUpdateOrderLeftItems();
        logger.info("批量修改订单表中所有订单剩余期数记录数:{}", result);
        logger.info("批量修改订单表中所有订单剩余期数结束时间:{}", new Date().getTime());
    }

    /**
     * 
     * Description:每日获取已逾期且逾期天数小于等于5天的账单
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月24日 16:16:20
     */
    @Scheduled(cron = " 0 0 7 * * ?")
    public void queryBillLastDateFive() {
        logger.info("每日获取已逾期且逾期天数小于等于5天的账单开始时间:{}", new Date().getTime());
        long result = settlementTaskServiceImpl.queryBillLastDateThree();
        logger.info("每日获取已逾期且逾期天数小于等于5天的账单记录数:{}", result);
        logger.info("每日获取已逾期且逾期天数小于等于5天的账单结束时间:{}", new Date().getTime());
    }

    /**
     * 
     * Description:每日获取已逾期且逾期天数大于等于5天的账单
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月24日 16:16:20
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void queryBillLastBigDateFive() {
        logger.info("每日获取已逾期且逾期天数大于等于5天的账单开始时间:{}", new Date().getTime());
        long result = settlementTaskServiceImpl.queryBillLastBigDateFive();
        logger.info("每日获取已逾期且逾期天数大于等于5天的账单记录数:{}", result);
        logger.info("每日获取已逾期且逾期天数大于等于5天的账单结束时间:{}", new Date().getTime());
    }

    /**
     * 
     * Description:划扣失败-短信通知
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月15日
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void failureAfterSms() {
        logger.info("划扣失败-短信通知开始时间:{}", new Date().getTime());
        long result = settlementTaskServiceImpl.failureAfterSms();
        logger.info("划扣失败-短信通知记录数:{}", result);
        logger.info("划扣失败-短信通知结束时间:{}", new Date().getTime());
    }
}
