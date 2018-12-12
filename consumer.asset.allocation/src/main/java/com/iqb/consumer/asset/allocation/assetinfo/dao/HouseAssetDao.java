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
 * 2017年8月16日下午1:51:33 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.asset.allocation.assetinfo.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.asset.allocation.assetinfo.bean.HouseAssetBean;

/**
 * @author haojinlong
 * 
 */
public interface HouseAssetDao {
    /**
     * 
     * Description:根据条件查询房贷资产分配信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    public List<HouseAssetBean> selectHouseAssetByParams(JSONObject objs);

    /**
     * 
     * Description:根据条件查询房贷资产分配详情
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    public HouseAssetBean selectHouseAssetDetailByParams(JSONObject objs);
}
