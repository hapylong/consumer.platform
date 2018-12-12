package com.iqb.consumer.crm.crmpush.service;

import java.util.Map;

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
public interface ICrmPushService {

    /**
     * 
     * Description: crmPost方式推送
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年9月21日 下午7:39:14
     */
    public String crmPushByPost(String url, Map<String, String> paramMap);

    /**
     * 
     * Description: crmPost方式推送
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年9月21日 下午7:39:14
     */
    public String crmPushByPostByHttps(String url, Map<String, String> paramMap);

}
