package com.iqb.consumer.data.layer.dao.cheegu;

import java.util.List;

import com.iqb.consumer.data.layer.bean.cheegu.InstCarReportBean;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarReportImageBean;

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
 * 2018年5月25日上午9:01:15 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstCarReportDao {
    /**
     * 
     * Description:保存车辆评估报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月25日
     */
    public int insertInstCarReport(InstCarReportBean instCarReportBean);

    /**
     * 
     * Description:根据车架号查询车牌评估报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月25日
     */
    public InstCarReportBean getInstCarReportByOrderId(String vin);

    /**
     * 
     * Description:批量保存车辆评估报告图片信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月25日
     */
    public int batchInsertCarReportImage(List<InstCarReportImageBean> list);
}
