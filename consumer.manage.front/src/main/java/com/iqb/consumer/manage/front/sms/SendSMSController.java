/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月7日 下午2:45:16
 * @version V1.0
 */
package com.iqb.consumer.manage.front.sms;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.service.layer.sms.SmsService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * 发送短信
 */
@Controller
@RequestMapping("/SMS")
public class SendSMSController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(SendSMSController.class);

    public final static String FES_RET_SUCC = "000000";

    @Autowired
    private SmsService smsService;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/sendSms"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map sendSmsByStatus(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始发送短信...");
            List<String> result = (List<String>) smsService.sendSmsByStatus(objs);
            logger.debug("IQB信息---发送短信完成.结果：{}");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            linkedHashMap.put("count", result.size());
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

}
