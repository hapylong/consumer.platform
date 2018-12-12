package com.iqb.consumer.batch.util;

/**
 * 
 * Description: 根据策略 计算 下次发送时刻与当前时刻的时间间隔
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
public class CallBackPolicyRepository {

    public static final int BUCKLE_POLICY = 2; // 结算中心

    /**
     * 
     * Description: 第一次异步通知失败后：接下来的四次， 每 15分钟通知1次 。之后 每半小时通知一次
     * 
     * @param
     * @return long 返回累加时间间隔 ，用以计算下次推送时间
     * @throws
     * @Author adam Create Date: 2017年6月6日 下午5:33:53
     */
    public static long modelA(int i) {
        if (i <= 0) {
            return -1;
        } else if (i > 0 && i < 5) { // 第一次发送失败访问该接口 ，此时 sendNum=1。 sendNum = 1， 2， 3， 4 时执行如下规则
            return 15 * CalendarUtil.ONE_MIN;
        } else {
            return 30 * CalendarUtil.ONE_MIN;
        }
    }

    /**
     * 
     * Description: 通过发送策略和 发送次数，计算发送间隔
     * 
     * @param
     * @return long
     * @throws
     * @Author adam Create Date: 2017年6月6日 下午5:47:55
     */
    public static long getIntervalBySendPolicyAndSendNum(int sendPolicy, int sendNum) {
        switch (sendPolicy) {
            case 1:
                return modelA(sendNum);
            case 2:
                return modelB(sendNum);
            default:
                return modelA(sendNum);
        }
    }

    /**
     * 
     * Description: FINANCE-1360 轮询调度处理发送结算中心时间【根据策略计算下次发送时刻与当前时刻的时间间隔 ：0，1，2，5，15】
     * 
     * @param
     * @return long
     * @throws @Author adam Create Date: 2017年6月26日 上午11:38:40
     */
    private static long modelB(int i) {
        if (i <= 0) {
            return -1;
        } else if (i == 1) {
            return CalendarUtil.ONE_MIN;
        } else if (i == 2) {
            return 2 * CalendarUtil.ONE_MIN;
        } else if (i == 3) {
            return 5 * CalendarUtil.ONE_MIN;
        } else if (i == 4) {
            return 15 * CalendarUtil.ONE_MIN;
        } else {
            throw new RuntimeException("not define in this model. i value [" + i + "], please update .");
        }
    }
}
