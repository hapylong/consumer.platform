/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年6月13日 上午10:04:57
 * @version V1.0
 */

package com.iqb.consumer.common.spring;

import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class MappingJacksonJsonViewEx extends MappingJackson2JsonView {

    @SuppressWarnings("rawtypes")
    @Override
    protected Object filterModel(Map<String, Object> model) {
        Object result = super.filterModel(model);
        if (!(result instanceof Map)) {
            return result;
        }
        Map map = (Map) result;
        if (map.size() == 1) {
            return map.values().toArray()[0];
        }
        return map;
    }

}
