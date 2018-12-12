package com.iqb.consumer.data.layer.biz.merchant;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.merchant.MerchantKeypair;
import com.iqb.consumer.data.layer.dao.merchant.MerchantKeypairDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;

@Component
public class MerchantKeypairBiz extends BaseBiz {

    @Resource
    private MerchantKeypairDao merchantKeypairDao;
    @Resource
    private RedisPlatformDao redisPlatformDao;

    public long addKeyPair(MerchantKeypair merchantKeyPair) throws IqbException {
        super.setDb(0, super.MASTER);
        long resultId = this.merchantKeypairDao.addKeyPair(merchantKeyPair);
        // 将商户密钥存放Redis
        String key = "KeyPair" + merchantKeyPair.getMerchantNo();
        redisPlatformDao.setKeyAndValue(key, JSONObject.toJSONString(merchantKeyPair));
        return resultId;
    }

    public MerchantKeypair queryKeyPair(String merchantNo) throws IqbException {
        String key = "KeyPair" + merchantNo;
        MerchantKeypair merchantKeypair = null;
        if (merchantNo.endsWith("*")) {// 特殊标识,只要已*结尾的商户都通过数据库查询,防止手工添加数据库数据redis无法get的问题
            merchantKeypair = this.merchantKeypairDao.queryKeyPair(merchantNo.replaceAll("\\*", ""));
            if (merchantKeypair != null) {
                redisPlatformDao.setKeyAndValue(key, JSONObject.toJSONString(merchantKeypair));
            }
            return merchantKeypair;
        } else {
            // 先从redis获取，获取不到在穿透
            String keyPairStr = redisPlatformDao.getValueByKey(key);
            if (keyPairStr == null || keyPairStr.equals("null")) {
                super.setDb(0, super.SLAVE);
                merchantKeypair = this.merchantKeypairDao.queryKeyPair(merchantNo);
                redisPlatformDao.setKeyAndValue(key, JSONObject.toJSONString(merchantKeypair));
            } else {
                merchantKeypair = JSONObject.parseObject(keyPairStr, MerchantKeypair.class);
            }
            return merchantKeypair;
        }
    }

    public PageInfo<MerchantKeypair> getKeyPairList(JSONObject requestMessage) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        return new PageInfo<MerchantKeypair>(this.merchantKeypairDao.getKeyPairList(requestMessage));
    }

    public void updateKeyPair(MerchantKeypair mkp) {
        super.setDb(0, super.MASTER);
        merchantKeypairDao.updateKeyPair(mkp);
        // 将商户密钥存放Redis
        String key = "KeyPair" + mkp.getMerchantNo();
        redisPlatformDao.setKeyAndValue(key, JSONObject.toJSONString(mkp));
    }

    public void updateIpsById(JSONObject requestMessage) {
        super.setDb(0, super.MASTER);
        merchantKeypairDao.updateIpsById(requestMessage);
        MerchantKeypair mkp = merchantKeypairDao.getSecurityInfoById(requestMessage);
        String key = "KeyPair" + mkp.getMerchantNo();
        redisPlatformDao.setKeyAndValue(key, JSONObject.toJSONString(mkp));
    }
}
