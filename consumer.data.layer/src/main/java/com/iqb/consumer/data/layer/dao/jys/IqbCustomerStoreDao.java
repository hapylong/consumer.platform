/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月15日 下午3:00:43
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao.jys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.jys.Iqb_customer_store_info;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface IqbCustomerStoreDao {

    /**
     * 查询所有
     * 
     * @return
     */
    List<Iqb_customer_store_info> queryAllInfo(@Param("guarantee_corporation_name") String guarantee_corporation_name);
}
