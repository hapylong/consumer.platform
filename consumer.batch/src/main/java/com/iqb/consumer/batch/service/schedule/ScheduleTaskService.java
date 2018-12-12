package com.iqb.consumer.batch.service.schedule;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.batch.data.pojo.BuckleScheduleTaskAnalysisPojo;

public interface ScheduleTaskService {

    int fullScaleLendingScheduleTask();

    Map<String, Object> callBackScheduleTask();

    /**
     * 
     * Description: 结算中心：如果需要查看统计数据请升级BuckleScheduleTaskAnalysisPojo.java
     * 
     * @param
     * @return BuckleScheduleTaskAnalysisPojo
     * @throws @Author adam Create Date: 2017年6月26日 下午6:03:36
     */
    BuckleScheduleTaskAnalysisPojo buckleScheduleTask();

    void orderSpecialTimeScheduleTask();

    /**
     * 电子合同任务
     * 
     * @Description: TODO(这里用一句话描述这个方法的作用) 设定文件 void 返回类型
     * @throws
     * @author guojuan 2017年10月12日下午1:59:29
     */
    void contractScheduleTask();

    /**
     * 
     * Description:罚息减免每日作废任务
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月29日
     */
    void instPenaltyDerateScheduleTask();

    /**
     * 提前结清每日作废任务 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月28日
     */
    void prepaymentEndScheduleTask();

    /**
     * 
     * Description:获取距当前系统时间三天后的账单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月27日
     */
    long batchSelectLatelyThreeDaysBill();

    /**
     * 
     * Description:资产存量数据每日入库
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月1日
     */
    long everyDayAssetStock(JSONObject objs);

    /**
     * 
     * Description:将已结清并且已发送车300进行车辆监控的车辆取消监控
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月16日
     */
    long stopMonitorOrderLoanrisk(JSONObject objs);
}
