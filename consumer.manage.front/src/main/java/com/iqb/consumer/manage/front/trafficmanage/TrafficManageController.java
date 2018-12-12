package com.iqb.consumer.manage.front.trafficmanage;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.trafficmanage.ITrafficManageService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.SysUserSession;

/**
 * 
 * Description: 车务模块
 * 
 * @author chengzhen
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2018年8月6日 15:15:01    chenyong       1.0        1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/trafficManage")
public class TrafficManageController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(TrafficManageController.class);

    @Resource
    private AdminService adminServiceImpl;

    @Resource
    private ITrafficManageService trafficManageServiceImpl;

    @Resource
    private SysUserSession sysUserSession;

    /**
     * 
     * 保存补充资料
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/saveTrafficManageAdditional"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> saveTrafficManageAdditional(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---保存补充资料--- {}", requestMessage);
            requestMessage.put("operatorRegId", sysUserSession.getUserPhone());
            requestMessage.put("procTaskUser", sysUserSession.getUserCode());
            int result = trafficManageServiceImpl.saveTrafficManageAdditional(requestMessage);
            logger.info("---保存补充资料结果--- {}", result);
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
     * 车务管理订单查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectTrafficManageOrderList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectTrafficManageOrderList(@RequestBody JSONObject objs, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            logger.info("---车务管理订单查询--- {}", objs);
            adminServiceImpl.getMerchantNos(objs);
            PageInfo<Map<String, Object>> result =
                    trafficManageServiceImpl.selectTrafficManageOrderList(objs);
            logger.info("---车务管理订单查询--- {}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            Map<String, Object> cntMap = new HashMap<String, Object>();
            cntMap.put("count", trafficManageServiceImpl.countTrafficManageOrder(objs));
            linkedHashMap.put("resultTotal", cntMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 车务查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectTrafficManageInfoList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectTrafficManageInfoList(@RequestBody JSONObject objs, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            logger.info("---车务管理订单查询--- {}", objs);
            adminServiceImpl.getMerchantNos(objs);
            PageInfo<Map<String, Object>> result =
                    trafficManageServiceImpl.selectTrafficManageInfoList(objs);
            logger.info("---车务管理订单查询--- {}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            Map<String, Object> cntMap = new HashMap<String, Object>();
            cntMap.put("count", trafficManageServiceImpl.countTrafficManageInfo(objs));
            linkedHashMap.put("resultTotal", cntMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 车务查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/exportTrafficManageInfoList", method = {RequestMethod.POST, RequestMethod.GET})
    public Map exportTrafficManageInfoList(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.debug("开始导出车务查询报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            adminServiceImpl.getMerchantNos(objs);
            String result = trafficManageServiceImpl.exportTrafficManageInfoList(
                    objs, response);
            logger.debug("导出车务查询报表数据完成.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 车务补充资料详情
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/getTrafficManageDetail"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getTrafficManageDetail(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---查询车务补充资料详情开始...{}", objs);
            Map<String, Object> result = trafficManageServiceImpl.getTrafficManageDetail(objs);
            logger.info("---查询车务补充资料详情完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 历史记录
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/selectTrafManaAdditionalhistory", method = {RequestMethod.POST, RequestMethod.GET})
    public Map selectTrafManaAdditionalhistory(@RequestBody JSONObject objs,
            HttpServletResponse response) {
        try {
            logger.info("---查询历史记录开始...{}", objs);
            List<Map<String, Object>> result = trafficManageServiceImpl.selectTrafManaAdditionalhistory(objs);
            logger.info("---查询历史记录完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

}
