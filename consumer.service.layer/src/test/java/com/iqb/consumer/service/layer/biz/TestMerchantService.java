/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年10月12日 下午7:08:34
 * @version V1.0
 */
package com.iqb.consumer.service.layer.biz;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.service.layer.merchant.service.IMerchantService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class TestMerchantService extends AbstractService {

    @Resource
    private IMerchantService merchantService;

    @Test
    public void testQueryMerchant() {
        JSONObject objs = new JSONObject();
        objs.put("id", "1006");
        MerchantBean mb = merchantService.getMerByID(objs);
        System.out.println(mb.getId());
    }
}
