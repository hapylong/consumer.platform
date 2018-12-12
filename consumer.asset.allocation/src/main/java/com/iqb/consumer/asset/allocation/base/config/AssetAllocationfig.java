package com.iqb.consumer.asset.allocation.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.iqb.etep.common.base.config.BaseConfig;

/**
 * 
 * Description: 资产配置配置
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月29日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Configuration
public class AssetAllocationfig extends BaseConfig {

    @Value("${url_of_ffjf_push}")
    private String urlOfFFJFPush;

    @Value("${redis_queue_asset_allocation_key}")
    private String redisQueueAssetAllocationKey;

    @Value("${url_of_iqb_push}")
    private String urlOfIQBReg;

    public String getUrlOfFFJFPush() {
        return urlOfFFJFPush;
    }

    public void setUrlOfFFJFPush(String urlOfFFJFPush) {
        this.urlOfFFJFPush = urlOfFFJFPush;
    }

    public String getRedisQueueAssetAllocationKey() {
        return redisQueueAssetAllocationKey;
    }

    public void setRedisQueueAssetAllocationKey(String redisQueueAssetAllocationKey) {
        this.redisQueueAssetAllocationKey = redisQueueAssetAllocationKey;
    }

    public String getUrlOfIQBReg() {
        return urlOfIQBReg;
    }

    public void setUrlOfIQBReg(String urlOfIQBReg) {
        this.urlOfIQBReg = urlOfIQBReg;
    }

}
