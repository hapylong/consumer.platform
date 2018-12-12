/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 抵押车业务分成接口
 * @date 2017年4月25日
 * @version V1.0
 */
package com.iqb.consumer.service.layer.query;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

/**
 * @author <a href="haojinlong@iqianbang.com">haojinlong</a>
 */
public interface MortgagecarService {
    PageInfo<Map<String, Object>> selectMortgagecatList(JSONObject obj);

    String exportMortgageList(JSONObject objs, HttpServletResponse response);
}
