package com.iqb.consumer.manage.front.business;

import java.util.LinkedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyOrderPojo;
import com.iqb.consumer.data.layer.biz.wf.SettleApplyBeanBiz;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.business.service.SettleApplyService;
import com.iqb.consumer.service.layer.penalty.derate.IPenaltyDerateService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.StringUtil;

@RestController
@RequestMapping("/settle")
public class SettleApplyController extends BasicService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int SERVICE_CODE_UPDATE_CUT_INFO = 1; // 保存罚息减免金额 和 原因
    @Resource
    private SettleApplyBeanBiz settleApplyBeanBiz;
    @Resource
    private SettleApplyService settleApplyService;
    @Autowired
    private AdminService adminServiceImpl;
    @Resource
    private IPenaltyDerateService iPenaltyDerateService;

    /**
     * 通过订单号查询提前结清相关数据
     * 
     * @param objs
     * @param request
     * @return
     */
    @RequestMapping(value = {"/getSettleBean"}, method = {RequestMethod.POST})
    public Object getSettleBean(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            String orderId = objs.getString("orderId");
            if (orderId.contains("-")) {
                orderId = orderId.substring(0, orderId.lastIndexOf("-"));
            }
            logger.info("通过订单号:{}查询提前结清相关信息", objs);
            SettleApplyBean sab = settleApplyService.getSettleBeanByOrderId(orderId);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", sab);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @RequestMapping(value = {"/updateCutInfo"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_UPDATE_CUT_INFO)
    public Object updateCutInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            SettleApplyBean sab = JSONObject.toJavaObject(objs, SettleApplyBean.class);
            if (sab.getOrderId() == null || sab.getCutOverdueAmt() == null
                    || StringUtil.isEmpty(sab.getCutOverdueRemark())) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A);
            }
            settleApplyBeanBiz.updateCutInfo(sab);
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_CUT_INFO);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_CUT_INFO);
        }
    }

    public static void main(String[] args) {
        System.out.println("20160927-593371-".lastIndexOf("-"));
    }

    /**
     * 通过ID号去完善信息
     * 
     * @param objs
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveOtherInfo", method = RequestMethod.POST)
    public Object saveOtherInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("通过ID:{}保存原因,备注等信息", objs);
            if (!StringUtil.isNull(objs.getString("payMethod")) && objs.getString("payMethod").equals("1")) {
                objs.put("amtStatus", "2");
            }
            SettleApplyBean settleApplyBean = JSONObject.toJavaObject(objs, SettleApplyBean.class);
            logger.info("----settleApplyBean--{}", JSON.toJSONString(settleApplyBean));
            String orderId = objs.getString("orderId");
            if (orderId.contains("-")) {
                if (orderId.indexOf("-") > 8) {
                    // 新订单号
                    String id = orderId.substring(orderId.indexOf("-") + 1, orderId.length());
                    orderId = orderId.substring(0, orderId.lastIndexOf("-"));
                    settleApplyBean.setOrderId(orderId);
                    settleApplyBean.setId(Long.parseLong(id));
                } else if (orderId.lastIndexOf("-") > 8) {
                    // 旧订单号且格式为20160927-593371-**
                    String id = orderId.substring(orderId.lastIndexOf("-") + 1, orderId.length());
                    orderId = orderId.substring(0, orderId.lastIndexOf("-"));
                    settleApplyBean.setOrderId(orderId);
                    settleApplyBean.setId(Long.parseLong(id));
                }
            }
            if (objs.getBigDecimal("finalOverdueAmt") == null) {
                settleApplyBean.setFinalOverdueAmt(settleApplyService.getSettleBeanByOrderId(orderId)
                        .getFinalOverdueAmt());
            }

            int result = settleApplyService.updateSettleBean(settleApplyBean);
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
     * Description:根据订单号获取提前结清信息并计算金额
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月25日
     */
    @RequestMapping(value = {"/selectSettleBean"}, method = {RequestMethod.POST})
    public Object selectSettleBean(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            String orderId = objs.getString("orderId");
            logger.info("通过订单号:{}根据订单号获取提前结清信息并计算金额", objs);
            SettleApplyBean sab = settleApplyService.selectSettleBean(orderId);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", sab);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:提前还款流程启动接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    @RequestMapping(value = {"/prepaymentStartWF"}, method = {RequestMethod.POST})
    public Object prepaymentStartWF(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            String orderId = objs.getString("orderId");
            logger.info("提前还款流程启动接口", objs);
            int result = settleApplyService.prepaymentStartWF(orderId);
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
     * Description:提前还款代偿分页查询接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月27日
     */
    @RequestMapping(value = {"/selectPrepaymentList"}, method = {RequestMethod.POST})
    public Object selectPrepaymentList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("提前还款代偿分页查询接口", objs);
            adminServiceImpl.getMerchantNos(objs);
            PageInfo<SettleApplyBean> pageInfo = settleApplyService.selectPrepaymentList(objs);
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
     * @author chengzhen 2017年12月26日 21:14:59
     */
    @RequestMapping(value = {"/selectSettleOrderList"}, method = {RequestMethod.POST})
    public Object selectSettleOrderList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("门店申请分页查询入参{}", objs);
            objs.put("merchNames", objs.get("merchantShortName"));
            adminServiceImpl.merchantNos(objs);
            PageInfo<SettleApplyOrderPojo> sab = settleApplyService.selectSettleOrderList(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", sab);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:提前申请流程校验订单在罚息减免流程中是否发起
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月29日
     */
    @RequestMapping(value = {"/validateIPenaltyDerate"}, method = {RequestMethod.POST})
    public Object validateIPenaltyDerate(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            String orderId = objs.getString("orderId");
            if (iPenaltyDerateService.getPenaltyDerateBeanByOrderId(orderId) != null) {
                throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000006);
            } else {
                LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
                linkedHashMap.put("result", "success");
                return super.returnSuccessInfo(linkedHashMap);
            }
        } catch (Exception e) {
            return super.returnFailtrueInfo(new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000006));
        }
    }

    /**
     * FINANCE-2681 以租代购提前还款页面调整 列表导出
     * 
     * @author chengzhen 2017年12月28日 2017年12月28日 15:42:05
     */
    @ResponseBody
    @RequestMapping(value = {"/selectSettleOrderListExle"}, method = {RequestMethod.POST, RequestMethod.GET})
    public void selectSettleOrderListExle(HttpServletRequest request,
            HttpServletResponse response) {

        try {
            request.setCharacterEncoding("UTF-8");
            JSONObject objs = new JSONObject();
            objs.put("merchNames", request.getParameter("merchantShortName"));
            objs.put("orderId", request.getParameter("orderId"));
            objs.put("realName", request.getParameter("realName"));
            objs.put("regId", request.getParameter("regId"));
            objs.put("beginTime", request.getParameter("beginTime"));
            objs.put("endTime", request.getParameter("endTime"));
            objs.put("settleStatus", request.getParameter("settleStatus"));
            adminServiceImpl.merchantNos(objs);
            settleApplyService.selectSettleOrderListExle(objs, response);
        } catch (Throwable e) {
            logger.error("analysisXlsx :", e);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_SETTLE_APPLY_CENTER;
    }
}
