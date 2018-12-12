/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月7日 下午2:45:16
 * @version V1.0
 */
package com.iqb.consumer.manage.front.back;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.consumer.service.layer.back.IBackGroundService;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.consumer.service.layer.business.service.IOrderService;
import com.iqb.consumer.service.layer.user.UserService;
import com.iqb.consumer.service.layer.xfpay.XFPayService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.SysUserSession;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Controller
@RequestMapping("/back")
public class BackGroundController extends BaseService {

    protected static final Logger logger = LoggerFactory.getLogger(BackGroundController.class);

    @Resource
    private ParamConfig paramConfig;
    @Resource
    private BillInfoService billInfoService;
    @Resource
    private XFPayService xfPayService;
    @Resource
    private SysUserSession sysUserSession;
    @Resource
    private RedisPlatformDao redisPlatformDao;
    @Resource
    private IOrderService orderService;
    @Resource
    private UserService userService;
    @Resource
    private IBackGroundService backGroundService;

    @ResponseBody
    @RequestMapping(value = "/singleInstall", method = {RequestMethod.GET, RequestMethod.POST})
    public Object singleInstall(@RequestBody JSONObject objs) {
        logger.info("后台分期，获取参数:{}", objs);
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = backGroundService.singleInstall(objs);
            logger.info("后台分期完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }

    }

    @ResponseBody
    @RequestMapping(value = "/refund", method = {RequestMethod.GET, RequestMethod.POST})
    public Object refund(@RequestBody JSONObject objs) {
        logger.info("后台平账，获取参数:{}", objs);
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = backGroundService.goToRefund(objs);
            logger.info("后台平账完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }

    }

}
