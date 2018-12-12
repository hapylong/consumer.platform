package com.iqb.consumer.data.layer.dao.merchant;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.merchant.MerchantKeypair;

public interface MerchantKeypairDao {

    /**
     * 添加商户密钥对
     * 
     * @param merchantKeyPairBean
     * @return
     */
    long addKeyPair(MerchantKeypair merchantKeyPairBean);

    /**
     * 查询商户密钥对
     * 
     * @param merchantNo
     * @return
     */
    MerchantKeypair queryKeyPair(String merchantNo);

    List<MerchantKeypair> getKeyPairList(JSONObject requestMessage);

    /**
     * 
     * Description: 更新商户密钥对
     * 
     * @param
     * @Author adam Create Date: 2017年5月27日 下午4:29:08
     */
    void updateKeyPair(MerchantKeypair mkp);

    void updateIpsById(JSONObject requestMessage);

    MerchantKeypair getSecurityInfoById(JSONObject requestMessage);
}
