package com.iqb.consumer.data.layer.dao.cheegu;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyBean;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyDetailBean;

/**
 * Description:车易估-车辆违章明细信息表Dao
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年5月28日上午10:21:28 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface InstCarPeccancyDetailDao {
    /**
     * 
     * Description:根据订单号 车架号查询车辆违章明细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public List<InstCarPeccancyDetailBean> selectInstCarPeccancyDetailList(JSONObject objs);

    /**
     * 
     * Description:根据订单号 车架号修改违章明细处理状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public int updateInstCarPeccancyDetailStatusByOrderId(JSONObject objs);

    /**
     * 
     * Description:批量插入车辆违章明细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public int insertInstCarPeccancyDetail(InstCarPeccancyDetailBean bean);

    /**
     * 
     * Description:根据订单号车架号查询车辆违章明细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月29日
     */
    public InstCarPeccancyDetailBean selectInstCarPeccancyDetailInfo(JSONObject objs);

    /**
     * 
     * Description:根据订单号 车架号 查询上月未处理的违章信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月29日
     */
    public List<InstCarPeccancyDetailBean> selectInstCarDetailList(JSONObject objs);

    /**
     * 
     * Description:根据订单号 车价号查询违章总扣分 总罚款金额
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月29日
     */
    public InstCarPeccancyBean selectInstCarDetailSum(JSONObject objs);
}
