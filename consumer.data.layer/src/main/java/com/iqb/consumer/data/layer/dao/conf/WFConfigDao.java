/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月24日 上午11:33:51
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao.conf;

import java.util.Map;

import com.iqb.consumer.data.layer.bean.conf.WFConfig;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface WFConfigDao {

    /**
     * 通过业务类型查找工作流配置信息
     * 
     * @param bizType
     * @return
     */
    WFConfig getConfigByBizType(Map<String, Object> params);
}
