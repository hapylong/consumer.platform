package com.iqb.consumer.manage.front.business;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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
import com.iqb.consumer.common.config.RBParamConfig;
import com.iqb.consumer.common.constant.CommonConstant;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.crm.customer.bean.IqbCustomerStoreInfoEntity;
import com.iqb.consumer.crm.store.service.ICustomerStoreService;
import com.iqb.consumer.data.layer.bean.authoritycard.AuthorityCardBean;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;
import com.iqb.consumer.data.layer.bean.order.InstOrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderBreakInfo;
import com.iqb.consumer.data.layer.bean.order.OrderManageResult;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskResultBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.RiskInfoBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.consumer.manage.front.workflow.domain.RiskCheckInfo;
import com.iqb.consumer.manage.front.workflow.domain.RiskOrderInfo;
import com.iqb.consumer.manage.front.workflow.domain.ToRiskCheckinfo;
import com.iqb.consumer.manage.front.workflow.domain.ToRiskCheckinfoForDandelion;
import com.iqb.consumer.manage.front.workflow.domain.ToRiskOrderinfo;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.api.AssetApiService;
import com.iqb.consumer.service.layer.authoritycard.AuthorityCardService;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.consumer.service.layer.business.service.IBankCardService;
import com.iqb.consumer.service.layer.business.service.IOrderService;
import com.iqb.consumer.service.layer.cheegu.CarAssessReportService;
import com.iqb.consumer.service.layer.pay.PayService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.ucf.sdk.UcfForOnline;

@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/business")
public class BusinessController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(BusinessController.class);
    /** 成功 **/
    public static final String SUCC = "success";
    /** 未成功 **/
    public static final String UNSUCC = "unsucc";

    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderBiz orderBiz;
    @Resource
    private BillInfoService billInfoService;
    @Autowired
    private UserBeanBiz userBeanBiz;
    @Autowired
    private ParamConfig paramConfig;
    @Autowired
    private RiskInfoBiz riskInfoBiz;
    @Resource
    private IBankCardService bankCardService;
    @Resource
    private AuthorityCardService authorityCardService;
    @Resource
    private AssetApiService assetApiService;
    @Autowired
    private ICustomerStoreService customerStoreServiceImpl;
    @Resource
    private RBParamConfig rbParamConfig;
    @Autowired
    private AdminService adminServiceImpl;
    @Autowired
    private PayService payService;
    @Autowired
    private CarAssessReportService carAssessReportService;

    @ResponseBody
    @RequestMapping(value = "/authInfo", method = RequestMethod.POST)
    public Object authInfo(@RequestBody JSONObject objs) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, Object> result = new HashMap<String, Object>();
        map.put("bizChannelCode", "zhongge");
        map.put("bankCardNum", objs.getString("bankCardNum"));
        map.put("userName", objs.getString("userName"));
        map.put("bankReservedPhoneNum", objs.getString("mobile"));
        map.put("idNum", objs.getString("idNo"));

        String httpStr = null;
        try {
            httpStr = SimpleHttpUtils.httpPost(paramConfig.getAuthInfoUrl(), map);
        } catch (Exception e) {
            logger.error("调用鉴权接口发生错误参数:{}", objs, e);
        }
        if (SUCC.equals(httpStr)) {
            result.put("result_code", "1");
            result.put("result_msg", "鉴权成功");
        } else {
            result.put("result_code", "2");
            result.put("result_msg", "鉴权失败");
        }
        return result;

    }

    @ResponseBody
    @RequestMapping(value = {"/exportOrderList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportOrderList(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.debug("开始导出订单报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
                // data.put(paraName, new String(para.trim().getBytes("ISO-8859-1"), "UTF-8"));

            }
            logger.debug("BusinessController[exportOrderList] data {}", data);
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            String result = orderService.exportOrderList(objs, response);
            logger.debug("导出订单报表数据完成.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/getOrderList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getOrderList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始分页查询订单数据...");
            adminServiceImpl.merchantNos(objs);
            Map map = new HashMap<String, String>();
            map = orderService.getOrderInfoByListTotal(objs);
            PageInfo<OrderBean> pageInfo = orderService.getOrderInfoByList(objs);
            logger.debug("IQB信息---分页查询订单数据完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("resultTotal", map);
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    public void updateRiskStatus(String orderId, String riskStatus) {
        orderBiz.updateStatus(orderId, riskStatus);
    }

    /**
     * 通过订单号和手机号查询账单详情
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getBillByOrderI"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getBillInfoByOrderInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        logger.info("通过订单号:{},手机号:{}查询账单", objs.getString("orderId"), objs.getString("regId"));
        List<Map<String, Object>> billMap =
                billInfoService.getAllBillInfo(objs.getString("orderId"), objs.getString("regId"));
        return billMap;
    }

    @ResponseBody
    @RequestMapping(value = {"/calDate"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object calDate(@RequestBody JSONObject objs) throws ParseException {
        logger.info("计算时间参数:{}", objs);
        int addMonth = 0;
        try {
            addMonth = Integer.parseInt(objs.getString("addMonth"));
        } catch (NumberFormatException e) {
            logger.error("时间计算参数非整数", e);
        }
        String startDate = objs.getString("startDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(startDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar afterMonth = DateUtil.afterOneMonth(calendar, addMonth);
        String returnDate = sdf.format(afterMonth.getTime());
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("result", returnDate);
        return super.returnSuccessInfo(linkedHashMap);
    }

    @ResponseBody
    @RequestMapping(value = {"/modifyOrder"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map modifyOrder(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始修改订单数据...");
            int result = orderService.updateOrderInfo(objs);
            logger.debug("IQB信息---修改订单数据完成.结果：{}", result);

            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateLoanInfo", method = RequestMethod.POST)
    public Map updateLoanInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("保存放款信息:{}", objs);
            OrderBean orderBean = new OrderBean();
            orderBean.setOrderId(objs.getString("orderId"));
            orderBean.setLoanAmt(objs.getBigDecimal("loanAmt"));
            orderBean.setLoanDate(objs.getDate("loanDate"));
            orderBiz.updateOrderInfo(orderBean);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/modifyOrder2"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map modifyOrder2(@RequestBody String obj, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始修改订单状态...");
            Map map = JSONObject.parseObject(obj);
            String orderId = (String) map.get("orderId");
            String wfStatus = (String) map.get("wfStatus");
            String preAmtStatus = (String) map.get("preAmtStatus");
            String specialTime = (String) map.get("specialTime");
            // String orderId = request.getParameter("orderId");
            String riskStatus = (String) map.get("riskStatus");
            int result = 0;
            if ((null != wfStatus) && (!"".equals(wfStatus))) {
                result = orderBiz.updatePledgeWfStatus(orderId, riskStatus, wfStatus, preAmtStatus, specialTime, null,
                        null, null);
                logger.debug("IQB信息---修改订单状态完成.结果：{}", result);
            } else {
                logger.debug("IQB信息---修改订单状态完成,保留原有状态.");
            }

            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/getOrderInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getOrderInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始查询订单详情数据...");
            OrderBean bean = orderService.selectOne(objs);
            logger.debug("IQB信息---查询订单详情数据完成.结果：{}", bean);
            logger.info("IQB信息---获取产品信息...");
            List<PlanBean> list = orderService.getPlanByMerNo(objs);
            logger.info("IQB信息---获取产品信息...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", bean);
            linkedHashMap.put("prodList", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 交易所需求:通过订单号查询订单详情,含计划列表
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getJysOrderInfo"}, method = {RequestMethod.POST})
    public Object getJysOrderInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("query single order info,query params:{}", objs);
            JYSOrderBean jysOrderInfo = orderService.getSingleOrderInfo(objs);

            /**
             * 2017-11-07 haojinlong FINANCE-2372 资产打包中回显债权人信息错误 资产打包债权人信息从交易所债权人信息表中获取
             * 
             */
            IqbCustomerStoreInfoEntity customerStoreInfo = customerStoreServiceImpl.getCreditInfoByOrderId(objs);

            if (jysOrderInfo != null) {
                if (jysOrderInfo.getCreditorId() == 0 && customerStoreInfo != null) {
                    jysOrderInfo.setCreditName(customerStoreInfo.getCreditorName());
                    jysOrderInfo.setCreditPhone(customerStoreInfo.getCreditorPhone());
                    jysOrderInfo.setCreditCardNo(customerStoreInfo.getCreditorIdNo());
                    jysOrderInfo.setCreditBank(customerStoreInfo.getCreditorBankName());
                    jysOrderInfo.setCreditBankCard(customerStoreInfo.getCreditorBankNo());
                }
            }

            objs.put("merchantNo", jysOrderInfo.getMerchantNo());
            objs.put("bizType", jysOrderInfo.getBizType());
            List<PlanBean> list = orderService.getPlanByMerAndBType(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", jysOrderInfo);
            linkedHashMap.put("icsi", customerStoreInfo);
            linkedHashMap.put("prodList", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/getPreOrderList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getPreOrderList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始分页查询订单数据...");
            PageInfo<OrderBean> pageInfo = orderService.getPreOrderInfoByList(objs);
            logger.debug("IQB信息---分页查询订单数据完成.");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/newModifyOrder"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map newModifyOrder(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始修改订单数据...");
            int result = orderService.newUpdateOrderInfo(objs);
            logger.debug("IQB信息---修改订单数据完成.结果：{}", result);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/send2Risk"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map send2Risk(@RequestBody String obj, HttpServletRequest request) {
        try {
            Map map = JSONObject.parseObject(obj);
            String orderId = (String) map.get("orderId");
            Map params = new HashMap();
            params.put("orderId", orderId);
            OrderBean orderBean = orderBiz.selectOne(params);
            logger.debug("开始发送给风控");
            String result = sendCheckOrderInfo2Risk(orderBean);
            logger.debug("发送风控完成.结果：{}", result);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("unchecked")
    private String sendCheckOrderInfo2Risk(OrderBean orderBean) {
        Map checkOrderMap = new HashMap();
        String regId = orderBean.getRegId();
        checkOrderMap.put("regId", regId);
        checkOrderMap.put("appType", "weixin");
        checkOrderMap.put("order_handel_flag", getRandomString(paramConfig.getEnvFlag()));
        checkOrderMap.put("website", "");
        checkOrderMap.put("traceNo", getTraceNo());
        checkOrderMap.put("version", "1.0.0");
        checkOrderMap.put("source", "huahuadata");// 來源
        UserBean userBean = userBeanBiz.selectOne(regId);
        // 设置订单信息
        ToRiskOrderinfo orderinfo = new ToRiskOrderinfo();
        if ("2001".equals(orderBean.getBizType())) {
            checkOrderMap.put("noticeUrl", paramConfig.getIdumai_riskcontrol_oldcar_notice_url()); // 回調地址
            orderinfo.setThetype("10106");// 爱享融（新车）
        } else if ("2002".equals(orderBean.getBizType())) {
            checkOrderMap.put("noticeUrl", paramConfig.getIdumai_riskcontrol_notice_url()); // 回調地址
            orderinfo.setThetype("10107");// 爱享融（二手车）
        }
        String riskType = "3";
        RiskInfoBean riskInfoBean = riskInfoBiz.getRiskInfoByRegId(regId, riskType);
        String ciJsonStr = riskInfoBean.getCheckInfo();
        ToRiskCheckinfo checkinfo = JSONObject.parseObject(ciJsonStr, ToRiskCheckinfo.class);
        // 通过用户查询查询最早的绑定的卡片
        BankCardBean bankCardBean = bankCardService.getOpenBankCardByRegId(userBean.getId() + "");
        checkinfo.setRealname(userBean.getRealName());
        checkinfo.setIdcard(userBean.getIdNo());
        checkinfo.setBankno(bankCardBean == null ? "" : bankCardBean.getBankCardNo());
        checkinfo.setAdddetails(checkinfo.getAddprovince());

        orderinfo.setOrderid(orderBean.getOrderId());
        orderinfo.setOrdeinfo(orderBean.getOrderName());
        orderinfo.setAmount(orderBean.getOrderAmt());
        orderinfo.setInstalmentterms(orderBean.getOrderItems());
        orderinfo.setInstalmentno("");

        orderinfo.setAttribute1(orderBean.getOrderName());
        orderinfo.setAttribute2(orderBean.getOrderName());
        orderinfo.setOrganization("iqb");
        // （10101质押车改为）10101爱质贷、
        // （10103美容改为）10103医美分期、
        // （10104二手车抵押改为）10104爱抵贷、
        // （新增）10105滴滴车分期、
        // （新增）10106爱享融（新车）
        // （新增）10107爱享融（二手车）
        // 爱享融就是以租代售
        JSONObject objs = new JSONObject();
        objs.put("orderId", orderBean.getOrderId());
        AuthorityCardBean ac = authorityCardService.selectOneByOrderId(objs);
        orderinfo.setPlate(ac.getPlate());// 车牌号
        orderinfo.setPlateType(ac.getPlateType());// 车型号"（"01":"大型汽车"，"02":"小型汽车"，"15":"挂车"）
        orderinfo.setVin(ac.getCarNo());// 车架号
        orderinfo.setEngine(ac.getEngine());// 发动机号
        RiskCheckInfo riskCheckInfo = (RiskCheckInfo) copyConvert(checkinfo);
        RiskOrderInfo riskOrderInfo = (RiskOrderInfo) copyConvert(orderinfo);
        checkOrderMap.put("checkInfo", riskCheckInfo);
        checkOrderMap.put("orderInfo", riskOrderInfo);
        String jsonString = JSON.toJSONString(checkOrderMap);
        logger.info("send risk info :{}", jsonString);

        RiskResultBean riskResultBean = new RiskResultBean();
        riskResultBean.setOrderId(orderBean.getOrderId());
        riskResultBean.setContent(jsonString);
        riskResultBean.setSendUrl(paramConfig.getIdumai_riskcontrol_checkorder_url());
        riskResultBean.setType("1");
        riskResultBean.setStatus(2);

        return riskInfoBiz.saveRiskResultInfo(riskResultBean).toString();
    }

    private String getTraceNo() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-");
        String traceNo = sdf.format(date) + (int) ((Math.random() * 9 + 1) * 100000);
        return traceNo;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/send2Risk4Pledge"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map send2Risk4Pledge(@RequestBody String obj, HttpServletRequest request) {
        try {
            Map map = JSONObject.parseObject(obj);
            String orderId = (String) map.get("orderId");
            logger.debug("获取到orderId:{}", orderId);
            Map params = new HashMap<>();
            params.put("orderId", orderId);
            OrderBean orderBean = orderBiz.selectOne(params);
            logger.debug("开始发送给风控");
            String result = sendCheckOrderInfo2Risk4Pledge(orderBean);
            logger.debug("发送风控完成.结果：{}", result);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("unchecked")
    private String sendCheckOrderInfo2Risk4Pledge(OrderBean orderBean) {
        Map checkOrderMap = new HashMap();
        String regId = orderBean.getRegId();
        checkOrderMap.put("regId", regId);
        checkOrderMap.put("appType", "weixin");
        checkOrderMap.put("order_handel_flag", getRandomString(paramConfig.getEnvFlag()));
        checkOrderMap.put("website", "");
        checkOrderMap.put("traceNo", getTraceNo());
        checkOrderMap.put("version", "1.0.0");
        checkOrderMap.put("source", "huahuadata");// 來源
        checkOrderMap.put("noticeUrl", paramConfig.getIdumai_riskcontrol_pledge_notice_url()); // 抵押车回調地址
        String merchType = "3";
        RiskInfoBean riskInfoBean = riskInfoBiz.getRiskInfoByRegId(regId, merchType);
        String ciJsonStr = riskInfoBean.getCheckInfo();
        ToRiskCheckinfo checkinfo = JSONObject.parseObject(ciJsonStr, ToRiskCheckinfo.class);

        UserBean userBean = userBeanBiz.selectOne(regId);
        BankCardBean bankCardBean = bankCardService.getOpenBankCardByRegId(userBean.getId() + "");
        checkinfo.setRealname(userBean.getRealName());
        checkinfo.setIdcard(userBean.getIdNo());
        // 通过用户查询查询最早的绑定的卡片
        checkinfo.setBankno(bankCardBean.getBankCardNo());
        checkinfo.setAdddetails(checkinfo.getAddprovince());

        // 设置订单信息
        ToRiskOrderinfo orderinfo = new ToRiskOrderinfo();
        orderinfo.setOrderid(orderBean.getOrderId());
        orderinfo.setOrdeinfo(orderBean.getOrderName());
        orderinfo.setAmount(orderBean.getOrderAmt());
        orderinfo.setInstalmentterms(orderBean.getOrderItems());
        orderinfo.setInstalmentno("");
        // orderinfo.setThetype("10115");// 抵押车
        orderinfo.setThetype("10104");// 10104爱抵贷
        orderinfo.setAttribute1(orderBean.getOrderName());
        orderinfo.setAttribute2(orderBean.getOrderName());
        orderinfo.setOrganization("iqb");
        // 风控接口调整联调
        // （10101质押车改为）10101爱质贷、
        // （10103美容改为）10103医美分期、
        // （10104二手车抵押改为）10104爱抵贷、
        // （新增）10105滴滴车分期、
        // （新增）10106爱享融（新车）
        // （新增）10107爱享融（二手车）
        JSONObject objs = new JSONObject();
        objs.put("orderId", orderBean.getOrderId());
        AuthorityCardBean ac = authorityCardService.selectOneByOrderId(objs);
        orderinfo.setPlate(ac.getPlate());// 车牌号
        orderinfo.setPlateType(ac.getPlateType());// 车型号"（"01":"大型汽车"，"02":"小型汽车"，"15":"挂车"）
        orderinfo.setVin(ac.getCarNo());// 车架号
        orderinfo.setEngine(ac.getEngine());// 发动机号
        RiskCheckInfo riskCheckInfo = (RiskCheckInfo) copyConvert(checkinfo);
        RiskOrderInfo riskOrderInfo = (RiskOrderInfo) copyConvert(orderinfo);
        checkOrderMap.put("checkInfo", riskCheckInfo);
        checkOrderMap.put("orderInfo", riskOrderInfo);
        String jsonString = JSON.toJSONString(checkOrderMap);
        logger.info("send risk info :{}", jsonString);

        RiskResultBean riskResultBean = new RiskResultBean();
        riskResultBean.setOrderId(orderBean.getOrderId());
        riskResultBean.setContent(jsonString);
        riskResultBean.setSendUrl(paramConfig.getIdumai_riskcontrol_checkorder_url());
        riskResultBean.setType("1");
        riskResultBean.setStatus(2);

        return riskInfoBiz.saveRiskResultInfo(riskResultBean).toString();
    }

    private Object copyConvert(Object obj) {
        if (obj instanceof ToRiskCheckinfo) {
            ToRiskCheckinfo checkInfo = (ToRiskCheckinfo) obj;
            RiskCheckInfo returnOjb = new RiskCheckInfo();
            returnOjb.setRealName(checkInfo.getRealname());
            returnOjb.setIdCard(checkInfo.getIdcard());
            returnOjb.setAddDetails(checkInfo.getAdddetails());
            returnOjb.setAddProvince(checkInfo.getAddprovince());
            returnOjb.setMarriedStatus(checkInfo.getMarriedstatus());
            returnOjb.setBankName(checkInfo.getBankname());
            returnOjb.setBankNo(checkInfo.getBankno());
            returnOjb.setPhone(checkInfo.getPhone());
            returnOjb.setContactName1(checkInfo.getContactname1());
            returnOjb.setContactName2(checkInfo.getContactname2());
            returnOjb.setContactPhone1(checkInfo.getContactphone1());
            returnOjb.setContactPhone2(checkInfo.getContactphone2());
            return returnOjb;
        } else if (obj instanceof ToRiskOrderinfo) {
            ToRiskOrderinfo orderInfo = (ToRiskOrderinfo) obj;
            RiskOrderInfo returnOjb = new RiskOrderInfo();
            returnOjb.setAmount(orderInfo.getAmount());
            returnOjb.setAttribute1(orderInfo.getAttribute1());
            returnOjb.setAttribute2(orderInfo.getAttribute2());
            returnOjb.setEngine(orderInfo.getEngine());
            returnOjb.setInstalmentNo(orderInfo.getInstalmentno());
            returnOjb.setInstalmentTerms(orderInfo.getInstalmentterms());
            returnOjb.setOrdeInfo(orderInfo.getOrdeinfo());
            returnOjb.setOrderId(orderInfo.getOrderid());
            returnOjb.setOrganization(orderInfo.getOrganization());
            returnOjb.setPlate(orderInfo.getPlate());
            returnOjb.setPlateType(orderInfo.getPlateType());
            returnOjb.setThetype(orderInfo.getThetype());
            returnOjb.setVin(orderInfo.getVin());
            return returnOjb;
        }
        return null;
    }

    /**
     * 权证资料管理查询订单方法
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getAuthorityOrderList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getAuthorityOrderList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始分页查询订单数据...");
            PageInfo<OrderBean> pageInfo = orderService.getAuthorityOrderList(objs);
            logger.debug("IQB信息---分页查询订单数据完成.");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", pageInfo);
            linkedHashMap.put("totalCount", pageInfo.getTotal());
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 交易所需求:订单列表分页
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/listJYSOrderInfo"}, method = {RequestMethod.POST})
    public Object listJYSOrderInfo(@RequestBody JSONObject objs) {
        try {
            logger.info("交易所需求:订单列表分页参数:{}", objs);
            PageInfo<JYSOrderBean> pageInfo = orderService.listJYSOrderInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", pageInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 交易所需求:删除订单
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/deleteJYSOrderInfo"}, method = {RequestMethod.POST})
    public Object deleteJYSOrderInfo(@RequestBody JSONObject objs) {
        try {
            logger.info("交易所需求:删除订单:{}", objs);
            orderService.deleteJYSOrderInfo(objs);
            return super.returnSuccessInfo(new LinkedHashMap<String, Object>());
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 交易所需求:修改订单
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/updateJYSOrderInfo"}, method = {RequestMethod.POST})
    public Object updateJYSOrderInfo(@RequestBody JSONObject objs) {
        try {
            logger.info("交易所需求:修改订单:{}", objs);
            orderService.updateJYSOrderInfo(objs);
            return super.returnSuccessInfo(new LinkedHashMap<String, Object>());
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 查询订单信息
     * 
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/selOrderInfo"}, method = {RequestMethod.POST})
    public Object selOrderInfo(@RequestBody JSONObject objs) {
        try {
            logger.info("交易所需求:修改订单:{}", objs);
            OrderBreakInfo orderBreakInfo = orderService.selOrderInfo(objs.getString("orderId"));
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", orderBreakInfo);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 查询订单信息
     * 
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/saveBrkOrderInfo"}, method = {RequestMethod.POST})
    public Object saveBrkOrderInfo(@RequestBody JSONObject objs) {
        try {
            logger.info("交易所需求:修改订单:{}", objs);
            OrderBreakInfo orderBreakInfo = JSONObject.toJavaObject(objs, OrderBreakInfo.class);

            InstOrderInfoEntity ioie = new InstOrderInfoEntity();
            ioie.setEmployeeName(objs.getString("employeeName"));
            ioie.setEmployeeID(objs.getString("employeeID"));
            ioie.setOrderId(objs.getString("orderId"));
            ioie.setOrderAmt(objs.getBigDecimal("orderAmt"));
            orderService.updateIOIE(ioie);

            orderService.saveOrderBreakInfo(orderBreakInfo);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", 1);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 蒲公英行风控
     * 
     * @param obj
     * @param request
     * @return Map
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/send2RiskForDandelion"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map send2RiskForDandelion(@RequestBody String obj, HttpServletRequest request) {
        try {
            Map map = JSONObject.parseObject(obj);
            String orderId = (String) map.get("orderId");
            Map params = new HashMap();
            params.put("orderId", orderId);
            OrderManageResult orderBean = (OrderManageResult) orderBiz.selectOne(params);
            logger.debug("开始发送给风控");
            String result = sendCheckOrderInfo2RiskForDandelion(orderBean);
            logger.debug("发送风控完成.结果：{}", result);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("unchecked")
    private String sendCheckOrderInfo2RiskForDandelion(OrderManageResult orderBean) {
        Map checkOrderMap = new HashMap();
        String regId = orderBean.getRegId();
        checkOrderMap.put("regId", regId);
        checkOrderMap.put("appType", "weixin");
        checkOrderMap.put("order_handel_flag", getRandomString(paramConfig.getEnvFlag()));
        checkOrderMap.put("website", "");
        checkOrderMap.put("traceNo", getTraceNo());
        checkOrderMap.put("version", "1.0.0");
        checkOrderMap.put("source", "huahuadata");// 來源
        UserBean userBean = userBeanBiz.selectOne(regId);
        // 设置订单信息
        ToRiskOrderinfo orderinfo = new ToRiskOrderinfo();
        if ("3001".equals(orderBean.getBizType())) {
            checkOrderMap.put("noticeUrl", paramConfig.getPgyh_riskcontrol_notice_url()); // 回調地址
            orderinfo.setThetype("10109");// 蒲公英行(现金贷)
        }
        String riskType = "4";
        RiskInfoBean riskInfoBean = riskInfoBiz.getRiskInfoByRegId(regId, riskType);
        String ciJsonStr = riskInfoBean.getCheckInfo();
        ToRiskCheckinfoForDandelion checkinfo = JSONObject.parseObject(ciJsonStr, ToRiskCheckinfoForDandelion.class);
        // 通过用户查询查询最早的绑定的卡片
        BankCardBean bankCardBean = bankCardService.getOpenBankCardByRegId(userBean.getId() + "");
        checkinfo.setRealName(userBean.getRealName());
        checkinfo.setIdCard(userBean.getIdNo());
        checkinfo.setBankCard(bankCardBean == null ? "" : bankCardBean.getBankCardNo());
        checkinfo.setServiceOrganization(orderBean.getMerchName());

        orderinfo.setOrderid(orderBean.getOrderId());
        orderinfo.setOrdeinfo(orderBean.getOrderName());
        orderinfo.setAmount(orderBean.getOrderAmt());
        orderinfo.setInstalmentterms(orderBean.getOrderItems());
        orderinfo.setInstalmentno("");

        orderinfo.setAttribute1(orderBean.getOrderName());
        orderinfo.setAttribute2(orderBean.getOrderName());
        orderinfo.setOrganization("iqb");
        // （10101质押车改为）10101爱质贷、
        // （10103美容改为）10103医美分期、
        // （10104二手车抵押改为）10104爱抵贷、
        // （新增）10105滴滴车分期、
        // （新增）10106爱享融（新车）
        // （新增）10107爱享融（二手车）
        // 爱享融就是以租代售
        /*
         * JSONObject objs = new JSONObject(); objs.put("orderId", orderBean.getOrderId());
         * AuthorityCardBean ac = authorityCardService.selectOneByOrderId(objs);
         * orderinfo.setPlate(ac.getPlate());// 车牌号 orderinfo.setPlateType(ac.getPlateType());//
         * 车型号"（"01":"大型汽车"，"02":"小型汽车"，"15":"挂车"） orderinfo.setVin(ac.getCarNo());// 车架号
         * orderinfo.setEngine(ac.getEngine());// 发动机号
         */RiskCheckInfo riskCheckInfo = (RiskCheckInfo) copyConvertForDandelion(checkinfo);
        RiskOrderInfo riskOrderInfo = (RiskOrderInfo) copyConvert(orderinfo);
        checkOrderMap.put("checkInfo", riskCheckInfo);
        checkOrderMap.put("orderInfo", riskOrderInfo);
        String jsonString = JSON.toJSONString(checkOrderMap);
        logger.info("send risk info :{}", jsonString);

        RiskResultBean riskResultBean = new RiskResultBean();
        riskResultBean.setOrderId(orderBean.getOrderId());
        riskResultBean.setContent(jsonString);
        riskResultBean.setSendUrl(paramConfig.getIdumai_riskcontrol_checkorder_url());
        riskResultBean.setType("1");
        riskResultBean.setStatus(2);

        return riskInfoBiz.saveRiskResultInfo(riskResultBean).toString();
    }

    /**
     * 蒲公英风控信息转换方法
     * 
     * @param obj
     * @return Object
     */
    private Object copyConvertForDandelion(Object obj) {
        if (obj instanceof ToRiskCheckinfoForDandelion) {
            ToRiskCheckinfoForDandelion checkInfo = (ToRiskCheckinfoForDandelion) obj;
            RiskCheckInfo returnOjb = new RiskCheckInfo();

            returnOjb.setRealName(checkInfo.getRealName());
            returnOjb.setIdCard(checkInfo.getIdCard());
            returnOjb.setAddDetails(checkInfo.getAddress());
            returnOjb.setAddProvince(checkInfo.getAddress());
            returnOjb.setMarriedStatus("");

            returnOjb.setBankName("");
            returnOjb.setBankNo(checkInfo.getBankCard());
            returnOjb.setJob("");
            returnOjb.setIncome("0");
            returnOjb.setIncomepic("");

            returnOjb.setOtherincome("0");
            returnOjb.setEducation("");
            returnOjb.setPhone(checkInfo.getRegId());
            returnOjb.setServerpwd("");
            returnOjb.setInsuranceid(checkInfo.getCreditNo());

            returnOjb.setInsurancepwd(checkInfo.getCreditPasswd());
            returnOjb.setFundid("");
            returnOjb.setFundpwd("");

            returnOjb.setContactrelation1(checkInfo.getRelation1());
            returnOjb.setContactName1("");
            returnOjb.setContactPhone1(checkInfo.getPhone1());
            returnOjb.setContactSex1(checkInfo.getSex1());

            returnOjb.setContactrelation2(checkInfo.getRelation2());
            returnOjb.setContactName2("");
            returnOjb.setContactPhone2(checkInfo.getPhone2());
            returnOjb.setContactSex2(checkInfo.getSex2());

            returnOjb.setContactrelation3(checkInfo.getRelation3());
            returnOjb.setContactName3("");
            returnOjb.setContactPhone3(checkInfo.getPhone3());
            returnOjb.setContactSex3(checkInfo.getSex3());

            returnOjb.setCompany(checkInfo.getCompanyName());
            returnOjb.setCompanyAddress(checkInfo.getCompanyAddress());
            returnOjb.setCompanyPhone(checkInfo.getCompanyPhone());

            returnOjb.setColleagueName1(checkInfo.getColleagues1());
            returnOjb.setColleaguePhone1("");
            returnOjb.setColleagueName2(checkInfo.getColleagues2());
            returnOjb.setColleaguePhone2("");
            returnOjb.setServiceOrganization(checkInfo.getServiceOrganization());

            return returnOjb;
        }
        return null;
    }

    /**
     * 通过订单号查询账单详情
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getAllBillByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getAllBillInfoByOrderInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        logger.info("通过订单号:{} 查询账单", objs.getString("orderId"));
        List<Map<String, Object>> billMap =
                billInfoService.getAllBillInfo(objs.getString("orderId"));
        return billMap;
    }

    /**
     * Description:银行卡解绑
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/unBindBankCard"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object unBindBankCard(@RequestBody JSONObject objs, HttpServletRequest request) {
        logger.info("调用先锋接口进行银行卡解绑--开始:{} ", objs);

        Map<String, String> result = new HashMap<>();// 返回结果
        String userId = objs.getString("userId") == null ? objs.getString("regId") : objs.getString("userId");
        // 查询绑卡信息
        Map<String, Object> bindObj = (Map<String, Object>) getBindBankCard(objs, request);
        List<JSONObject> objList = (List<JSONObject>) bindObj.get("bankList");
        String bankCardNo = null;
        if (objList.size() > 0) {
            bankCardNo = objList.get(0).getString("bankCardNo");
        } else {
            result.put("status", "00");// 00 成功
            result.put("respMsg", "当前用户未绑定卡信息");
            return result;
        }
        PayConfBean payConfBean =
                payService.getPayChannelBy(objs.getString("payOwnerId"), objs.getString("merchantNo"));
        Map<String, String> reqMap = new HashMap<>();// 使用参数
        reqMap.put("service", "MOBILE_CERTPAY_API_UNBIND_CARD");
        reqMap.put("secId", "RSA");
        reqMap.put("version", "3.0.0");
        UUID uuid = UUID.randomUUID();
        reqMap.put("reqSn", uuid.toString().replaceAll("-", "").toUpperCase());// token
        reqMap.put("merchantId", payConfBean.getMerchantId());// 商户号
        reqMap.put("userId", userId);// 用户号
        reqMap.put("bankCardNo", bankCardNo);// 银行账号----------------
        String sign = null;
        try {
            sign = UcfForOnline.createSign(payConfBean.getKey(), "sign",
                    reqMap, "RSA");
        } catch (Exception e) {}
        reqMap.put("sign", sign);
        // get 请求:
        logger.info("调用解绑参数:{}", reqMap);
        String unResult = SimpleHttpUtils.httpGet(payConfBean.getGateWay(), reqMap);
        JSONObject rs = JSON.parseObject(unResult);
        result.put("status", rs.getString("status"));// 00 成功
        result.put("respMsg", rs.getString("respMsg"));
        logger.info("调用先锋接口进行银行卡解绑--结束:{} ", result);
        return result;
    }

    /**
     * 
     * Description:根据手机号码获取绑定的银行卡
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getBindBankCard"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getBindBankCard(@RequestBody JSONObject objs, HttpServletRequest request) {
        logger.info("调用先锋接口根据手机号查询绑定的银行卡---开始:{} ", objs);

        String userId = objs.getString("userId") == null ? objs.getString("regId") : objs.getString("userId");
        PayConfBean payConfBean =
                payService.getPayChannelBy(objs.getString("payOwnerId"), objs.getString("merchantNo"));
        Map<String, Object> result = new HashMap<>();// 返回结果
        Map<String, String> reqMap = new HashMap<>();// 使用参数
        reqMap.put("service", "MOBILE_CERTPAY_GETBINDCARDS");
        reqMap.put("secId", "RSA");
        reqMap.put("version", "3.0.0");
        UUID uuid = UUID.randomUUID();
        reqMap.put("reqSn", uuid.toString().replaceAll("-", "").toUpperCase());// token
        reqMap.put("merchantId", payConfBean.getMerchantId());// 商户号
        reqMap.put("userId", userId);// 用户号
        String sign = null;
        try {
            sign = UcfForOnline.createSign(payConfBean.getKey(), "sign",
                    reqMap, "RSA");
        } catch (Exception e) {}
        reqMap.put("sign", sign);
        // get 请求:
        String unResult = SimpleHttpUtils.httpGet(payConfBean.getGateWay(), reqMap);
        JSONObject rs = JSON.parseObject(unResult);
        if (rs.get("status") != null && rs.get("status").equals("00")) {
            JSONArray bankList = rs.getJSONArray("bankList");
            result.put("bankList", bankList);
        } else {
            result.put("bankList", new ArrayList<>());
        }
        result.put("status", rs.getString("status"));// 00 成功
        result.put("respMsg", rs.getString("respMsg"));
        logger.info("调用先锋接口根据手机号查询绑定的银行卡---结束:{} ", result);
        return result;
    }

    /**
     * 
     * Description:自动切换解绑银行卡 根据用户使用的卡，如果不是支付公司正在使用的卡，需解绑激活卡后再使用新卡
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/autoSwitchUnBindBankCard"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object autoSwitchUnBindBankCard(@RequestBody JSONObject objs, HttpServletRequest request) {
        logger.info("自动切换解绑银行卡---开始:{} ", objs);
        Map<String, Object> result = new HashMap<>();// 返回结果

        Map<String, String> params = (Map<String, String>) JSONObject.parse(objs.toJSONString());
        result = assetApiService.autoSwitchUnBindBankCard(params);
        logger.info("自动切换解绑银行卡---结束:{} ", result);
        return result;
    }

    /**
     * 
     * Description:读脉风控生成随机结果 pro test_s test_f
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    private static String getRandomString(String flag) {
        String base = CommonConstant.OrderHandelFlag.base;
        String orderFlag = CommonConstant.OrderHandelFlag.orderFlag;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        if (flag.equals(CommonConstant.OrderHandelFlag.pro)) {
            sb.append(CommonConstant.OrderHandelFlag.pro);
        } else {
            for (int i = 0; i < 1; i++) {
                int number = random.nextInt(orderFlag.length());
                sb.append(base);
                sb.append(orderFlag.charAt(number));
            }
        }

        return sb.toString();
    }

    /**
     * 
     * Description:获取放款订单列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月23日
     */
    @ResponseBody
    @RequestMapping(value = {"/getLoanOrderList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getLoanOrderList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始分页查询放款订单数据...");
            adminServiceImpl.merchantNos(objs);
            PageInfo<InstOrderBean> pageInfo = orderService.getLoanOrderList(objs);
            Map map = new HashMap<String, String>();
            map = orderService.getLoanOrderListTotal(objs);
            logger.debug("IQB信息---分页查询放款订单数据完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", pageInfo);
            linkedHashMap.put("resultTotal", map);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 根据订单号修改放款日期 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月23日
     */
    @ResponseBody
    @RequestMapping(value = {"/updateLoanDateByOrderIds"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map updateLoanDateByOrderIds(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始根据订单号修改放款日期...");
            long result = orderService.updateLoanDateByOrderIds(objs);
            logger.debug("IQB信息---根据订单号修改放款日期完成.");

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
     * Description:放款订单订单导出
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年10月23日
     */
    @ResponseBody
    @RequestMapping(value = {"/exportLoanOrderList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportLoanOrderList(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.debug("开始导出放款订单报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            logger.debug("BusinessController[exportLoanOrderList] data {}", data);
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            adminServiceImpl.merchantNos(objs);
            String result = orderService.exportLoanOrderList(objs, response);
            logger.debug("导出导出放款订单报表数据完成.结果：{}", result);
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
     * Description:新增放款查询界面
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/getSelectLoanOrderList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getSelectLoanOrderList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始分页放款查询界面...");
            adminServiceImpl.merchantNos(objs);
            PageInfo<InstOrderBean> pageInfo = orderService.getSelectLoanOrderList(objs);
            logger.debug("IQB信息---开始分页放款查询界面完成.");
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
     * Description:新增放款查询界面导出
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月10日 20:23:08
     */
    @ResponseBody
    @RequestMapping(value = {"/exportSelectLoanOrderList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportSelectLoanOrderList(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.debug("开始导出放款订单报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            logger.debug("BusinessController[exportLoanOrderList] data {}", data);
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            // String result = orderService.exportLoanOrderList(objs, response);
            adminServiceImpl.merchantNos(objs);
            String result1 = orderService.exportSelectLoanOrderList(objs, response);

            logger.debug("导出导出放款订单报表数据完成.结果：{}", result1);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result1);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:新以租代购流程新界面接口
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月13日 10:35:05
     */
    @ResponseBody
    @RequestMapping(value = {"/getOrderInfoNew"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getOrderInfoNew(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---以租代购新流程回显页面开始...");
            InstOrderBean instOrderBean = orderService.getOrderInfoNew(objs);
            logger.debug("IQB信息---以租代购新流程回显页面完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", instOrderBean);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:新以租代购流程新保存审批意见
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月16日 14:25:59
     */
    @ResponseBody
    @RequestMapping(value = {"/saveApprovalOpinion"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map saveApprovalOpinion(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---以租代购新流程保存审批意见开始...");
            orderService.saveApprovalOpinion(objs);
            logger.debug("IQB信息---以租代购新流程保存审批意见完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", "");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:新以租代购流程新回显审批意见
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月16日 14:44:23
     */
    @ResponseBody
    @RequestMapping(value = {"/getApprovalOpinion"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getApprovalOpinion(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---以租代购新流程回显审批意见开始...");
            Map<String, String> map = orderService.getApprovalOpinion(objs);
            logger.debug("IQB信息---以租代购新流程回显审批意见完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", map);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据车架号获取车辆评估报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月25日
     */
    @ResponseBody
    @RequestMapping(value = {"/getCarAssessReport"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getCarAssessReport(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---根据车架号获取车辆评估报告...{}", objs);
            Map<String, Object> map =
                    carAssessReportService.getCarAssessReport(objs.getString("orderId"), objs.getString("vin"));
            logger.info("IQB信息---根据车架号获取车辆评估报告完成.{}", map);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", map);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
