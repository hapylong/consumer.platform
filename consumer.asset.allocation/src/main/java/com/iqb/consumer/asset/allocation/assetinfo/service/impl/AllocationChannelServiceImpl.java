package com.iqb.consumer.asset.allocation.assetinfo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AllocationChannelBean;
import com.iqb.consumer.asset.allocation.assetinfo.biz.AllocationChannelBiz;
import com.iqb.consumer.asset.allocation.assetinfo.service.AllocationChannelService;

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
 * 2017年9月12日下午4:21:24 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class AllocationChannelServiceImpl implements AllocationChannelService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(AllocationChannelServiceImpl.class);

    @Resource
    private AllocationChannelBiz allocationChannelBiz;

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年9月12日
     */
    @Override
    public int insertAllocationChannel(AllocationChannelBean bean) {
        return allocationChannelBiz.insertAllocationChannel(bean);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年9月12日
     */
    @Override
    public int updateAllocationChannelById(JSONObject objs) {
        return allocationChannelBiz.updateAllocationChannelById(objs);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年9月12日
     */
    @Override
    public List<AllocationChannelBean> getAlloactionChannelList(JSONObject objs) {
        logger.info("---获取资产分配渠道配置列表信息---");
        return allocationChannelBiz.getAlloactionChannelList(objs);
    }

}
