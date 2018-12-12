package com.iqb.consumer.manage.front.settlementresult;

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
import com.iqb.consumer.data.layer.bean.settlementresult.SettlementResultWithHoldBean;
import com.iqb.consumer.service.layer.settlementresult.SettlementResultWithHoldService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
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
 * 2018年1月7日下午3:17:17 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/settlementresultHold")
public class SettlementResultWithHoldController extends BaseService {
    private static Logger logger = LoggerFactory.getLogger(SettlementResultWithHoldController.class);

    @Resource
    private SettlementResultWithHoldService settlementResultWithHoldService;

    /**
     * 
     * Description:还款代扣分页信息查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Map listSettlementResultWithHold(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始查询还款代扣信息开始...");
            PageInfo<SettlementResultWithHoldBean> pageInfo =
                    settlementResultWithHoldService.listSettlementResultWithHold(objs);
            logger.debug("---开始查询还款代扣信息开始完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:导出代扣信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    @ResponseBody
    @RequestMapping(value = {"/exportBillList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportSettlementResultList(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.debug("开始导出还款代扣报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            String result = settlementResultWithHoldService.exportSettlementResultList(objs, response);
            logger.debug("导出还款代扣报表数据完成.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:账单还款代扣接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    @ResponseBody
    @RequestMapping(value = "/billWithHold", method = {RequestMethod.POST, RequestMethod.GET})
    public Map billWithHold(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---还款代扣开始...");
            String resultStr =
                    settlementResultWithHoldService.billWithHold(objs);
            logger.debug("---还款代扣完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", resultStr);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:账单还款代扣回调接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-billWithHold", method = {RequestMethod.POST, RequestMethod.GET})
    public Map unIntcptBillWithHold(@RequestBody JSONObject objs, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            logger.debug("---还款代扣回调接口开始...");
            int result = settlementResultWithHoldService.callback(objs);
            logger.debug("---还款代扣回调完成.");
            if (result > 0) {
                response.put("succ", "1");
            } else {
                response.put("succ", "2");
            }
            logger.debug("---还款代扣回调完成.");
            return response;
        } catch (Exception e) {
            response.put("succ", "还款代扣回调异常");
            return response;
        }
    }
}
