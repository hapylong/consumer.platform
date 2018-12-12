/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月13日 上午10:21:08
 * @version V1.0
 */
package com.iqb.consumer.service.layer.merchant.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBizTypeBean;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBizTypeBiz;
import com.iqb.consumer.service.layer.merchant.service.MerchantBizTypeService;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Service("merchantBizTypeService")
public class MerchantBizTypeServiceImpl implements MerchantBizTypeService {

    protected static Logger logger = LoggerFactory.getLogger(MerchantBizTypeServiceImpl.class);

    @Resource
    private MerchantBizTypeBiz merchantBizTypeBiz;
    @Resource
    private MerchantBeanBiz merchantBiz;

    @Override
    public List<MerchantBizTypeBean> getMerchantBizTypeList(JSONObject objs) {
        objs.put("merchantShortName", objs.getString("merchantNo"));
        String merchantNo = merchantBiz.getMerCodeByMerSortName(objs);
        objs.put("merchantNo", merchantNo);
        objs.remove("merchantShortName");
        List<MerchantBizTypeBean> list = merchantBizTypeBiz.getMerchantBizTypeList(objs);
        return list;
    }
}
