package com.iqb.consumer.asset.allocation.assetinfo.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AllocationChannelBean;

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
 * 2017年9月12日下午4:03:12 haojinlong   1.0        1.0 Version 
 * </pre>
 */
public interface AllocationChannelDao {
    /**
     * 
     * Description:增加资产推送渠道配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月12日
     */
    public int insertAllocationChannel(AllocationChannelBean bean);

    /**
     * 
     * Description:更新资产推送渠道配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月12日
     */
    public int updateAllocationChannelById(JSONObject objs);

    /**
     * 
     * Description:根据id删除资产推送渠道配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月12日
     */
    public int deleteAllocationChannelById(int id);

    /**
     * 
     * Description:根据条件获取资产推送渠道信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月12日
     */
    public List<AllocationChannelBean> getAlloactionChannelList(JSONObject objs);
}
