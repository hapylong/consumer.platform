package com.iqb.consumer.manage.front.afterLoan;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
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
import com.iqb.consumer.asset.allocation.assetinfo.service.IAssetInfoService;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.constant.CommonConstant;
import com.iqb.consumer.common.exception.OverDueReturnInfo;
import com.iqb.consumer.common.utils.HttpsClientUtil;
import com.iqb.consumer.common.utils.RedisUtils;
import com.iqb.consumer.data.layer.bean.afterLoan.AfterLoanBean;
import com.iqb.consumer.data.layer.bean.afterLoan.AfterLoanCustomerInfoBean;
import com.iqb.consumer.data.layer.bean.afterLoan.BreachOfContractBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstGpsInfo;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderCaseExecuteBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderLawBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderLawResultBean;
import com.iqb.consumer.data.layer.bean.afterLoan.InstReceivedPaymentBean;
import com.iqb.consumer.data.layer.bean.afterLoan.OutSourceOrderBean;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.riskinfo.LocalRiskInfoBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.RiskInfoBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.afterLoan.IAfterLoanService;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.consumer.service.layer.business.service.IBankCardService;
import com.iqb.consumer.service.layer.domain.ToRiskCheckinfo;
import com.iqb.consumer.service.layer.domain.ToRiskCheckinfo2;
import com.iqb.consumer.service.layer.riskinfo.RiskInfoService;
import com.iqb.consumer.service.layer.util.DesConstant;
import com.iqb.consumer.service.layer.util.DesUtil;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.SysUserSession;

/**
 * 
 * Description: 贷后模块
 * 
 * @author chengzhen
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2018年3月8日 15:15:01    chengzhen       1.0        1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/afterLoan")
public class AfterLoanController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(AfterLoanController.class);

    @Resource
    private IAfterLoanService afterLoanServiceImpl;
    @Resource
    private AdminService adminServiceImpl;
    @Resource
    private BillInfoService billInfoService;
    @Resource
    private SysUserSession sysUserSession;
    @Autowired
    private IAssetInfoService assetInfoServiceImpl;
    @Resource
    private YMForeignConfig ymForeignConfig;
    @Autowired
    private OrderBiz orderBiz;

    @Autowired
    private UserBeanBiz userBeanBiz;
    @Resource
    private ConsumerConfig consumerConfig;
    @Autowired
    private RiskInfoBiz riskInfoBiz;
    @Resource
    private IBankCardService bankCardService;
    @Autowired
    private RiskInfoService riskInfoService;

    /**
     * 
     * Description: 贷后客户跟踪列表
     * 
     * @param
     * @return Object
     * @throws
     * @Author chengzhen Create Date: 2018年3月8日 15:16:24
     */
    @ResponseBody
    @RequestMapping(value = "/afterLoanList", method = RequestMethod.POST)
    public Object afterLoanList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---开始贷后客户跟踪列表..." + objs.toJSONString());
            if (objs.entrySet().size() == 2) {// 点击菜单 列表显示为空,前端只传来两个分页参数
                LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
                linkedHashMap.put("result", null);
                return super.returnSuccessInfo(linkedHashMap);
            }
            adminServiceImpl.getMerchantNos(objs);
            PageInfo<AfterLoanBean> pushRecordByOrderIdList = afterLoanServiceImpl.afterLoanList(objs);
            logger.info("IQB信息---获取贷后客户跟踪列表结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", pushRecordByOrderIdList);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 通过订单号查询已逾期和当期待还的账单
     * 
     * @param objs
     * @param request chengzhen 2018年3月8日 17:10:49
     */
    @SuppressWarnings("deprecation")
    @ResponseBody
    @RequestMapping(value = {"/getAllBillByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getAllBillInfoByOrderInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        logger.info("通过订单号:{} 查询账单", objs.getString("orderId"));
        List<Map<String, Object>> billMap =
                billInfoService.getAllBillInfo(objs.getString("orderId"));
        Map<String, Object> mapNo1 = billMap.get(0);
        Iterator<Map<String, Object>> it = billMap.iterator();
        long time = System.currentTimeMillis();
        Date day = new Date(time);// 当前系统时间
        day.setMonth(day.getMonth() + 1);
        day.setHours(0);
        day.setMinutes(0);
        day.setSeconds(0);
        long time2 = day.getTime();
        String string = Long.toString(time2);
        String substring = string.substring(0, 8);
        String strNew = substring + "00000";
        long parseLong2 = Long.parseLong(strNew);
        while (it.hasNext()) {
            Map<String, Object> x = it.next();
            x.get("lastRepayDate").toString();
            // 账单月份
            long parseLong = Long.parseLong(x.get("lastRepayDate").toString());
            Date date2 = new Date(parseLong);
            date2.setHours(0);
            date2.setMinutes(0);
            date2.setSeconds(0);
            if (date2.getTime() >= parseLong2) {
                it.remove();
            }
        }
        if (billMap.size() == 0) {
            billMap.add(mapNo1);
        }
        return billMap;
    }

    /**
     * 保存 贷后客户信息补充
     * 
     * @param objs
     * @param request chengzhen 2018年3月9日 11:10:10
     */
    @ResponseBody
    @RequestMapping(value = {"/saveAfterLoanCustomer"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object saveAfterLoanCustomer(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("保存贷后客户信息补充到inst_customer_info表开始 {}", objs);
            Map<String, Object> params = new HashMap<>();
            params.put("regId", sysUserSession.getUserPhone());
            params.put("userCode", sysUserSession.getUserCode());
            UserBean userBean = assetInfoServiceImpl.getSysUserByRegId(params);
            objs.put("optName", userBean.getRealName());
            afterLoanServiceImpl.saveAfterLoanCustomer(objs);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: FINANCE-3043 新增贷后客户跟踪及查询页面；FINANCE-3071客户信息补充分页展示接口
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月9日 11:58:05
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/selectCustomerInfoListByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectCustomerInfoListByOrderId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("根据orderID查询展示inst_customer_info表数据 {}", requestMessage);
            PageInfo<AfterLoanCustomerInfoBean> result =
                    afterLoanServiceImpl.selectCustomerInfoListByOrderId(requestMessage);
            logger.info("根据orderID查询展示inst_customer_info表数据结果 {}", result);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:FINANCE-3048 新增资产赎回页面；FINANCE-30824.5分配详情页 字典列表
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return chengzhen
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/selectPayMoneyListByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectPayMoneyListByOrderId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("根据orderID查询展示inst_customer_info表数据 {}", requestMessage);
            List<Map<String, String>> result =
                    afterLoanServiceImpl.selectPayMoneyListByOrderId(requestMessage);
            Map<Integer, Integer> result2 =
                    afterLoanServiceImpl.selectNumberByOrderId(requestMessage);// 根据订单号查询剩余期数
            logger.info("根据orderID查询展示inst_customer_info表数据结果 {}", result);
            List list = new ArrayList<>();
            for (Map<String, String> map : result) {
                list.add(map.get("id"));
            }
            Integer string = result2.get("month");
            int newInt = string + 1;
            if (result2.get("day") == 0) {// 为0
                Iterator<Map<String, String>> it = result.iterator();
                while (it.hasNext()) {
                    Map<String, String> x = it.next();
                    if (Integer.parseInt(x.get("id")) > result2.get("month")) {
                        it.remove();
                    }
                }
            } else {// 不为0
                Iterator<Map<String, String>> it = result.iterator();
                while (it.hasNext()) {
                    Map<String, String> x = it.next();
                    if (Integer.parseInt(x.get("id")) > Integer.parseInt(Integer.toString(newInt))) {
                        it.remove();
                    }
                }

            }
            if (result2.get("day") != 0) {
                if (!list.contains(Integer.toString(newInt))) {
                    Map m = new HashMap<>();
                    m.put("id", Integer.toString(newInt));
                    m.put("text", Integer.toString(newInt));
                    result.add(m);
                }
            }

            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:FINANCE-3106 新增客户违约保证金结算查询页面；FINANCE-3135客户违约状态记录分页查询
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月16日 15:47:11
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/selectBreachContractList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectBreachContractList(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("客户违约保证金结算查询页面参数 {}", requestMessage);
            adminServiceImpl.getMerchantNos(requestMessage);
            PageInfo<BreachOfContractBean> result =
                    afterLoanServiceImpl.selectBreachContractList(requestMessage);
            logger.info("客户违约保证金结算查询页面结果 {}", result);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: FINANCE-3106 新增客户违约保证金结算查询页面；FINANCE-3136批量违约标记接口
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月19日 10:47:17
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/batchListToMark"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> batchListToMark(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("批量违约标记参数 {}", requestMessage);
            Redisson redisson =
                    RedisUtils.getInstance()
                            .getRedisson(ymForeignConfig.getRedisHost(), ymForeignConfig.getRedisPort());
            // 发起违约流程只能单线程进行操作,防止同时间多用户操作同一笔订单
            RLock rLock = null;
            try {
                rLock = RedisUtils.getInstance().getRLock(redisson, "OverdueInfobatchListToMarkRedis");
                if (rLock.tryLock(15, 15, TimeUnit.SECONDS)) { // 第一个参数代表等待时间，第二是代表超过时间释放锁，第三个代表设置的时间制
                    int batchListToMark = afterLoanServiceImpl.batchListToMark(requestMessage);
                    if (batchListToMark == 0) {
                        LinkedHashMap linkedHashMap = new LinkedHashMap();
                        linkedHashMap.put("result", "数据已被标记请刷新界面,重新标记");
                        linkedHashMap.put("success", "110");
                        return linkedHashMap;
                    }
                } else {
                    return super.returnFailtrueInfo(new IqbException(OverDueReturnInfo.OVERDUE_DOUBLE));
                }
            } catch (Exception e) {
                return super.returnFailtrueInfo(e);
            } finally {
                rLock.unlock();
            }
            try {
                if (redisson != null) {
                    RedisUtils.getInstance().closeRedisson(redisson);
                }
            } catch (Exception e) {
                logger.error("---标记违约客户出现异常---{}", e);
            }
            logger.info("批量违约标记完成");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:
     * 
     * @author chengzhen FINANCE-2963 以租代购：门店预处理通过后，在风控审批和风控部门审批节点显示风控报告FINANCE-3186
     *         以租代购：门店预处理通过后，在风控审批和风控部门审批节点显示风控报告
     * @param objs
     * @param request
     * @return 2018年3月28日 10:25:27
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/getReportList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getReportList(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        LinkedHashMap<Object, Object> linkMap = new LinkedHashMap<>();
        try {
            logger.info("审批界面如何显示报告接口 入参{}", requestMessage);
            List<LocalRiskInfoBean> localBeanList = afterLoanServiceImpl.getReportList(requestMessage);
            OrderBean orderBean = orderBiz.selByOrderId(requestMessage.getString("orderId"));
            requestMessage.put("regId", orderBean.getRegId());
            requestMessage.put("bizType", orderBean.getBizType());
            List<LocalRiskInfoBean> localRiskInfoBeanList = afterLoanServiceImpl.getReportListByRegId(requestMessage);

            if (CollectionUtils.isEmpty(localBeanList) && !CollectionUtils.isEmpty(localRiskInfoBeanList)) {
                localBeanList = localRiskInfoBeanList;
            }
            logger.info("审批界面如何显示报告接口 出参{}", localBeanList);
            if (!CollectionUtils.isEmpty(localBeanList)) {
                // 1.如果发送失败，库中此时会有一条数据，此时把风控的错误信息返回给前端
                if (localBeanList.size() == 1 && "1".equals(localBeanList.get(0).getErrCode())) {
                    linkMap.put("result", localBeanList.get(0).getErrMsg());
                    linkMap.put("success", "110");
                    return linkMap;
                } else if (localBeanList.size() >= 2) {
                    List list = new ArrayList<>();
                    for (LocalRiskInfoBean localRiskInfoBean : localBeanList) {
                        HashMap<Object, Object> map = new HashMap<>();
                        map.put("reportNo", localRiskInfoBean.getReportNo());
                        map.put("reportName", localRiskInfoBean.getReportName());
                        map.put("reportType", localRiskInfoBean.getReportType());
                        map.put("orderId", localRiskInfoBean.getOrderId());
                        list.add(map);
                        linkMap.put("result", list);
                    }
                    return super.returnSuccessInfo(linkMap);
                }
            }
            linkMap.put("result", "没有生成风控报告");
            linkMap.put("success", "100");
            return super.returnSuccessInfo(linkMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:获取风控报告
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月21日
     */
    @SuppressWarnings({"rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/getReportByReprotNo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getReportByReprotNo(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("审批界面根据reportNo查报告内容 入参{}", requestMessage);
            LocalRiskInfoBean localBean = afterLoanServiceImpl.getReportByReprotNo(requestMessage);
            OrderBean orderBean = orderBiz.selectOne(requestMessage);
            if (localBean == null) {
                requestMessage.put("regId", orderBean.getRegId());
                localBean = afterLoanServiceImpl.getReportByRegId(requestMessage);
            }
            logger.info("审批界面如何显示报告接口 出参{}", JSONObject.toJSONString(localBean));

            UserBean userBean = userBeanBiz.selectOne(orderBean.getRegId());
            String reportContent = localBean.getReportContent();
            Object object = com.alibaba.fastjson.JSON.parseObject(reportContent);
            JSONObject obj = new JSONObject();
            obj.put("result", object);
            obj.put("idNo", userBean.getIdNo());
            obj.put("name", userBean.getRealName());
            obj.put("phone", userBean.getRegId());
            obj.put("reportNo", localBean.getReportNo());
            BankCardBean bankCardBean = bankCardService.getOpenBankCardByRegId(userBean.getId() + "");
            obj.put("bankNum", bankCardBean.getBankCardNo());
            obj.put("bankName", bankCardBean.getBankName());
            if ("1".equals(requestMessage.get("reportType").toString())) {
                obj.put("updateTime", localBean.getContentCreateTime1());
            } else {
                obj.put("updateTime", localBean.getContentCreateTime2());
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String format = df.format(localBean.getCreateTime());
            obj.put("createTime", format);// 查询时间

            String riskType = "3";
            if ("3001".equals(orderBean.getBizType()) || "4001".equals(orderBean.getBizType())) {
                riskType = "4";
            }

            RiskInfoBean riskInfoBean = riskInfoBiz.getRiskInfoByRegId(userBean.getRegId(), riskType);
            String ciJsonStr = riskInfoBean.getCheckInfo();
            ToRiskCheckinfo checkinfo = new ToRiskCheckinfo();
            ToRiskCheckinfo2 checkinfo2 = new ToRiskCheckinfo2();
            if ("3".equals(riskType)) {
                checkinfo = JSONObject.parseObject(ciJsonStr, ToRiskCheckinfo.class);
                if (!StringUtils.isEmpty(checkinfo.getMarriedstatus())) {
                    if (checkinfo.getMarriedstatus().equals("未婚")) {
                        obj.put("married", "1");// 婚姻状态
                    } else {
                        obj.put("married", "2");// 婚姻状态
                    }
                }
            } else {
                checkinfo2 = JSONObject.parseObject(ciJsonStr, ToRiskCheckinfo2.class);
            }
            if ("3".equals(riskType)) {
                obj.put("nativePlace", checkinfo.getAddprovince());
                obj.put("contact1Name", checkinfo.getContactname1());// 第一联系人名称
                obj.put("contact1IdNumber", "");// 第一联系人身份证
                obj.put("contact1Mobile", checkinfo.getContactphone1());// 第一联系人手机号
                obj.put("contact1Relation", checkinfo.getContactrelation1());// 第一联系人社会关系
            } else {
                obj.put("nativePlace", checkinfo2.getAddress());
                obj.put("contact1Name", checkinfo2.getRName1());// 第一联系人名称
                obj.put("contact1IdNumber", "");// 第一联系人身份证
                obj.put("contact1Mobile", checkinfo2.getTel1());// 第一联系人手机号
                obj.put("contact1Relation", checkinfo2.getRelation1());// 第一联系人社会关系
            }
            JSONObject obj1 = new JSONObject();
            obj1.put("pojo", obj);
            return obj1;
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/getReportStateByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getReportStateByOrderId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("点击审批需要判断该订单是否已经生成合同入参数 {}", requestMessage);
            OrderBean orderBean = orderBiz.selByOrderId(requestMessage.getString("orderId"));
            requestMessage.put("bizType", orderBean.getBizType());
            requestMessage.put("regId", orderBean.getRegId());
            List<LocalRiskInfoBean> localBeanList = afterLoanServiceImpl.getReportStateByOrderId(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            if (localBeanList.size() == 0) {
                linkedHashMap.put("success", "110");
                linkedHashMap.put("result", "报告未生成不可以审核通过,稍等再试");
                return linkedHashMap;
            }
            for (LocalRiskInfoBean localRiskInfoBean : localBeanList) {

                if ("1".equals(localRiskInfoBean.getErrCode())) {
                    linkedHashMap.put("success",
                            "120");
                    linkedHashMap.put("result", "报告发送失败,不能审核通过,请联系管理员");
                    return linkedHashMap;
                }
                if ("1".equals(localRiskInfoBean.getReportState())) {// 两个合同只要有一个未生成 不可以点审核
                    linkedHashMap.put("success", "110");
                    linkedHashMap.put("result", "报告未生成不可以审核通过,稍等再试");
                    return linkedHashMap;
                }
            }
            linkedHashMap.put("success", "0");
            linkedHashMap.put("result", "可以审核");
            return linkedHashMap;
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 获取风控报告-需校验与第一次获取报告时间是否超过7天
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/getReportByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getReportByOrderId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("点击审批需要判断该订单是否已经生成合同入参数 {}", requestMessage);

            OrderBean orderBean = orderBiz.selByOrderId(requestMessage.getString("orderId"));
            // 根据手机号查询比当前系统时间小于7天的风控信息
            List<RiskInfoBean> riskList =
                    riskInfoBiz.getRiskListInfoByOrderId(orderBean.getRegId(), orderBean.getBizType());
            List<LocalRiskInfoBean> localBeanList = afterLoanServiceImpl.getReportStateByOrderId(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            if (!CollectionUtils.isEmpty(riskList)) {
                linkedHashMap.put("success", "0");
                linkedHashMap.put("result", "可以审核");
                return linkedHashMap;
            } else {

                if (localBeanList.size() == 0) {
                    linkedHashMap.put("success", "110");
                    linkedHashMap.put("result", "报告未生成不可以审核通过,稍等再试");
                    return linkedHashMap;
                }
                for (LocalRiskInfoBean localRiskInfoBean : localBeanList) {

                    if ("1".equals(localRiskInfoBean.getErrCode())) {
                        linkedHashMap.put("success",
                                "120");
                        linkedHashMap.put("result", "报告发送失败,不能审核通过,请联系管理员");
                        return linkedHashMap;
                    }
                    if ("1".equals(localRiskInfoBean.getReportState())) {// 两个合同只要有一个未生成 不可以点审核
                        linkedHashMap.put("success", "110");
                        linkedHashMap.put("result", "报告未生成不可以审核通过,稍等再试");
                        return linkedHashMap;
                    }
                }
            }
            linkedHashMap.put("success", "0");
            linkedHashMap.put("result", "可以审核");
            return linkedHashMap;
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/unIntcpt-getPersonDetail"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object getPersonDetail(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            // 先根据订单号查库表 看涉案详情是否已经落地
            JSONObject obj = new JSONObject();
            LocalRiskInfoBean isean = afterLoanServiceImpl.getPersonDetail(requestMessage);
            String orderId = requestMessage.get("orderId").toString();
            if (isean == null || isean.getPersonDetail() == null || "".equals(isean.getPersonDetail())) {
                long time = System.currentTimeMillis();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date(time);
                String format1 = df.format(date);
                requestMessage.remove("orderId");
                String strDec =
                        DesUtil.strEnc(requestMessage.toJSONString(), DesConstant.DES_KEY_1, DesConstant.DES_KEY_2,
                                format1);
                JSONObject json = new JSONObject();
                json.put("data", strDec);
                json.put("customerCode", "0002");
                json.put("sendTime", format1);
                String result = HttpsClientUtil.getInstance().doPost(consumerConfig.
                        getLocal_riskcontrol_getDetail_url(), json.toJSONString(), "utf-8");

                logger.info("详情返回信息:{}", result);
                obj = new JSONObject();
                obj.put("personDetail", result.toString());
                obj.put("orderId", orderId);
                afterLoanServiceImpl.savePersonDetail(obj);
                LocalRiskInfoBean isean2 = afterLoanServiceImpl.getPersonDetail(obj);
                JSONObject obj1 = JSONObject.parseObject(isean2.getPersonDetail());
                return obj1;
            }
            obj = JSONObject.parseObject(isean.getPersonDetail());
            return obj;
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/unIntcpt-toLocalRisk"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object toLocalRisk(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        logger.info("--以租代售流程门店预处理-分控调用开始--");
        Map parseObject = JSON.parseObject(requestMessage.toJSONString());
        Map<String, Object> send2Risk = riskInfoService.send2Risk(parseObject);
        logger.info("--以租代售流程门店预处理分控调用结束--{}", JSONObject.toJSONString(send2Risk));
        return send2Risk;
    }

    /**
     * 
     * Description:根据订单号查询贷后处置信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/getOrderInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getAfterLoanInfoByOrderId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---根据订单号查询贷后处置信息开始...{}", objs);
            AfterLoanBean result = afterLoanServiceImpl.getAfterLoanInfoByOrderId(objs.getString("orderId"));
            logger.info("---根据订单号查询贷后处置信息完成...");
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
     * Description:更新车辆贷后信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/updateAfterLoanInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map updateAfterLoanInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---更新车辆贷后信息开始...{}", objs);
            int result = afterLoanServiceImpl.updateAfterLoanInfo(objs);
            logger.info("---更新车辆贷后信息完成...");
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
     * Description:保存gps信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/saveInstGpsInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map saveInstGpsInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---保存gps信息开始...{}", objs);
            int result = afterLoanServiceImpl.saveInstGpsInfo(objs);
            logger.info("---保存gps信息完成...");
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
     * Description:根据订单号查询gps信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月4日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/getGpsInfoByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map getGpsInfoByOrderId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---根据订单号查询gps信息开始...{}", objs);
            List<InstGpsInfo> result = afterLoanServiceImpl.getGpsInfoByOrderId(objs.getString("orderId"));
            logger.info("---根据订单号查询gps信息完成...");
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
     * 外包受理分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectOutSourcetList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectOutSourcetList(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---外包受理分页查询--- {}", requestMessage);
            adminServiceImpl.getMerchantNos(requestMessage);
            PageInfo<OutSourceOrderBean> result =
                    afterLoanServiceImpl.selectOutSourcetList(requestMessage);
            logger.info("---外包受理分页查询结果--- {}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 外包处理分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/processList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectOutSourceProcesstList(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---外包处理分页查询--- {}", requestMessage);
            adminServiceImpl.getMerchantNos(requestMessage);
            PageInfo<OutSourceOrderBean> result =
                    afterLoanServiceImpl.selectOutSourceProcesstList(requestMessage);
            logger.info("---外包处理分页查询结果--- {}", result);
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
     * 保存外包处理信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月4日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/saveOutSource"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> saveOutSourceOrder(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---保存外包处理信息 --- {}", requestMessage);
            int result =
                    afterLoanServiceImpl.saveOutSourceOrder(requestMessage);
            logger.info("---保存外包处理信息 结果--- {}", result);
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
     * 车辆退回--更新贷后车辆状态信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月10日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/updateManagerCarInfoForCarBack"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateManagerCarInfoForCarBack(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---更新贷后车辆状态信息 --- {}", requestMessage);
            requestMessage.put("processMethod", 1);
            requestMessage.put("processStatus", CommonConstant.ProcessStatus.ProcessStatus_20);
            int result =
                    afterLoanServiceImpl.updateManagerCarInfo(requestMessage);
            logger.info("---更新贷后车辆状态信息结果--- {}", result);
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
     * 保存贷后车辆-外包回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月12日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/saveInstManageCarReceive"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> saveInstManageCarReceive(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---保存贷后车辆-外包回款信息 --- {}", requestMessage);
            int result =
                    afterLoanServiceImpl.saveInstManageCarReceive(requestMessage);
            logger.info("---保存贷后车辆-外包回款信息结果--- {}", result);
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
     * 更新贷后车辆-外包回款信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月12日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/updateCarReceiveByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateInstManageCarReceiveByOrderId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---更新贷后车辆-外包回款信息 --- {}", requestMessage);
            int result =
                    afterLoanServiceImpl.updateInstManageCarReceiveByOrderId(requestMessage);
            logger.info("---更新贷后车辆-外包回款信息结果--- {}", result);
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
     * 委案受理分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectCasetList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectCasetList(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---外包处理分页查询--- {}", requestMessage);
            adminServiceImpl.getMerchantNos(requestMessage);
            PageInfo<OutSourceOrderBean> result =
                    afterLoanServiceImpl.selectCasetList(requestMessage);
            logger.info("---外包处理分页查询结果--- {}", result);
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
     * 委案受理新增功能
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/newCase"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> newCase(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---委案受理新增功能--- {}", requestMessage);
            Map<String, Object> returnMap =
                    afterLoanServiceImpl.newCase(requestMessage);
            logger.info("---委案受理新增功能结果--- {}", returnMap);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", returnMap);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 委案受理-资料登记
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/registerCase"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> registerCase(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---委案受理-资料登记功能--- {}", requestMessage);
            int result =
                    afterLoanServiceImpl.saveInstOrderLawnInfo(requestMessage);
            logger.info("---委案受理-资料登记结果--- {}", result);
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
     * 立案申请分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectRegisterCasetList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectRegisterCasetList(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---立案申请分页查询--- {}", requestMessage);
            adminServiceImpl.getMerchantNos(requestMessage);
            PageInfo<OutSourceOrderBean> result =
                    afterLoanServiceImpl.selectCasetList(requestMessage);
            logger.info("---立案申请分页查询结果--- {}", result);
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
     * 根据订单号查询立案申请信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/getCaseInfoByOrderId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getCaseInfoByOrderId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---根据订单号查询立案申请信息--- {}", requestMessage);
            OutSourceOrderBean result =
                    afterLoanServiceImpl.getCaseInfoByCaseId(requestMessage.getString("caseId"));
            InstOrderLawBean instOrderLawBean = afterLoanServiceImpl.getInstOrderLawnInfoByCaseId(requestMessage);
            List<InstReceivedPaymentBean> receivedPaymentList = new ArrayList<>();
            if (instOrderLawBean != null) {
                receivedPaymentList =
                        afterLoanServiceImpl.selectInstReceivedPaymentList(instOrderLawBean.getOrderId(),
                                requestMessage.getString("caseId"));
            }
            logger.info("---根据订单号查询立案申请信息结果--- {}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            linkedHashMap.put("receivedPaymentList", receivedPaymentList);

            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 立案申请-庭前调解-保存资料
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/updateRegisterCase"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateRegisterCase(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---立案申请-庭前调解-保存资料--- {}", requestMessage);
            int result =
                    afterLoanServiceImpl.updateInstOrderLawnInfo(requestMessage);
            logger.info("---立案申请-庭前调解-保存资料结果--- {}", result);
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
     * 根据订单号查询法务回款列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月17日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectInstReceivedPaymentList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectInstReceivedPaymentList(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---根据订单号查询法务回款列表信息--- {}", requestMessage);
            List<InstReceivedPaymentBean> result =
                    afterLoanServiceImpl.selectInstReceivedPaymentList(requestMessage.getString("orderId"),
                            requestMessage.getString("caseId"));
            logger.info("---根据订单号查询法务回款列表信息结果--- {}", result);
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
     * 庭审登记信息分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectTrialRegistList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectTrialRegistList(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---立案申请分页查询--- {}", requestMessage);
            adminServiceImpl.getMerchantNos(requestMessage);
            PageInfo<OutSourceOrderBean> result =
                    afterLoanServiceImpl.selectTrialRegistList(requestMessage);
            logger.info("---立案申请分页查询结果--- {}", result);
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
     * 根据caseId查询案件信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/getInstOrderLawnInfoByCaseId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> getInstOrderLawnInfoByCaseId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---根据caseId查询案件信息--- {}", requestMessage);
            InstOrderLawBean result =
                    afterLoanServiceImpl.getInstOrderLawnInfoByCaseId(requestMessage);
            logger.info("---根据caseId查询案件信息结果--- {}", result);
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
     * 保存庭审登记信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/saveInstOrderLawResult"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> saveInstOrderLawResult(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---保存庭审登记信息--- {}", requestMessage);
            requestMessage.put("operatorRegId", sysUserSession.getUserPhone());
            int result =
                    afterLoanServiceImpl.saveInstOrderLawResult(requestMessage);
            logger.info("---保存庭审登记信息结果--- {}", result);
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
     * 显示庭审登记列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月20日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectInstOrderLawResultByCaseId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectInstOrderLawResultByCaseId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---显示庭审登记列表信息--- {}", requestMessage);
            PageInfo<InstOrderLawResultBean> result =
                    afterLoanServiceImpl.selectInstOrderLawResultByCaseId(requestMessage);
            logger.info("---显示庭审登记列表信息结果--- {}", JSONObject.toJSONString(result));
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
     * 贷后案件查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月19日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectAfterCaseOrderInfoList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectAfterCaseOrderInfoList(@RequestBody JSONObject objs, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            logger.info("---贷后案件分页查询--- {}", objs);
            adminServiceImpl.getMerchantNos(objs);
            PageInfo<Map<String, Object>> result =
                    afterLoanServiceImpl.selectAfterCaseOrderInfoList(objs);
            logger.info("---贷后案件分页查询结果--- {}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            linkedHashMap.put("resultTotal", afterLoanServiceImpl.totalAfterCaseOrderInfo(objs));
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Throwable e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 贷后案件查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月19日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/exportAfterCaseOrderInfoList", method = {RequestMethod.POST, RequestMethod.GET})
    public Map exportAfterCaseOrderInfoList(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.debug("开始导出贷后案件查询报表数据");
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
            String result = afterLoanServiceImpl.exportAfterCaseOrderInfoList(
                    objs, response);
            logger.debug("导出贷后案件查询报表数据完成.结果：{}", result);
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
     * 根据caseId查询案件进度列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月22日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectInstOrderCaseExecuteByCaseId"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectInstOrderCaseExecuteByCaseId(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---根据caseId查询案件进度列表信息--- {}", requestMessage);
            PageInfo<InstOrderCaseExecuteBean> result =
                    afterLoanServiceImpl.selectInstOrderCaseExecuteByCaseId(requestMessage);
            logger.info("---根据caseId查询案件进度列表信息结果--- {}", result);
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
     * 保存案件进度信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月22日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/saveInstOrderCaseExecute"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> saveInstOrderCaseExecute(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---根据caseId查询案件进度列表信息--- {}", requestMessage);
            requestMessage.put("operatorRegId", sysUserSession.getUserPhone());
            int result =
                    afterLoanServiceImpl.saveInstOrderCaseExecute(requestMessage);
            logger.info("---根据caseId查询案件进度列表信息结果--- {}", result);
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
     * 法务查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月23日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/selectAfterCaseOrderLawList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectAfterCaseOrderLawList(@RequestBody JSONObject objs, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            logger.info("---法务分页查询--- {}", objs);
            adminServiceImpl.getMerchantNos(objs);
            PageInfo<Map<String, Object>> result =
                    afterLoanServiceImpl.selectAfterCaseOrderLawList(objs);
            logger.info("---法务分页查询结果--- {}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            linkedHashMap.put("resultTotal", afterLoanServiceImpl.totalAfterCaseOrderLaw(objs));
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 法务查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月23日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/exportAfterCaseOrderLawList", method = {RequestMethod.POST, RequestMethod.GET})
    public Map exportAfterCaseOrderLawList(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.debug("开始导出法务查询报表数据");
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
            String result = afterLoanServiceImpl.exportAfterCaseOrderLawList(
                    objs, response);
            logger.debug("导出法务查询报表数据完成.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error(
                    "IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(),
                    e);
            return super.returnFailtrueInfo(new IqbException(
                    CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 案件编号是否唯一校验
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月30日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/validateCaseNo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> validateCaseNo(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---案件编号是否唯一校验--- {}", requestMessage);
            requestMessage.put("operatorRegId", sysUserSession.getUserPhone());
            int result =
                    afterLoanServiceImpl.validateCaseNo(requestMessage);
            logger.info("---案件编号是否唯一校验结果--- {}", result);
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
     * 立案申请方法
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月31日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = {"/applyCase"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> applyCase(@RequestBody JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            logger.info("---立案申请方法功能--- {}", requestMessage);
            int result =
                    afterLoanServiceImpl.applyCase(requestMessage);
            logger.info("---立案申请方法结果--- {}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
