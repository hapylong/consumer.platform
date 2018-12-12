/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月14日 上午11:28:57
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.wf.CombinationQueryBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface CombinationQueryDao {

    CombinationQueryBean getByOrderId(@Param("orderId") String orderId);
}
