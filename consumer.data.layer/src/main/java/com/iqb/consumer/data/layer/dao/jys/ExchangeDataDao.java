package com.iqb.consumer.data.layer.dao.jys;

import com.iqb.consumer.data.layer.bean.jys.ExchangeData;

public interface ExchangeDataDao {

    /**
     * 保存咨询范数据
     * 
     * @param exchangeData
     * @return
     */
    int saveExchangeData(ExchangeData exchangeData);

    /**
     * 通过资产挂牌编号查询
     * 
     * @param listNumber
     * @return
     */
    ExchangeData selExchangeData(String listNumber);
}
