package com.iqb.consumer.service.layer.creditorinfo;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;

public interface CreditorInfoService {

    @SuppressWarnings("rawtypes")
    int insertCreditorInfo(Map objs);

    @SuppressWarnings("rawtypes")
    CreditorInfoBean selectOneByOrderId(Map objs);

    @SuppressWarnings("rawtypes")
    int updateCreditorInfo(Map objs);

    /**
     * 
     * Description:车主贷-生成项目信息名称
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月15日
     */
    String createProjectName(Map<String, Object> map);

    int saveUpdateMortgageInfo(JSONObject objs);
}
