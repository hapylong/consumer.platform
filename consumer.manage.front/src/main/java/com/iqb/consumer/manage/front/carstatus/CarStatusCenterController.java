package com.iqb.consumer.manage.front.carstatus;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CgetCarStatusInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.GPSInfo;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.carstatus.CarStatusCenterService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.BeanUtil;

/**
 * 
 * Description: 车辆状态跟踪
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月15日    adam       1.0        1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/carstatus")
public class CarStatusCenterController extends BasicService {

    protected static final Logger logger = LoggerFactory.getLogger(CarStatusCenterController.class);
    private static final int SERVICE_CODE_GET_INFO_LIST = 1;
    private static final int SERVICE_CODE_PERSISIT_IMCI = 2;
    private static final int SERVICE_CODE_GET_INFO = 3;
    private static final int SERVICE_CODE_UPDATE_INFO = 4;

    @Autowired
    private CarStatusCenterService carStatusCenterServiceImpl;

    @Resource
    private AdminService adminServiceImpl;

    /**
     * 
     * Description: FINANCE-1439 车辆状态查询 FINANCE-1472 车辆状态跟踪/贷后处置/车辆出库/车辆状态 【有测试】
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年7月10日 下午2:25:41
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/cget_info_list"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_INFO_LIST)
    public Object cgetInfoList(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            adminServiceImpl.getMerchantNos(requestMessage);
            PageInfo<CgetCarStatusInfoResponseMessage> result = carStatusCenterServiceImpl
                    .cgetInfoList(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_INFO_LIST);
        } catch (Exception e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_INFO_LIST);
        }
    }

    /**
     * 根据订单号查询当前订单所在的审批页面
     * 
     * @param objs
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/getApproveHtml"}, method = RequestMethod.POST)
    public Object getApproveHtml(@RequestBody JSONObject objs) {
        try {
            logger.info("查询订单号:{}对应的审批页面", objs);
            // FINANCE-2681 兼容以租代购提前还款 objs.getString("orderId")
            Map<String, String> result = adminServiceImpl.getApproveHtml(objs);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            return returnFailtrueInfo(e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/persisit_imci"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_PERSISIT_IMCI)
    public Object persisitImci(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            String orderId = requestMessage.getString(KEY_ORDER_ID);
            carStatusCenterServiceImpl
                    .persisitImci(orderId);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_INFO_LIST);
        } catch (Exception e) {
            return generateFailResponseMessage(e, SERVICE_CODE_PERSISIT_IMCI);
        }
    }

    /**
     * 
     * Description: FINANCE-1439 车辆状态查询FINANCE-1474 拖回入库/GPS确认(后台接口) *回显*
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年7月10日 下午5:31:22
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/cget_info"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_INFO)
    public Object cgetInfo(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            CgetCarStatusInfoResponseMessage result = carStatusCenterServiceImpl
                    .cgetInfo(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_INFO);
        } catch (Exception e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_INFO);
        }
    }

    /**
     * 
     * Description: FINANCE-1439 车辆状态查询FINANCE-1474 拖回入库/GPS确认(后台接口) *更新*
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年7月10日 下午5:46:34
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/update_info"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_UPDATE_INFO)
    public Object updateInfo(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            carStatusCenterServiceImpl
                    .updateInfo(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_INFO);
        } catch (Exception e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_INFO);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_CAR_STATUS_CENTER;
    }

    /**
     * 
     * Description:车辆出售、车辆转租、车辆返还查询回显信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月10日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selOrderInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selOrderInfo(@RequestBody JSONObject requestMessage, HttpServletResponse response) {
        try {
            logger.debug("车辆跟踪-车辆出售获取订单信息开始 {}", requestMessage);
            ManageCarInfoBean bean = carStatusCenterServiceImpl.selectOrderInfoByOrderId(requestMessage);

            String dealerEvaluatesInfo = bean.getDealerEvaluatesInfo();
            String subleaseInfo = bean.getSubleaseInfo();
            String returnInfo = bean.getReturnInfo();
            if (dealerEvaluatesInfo != null) {
                bean.setDealerEvaluatesInfoList(dealerEvaluatesInfo);
            }
            if (subleaseInfo != null) {
                bean.setSubleaseInfoMap(subleaseInfo);
            }
            if (returnInfo != null) {
                bean.setReturnInfoMap(returnInfo);
            }
            Map<String, String> result = BeanUtil.entity2map(bean);
            logger.debug("车辆跟踪-车辆出售获取订单信息.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.debug("车辆跟踪-车辆出售获取订单信息开始结束 {}", JSONObject.toJSONString(super.returnSuccessInfo(linkedHashMap)));
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:车辆出库保存方法
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月10日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/saveAssessInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> saveAssessInfo(@RequestBody JSONObject requestMessage, HttpServletResponse response) {
        try {
            logger.debug("车辆跟踪-车辆出售保存评估金额 评估信息开始 {}", requestMessage);
            int result = carStatusCenterServiceImpl.updateManagerCarInfoByOrderId(requestMessage);
            logger.debug("车辆跟踪-车辆出售保存评估金额 评估信息.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.debug("车辆跟踪-车辆出售保存评估金额 评估信息结束 {}", JSONObject.toJSONString(super.returnSuccessInfo(linkedHashMap)));
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:车辆出售保存方法
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月10日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/saveSubleaseInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> saveSubleaseInfo(@RequestBody JSONObject requestMessage, HttpServletResponse response) {
        try {
            logger.debug("车辆跟踪-车辆转租开始 {}", requestMessage);
            int result = carStatusCenterServiceImpl.updateManagerCarInfoByOrderId(requestMessage);
            logger.debug("车辆跟踪-车辆转租保存.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.debug("车辆跟踪-车辆转租结束 {}", JSONObject.toJSONString(super.returnSuccessInfo(linkedHashMap)));
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:车辆返还保存方法
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月10日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/saveReturnInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> saveReturnInfo(@RequestBody JSONObject requestMessage, HttpServletResponse response) {
        try {
            logger.debug("车辆跟踪-车辆返还开始 {}", requestMessage);
            int result = carStatusCenterServiceImpl.updateManagerCarInfoByOrderId(requestMessage);
            logger.debug("车辆跟踪-车辆返还保存.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.debug("车辆跟踪-车辆返还结束 {}", JSONObject.toJSONString(super.returnSuccessInfo(linkedHashMap)));
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据订单号查询承租人回显信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月11日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/getSubleaseInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getSubleaseInfo(@RequestBody JSONObject requestMessage, HttpServletResponse response) {
        try {
            logger.debug("车辆跟踪-车辆转租-获取承租人信息开始 {}", requestMessage);
            ManageCarInfoBean bean = carStatusCenterServiceImpl.selectSubleaseInfoByOrderId(requestMessage);
            Map<String, String> result = BeanUtil.entity2map(bean);
            logger.debug("车辆跟踪-车辆转租-获取承租人信息.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            logger.debug("车辆跟踪-车辆转租-获取承租人信息 {}", JSONObject.toJSONString(super.returnSuccessInfo(linkedHashMap)));
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据订单号查询以及GPS状态回显信息 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 11:31:59
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/getOrderInfoAndGPSInfoByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getOrderInfoAndGPSInfoByOrderId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.debug("getOrderInfoAndGPSInfoByOrderId开始 {}", requestMessage);
            CgetCarStatusInfoResponseMessage bean =
                    carStatusCenterServiceImpl.getOrderInfoAndGPSInfoByOrderId(requestMessage);
            Map<String, String> result = BeanUtil.entity2map(bean);
            logger.debug("getOrderInfoAndGPSInfoByOrderId结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", bean);
            logger.debug("getOrderInfoAndGPSInfoByOrderId {}",
                    JSONObject.toJSONString(super.returnSuccessInfo(linkedHashMap)));
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:保存GPS状态到inst_gpsinfo表 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 14:47:18
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/saveGPSInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> saveGPSInfo(@RequestBody JSONObject requestMessage, HttpServletResponse response) {
        try {
            logger.debug("保存GPS状态到inst_gpsinfo表开始 {}", requestMessage);
            carStatusCenterServiceImpl.saveGPSInfo(requestMessage);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据orderID查询展示inst_gpsinfo表数据 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 15:10:18
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/selectGPSInfoListByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectGPSInfoListByOrderId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.debug("根据orderID查询展示inst_gpsinfo表数据 {}", requestMessage);
            PageInfo<GPSInfo> result = carStatusCenterServiceImpl.selectGPSInfoListByOrderId(requestMessage);
            logger.debug("根据orderID查询展示inst_gpsinfo表数据结果 {}", result);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:车辆状态跟踪模块列表与查询接口 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 17:02:31
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/selectCarToGPSList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectCarToGPSList(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            adminServiceImpl.getMerchantNos(requestMessage);
            logger.debug("车辆状态跟踪模块列表与查询接口参数 {}", requestMessage);
            PageInfo<CgetCarStatusInfoResponseMessage> result =
                    carStatusCenterServiceImpl.selectCarToGPSList(requestMessage);
            logger.debug("车辆状态跟踪模块列表与查询接口结果 {}", result);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:FINANCE-3031 车辆状态查询页面增加【导出】功能，导出列表页的所有信息；FINANCE-3057车辆状态查询页面增加【导出】功能，导出列表页的所有信息
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return chengzhen 2018年3月7日 14:51:27
     */
    @ResponseBody
    @RequestMapping(value = {"/export_cget_info_list"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object exportXlsxCgetinfolist(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject requestMessage = JSONObject.parseObject(json);
            adminServiceImpl.getMerchantNos(requestMessage);
            String result = carStatusCenterServiceImpl.exportXlsxCgetinfolist(requestMessage, response);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            return generateFailResponseMessage(e, SERVICE_CODE_GET_INFO_LIST);
        }
    }

    /**
     * 根据订单号获取应还金额 应还金额=剩余本金+违约金+罚息(总罚息-减免金额) Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年10月22日
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/getCurrentBalance"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getCurrentBalance(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("根据orderID根据订单号获取应还金额 {}", requestMessage);
            Map<String, BigDecimal> result = carStatusCenterServiceImpl.getCurrentBalance(requestMessage);
            logger.info("根据orderID根据订单号获取应还金额结果 {}", result);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:验证订单是否存在车秒贷订单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年10月22日
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/validateExistCmd"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> validateOrder(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("根据orderID根据订单号获取应还金额 {}", requestMessage);
            Map<String, String> result = carStatusCenterServiceImpl.validateOrder(requestMessage);
            logger.info("根据orderID根据订单号获取应还金额结果 {}", result);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
