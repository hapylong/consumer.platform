package com.iqb.consumer.service.layer.exchange;

import java.util.Map;

import com.iqb.consumer.data.layer.bean.jys.ExchangeData;
import com.iqb.consumer.data.layer.bean.jys.RecordAssets;

public interface ExchangeDataService {

    /**
     * 保存咨询范信息
     * 
     * @param exchangeData
     * @return
     */
    public Map<String, Object> saveExchangeData(ExchangeData exchangeData);

    /**
     * 通过挂牌资产编号查询
     * 
     * @param listNumber
     * @return
     */
    public ExchangeData selExchangeData(String listNumber);

    /**
     * 保存备案资产要素
     * 
     * @param recordAssets
     * @return
     */
    public Map<String, Object> saveRecordAssets(RecordAssets recordAssets);

    /**
     * 根据备案号查询资产要素
     * 
     * @param assetNumber
     * @return
     */
    public RecordAssets selRecordAssets(String assetNumber);
}
