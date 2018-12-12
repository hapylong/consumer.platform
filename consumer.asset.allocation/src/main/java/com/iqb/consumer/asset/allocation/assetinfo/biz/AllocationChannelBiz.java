package com.iqb.consumer.asset.allocation.assetinfo.biz;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.iqb.consumer.asset.allocation.assetinfo.bean.AllocationChannelBean;
import com.iqb.consumer.asset.allocation.assetinfo.dao.AllocationChannelDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.alibaba.fastjson.JSONObject;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date                     Author      Version     Description 
------------------------------------------------------------------
 * 2017年9月12日下午4:02:19  haojinlong      1.0        1.0 Version 
 * </pre>
 */
@Component
public class AllocationChannelBiz extends BaseBiz {
    @Resource
    private AllocationChannelDao allocationChannelDao;

    /**
     * 
     * Description:增加资产推送渠道配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月12日
     */
    public int insertAllocationChannel(AllocationChannelBean bean) {
        setDb(1, super.MASTER);
        return allocationChannelDao.insertAllocationChannel(bean);
    }

    /**
     * 
     * Description:更新资产推送渠道配置
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月12日
     */
    public int updateAllocationChannelById(JSONObject objs) {
        setDb(0, super.MASTER);
        return allocationChannelDao.updateAllocationChannelById(objs);
    }

    /**
     * 
     * Description:根据条件获取资产推送渠道信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月12日
     */
    public List<AllocationChannelBean> getAlloactionChannelList(JSONObject objs) {
        setDb(1, super.MASTER);
        return allocationChannelDao.getAlloactionChannelList(objs);
    }
}
