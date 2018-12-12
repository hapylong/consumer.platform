package com.iqb.consumer.asset.allocation.push.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.asset.allocation.push.service.IAssetPushService;
import com.iqb.etep.common.utils.HttpClientUtil;
import com.iqb.etep.common.utils.StringUtil;

/**
 * 
 * Description: 资产系统推送路由服务接口
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月21日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Service
public class AssetPushServiceImpl implements IAssetPushService {

    /** 日志 **/
    private static Logger logger = LoggerFactory.getLogger(AssetPushServiceImpl.class);

    @Override
    public String crmPushByPost(String url, Map<String, String> paramMap) {
        if (StringUtil.isEmpty(url)) {
            logger.error("资产推送url空值");
            return null;
        }
        if (CollectionUtils.isEmpty(paramMap)) {
            logger.warn("资产推送paraMap空值");
        }
        logger.info("资产推送url:" + url + ",参数信息:" + JSONObject.toJSONString(paramMap));
        try {
            return HttpClientUtil.restPost(url, paramMap);
        } catch (Exception e) {
            logger.error("调用httpClient接口异常", e);
            return null;
        }
    }

}
