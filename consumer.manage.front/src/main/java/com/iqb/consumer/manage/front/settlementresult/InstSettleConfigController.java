/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年8月11日下午3:40:16 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.manage.front.settlementresult;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.settlementresult.InstSettleConfigBean;
import com.iqb.consumer.service.layer.settlementresult.InstSettleConfigService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * @author haojinlong
 * 
 */
@Controller
@RequestMapping("/instSettleConfig")
public class InstSettleConfigController extends BaseService {
    private static Logger logger = LoggerFactory.getLogger(InstSettleConfigController.class);

    @Resource
    private InstSettleConfigService instSettleConfigService;

    /**
     * 
     * Description:查询商户代扣配置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月14日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Map selectInstSettleConfigResultByParams(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始查询商户代扣配置记录表...");
            PageInfo<InstSettleConfigBean> list = instSettleConfigService.selectInstSettleConfigResultByParams(objs);
            logger.debug("---查询商户代扣配置记录表完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:保存商户代扣配置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月14日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    public Map saveInstSettleConfigResultByParams(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始保存商户代扣配置记录表...");
            int result = instSettleConfigService.saveInstSettleConfig(objs);
            logger.debug("---保存商户代扣配置记录表完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:修改商户代扣配置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月14日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/mod", method = {RequestMethod.POST, RequestMethod.GET})
    public Map updateInstSettleConfigResultByParams(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始修改商户代扣配置记录表...");
            int result = instSettleConfigService.updateInstSettleConfigById(objs);
            logger.debug("---修改商户代扣配置记录表完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/{status}/update_status", method = {RequestMethod.POST, RequestMethod.GET})
    public Map updateStatusInstSettleConfigResultByParams(@RequestBody JSONObject objs,
            @PathVariable("status") String status, HttpServletRequest request) {
        try {
            logger.debug("---开始修改商户代扣配置记录表...");
            int result = instSettleConfigService.updateStatus(status, objs);
            logger.debug("---修改商户代扣配置记录表完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/queryById", method = {RequestMethod.POST, RequestMethod.GET})
    public Map queryById(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始修改商户代扣配置记录表...");
            InstSettleConfigBean result = instSettleConfigService.queryById(objs);
            logger.debug("---修改商户代扣配置记录表完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
