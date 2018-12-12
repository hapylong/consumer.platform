package com.iqb.consumer.service.layer.cheegu;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyBean;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyDetailBean;

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
 * 2018年5月28日下午3:40:29 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstCarPeccancyService {
    /**
     * 
     * Description:根据条件查询车辆违章信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    PageInfo<InstCarPeccancyBean> selectInstCarPeccancyList(JSONObject objs);

    /**
     * 
     * Description:根据订单号 车架号查询车辆违章明细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public PageInfo<InstCarPeccancyDetailBean> selectInstCarPeccancyDetailList(JSONObject objs);

    /**
     * 
     * Description:根据订单号 车架号修改违章信息表处理状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public int updateInstCarPeccancyStatusByOrderId(JSONObject objs);

    /**
     * 
     * Description:违章查询列表导出
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月30日
     */
    public String exportInstCarPeccancyList(JSONObject objs, HttpServletResponse response);

    /**
     * 
     * Description:获取
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月4日
     */
    public int doSendAndGetInstCarPeccancyDetail(JSONObject objs);

    /**
     * 
     * 车易估-中阁毁回调接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    public int callbackForCheegu(JSONObject objs);
}
