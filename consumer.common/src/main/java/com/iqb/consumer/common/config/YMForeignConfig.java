package com.iqb.consumer.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.iqb.etep.common.base.config.BaseConfig;

/**
 * 医美类对外接口相关参数
 */
@Configuration
public class YMForeignConfig extends BaseConfig {

    @Value("${YM.APPID}")
    private String ymAppid;
    @Value("${YM.RISKURL}")
    private String ymRiskUrl;
    @Value("${YM.FQURL}")
    private String ymFqUrl;
    @Value("${YM.MERCH.LIST}")
    private String ymMerchList;// 商户列表(用','分割)
    @Value("${common.platform.redis.ip}")
    private String redisHost;
    @Value("${common.platform.redis.port}")
    private String redisPort;

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public String getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(String redisPort) {
        this.redisPort = redisPort;
    }

    public String getYmAppid() {
        return ymAppid;
    }

    public void setYmAppid(String ymAppid) {
        this.ymAppid = ymAppid;
    }

    public String getYmRiskUrl() {
        return ymRiskUrl;
    }

    public void setYmRiskUrl(String ymRiskUrl) {
        this.ymRiskUrl = ymRiskUrl;
    }

    public String getYmFqUrl() {
        return ymFqUrl;
    }

    public void setYmFqUrl(String ymFqUrl) {
        this.ymFqUrl = ymFqUrl;
    }

    public String getYmMerchList() {
        return ymMerchList;
    }

    public void setYmMerchList(String ymMerchList) {
        this.ymMerchList = ymMerchList;
    }

    // 配置的参数转换为list
    public List<String> getMerchList(String merchList) {
        String[] merchs = merchList.split(",");
        return Arrays.asList(merchs);
    }
}
