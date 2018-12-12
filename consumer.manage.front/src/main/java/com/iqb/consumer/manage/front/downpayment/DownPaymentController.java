/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月7日 下午2:45:16
 * @version V1.0
 */
package com.iqb.consumer.manage.front.downpayment;

import java.io.UnsupportedEncodingException;
import java.util.Date;
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
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.data.layer.bean.downpayment.DownPaymentBean;
import com.iqb.consumer.service.layer.downpayment.DownPaymentService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Controller
@RequestMapping("/downPayment")
public class DownPaymentController extends BaseService {

    protected static final Logger logger = LoggerFactory.getLogger(DownPaymentController.class);

    public final static String FES_RET_SUCC = "000000";

    @Resource
    private DownPaymentService downPaymentService;

    /*
     * Description: 获取首付款列表
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/getDownPaymentList", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getDownPaymentList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            /* 日期校验 */
            if (null != objs.getDate("startDate") && null != objs.getDate("endDate")) {
                Date startDate = objs.getDate("startDate");
                Date endDate = objs.getDate("endDate");

                if (DateUtil.diffDays(startDate, endDate) < 0) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("success", 2);
                    map.put("retDatetype", 1);
                    map.put("retCode", "00000095");
                    map.put("retUserInfo", "处理失败，开始日期不能大于结束日期");
                    map.put("retFactInfo", "处理失败");
                    return map;
                }
            }

            /* 单独处理 结束日期 */
            if (null != objs.getDate("endDate")) {
                Date endDate = objs.getDate("endDate");
                endDate = DateUtil.addDays(endDate, 1).getTime();
                objs.put("endDate", endDate);
            }

            logger.debug("IQB信息---获取首付款列表信息开始...");
            PageInfo<DownPaymentBean> pageInfo = downPaymentService.getDownPaymentList(objs);
            logger.debug("IQB信息---获取首付款列表信息结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 获取首付款列表导出
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/getDownPaymentListSave", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getDownPaymentListSave(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> data = new HashMap<String, String>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para;
                try {
                    para = new String(request.getParameter(paraName).getBytes("ISO-8859-1"), "UTF-8");
                    data.put(paraName, para.trim());
                } catch (UnsupportedEncodingException e) {}
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);

            /* 日期校验 */
            if (null != objs.getDate("startDate") && null != objs.getDate("endDate")) {
                Date startDate = objs.getDate("startDate");
                Date endDate = objs.getDate("endDate");

                if (DateUtil.diffDays(startDate, endDate) < 0) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("success", 2);
                    map.put("retDatetype", 1);
                    map.put("retCode", "00000095");
                    map.put("retUserInfo", "处理失败，开始日期不能大于结束日期");
                    map.put("retFactInfo", "处理失败");
                    return map;
                }
            }

            /* 单独处理 结束日期 */
            if (null != objs.getDate("endDate")) {
                Date endDate = objs.getDate("endDate");
                endDate = DateUtil.addDays(endDate, 1).getTime();
                objs.put("endDate", endDate);
            }

            logger.debug("IQB信息---导出首付款列表信息开始...");
            String result = downPaymentService.getDownPaymentListSave(objs, response);
            logger.debug("IQB信息---导出首付款列表信息结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
