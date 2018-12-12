package com.iqb.consumer.manage.front.dandelion;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.dandelion.HuayiLoanService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description:华益周转贷Controller
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年3月30日下午3:45:49 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/huayi")
public class HuayiLoanController extends BasicService {
    protected static final Logger logger = LoggerFactory.getLogger(HuayiLoanController.class);
    @Resource
    private HuayiLoanService huayiLoanService;

    /**
     * 
     * Description:华益周转贷订单提交、流程启动
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月30日
     */
    @ResponseBody
    @RequestMapping(value = "/startHuaYiLoanWF", method = RequestMethod.POST)
    public Object startHuaYiLoanWf(@RequestBody JSONObject objs, HttpServletRequest request) {
        logger.info("保存订单金额和期数:{}", objs);
        try {
            Map<String, String> result = huayiLoanService.startHuayiLoanProcess(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
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
     * @Author haojinlong Create Date: 2018年3月30日
     */
    @Override
    public int getGroupCode() {
        return 0;
    }

}
