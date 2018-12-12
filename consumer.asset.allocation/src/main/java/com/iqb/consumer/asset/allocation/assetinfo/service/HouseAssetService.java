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
 * 2017年8月16日下午2:12:04 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.asset.allocation.assetinfo.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.asset.allocation.assetinfo.bean.HouseAssetBean;
import com.iqb.etep.common.exception.IqbException;

/**
 * @author haojinlong
 * 
 */
public interface HouseAssetService {
    /**
     * 
     * Description:根据条件查询房贷资产分配信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    public PageInfo<HouseAssetBean> selectHouseAssetByParams(JSONObject objs);

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

    public Map<String, Object> houseAssetAllot(JSONObject objs) throws IqbException;
}
