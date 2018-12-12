package com.iqb.consumer.data.layer.biz.schedule;

import java.util.Date;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.data.layer.bean.schedule.entity.InstScheduleTaskEntity;

@Component
public class ScheduleTaskAnalysisAllot {
    private static Logger logger = LoggerFactory.getLogger(ScheduleTaskAnalysisAllot.class);
    @Autowired
    private ScheduleTaskManager scheduleTaskManager;

    public static final int MODULE_A = 1;

    static class CallBackResponsePojo {
        public static final String CODE_SUCCESS = "00";
        private String result_code;
        private String result_msg;

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        public String getResult_msg() {
            return result_msg;
        }

        public void setResult_msg(String result_msg) {
            this.result_msg = result_msg;
        }
    }

    /**
     * @Demo try { String url = orderBean.getOrderRemark(); boolean save =
     *       scheduleTaskManager.isMerchantNeedSave(orderBean.getMerchantNo()); ApiRequestMessage
     *       data = new ApiRequestMessage(); data.setOrderId(orderBean.getOrderId());
     *       data.setStatus(1); scheduleTaskAnalysisAllot.send(data.toString(), url,
     *       ScheduleTaskAnalysisAllot.MODULE_A, orderBean.getOrderId(), save); } catch (Throwable
     *       e) { logger.error("WFNoticeZYReturnController.guaranty_price exception :", e); }
     *       Description:
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年6月7日 下午5:11:12
     */
    @SuppressWarnings("rawtypes")
    public void send(String data, String url, int sendPolicy, String orderId, boolean save) {
        logger.info(String.format("ScheduleTaskAnalysisAllot.send: data[%s] url[%s] save[%s]",
                JSONObject.toJSONString(data), url, save));
        if (!save) {
            return;
        }
        LinkedHashMap response = null;
        try {
            response = SendHttpsUtil.postMsg4GetMap(url, data);
            logger.info(String.format("ScheduleTaskAnalysisAllot.response: [%s] save[%s]",
                    JSONObject.toJSONString(response), save));
        } catch (Exception e) {
            logger.info(String.format("ScheduleTaskAnalysisAllot.response error : [%s] save[%s]",
                    JSONObject.toJSONString(response), save));
        }
        if (save) {
            InstScheduleTaskEntity iste = new InstScheduleTaskEntity();
            if (response != null && callBackAnlyze(JSONObject.toJSONString(response))) {
                iste.setCreateTime(new Date());
                iste.setJsonData(data);
                iste.setNextSendTime(new Date());
                iste.setOrderId(orderId);
                iste.setSendNum(1);
                iste.setSendPolicy(sendPolicy);
                iste.setState(InstScheduleTaskEntity.STATE_SUCC);
                iste.setUrl(url);
            } else {
                Date nextSendTime =
                        DateUtil.getEndCalendarByStartAndInterval(
                                new Date(),
                                getIntervalBySendPolicyAndSendNum(sendPolicy,
                                        1));
                iste.setCreateTime(new Date());
                iste.setJsonData(data);
                iste.setNextSendTime(nextSendTime);
                iste.setOrderId(orderId);
                iste.setSendNum(1);
                iste.setSendPolicy(sendPolicy);
                iste.setState(InstScheduleTaskEntity.STATE_FAIL);
                iste.setUrl(url);
            }
            scheduleTaskManager.createISTE(iste);
        }
    }

    private static boolean callBackAnlyze(String response) {
        CallBackResponsePojo cbrp = null;
        try {
            cbrp = JSONObject.parseObject(response, CallBackResponsePojo.class);
            if (cbrp != null && cbrp.getResult_code().equals(CallBackResponsePojo.CODE_SUCCESS)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("callBackAnlyze.CallBackResponsePojo error : [%s]", response, e);
            return false;
        }
    }

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
            return 15 * DateUtil.ONE_MIN;
        } else {
            return 30 * DateUtil.ONE_MIN;
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
            default:
                return modelA(sendNum);
        }
    }
}
