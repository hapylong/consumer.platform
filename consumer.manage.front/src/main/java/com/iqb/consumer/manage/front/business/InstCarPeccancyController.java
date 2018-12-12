package com.iqb.consumer.manage.front.business;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyBean;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyDetailBean;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.cheegu.InstCarPeccancyService;
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
 * 2018年5月28日下午3:53:32 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/carPeccancy")
public class InstCarPeccancyController extends BaseService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(InstCarPeccancyController.class);
    @Autowired
    private InstCarPeccancyService instCarPeccancyService;
    @Autowired
    private AdminService adminServiceImpl;

    /**
     * 
     * Description:根据条件查询车辆违章信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    @ResponseBody
    @RequestMapping(value = {"/selectInstCarPeccancyList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map selectInstCarPeccancyList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---根据条件查询车辆违章信息...{}", objs);
            adminServiceImpl.merchantNos(objs);
            PageInfo<InstCarPeccancyBean> pageInfo = instCarPeccancyService.selectInstCarPeccancyList(objs);
            logger.info("IQB信息---分页查询根据条件查询车辆违章信息完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据订单号 车架号查询车辆违章明细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    @ResponseBody
    @RequestMapping(value = {"/selectInstCarPeccancyDetailList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map selectInstCarPeccancyDetailList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---根据订单号 车架号查询车辆违章明细信息...{}", objs);
            PageInfo<InstCarPeccancyDetailBean> pageInfo = instCarPeccancyService.selectInstCarPeccancyDetailList(objs);
            logger.info("IQB信息---分页查询-根据订单号 车架号查询车辆违章明细信息完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:更新车辆违章信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月29日
     */
    @ResponseBody
    @RequestMapping(value = {"/updateInstCarPeccancy"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map updateInstCarPeccancyStatusByOrderId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---根据订单号 车架号查询车辆违章明细信息...{}", objs);
            int result = instCarPeccancyService.updateInstCarPeccancyStatusByOrderId(objs);
            logger.info("IQB信息---分页查询-根据订单号 车架号查询车辆违章明细信息完成.");
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
     * Description:违章查询列表导出
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月30日
     */
    @ResponseBody
    @RequestMapping(value = {"/exportInstCarPeccancyList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportInstCarPeccancyList(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.info("开始导出违章查询列表");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            String result = instCarPeccancyService.exportInstCarPeccancyList(objs, response);
            logger.info("导出违章查询报表数据完成.结果：{}", result);
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
     * Description:发送-获取车辆违章明细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月4日
     */
    @ResponseBody
    @RequestMapping(value = {"/doSendAndGetInstCarPeccancyDetail"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map doSendAndGetInstCarPeccancyDetail(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---发送-获取车辆违章明细信息...{}", objs);
            int result = instCarPeccancyService.doSendAndGetInstCarPeccancyDetail(objs);
            logger.info("IQB信息---发送-获取车辆违章明细信息完成.");
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
     * 车易估-中阁毁回调接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    @ResponseBody
    @RequestMapping(value = {"/unIntcpt-callbackForCheegu"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map callbackForCheegu(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                logger.info("IQB信息---paraName...{}", paraName);
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            logger.info("IQB信息---车易估违章信息回调接口,参数...{}", objs);
            int result = instCarPeccancyService.callbackForCheegu(objs);
            logger.info("IQB信息---发送-获取车辆违章明细信息完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
