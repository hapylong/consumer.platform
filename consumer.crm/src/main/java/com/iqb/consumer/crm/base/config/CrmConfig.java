package com.iqb.consumer.crm.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.iqb.etep.common.base.config.BaseConfig;

/**
 * 
 * Description: Crm配置文件
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月27日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Configuration
public class CrmConfig extends BaseConfig {

    @Value("${url_crm_customer_cfm_push}")
    private String urlCrmCustomerCfmPush;

    @Value("${http_interface_interaction_mode}")
    private String httpInterfaceInteractionMode;

    public String getUrlCrmCustomerCfmPush() {
        return urlCrmCustomerCfmPush;
    }

    public void setUrlCrmCustomerCfmPush(String urlCrmCustomerCfmPush) {
        this.urlCrmCustomerCfmPush = urlCrmCustomerCfmPush;
    }

    public String getHttpInterfaceInteractionMode() {
        return httpInterfaceInteractionMode;
    }

    public void setHttpInterfaceInteractionMode(String httpInterfaceInteractionMode) {
        this.httpInterfaceInteractionMode = httpInterfaceInteractionMode;
    }

}
