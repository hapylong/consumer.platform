/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月20日 下午8:51:49
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.conf;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.conf.SysSmsConfig;
import com.iqb.consumer.data.layer.dao.SysSmsConfigDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.redis.RedisPlatformDao;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class SysSmsConfigBiz extends BaseBiz {

    @Resource
    private SysSmsConfigDao sysSmsConfigDao;
    @Resource
    private RedisPlatformDao redisPlatformDao;

    public SysSmsConfig getSmsChannelByWechatNo(String wechatNo) {
        String key = "SmsChannel" + wechatNo;
        String value = redisPlatformDao.getValueByKey(key);
        SysSmsConfig sysSmsConfig = JSONObject.parseObject(value, SysSmsConfig.class);
        if (sysSmsConfig == null) {
            super.setDb(0, super.SLAVE);
            sysSmsConfig = sysSmsConfigDao.getSmsChannelByWechatNo(wechatNo);
            redisPlatformDao.setKeyAndValue(key, JSON.toJSONString(sysSmsConfig));
        }
        return sysSmsConfig;
    }
}
