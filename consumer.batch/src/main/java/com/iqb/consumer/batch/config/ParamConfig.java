/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月12日 上午10:54:07
 * @version V1.0
 */
package com.iqb.consumer.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * Description: 参数配置
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年4月14日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Configuration
public class ParamConfig {

    /** 风控读脉接口 **/
    @Value("${idumai_riskcontrol_notice_url}")
    private String idumai_riskcontrol_notice_url;

    @Value("${end_process_url}")
    private String endProcessUrl;

    @Value("${get.repay.params.url}")
    private String repayParamsUrl;

    public String getIdumai_riskcontrol_notice_url() {
        return idumai_riskcontrol_notice_url;
    }

    public void setIdumai_riskcontrol_notice_url(String idumai_riskcontrol_notice_url) {
        this.idumai_riskcontrol_notice_url = idumai_riskcontrol_notice_url;
    }

    public String getEndProcessUrl() {
        return endProcessUrl;
    }

    public void setEndProcessUrl(String endProcessUrl) {
        this.endProcessUrl = endProcessUrl;
    }

    public String getRepayParamsUrl() {
        return repayParamsUrl;
    }

    public void setRepayParamsUrl(String repayParamsUrl) {
        this.repayParamsUrl = repayParamsUrl;
    }
}
