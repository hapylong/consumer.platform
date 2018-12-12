package com.iqb.consumer.manage.front.dandelion;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.dandelion.HouseLoanService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年5月14日上午11:55:08 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/house")
public class HouseLoanController extends BasicService {
    protected static final Logger logger = LoggerFactory.getLogger(HouseLoanController.class);
    @Autowired
    private HouseLoanService houseLoanService;

    /**
     * 
     * Description:房融保订单申请-启动工作流
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月14日
     */
    @ResponseBody
    @RequestMapping(value = "/startHouseLoanProcess", method = RequestMethod.POST)
    public Object startHouseLoanProcess(@RequestBody JSONObject objs, HttpServletRequest request) {
        logger.info("---房融宝订单申请开始:{}", objs);
        try {
            Map<String, String> result = houseLoanService.startHouseLoanProcess(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.info("---房融宝订单申请完成:{}", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:房融保用户信息三码鉴权
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月14日
     */
    @ResponseBody
    @RequestMapping(value = "/userInfoAuth/{type}", method = RequestMethod.POST)
    public Object userInfoAuthByThree(@PathVariable("type") String type, @RequestBody JSONObject objs,
            HttpServletRequest request) {
        logger.info("---房融保用户信息三码鉴权开始:{}", objs);
        try {
            Map<String, String> result = houseLoanService.userInfoAuthByThree(type, objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.info("---房融保用户信息三码鉴权完成:{}", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年5月14日
     */
    @Override
    public int getGroupCode() {
        return 0;
    }
}
