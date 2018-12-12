/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月13日 下午5:25:32
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao.jys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface JysDictInfoDao<T> {

    /**
     * 交易所需求:查询摘牌机构
     * 
     * @param jysPackInfo
     * @return
     */
    List<T> queryDictByKey(@Param("key") String key);
}
