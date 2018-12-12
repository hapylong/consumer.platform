/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 抵押车业务统计Controller
 * @date 2017年4月25日
 * @version V1.0
 */
package com.iqb.consumer.manage.front.query;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.service.layer.query.MortgagecarService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * @author <a href="haojinlong@iqianbang.com">haojinlong</a>
 */
@SuppressWarnings({"rawtypes"})
@Controller
@RequestMapping("/mortgagecarQuery")
public class MortgagecarQueryController extends BaseService {
    private static Logger logger = LoggerFactory.getLogger(MortgagecarQueryController.class);

    @Resource
    private MortgagecarService mortgagecarService;

    @ResponseBody
    @RequestMapping(value = "/getAllMortgagecars", method = {RequestMethod.GET, RequestMethod.POST})
    public Map getAllMortgagecars(@RequestBody JSONObject obj, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始分页查询抵押车业务数据...");
            PageInfo<Map<String, Object>> list = mortgagecarService.selectMortgagecatList(obj);
            logger.debug("IQB信息---分页查询抵押车业务数据完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/exportMortgagecarsList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportMortgagecarsList(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.debug("开始导出抵押车业务分成报表数据");
            Map<String, String> data = new HashMap<String, String>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
                /*
                 * data.put(paraName, new String(para.trim() .getBytes("ISO-8859-1"), "UTF-8"));
                 */

            }
            logger.debug("MortgagecarQueryController[exportMortgagecarsList] data {}", data);
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            String result = mortgagecarService.exportMortgageList(objs, response);
            logger.debug("导出抵押车业务分成报表数据完成.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

}
