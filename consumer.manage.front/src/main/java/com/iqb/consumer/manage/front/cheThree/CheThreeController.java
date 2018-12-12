package com.iqb.consumer.manage.front.cheThree;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.carthreehundred.CheThreeService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;

/**
 * 
 * Description: 车300接口汇总
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
* Modification History:
* Date         Author      Version     Description
------------------------------------------------------------------
* 2018年1月24日     chengzhen       1.0        1.0 Version
* </pre>
 */
@Controller
@RequestMapping("/cheThreeHunder")
public class CheThreeController extends BasicService {
    protected static final Logger logger = LoggerFactory.getLogger(CheThreeController.class);
    @Autowired
    private CheThreeService cheThreeService;
    @Resource
    private AdminService adminService;

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-城市接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeCity"}, method = {RequestMethod.POST})
    public JSONObject carThreeCity(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost = SimpleHttpUtils.httpGet("http://testapi.che300.com/service/getAllCity", map);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-品牌接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeBind"}, method = {RequestMethod.POST})
    public JSONObject carThreeBind(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost = SimpleHttpUtils.httpGet("http://testapi.che300.com/service/getCarBrandList", map);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-车系接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeSeries"}, method = {RequestMethod.POST})
    public JSONObject carThreeSeries(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/getCarSeriesList", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-车型接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeSeriesAll"}, method = {RequestMethod.POST})
    public JSONObject carThreeSeriesAll(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/getCarModelList", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-定价接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeFixPrice"}, method = {RequestMethod.POST})
    public JSONObject carThreeFixPrice(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/eval/getUsedCarPriceAnalysis",
                            requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-VIN码接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeVINUrl"}, method = {RequestMethod.POST})
    public JSONObject carThreeVINUrl(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/identifyModelByVIN", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-VIN码接口Eval
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeVINUrlEval"}, method = {RequestMethod.POST})
    public JSONObject carThreeVINUrlEval(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost = SimpleHttpUtils.httpGet("http://testapi.che300.com/service/common/eval", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-车型详细参数
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeSeriesDetail"}, method = {RequestMethod.POST})
    public JSONObject carThreeSeriesDetail(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "03a6741f94109d1d590af94715cf983a");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://api.che300.com/service/getModelParameters", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-车型详细参数更新时间
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeSeriesDetailUpdateTime"}, method = {RequestMethod.POST})
    public JSONObject carThreeSeriesDetailUpdateTime(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "03a6741f94109d1d590af94715cf983a");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://api.che300.com/service/getModelParamsUpdateTime", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-新车价格和当地优惠价
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeNewCarPrice"}, method = {RequestMethod.POST})
    public JSONObject carThreeNewCarPrice(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "03a6741f94109d1d590af94715cf983a");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://api.che300.com/service/getNewCarPrice", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-年月残值
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeMonthlyYearResidual"}, method = {RequestMethod.POST})
    public JSONObject carThreeMonthlyYearResidual(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/common/eval", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911 1 增量获取车300车型库1.1 功能说明返回车300增量车型库。
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @ResponseBody
    @RequestMapping(value = {"/carThreeMonthlyYearResidual1"}, method = {RequestMethod.POST})
    public JSONObject carThreeMonthlyYearResidual1(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/common/eval", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 注册贷后监控车辆信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月7日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/registerPostLoanMonitorCar"}, method = {RequestMethod.POST})
    public Map registerPostLoanMonitorCar(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            logger.info("---注册贷后监控车辆信息开始...{}", requestMessage);
            Map<String, String> map = cheThreeService.registerPostLoanMonitorCar(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", map);
            logger.info("---注册贷后监控车辆信息完成---");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 关闭贷后监控
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月14日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/unIntcpt-stopPostLoanMonitor"}, method = {RequestMethod.POST})
    public Map stopPostLoanMonitor(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            logger.info("---关闭贷后监控开始...{}", requestMessage);
            Map<String, String> map = cheThreeService.stopPostLoanMonitor(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", map);
            logger.info("---关闭贷后监控完成---");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 贷后风险信息回调接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月14日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/unIntcpt-loanRiskAccept"}, method = {RequestMethod.POST})
    public Map loanRiskAccept(
            @RequestBody JSONArray jsonArray, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            logger.info("---贷后风险信息回调接口开始...{}", jsonArray);

            int result = cheThreeService.loanRiskAccept(jsonArray);
            if (result > 0) {
                resultMap.put("status", 1);
            } else {
                resultMap.put("status", 0);
                resultMap.put("error_msg", 1);
            }
            logger.info("---贷后风险信息回调接口完成---");
            return resultMap;
        } catch (Exception e) {
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            resultMap.put("status", 0);
            resultMap.put("error_msg", "插入数据失败");
            return resultMap;
        }
    }

    /**
     * 
     * 灰名单待发送列表查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectCheThreeHWaitSendList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectCheThreeHWaitSendList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---灰名单待发送列表查询--- {}", objs);
            adminService.getMerchantNos(objs);
            PageInfo<Map<String, Object>> result =
                    cheThreeService.selectCheThreeHWaitSendList(objs);
            logger.info("---灰名单待发送列表查询--- {}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            Map<String, Object> cntMap = new HashMap<String, Object>();
            cntMap.put("count", result.getTotal());
            linkedHashMap.put("resultTotal", cntMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 车贷监控列表查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectCheThreeMonitorList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectCheThreeMonitorList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---车贷监控列表查询--- {}", objs);
            adminService.getMerchantNos(objs);
            PageInfo<Map<String, Object>> result =
                    cheThreeService.selectCheThreeMonitorList(objs);
            logger.info("---车贷监控列表查询--- {}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            Map<String, Object> cntMap = new HashMap<String, Object>();
            cntMap.put("count", result.getTotal());
            linkedHashMap.put("resultTotal", cntMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 车贷监控列表查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/exportCheThreeMonitorList", method = {RequestMethod.POST, RequestMethod.GET})
    public Map exportCheThreeMonitorList(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.debug("开始导出车贷监控列表查询报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            adminService.getMerchantNos(objs);
            String result = cheThreeService.exportCheThreeMonitorList(
                    objs, response);
            logger.debug("导出车贷监控列表查询报表数据完成.结果：{}", result);
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
     * 反欺诈详情查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月15日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectCheThreeMonitorReceive"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectCheThreeMonitorReceive(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---反欺诈详情查询--- {}", objs);
            // adminServiceImpl.getMerchantNos(objs);
            PageInfo<Map<String, Object>> result =
                    cheThreeService.selectCheThreeMonitorReceive(objs);
            logger.info("---反欺诈详情查询--- {}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            Map<String, Object> cntMap = new HashMap<String, Object>();
            cntMap.put("count", result.getTotal());
            linkedHashMap.put("resultTotal", cntMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @Override
    public int getGroupCode() {
        return 0;
    }
}
