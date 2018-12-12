package com.iqb.consumer.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PledgeUrlConfig {
    @Value("${iqb.schedule.task.full.scale.lending.ids.url}")
    private String fullScaleLendingIdsUrl;

    @Value("${iqb.schedule.task.full.scale.lending.pojo.url}")
    private String fullScaleLendingPojoUrl;
    /** 资产存量接口地址 **/
    @Value("${iqb.front.every.assetstock.url}")
    private String everyAssetstockUrl;
    /** 停止订单车300监控接口地址 **/
    @Value("${iqb.front.orderchethree.stopmonitor.url}")
    private String orderChethreeStopMonitorUrl;

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

    public String getEveryAssetstockUrl() {
        return everyAssetstockUrl;
    }

    public void setEveryAssetstockUrl(String everyAssetstockUrl) {
        this.everyAssetstockUrl = everyAssetstockUrl;
    }

    public String getOrderChethreeStopMonitorUrl() {
        return orderChethreeStopMonitorUrl;
    }

    public void setOrderChethreeStopMonitorUrl(String orderChethreeStopMonitorUrl) {
        this.orderChethreeStopMonitorUrl = orderChethreeStopMonitorUrl;
    }

}
