/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 工作流回调接口
 * @date 2016年9月19日 上午11:24:17
 * @version V1.0
 */
package com.iqb.consumer.manage.front.workflow;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.schedule.http.ApiRequestMessage;
import com.iqb.consumer.data.layer.bean.wf.CombinationQueryBean;
import com.iqb.consumer.data.layer.biz.CombinationQueryBiz;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.schedule.ScheduleTaskAnalysisAllot;
import com.iqb.consumer.data.layer.biz.schedule.ScheduleTaskManager;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.consumer.manage.front.workflow.wfstructure.WorkFlowsStructure;
import com.iqb.consumer.service.layer.pledge.IPledgeSerivce;
import com.iqb.consumer.service.layer.sublet.SubletCenterService;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.etep.workflow.constant.WfAttribute.WfProcDealType;
import com.iqb.etep.workflow.task.service.IWfProcTaskCallBackService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class WFNoticeReturnController extends BaseService implements IWfProcTaskCallBackService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(WFNoticeReturnController.class);

    /** 流程启动 */
    public static final String PROC_START = WfProcDealType.PROC_START;
    /** 流程启动并提交 */
    public static final String PROC_COMMIT = WfProcDealType.PROC_COMMIT;
    /** 流程任务审批 */
    public static final String PROC_APPROVE = WfProcDealType.PROC_APPROVE;
    /** 流程任务签收 */
    public static final String PROC_CLAIM = WfProcDealType.PROC_CLAIM;
    /** 流程任务取消签收 */
    public static final String PROC_UNCLAIM = WfProcDealType.PROC_UNCLAIM;
    /** 流程任务删除 */
    public static final String PROC_DELETE = WfProcDealType.PROC_DELETE;

    @Resource
    private ParamConfig paramConfig;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private WFConfigBiz wfConfigBiz;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private SysUserSession sysUserSession;
    @Resource
    private CombinationQueryBiz combinationQueryBiz;
    @Autowired
    private IPledgeSerivce pledgeSerivceImpl;
    @Autowired
    private SubletCenterService subletCenterServiceImpl;
    @Autowired
    private ScheduleTaskManager scheduleTaskManager;
    @Autowired
    private ScheduleTaskAnalysisAllot scheduleTaskAnalysisAllot;

    @SuppressWarnings({"rawtypes", "unused"})
    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws IqbException {
        logger.debug("处理类型dealType:{},业务参数procBizData:{}", dealType, procBizData);
        if (WfProcDealType.PROC_APPROVE.equalsIgnoreCase(dealType)) {
            try {
                // 工作流处理后需要同步order表状态,根据约定的接口状态去维护riskStatus
                String procBizId = (String) procBizData.get("procBizId");
                String procApprStatus = (String) procBizData.get("procApprStatus");
                String procCurrTask = (String) procBizData.get("procCurrTask");// 当前节点

                OrderBean ob = orderBiz.selByOrderId(procBizId);
                String wfStatus = null;
                String preAmtStatus = null;
                String specialTime = "";
                OrderBean orderBean = new OrderBean();
                orderBean.setOrderId(procBizId);
                /*
                 * 流程状态修改标识,0 表示默认执行流程 2项目初审并且有转租的情况下一次修改订单状态及其相关信息
                 */
                String flag = "0";
                CombinationQueryBean cqb = combinationQueryBiz.getByOrderId(procBizId);
                switch (procCurrTask) {
                    case "lease_pretreatment": // 项目信息维护
                        if ("1".equals(procApprStatus)) {
                            wfStatus = WorkFlowsStructure.WF_WAIT_RISK_APPROVE;
                            // 调风控
                            Map<String, String> params2 = new HashMap<>();
                            params2.put("orderId", procBizId);
                            LinkedHashMap result2 =
                                    SendHttpsUtil.postMsg4GetMap(
                                            // "http://101.201.151.38:8088/consumer.manage.front/business/send2Risk",
                                            paramConfig.getManagerSend2RiskYZDSurl(),
                                            params2);
                            logger.debug("调用风控结果:{}", result2);
                            logger.debug("WFNoticeReturnController lease_pretreatment passed!");
                            break;
                        } else {
                            logger.debug("WFNoticeReturnController lease_pretreatment refused!");
                            break;
                        }
                    case "lease_risk": // 读脉风控节点
                        flag = "1";
                        if ("1".equals(procApprStatus)) {

                            orderBean.setWfStatus(Integer.parseInt(WorkFlowsStructure.WF_WAIT_MAN_RISK));
                            logger.debug("WFNoticeReturnController lease_risk passed!");
                        } else if ("2".equals(procApprStatus)) {
                            logger.debug("WFNoticeReturnController lease_risk passed!");
                            if (cqb != null) {
                                orderBean.setRiskStatus(2);
                            }
                            orderBean.setWfStatus(Integer.parseInt(WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL));
                            try {
                                String url = ob.getOrderRemark();
                                boolean save = scheduleTaskManager.isMerchantNeedSave(ob.getMerchantNo());
                                ApiRequestMessage data = new ApiRequestMessage();
                                data.setOrderId(ob.getOrderId());
                                data.setStatus(4);
                                scheduleTaskAnalysisAllot.send(data.toString(), url,
                                        ScheduleTaskAnalysisAllot.MODULE_A, ob.getOrderId(), save);
                            } catch (Throwable e) {
                                logger.error("WFNoticeZYReturnController.guaranty_price exception :", e);
                            }
                        } else {
                            orderBean.setWfStatus(Integer.parseInt(WorkFlowsStructure.WF_WAIT_MAN_RISK));
                            logger.debug("WFNoticeReturnController lease_risk passed!");
                        }

                        break;
                    case "lease_risk_department": // 人工风控or车帮风控
                        flag = "1";
                        if ("1".equals(procApprStatus)) {
                            String isSublet = (String) procBizData.get("isSublet"); // 是否转租
                            if (isSublet.equalsIgnoreCase("true")) {
                                // 转租订单,首付款大于0,待支付，否则支付完成。
                                orderBean.setWfStatus(5);
                                if (new BigDecimal(cqb.getPreAmt()).compareTo(BigDecimal.ZERO) > 0) {
                                    orderBean.setPreAmtStatus("2");
                                } else {
                                    orderBean.setPreAmtStatus("1");
                                }
                            } else {
                                orderBean.setWfStatus(4);
                            }

                            logger.debug("WFNoticeReturnController lease_risk_department passed!");
                            if (cqb != null) {
                                orderBean.setRiskStatus(0);
                            }

                            // 退回
                        } else if ("2".equals(procApprStatus)) {
                            wfStatus = WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL;
                            logger.debug("WFNoticeReturnController lease_risk_department passed!");
                            if (cqb != null) {
                                orderBean.setRiskStatus(2);
                            }
                            orderBean.setWfStatus(2);
                            try {
                                String url = ob.getOrderRemark();
                                boolean save = scheduleTaskManager.isMerchantNeedSave(ob.getMerchantNo());
                                ApiRequestMessage data = new ApiRequestMessage();
                                data.setOrderId(ob.getOrderId());
                                data.setStatus(4);
                                scheduleTaskAnalysisAllot.send(data.toString(), url,
                                        ScheduleTaskAnalysisAllot.MODULE_A, ob.getOrderId(), save);
                            } catch (Throwable e) {
                                logger.error("WFNoticeZYReturnController.guaranty_price exception :", e);
                            }
                        } else {
                            wfStatus = WorkFlowsStructure.WF_OVER_REFUSE;
                            orderBean.setRiskStatus(WorkFlowsStructure.RISK_OVER_REFUSE);
                            orderBean.setWfStatus(Integer.parseInt(wfStatus));
                            logger.debug("WFNoticeReturnController lease_risk_department refused!");
                        }
                        break;
                    case "lease_price": // 抵质押物估价
                        if ("1".equals(procApprStatus)) {
                            Map<String, String> params = new HashMap<>();
                            params.put("orderId", procBizId);
                            // 判断是否车秒贷，如果车秒贷状态修改为41，否则修改为5
                            if (cqb != null && StringUtil.isNotEmpty(cqb.getAmtLines())) {
                                wfStatus = WorkFlowsStructure.WF_WAIT_CAR_LOAN_ESTIMATED;
                            } else {
                                wfStatus = WorkFlowsStructure.WF_WAIT_ITEM_INFO_VINDICATE;
                                /** task1，以租代售无车秒贷 在通过抵质押估价后，判断预支付金额大于0，将订单金额修改为待支付 **/
                                if (new BigDecimal(cqb.getPreAmt()).compareTo(BigDecimal.ZERO) > 0) {
                                    preAmtStatus = String.valueOf(WorkFlowsStructure.PRE_AMT_WAIT_PAY);

                                    try {
                                        String url = ob.getOrderRemark();
                                        boolean save =
                                                scheduleTaskManager.isMerchantNeedSave(ob.getMerchantNo());
                                        ApiRequestMessage data = new ApiRequestMessage();
                                        data.setOrderId(ob.getOrderId());
                                        data.setStatus(1);
                                        scheduleTaskAnalysisAllot.send(data.toString(), url,
                                                ScheduleTaskAnalysisAllot.MODULE_A, ob.getOrderId(), save);
                                    } catch (Throwable e) {
                                        logger.error("WFNoticeZYReturnController.guaranty_price exception :", e);
                                    }

                                }
                                /** end **/
                            }
                            LinkedHashMap result =
                                    SendHttpsUtil
                                            .postMsg4GetMap(
                                                    // "http://101.201.151.38:8088/consumer.manage.front/creditorInfo/WFReturn/YZDS",
                                                    paramConfig.getManagerWfreturnYZDSurl(),
                                                    params);
                            specialTime = DateUtil.format(Calendar.getInstance(), "yyyy-MM-dd");
                            logger.debug("WFNoticeReturnController lease_price passed!");
                            break;
                        } else if ("2".equals(procApprStatus)) {
                            flag = "1";
                            wfStatus = WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL;
                            logger.debug("WFNoticeReturnController lease_price passed!");
                            if (cqb != null) {
                                orderBean.setRiskStatus(2);
                            }
                            orderBean.setWfStatus(2);
                            try {
                                String url = ob.getOrderRemark();
                                boolean save = scheduleTaskManager.isMerchantNeedSave(ob.getMerchantNo());
                                ApiRequestMessage data = new ApiRequestMessage();
                                data.setOrderId(ob.getOrderId());
                                data.setStatus(4);
                                scheduleTaskAnalysisAllot.send(data.toString(), url,
                                        ScheduleTaskAnalysisAllot.MODULE_A, ob.getOrderId(), save);
                            } catch (Throwable e) {
                                logger.error("WFNoticeZYReturnController.guaranty_price exception :", e);
                            }
                        } else {
                            wfStatus = WorkFlowsStructure.WF_OVER_REFUSE;
                            logger.debug("WFNoticeReturnController lease_price refused!");
                            break;
                        }
                    case "lease_store": // 项目信息维护
                        if ("1".equals(procApprStatus)) {
                            wfStatus = WorkFlowsStructure.WF_WAIT_RISK_TWICE_APPROVE;
                            logger.debug("WFNoticeReturnController lease_store passed!");
                            break;
                        } else {
                            logger.debug("WFNoticeReturnController lease_store refused!");
                            break;
                        }
                    case "lease_audit": // 内控审批
                        if ("1".equals(procApprStatus)) {
                            wfStatus = WorkFlowsStructure.WF_WAIT_FINANCE_VERIFY;
                            logger.debug("WFNoticeReturnController lease_audit passed!");
                            break;
                        } else {
                            wfStatus = WorkFlowsStructure.WF_WAIT_ITEM_INFO_VINDICATE;
                            logger.debug("WFNoticeReturnController lease_audit refused!");
                            break;
                        }
                    case "lease_finance": // 财务收款确认
                        if ("1".equals(procApprStatus)) {
                            wfStatus = WorkFlowsStructure.WF_WAIT_ITEM_FIRST_REVIEW;
                            logger.debug("WFNoticeReturnController lease_finance passed!");
                            break;
                        } else {
                            logger.debug("WFNoticeReturnController lease_finance refused!");
                            break;
                        }
                    case "lease_project": // 项目初审
                        if ("1".equals(procApprStatus)) {

                            wfStatus = WorkFlowsStructure.WF_OVER_FINISHED;
                            logger.debug("WFNoticeReturnController lease_project 项目初审---是否转租isSublet--{}",
                                    procBizData.get("isSublet"));
                            String isSublet = (String) procBizData.get("isSublet"); // 是否转租
                            if (isSublet != null && isSublet.equalsIgnoreCase("true")) {
                                flag = "2";
                                Map<String, Object> params = new HashMap<>();
                                params.put("orderId", procBizId);
                                params.put("wfStatus", wfStatus);
                                Map result = subletCenterServiceImpl.getSubletInfo(params);
                                logger.debug("转租结果:{}", result);
                            }
                            logger.debug("WFNoticeReturnController lease_project passed!");
                            break;
                        } else {
                            wfStatus = WorkFlowsStructure.WF_WAIT_ITEM_INFO_VINDICATE;
                            logger.debug("WFNoticeReturnController lease_project refused!");
                            break;
                        }
                    case "lease_credit":
                        if ("1".equals(procApprStatus)) {
                            wfStatus = WorkFlowsStructure.WF_WAIT_ITEM_INFO_VINDICATE;
                            /** task2，以租代售含车秒贷，在门店金额确认通过后，判断预支付金额大于0，将订单金额修改为待支付 **/
                            if (cqb != null && new BigDecimal(cqb.getPreAmt()).compareTo(BigDecimal.ZERO) > 0) {
                                preAmtStatus = String.valueOf(WorkFlowsStructure.PRE_AMT_WAIT_PAY);
                            }
                            /** end **/

                            // 判断车秒贷金额是否有金额来启动工作
                            startWF(procBizId);
                        } else {
                            wfStatus = WorkFlowsStructure.WF_WAIT_GOOD_EVALUATE;
                            logger.debug("WFNoticeReturnController lease_credit refused!");
                        }
                        break;
                }
                if (flag.equals("1")) {
                    logger.debug("flag状态结果:{}", flag);
                    pledgeSerivceImpl.updatePledgeOrderByWF(orderBean);
                } else if (flag.equals("0")) {
                    // 修改工作流状态
                    Map<String, String> params = new HashMap<>();
                    params.put("orderId", procBizId);
                    params.put("wfStatus", wfStatus);
                    params.put("preAmtStatus", preAmtStatus);
                    params.put("specialTime", specialTime);
                    LinkedHashMap result =
                            SendHttpsUtil
                                    .postMsg4GetMap(
                                            paramConfig.getManagerModifyOrderUrl(),
                                            params);
                    logger.debug("修改订单状态结果:{}", result);
                }
            } catch (Exception e) {
                logger.error("工作流回调更改订单状态错误", e);
            }
        }
    }

    @Override
    public void before(String arg0, Map<String, Object> arg1) throws IqbException {

    }

    // 启动工作流
    private LinkedHashMap startWF(String orderId)
            throws IqbException {
        OrderBean orderBean = orderBiz.selByOrderId(orderId + "X");
        double amt = orderBean.getOrderAmt() == null ? 0d : Double.parseDouble(orderBean.getOrderAmt());
        if (amt <= 0) {// 不走工作流
            logger.debug("订单{}无须走车秒贷工作流", orderId);
            return null;
        }
        MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(orderBean.getMerchantNo());
        String procBizMemo =
                sysUserSession.getRealName() + ";" + merchantBean.getMerchantFullName() + ";"
                        + orderBean.getOrderName() + ";" + orderBean.getRegId() + ";" + orderBean.getOrderAmt()
                        + ";" + orderBean.getOrderItems();// 摘要添加手机号

        WFConfig wfConfig = wfConfigBiz.getConfigByBizType(orderBean.getBizType(), orderBean.getWfStatus());

        Map<String, Object> hmProcData = new HashMap<>();
        hmProcData.put("procDefKey", wfConfig.getProcDefKey());

        Map<String, Object> hmVariables = new HashMap<>();
        hmVariables.put("procAuthType", "1");
        hmVariables.put("procTokenUser", wfConfig.getTokenUser());
        hmVariables.put("procTokenPass", wfConfig.getTokenPass());
        hmVariables.put("procTaskUser", orderBean.getRegId());
        hmVariables.put("procTaskRole", wfConfig.getTaskRole());
        hmVariables.put("procApprStatus", "1");
        hmVariables.put("procApprOpinion", "同意");
        hmVariables.put("procAssignee", "");
        hmVariables.put("procAppointTask", "");

        Map<String, Object> hmBizData = new HashMap<>();
        hmBizData.put("procBizId", orderId);
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", merchantBean.getId() + "");
        hmBizData.put("procBizMemo", procBizMemo);
        hmBizData.put("amount", orderBean.getOrderAmt());
        // hmBizData.put("baseUrl", commonParamConfig.getSelfCallURL());

        Map<String, Map<String, Object>> reqData = new HashMap<>();
        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        String url = wfConfig.getStartWfurl();
        // 发送Https请求
        LinkedHashMap responseMap = null;
        logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
        Long startTime = System.currentTimeMillis();
        try {
            // repRes = SimpleHttpUtils.httpPost(url, reqData);
            responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
        } catch (Exception e) {
            throw new RuntimeException("工作流接口交易失败");
            // throw new IqbException(SysServiceReturnInfo.SYS_WF_TRANSFER_FAIL_01010038);
        }
        Long endTime = System.currentTimeMillis();
        logger.info("调用工作流接口返回信息：{}" + responseMap);
        logger.info("工作流接口交互花费时间，{}" + (endTime - startTime));
        return responseMap;
    }

    public void parallelAfter(String arg0, Map<String, Object> arg1) throws IqbException {
        logger.debug("--并行汇聚节点回调处理类型dealType:{},业务参数procBizData:{}", arg0, arg1);
    }

    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", "TYLD2002170411001");
        LinkedHashMap result =
                SendHttpsUtil
                        .postMsg4GetMap(
                                "https://test.ishandian.cn/consumer.manage.front/unIntcpt-pledge_repay/byType",
                                "{\"orderId\": \"SYQS2200170418007\",\"repayNo\": \"0\",\"flag\": \"2\",\"amt\": \"806.84\",\"timestamp\": \"1492527883\"}");
        System.err.println(JSONObject.toJSONString(result));
    }
}
