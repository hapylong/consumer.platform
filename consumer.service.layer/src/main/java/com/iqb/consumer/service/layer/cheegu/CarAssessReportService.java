package com.iqb.consumer.service.layer.cheegu;

import java.util.Map;

import com.iqb.consumer.data.layer.bean.cheegu.InstCarReportBean;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年5月23日下午5:32:05 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface CarAssessReportService {
    /**
     * 
     * Description:根据车架号获取车辆评估报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月23日
     */
    public Map<String, Object> getCarAssessReport(String orderId, String vin);

    /**
     * 
     * Description:车易估车辆评估接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月23日
     */
    public InstCarReportBean cheeguForReport(String vin);
}
