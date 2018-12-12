package com.iqb.consumer.data.layer.dao.jys;

import com.iqb.consumer.data.layer.bean.jys.RecordAssets;

public interface RecordAssetsDao {

    /**
     * 保存备案资产要素
     * 
     * @param recordAssets
     * @return
     */
    int saveRecordAssets(RecordAssets recordAssets);

    /**
     * 根据备案资产编号查询备案资产要素
     * 
     * @param assetNumber
     * @return
     */
    RecordAssets selRecordAssets(String assetNumber);
}
