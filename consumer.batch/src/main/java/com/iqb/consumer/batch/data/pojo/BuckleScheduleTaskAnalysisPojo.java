package com.iqb.consumer.batch.data.pojo;

/**
 * 
 * Description: 对调度情况进行记录统计分析
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
public class BuckleScheduleTaskAnalysisPojo {

    private KeyValuePair<String, String> step1; // 第一步

    public KeyValuePair<String, String> getStep1() {
        return step1;
    }

    public void setStep1(KeyValuePair<String, String> step1) {
        this.step1 = step1;
    }
}
