/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月24日 上午11:42:53
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.conf;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.dao.conf.WFConfigDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.redis.RedisPlatformDao;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class WFConfigBiz extends BaseBiz {

    @Resource
    private WFConfigDao wfConfigDao;
    @Resource
    private RedisPlatformDao redisPlatformDao;

    public WFConfig getConfigByBizType(String bizType, Integer wfStatus) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bizType", bizType);
        params.put("wfStatus", wfStatus);
        return wfConfigDao.getConfigByBizType(params);
    }
}
