package com.iqb.consumer.data.layer.biz.cheegu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.cheegu.InstCarReportBean;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarReportImageBean;
import com.iqb.consumer.data.layer.dao.cheegu.InstCarReportDao;
import com.iqb.etep.common.base.biz.BaseBiz;

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
 * 2018年5月25日上午9:01:03 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class InstCarReportBiz extends BaseBiz {
    @Autowired
    private InstCarReportDao instCarReportDao;

    /**
     * 
     * Description:保存车辆评估报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月25日
     */
    public int insertInstCarReport(InstCarReportBean instCarReportBean) {
        setDb(0, super.MASTER);
        return instCarReportDao.insertInstCarReport(instCarReportBean);
    }

    /**
     * 
     * Description:根据车架号查询车牌评估报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月25日
     */
    public InstCarReportBean getInstCarReportByOrderId(String vin) {
        setDb(1, super.MASTER);
        return instCarReportDao.getInstCarReportByOrderId(vin);
    }

    /**
     * 
     * Description:批量保存车辆评估报告图片信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月25日
     */
    public int batchInsertCarReportImage(List<InstCarReportImageBean> list) {
        setDb(0, super.MASTER);
        return instCarReportDao.batchInsertCarReportImage(list);
    }
}
