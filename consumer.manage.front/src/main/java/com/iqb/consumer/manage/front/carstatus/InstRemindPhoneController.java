package com.iqb.consumer.manage.front.carstatus;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CgetCarStatusInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CreditLoanInfoBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindPhoneBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindRecordBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.carstatus.InstRemindPhoneService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.StringUtil;

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
 * 2018年4月26日下午2:58:53 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/instRemindPhone")
public class InstRemindPhoneController extends BaseService {
    protected static final Logger logger = LoggerFactory.getLogger(InstRemindPhoneController.class);

    @Autowired
    private InstRemindPhoneService instRemindPhoneService;
    @Resource
    private AdminService adminServiceImpl;

    /**
     * 
     * 电话提醒分页列表查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月26日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/queryList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map queryList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---电话提醒分页列表查询开始...{}", objs);
            PageInfo<InstRemindPhoneBean> result = instRemindPhoneService.selectInstRemindPhoneList(objs);
            logger.info("---电话提醒分页列表查询完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 根据订单号 期数查询电话提醒列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月26日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/queryRecordList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map queryRecordList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---根据订单号 期数查询电话提醒列表信息开始...{}", objs);
            PageInfo<InstRemindRecordBean> result = instRemindPhoneService.selectInstRemindRecordList(objs);
            logger.info("---根据订单号 期数查询电话提醒列表信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 保存电话提醒信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月26日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/saveRecordInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map saveRecordInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---保存电话提醒信息开始...{}", objs);
            InstRemindRecordBean bean = JSONObject.toJavaObject(objs, InstRemindRecordBean.class);
            int result = instRemindPhoneService.insertInstRemindRecordInfo(bean);
            logger.info("---保存电话提醒信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 根据订单号 回显贷后客户信息流程页面数据
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月27日 14:11:50
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/queryCustomerInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map queryCustomerInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---根据订单号  回显贷后客户信息流程页面数据开始...{}", objs);
            // 前端传来得objs.get("orderId").toString() 是订单号-当前期数这样得格式
            String str = objs.get("orderId").toString();
            String[] split = str.split("-");
            objs.put("orderId", split[0]);
            objs.put("curItems", split[1]);
            objs.put("flag", split[2]);
            InstRemindRecordBean result = instRemindPhoneService.queryCustomerInfo(objs);
            logger.info("---根据订单号  回显贷后客户信息流程页面数据完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 贷后客户信息流程页面点击审核通过前执行该接口,修改状态为未处理
     * 
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月27日 16:03:02
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/updateInstRemindPhoneBean"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map updateInstRemindPhoneBean(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---贷后客户信息流程页面点击审核通过前执行该接口,修改状态为未处理开始...{}", objs);
            String str = objs.get("orderId").toString();
            String[] split = str.split("-");
            objs.put("orderId", split[0]);
            objs.put("curItems", split[1]);
            objs.put("flag", split[2]);
            instRemindPhoneService.updateInstRemindPhoneBean(objs);
            logger.info("---贷后客户信息流程页面点击审核通过前执行该接口,修改状态为未处理完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 导出电话提醒列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月27日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/exportInstRemindPhoneList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportInstRemindPhoneList(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.info("--导出电话提醒列表信息--");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            logger.info("--导出电话提醒列表信息请求参数：{}", objs);
            String result = instRemindPhoneService.exportInstRemindPhoneList(objs, response);
            logger.info("--导出电话提醒列表信息完成.结果：{}", result);
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
     * 导出电催列表信息
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月2日 10:57:11
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/exportInstRemindPhoneList2"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportInstRemindPhoneList2(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.info("--导出电催列表信息--");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            String result = instRemindPhoneService.exportInstRemindPhoneList2(objs, response);
            logger.info("--导出电催列表信息完成");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 贷后列表页
     * 
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月4日 10:27:44
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/afterLoanList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map afterLoanList(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            adminServiceImpl.getMerchantNos(requestMessage);
            PageInfo<CgetCarStatusInfoResponseMessage> result = instRemindPhoneService
                    .afterLoanList(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result);
            return returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 根据订单号查询订单信息
     * 
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月4日 15:27:45
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/queryOrderInfoByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map queryOrderInfoByOrderId(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            adminServiceImpl.getMerchantNos(requestMessage);
            OrderBean result = instRemindPhoneService.queryOrderInfoByOrderId(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            if (result != null) {
                if (!"2001".equals(result.getBizType()) && !"2002".equals(result.getBizType())) {
                    linkedHashMap.put("code", "2");
                    linkedHashMap.put("result", "输入的订单号不是以租代购订单");
                } else if (result.getRiskStatus() == 3 || result.getRiskStatus() == 7) {
                    linkedHashMap.put("code", "0");
                    linkedHashMap.put("result", result);
                } else {
                    linkedHashMap.put("code", "1");
                    linkedHashMap.put("result", "输入的订单号不是已分期或已放款订单");
                }
            }
            return returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 增加贷后信息
     * 
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月4日 15:42:08
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/insertManagecarInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map insertManagecarInfo(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            adminServiceImpl.getMerchantNos(requestMessage);
            requestMessage.put("status", "10");
            instRemindPhoneService.insertManagecarInfo(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("cussess", "");
            return returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 根据订单号查询账单
     * 
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月4日 15:59:26
     */
    @ResponseBody
    @RequestMapping(value = {"/queryBillIfoByOId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object queryBillIfoByOId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("通过订单号:{} 查询账单", objs.getString("orderId"));
            List<Map<String, Object>> billMap =
                    instRemindPhoneService.queryBillIfoByOId(objs.getString("orderId"));
            logger.info("---根据订单号查询账单完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", billMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 启贷后处置工作流,并保存贷后意见
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月4日 18:35:10
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/saveAfterRiskAndStartWf"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map saveAfterRiskAndStartWf(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---保存电话提醒信息开始...{}", objs);
            int result = instRemindPhoneService.saveAfterRiskAndStartWf(objs);
            logger.info("---保存电话提醒信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 根据订单号inst_managecar_info查询表中是否已拥有
     * 
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月8日 22:15:23
     */
    @ResponseBody
    @RequestMapping(value = {"/queryManagecarInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object queryManagecarInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("通过订单号:{} 查询账单", objs.getString("orderId"));
            List<ManageCarInfoBean> billMap =
                    instRemindPhoneService.queryManagecarInfo(objs.getString("orderId"));

            logger.info("---根据订单号查询账单完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (billMap.size() > 0) {
                linkedHashMap.put("code", "0");
                linkedHashMap.put("result", "该订单已经进入贷后无需重复申请");
            } else {
                linkedHashMap.put("code", "1");
                linkedHashMap.put("result", "该订单可以进入贷后,然后执行保存接口");
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * FINANCE-3299 新增电话提醒和电催管理查询页面；FINANCE-3339新增电话提醒和电催管理查询接口
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月9日 17:29:31
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/queryTotalList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map queryTotalList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---电话提醒分页列表查询开始...{}", objs);
            adminServiceImpl.getMerchantNos(objs);
            PageInfo<InstRemindPhoneBean> result = instRemindPhoneService.selectInstRemindPhoneList2(objs);
            int totalNum = instRemindPhoneService.getInstRemindRecordListTotal(objs);
            logger.info("---电话提醒分页列表查询完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            linkedHashMap.put("totalNum", totalNum);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 导出电话提醒列表信息Total
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月10日 18:52:27
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/exportInstRemindPhoneList3"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportInstRemindPhoneList3(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.info("开始导出电话提醒列表信息");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            logger.info("导出电话提醒列表信息请求参数：{}", objs);
            adminServiceImpl.getMerchantNos(objs);
            String result = instRemindPhoneService.exportInstRemindPhoneList3(objs, response);
            logger.info("导出电话提醒列表信息数据完成.结果：{}", result);
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
     * 导出电催列表信息Total
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年5月10日 18:52:20
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/exportInstRemindPhoneList4"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportInstRemindPhoneList4(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.info("开始导出订单报表数据");
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
            String result = instRemindPhoneService.exportInstRemindPhoneList4(objs, response);
            logger.info("导出订单报表数据完成.结果：{}", result);
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
     * Description:根据订单号查询电话提醒 电催风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月6日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/getRiskInfoByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getRiskInfoByOrderId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---根据订单号查询电话提醒 电催风控信息开始...{}", objs);
            InstRemindPhoneBean result = instRemindPhoneService.getRiskInfoByOrderId(objs);
            CreditLoanInfoBean creditLoanInfoBean = null;
            if (!StringUtil.isNull(result.getCheckInfo())) {
                creditLoanInfoBean = JSONObject.parseObject(result.getCheckInfo(), CreditLoanInfoBean.class);
                creditLoanInfoBean.setPhone(result.getSmsMobile());
                creditLoanInfoBean.setSmsMobile(result.getPhone());
            }
            logger.info("---根据订单号查询电话提醒 电催风控信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", creditLoanInfoBean);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据订单号查询电话提醒 电催信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月6日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/getRemindInfoByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getRemindInfoByOrderId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---根据订单号查询电话提醒 电催信息开始...{}", objs);
            InstRemindPhoneBean result = instRemindPhoneService.getRemindInfoByOrderId(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.info("---根据订单号查询电话提醒 电催信息完成...");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据订单号获取车辆贷后信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月29日
     */
    @ResponseBody
    @RequestMapping(value = {"/unIntcpt-queryManagecarInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectManagecarInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("通过订单号:{} 获取车辆贷后信息", objs.getString("orderId"));
            List<ManageCarInfoBean> list =
                    instRemindPhoneService.queryManagecarInfo(objs.getString("orderId"));

            logger.info("---根据订单号获取车辆贷后信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (!CollectionUtils.isEmpty(list)) {
                linkedHashMap.put("code", "000000");
                linkedHashMap.put("result", list.get(0));
            } else {
                linkedHashMap.put("code", "000001");
                linkedHashMap.put("result", "该订单在贷后信息中不存在");
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
