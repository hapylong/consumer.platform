package com.iqb.consumer.data.layer.dao.cheegu;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyBean;

/**
 * Description:车易估-车辆违章信息表Dao
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年5月28日上午10:20:49 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstCarPeccancyDao {
    /**
     * 
     * Description:根据条件查询车辆违章信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    List<InstCarPeccancyBean> selectInstCarPeccancyList(JSONObject objs);

    /**
     * 
     * Description:根据订单号 车架号修改违章信息表处理状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    int updateInstCarPeccancyStatusByOrderId(JSONObject objs);

    /**
     * 
     * Description:根据订单号车架号查询违章车辆信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    InstCarPeccancyBean selectInstCarPeccancyInfo(JSONObject objs);

    /**
     * 
     * Description:插入车辆违章信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    int insertInstCarPeccancy(InstCarPeccancyBean instCarPeccancyBean);

    /**
     * 
     * Description:根据订单号查询违章信息列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月4日
     */
    List<InstCarPeccancyBean> selectInstCarPeccancyListByOrderIds(List<InstCarPeccancyBean> list);
}
