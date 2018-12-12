package com.iqb.consumer.crm.crmpush.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.crm.crmpush.service.ICrmPushService;
import com.iqb.etep.common.utils.HttpClientUtil;
import com.iqb.etep.common.utils.StringUtil;

/**
 * 
 * Description: crm系统推送路由服务接口
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
public class CrmPushServiceImpl implements ICrmPushService {

    /** 日志 **/
    private static Logger logger = LoggerFactory.getLogger(CrmPushServiceImpl.class);

    @Override
    public String crmPushByPost(String url, Map<String, String> paramMap) {
        if (StringUtil.isEmpty(url)) {
            logger.error("crm推送url空值");
            return null;
        }
        if (CollectionUtils.isEmpty(paramMap)) {
            logger.warn("crm推送paraMap空值");
        }
        logger.info("crm推送url:" + url + ",参数信息:" + JSONObject.toJSONString(paramMap));
        try {
            return HttpClientUtil.restPost(url, paramMap);
        } catch (Exception e) {
            logger.error("调用httpClient接口异常", e);
            return null;
        }
    }

    @Override
    public String crmPushByPostByHttps(String url, Map<String, String> paramMap) {
        if (StringUtil.isEmpty(url)) {
            logger.error("etep推送url空值");
            return null;
        }
        if (CollectionUtils.isEmpty(paramMap)) {
            logger.warn("etep推送paraMap空值");
        }
        logger.info("etep推送url:" + url + ",参数信息:" + JSONObject.toJSONString(paramMap));
        try {
            return JSONObject.toJSONString(SendHttpsUtil.postMsg4GetMap(url, paramMap));
        } catch (Exception e) {
            logger.error("调用httpClient接口异常", e);
            return null;
        }
    }

}
