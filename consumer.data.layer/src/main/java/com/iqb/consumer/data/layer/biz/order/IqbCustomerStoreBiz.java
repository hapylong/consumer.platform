/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月15日 下午3:11:10
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.order;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.jys.Iqb_customer_store_info;
import com.iqb.consumer.data.layer.dao.jys.IqbCustomerStoreDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class IqbCustomerStoreBiz extends BaseBiz {

    @Resource
    private IqbCustomerStoreDao iqbCustomerStoreDao;

    public List<Iqb_customer_store_info> queryAllInfo(String name) {
        setDb(0, super.SLAVE);
        return iqbCustomerStoreDao.queryAllInfo(name);
    }
}
