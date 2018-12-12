package com.iqb.consumer.service.layer.exchange;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iqb.consumer.data.layer.bean.jys.ExchangeData;
import com.iqb.consumer.data.layer.bean.jys.RecordAssets;
import com.iqb.consumer.data.layer.biz.order.ExchangeDataBiz;
import com.iqb.consumer.data.layer.biz.order.RecordAssetsBiz;

@Service
public class ExchangeDataServiceImpl implements ExchangeDataService {

    @Resource
    private ExchangeDataBiz exchangeDataBiz;
    @Resource
    private RecordAssetsBiz recordAssetsBiz;

    @Override
    public Map<String, Object> saveExchangeData(ExchangeData exchangeData) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 保存需要判断是否存在重复的记录，存在则不保存，一个备案号对应多个挂牌资产编号
        ExchangeData selExchangeData = exchangeDataBiz.selExchangeData(exchangeData.getListNumber());// 挂牌资产编号确认唯一
        if (selExchangeData == null) { // 不存在重复记录则保存
            try {
                exchangeDataBiz.saveExchangeData(exchangeData);
            } catch (Exception e) {
                result.put("code", 0);
                result.put("msg", "保存数据发生错误");
                return result;
            }
            result.put("code", 1);
            result.put("msg", "接收成功");
        } else {
            result.put("code", 0);
            result.put("msg", "存在重复的挂牌资产编号");
        }
        return result;
    }

    @Override
    public ExchangeData selExchangeData(String listNumber) {
        return exchangeDataBiz.selExchangeData(listNumber);
    }

    @Override
    public Map<String, Object> saveRecordAssets(RecordAssets recordAssets) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 保存需要判断是否存在重复的记录，存在则不保存，一个备案号对应多个挂牌资产编号
        RecordAssets selRecordAssets = recordAssetsBiz.selRecordAssets(recordAssets.getAssetNumber());
        if (selRecordAssets == null) { // 不存在重复记录则保存
            try {
                recordAssetsBiz.saveRecordAssets(recordAssets);
            } catch (Exception e) {
                result.put("code", 0);
                result.put("msg", "保存数据发生错误");
                return result;
            }
            result.put("code", 1);
            result.put("msg", "接收成功");
        } else {
            result.put("code", 0);
            result.put("msg", "存在重复的备案资产要素");
        }
        return result;
    }

    @Override
    public RecordAssets selRecordAssets(String assetNumber) {
        return recordAssetsBiz.selRecordAssets(assetNumber);
    }

}
