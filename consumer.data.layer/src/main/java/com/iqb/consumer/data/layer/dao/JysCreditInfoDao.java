package com.iqb.consumer.data.layer.dao;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.jys.JYSCreditInfoBean;

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
 * 2017年11月3日下午6:08:18 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface JysCreditInfoDao {
    /**
     * 
     * Description:插入交易所债权人信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月3日
     */
    int insertJysCreditInfo(JYSCreditInfoBean job);

    /**
     * 
     * Description:获取交易所债权人信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月3日
     */
    JYSCreditInfoBean getJysCreditInfo(JSONObject objs);
}
