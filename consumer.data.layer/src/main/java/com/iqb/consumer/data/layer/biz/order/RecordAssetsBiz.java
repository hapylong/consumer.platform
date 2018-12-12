package com.iqb.consumer.data.layer.biz.order;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.jys.RecordAssets;
import com.iqb.consumer.data.layer.dao.jys.RecordAssetsDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class RecordAssetsBiz extends BaseBiz {

    @Resource
    private RecordAssetsDao recordAssetsDao;

    /**
     * 保存备案资产要素
     * 
     * @param recordAssets
     * @return
     */
    public long saveRecordAssets(RecordAssets recordAssets) {
        setDb(0, super.MASTER);
        recordAssetsDao.saveRecordAssets(recordAssets);
        return recordAssets.getId();
    }

    /**
     * 根据备案资产编号查询备案资产要素
     * 
     * @param assetNumber
     * @return
     */
    public RecordAssets selRecordAssets(String assetNumber) {
        setDb(0, super.SLAVE);
        return recordAssetsDao.selRecordAssets(assetNumber);
    }
}
