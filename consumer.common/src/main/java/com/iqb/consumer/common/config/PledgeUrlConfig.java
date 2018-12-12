package com.iqb.consumer.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PledgeUrlConfig {
    @Value("${iqb.schedule.task.full.scale.lending.ids.url}")
    private String fullScaleLendingIdsUrl;

    @Value("${iqb.schedule.task.full.scale.lending.pojo.url}")
    private String fullScaleLendingPojoUrl;

    public String getFullScaleLendingIdsUrl() {
        return fullScaleLendingIdsUrl;
    }

    public void setFullScaleLendingIdsUrl(String fullScaleLendingIdsUrl) {
        this.fullScaleLendingIdsUrl = fullScaleLendingIdsUrl;
    }

    public String getFullScaleLendingPojoUrl() {
        return fullScaleLendingPojoUrl;
    }

    public void setFullScaleLendingPojoUrl(String fullScaleLendingPojoUrl) {
        this.fullScaleLendingPojoUrl = fullScaleLendingPojoUrl;
    }
}
