/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年8月18日下午2:09:36 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.asset.allocation.assetinfo.dao;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.asset.allocation.assetinfo.bean.HouseOrderBean;

/**
 * @author haojinlong
 * 
 */
public interface HouseOrderDao {
    /**
     * 
     * Description:根据订单号查询房贷订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月18日
     */
    public HouseOrderBean selectHouseOrderByParams(JSONObject objs);

    /**
     * 更新房贷订单资产分配信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月22日
     */
    public int updateHouseOrderByOrderNo(JSONObject objs);
}
