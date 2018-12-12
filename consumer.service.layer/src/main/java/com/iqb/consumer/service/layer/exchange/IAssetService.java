package com.iqb.consumer.service.layer.exchange;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.jys.JYSDictInfo;
import com.iqb.consumer.data.layer.bean.jys.JYSPackInfo;

public interface IAssetService {

    public Map<String, Object> assetXlsImport(MultipartFile file);

    /**
     * 交易所需求:打包信息保存
     * 
     * @param jysPackInfo
     * @return
     */
    int insertPackInfo(JSONObject objs);

    /**
     * 通过订单号查询订单相关信息
     * 
     * @param orderId
     * @return
     */
    List<JYSDictInfo> queryAllByKey(JSONObject objs);

}
