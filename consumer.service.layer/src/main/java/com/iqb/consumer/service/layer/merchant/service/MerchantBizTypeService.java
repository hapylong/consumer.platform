/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月13日 上午10:20:36
 * @version V1.0
 */
package com.iqb.consumer.service.layer.merchant.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBizTypeBean;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
public interface MerchantBizTypeService {

    /**
     * 根据商户号查询业务类型列表
     * 
     * @param
     */
    List<MerchantBizTypeBean> getMerchantBizTypeList(JSONObject objs);
}
