package com.iqb.consumer.batch.data.pojo;

import java.util.List;
import java.util.Map;

import com.iqb.consumer.batch.data.entity.InstSettlementResultEntity;

public class BuckleSendPolicy {

    private int maxSendSize; // 最大发送次数
    private List<KeyValuePair<Map<String, String>, InstSettlementResultEntity>> dataSend;

    public int getMaxSendSize() {
        return maxSendSize;
    }

    public void setMaxSendSize(int maxSendSize) {
        this.maxSendSize = maxSendSize;
    }

    public static BuckleSendPolicy initPolicy() {
        BuckleSendPolicy bsp = new BuckleSendPolicy();
        bsp.setMaxSendSize(5);
        return bsp;
    }

    public void chatDataPrepare(List<KeyValuePair<Map<String, String>, InstSettlementResultEntity>> dataSend) {
        this.dataSend = dataSend;
    }

    public List<KeyValuePair<Map<String, String>, InstSettlementResultEntity>> getChatData() {
        return dataSend;
    }

}
