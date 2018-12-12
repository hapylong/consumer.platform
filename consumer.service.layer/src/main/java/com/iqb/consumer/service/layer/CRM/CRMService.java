/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月19日 下午2:22:00
 * @version V1.0
 */
package com.iqb.consumer.service.layer.CRM;

import com.alibaba.fastjson.JSONObject;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;

/**
 * @author gxy
 */
public interface CRMService {

    // 添加商户
    public int addMerchant(JSONObject objs) throws IqbException, IqbSqlException;
}
