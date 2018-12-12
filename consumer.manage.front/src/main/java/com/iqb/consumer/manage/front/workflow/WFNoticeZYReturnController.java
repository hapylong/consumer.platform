/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 工作流回调接口
 * @date 2016年9月19日 上午11:24:17
 * @version V1.0
 */
package com.iqb.consumer.manage.front.workflow;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.schedule.http.ApiRequestMessage;
import com.iqb.consumer.data.layer.bean.wf.CombinationQueryBean;
import com.iqb.consumer.data.layer.biz.CombinationQueryBiz;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.schedule.ScheduleTaskAnalysisAllot;
import com.iqb.consumer.data.layer.biz.schedule.ScheduleTaskManager;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.consumer.manage.front.workflow.wfstructure.WorkFlowsStructure;
import com.iqb.consumer.service.layer.pledge.IPledgeSerivce;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.workflow.constant.WfAttribute.WfProcDealType;
import com.iqb.etep.workflow.task.service.IWfProcTaskCallBackService;

/**
 * 抵押车工作流回调
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class WFNoticeZYReturnController extends BaseService implements IWfProcTaskCallBackService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(WFNoticeZYReturnController.class);

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
    private CombinationQueryBiz combinationQueryBiz;
    @Resource
    private OrderBiz orderBiz;
    @Autowired
    private IPledgeSerivce pledgeSerivceImpl;
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
                // List<String> procNextTasks = (List<String>) procBizData.get("procNextTasks");
                String wfStatus = null;
                // String procNextTask = procNextTasks.get(0);
                OrderBean orderBean = new OrderBean();
                OrderBean ob = orderBiz.selByOrderId(procBizId);
                orderBean.setOrderId(procBizId);
                String preAmtStatus = null;
                CombinationQueryBean cqb = combinationQueryBiz.getByOrderId(procBizId);
                Boolean flag = false;
                switch (procCurrTask) {
                    case "guaranty_pretreatment": // 门店预处理
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "3";
                            // 调风控
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("orderId", procBizId);
                            LinkedHashMap result2 =
                                    SendHttpsUtil
                                            .postMsg4GetMap(
                                                    // "http://101.201.151.38:8088/consumer.manage.front/business/send2Risk4Pledge",
                                                    paramConfig.getManagerSend2RiskDYurl(),
                                                    params);
                            logger.debug("调用风控结果:{}", result2);
                            logger.debug("WFNoticeReturnController guaranty_pretreatment passed!");
                            break;
                        } else {
                            logger.debug("WFNoticeReturnController guaranty_pretreatment refused!");
                            break;
                        }
                    case "guaranty_risk": // 读脉
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "31";
                            logger.debug("WFNoticeReturnController guaranty_risk passed!");
                            // 返回
                        } else if ("2".equals(procApprStatus)) {
                            flag = true;
                            wfStatus = WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL;
                            logger.debug("WFNoticeReturnController lease_risk passed!");
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
                            flag = true;
                            wfStatus = "0";
                            orderBean.setRiskStatus(1);
                            orderBean.setWfStatus(0);
                            logger.debug("WFNoticeReturnController guaranty_risk refused!");
                        }

                        break;
                    case "guaranty_risk_department": // 人工风控or车帮风控
                        flag = true;
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "4";
                            if (cqb != null) {
                                orderBean.setRiskStatus(0);
                            }
                            orderBean.setWfStatus(4);
                            logger.debug("WFNoticeReturnController lease_risk_department passed!");
                            break;
                        } else if ("2".equals(procApprStatus)) {
                            wfStatus = WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL;
                            logger.debug("WFNoticeReturnController lease_risk passed!");
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
                            wfStatus = "0";
                            orderBean.setRiskStatus(1);
                            orderBean.setWfStatus(0);
                            logger.debug("WFNoticeReturnController lease_risk_department refused!");
                            break;
                        }
                    case "guaranty_price": // 抵质押物估价
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "5";
                            if (new BigDecimal(cqb.getPreAmt()).compareTo(BigDecimal.ZERO) > 0) {
                                preAmtStatus = String.valueOf(WorkFlowsStructure.PRE_AMT_WAIT_PAY);

                                try {
                                    String url = ob.getOrderRemark();
                                    boolean save = scheduleTaskManager.isMerchantNeedSave(ob.getMerchantNo());
                                    ApiRequestMessage data = new ApiRequestMessage();
                                    data.setOrderId(ob.getOrderId());
                                    data.setStatus(1);
                                    scheduleTaskAnalysisAllot.send(data.toString(), url,
                                            ScheduleTaskAnalysisAllot.MODULE_A, ob.getOrderId(), save);
                                } catch (Throwable e) {
                                    logger.error("WFNoticeZYReturnController.guaranty_price exception :", e);
                                }

                            }
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("orderId", procBizId);
                            LinkedHashMap result2 =
                                    SendHttpsUtil
                                            .postMsg4GetMap(
                                                    // "http://101.201.151.38:8088/consumer.manage.front/creditorInfo/WFReturn/DY",
                                                    paramConfig.getManagerWfreturnDYurl(),
                                                    params);
                            logger.debug("WFNoticeReturnController guaranty_price passed!");
                            break;
                        } else if ("2".equals(procApprStatus)) {
                            wfStatus = WorkFlowsStructure.WF_WAIT_STORE_PRE_DEAL;
                            logger.debug("WFNoticeReturnController guaranty_price passed!");
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
                            wfStatus = "0";
                            logger.debug("WFNoticeReturnController guaranty_price refused!");
                            break;
                        }
                    case "guaranty_store": // 项目信息维护
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "6";
                            logger.debug("WFNoticeReturnController guaranty_store passed!");
                            break;
                        } else {
                            logger.debug("WFNoticeReturnController guaranty_store refused!");
                            break;
                        }
                    case "guaranty_audit": // 内控审批
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "7";
                            logger.debug("WFNoticeReturnController guaranty_audit passed!");
                            break;
                        } else {
                            wfStatus = "5";
                            logger.debug("WFNoticeReturnController guaranty_audit refused!");
                            break;
                        }
                    case "guaranty_finance": // 财务收款确认
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "8";
                            logger.debug("WFNoticeReturnController guaranty_finance passed!");
                            break;
                        } else {
                            logger.debug("WFNoticeReturnController guaranty_finance refused!");
                            break;
                        }
                    case "guaranty_project": // 项目初审
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "9";
                            logger.debug("WFNoticeReturnController guaranty_project passed!");
                            break;
                        } else {
                            wfStatus = "5";
                            logger.debug("WFNoticeReturnController guaranty_project refused!");
                            break;
                        }
                }
                if (flag) {
                    logger.debug("flag状态结果:{}", flag);
                    pledgeSerivceImpl.updatePledgeOrderByWF(orderBean);
                } else {
                    // 修改工作流状态
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("orderId", procBizId);
                    params.put("wfStatus", wfStatus);
                    params.put("preAmtStatus", preAmtStatus);
                    LinkedHashMap result2 =
                            SendHttpsUtil.postMsg4GetMap(
                                    // "http://101.201.151.38:8088/consumer.manage.front/business/modifyOrder2",
                                    paramConfig.getManagerModifyOrderUrl(),
                                    params);
                    logger.debug("修改订单状态结果:{}", result2);
                }
            } catch (Exception e) {
                logger.error("工作流回调更改订单状态错误", e);
            }
        }
    }

    @Override
    public void before(String arg0, Map<String, Object> arg1) throws IqbException {

    }

    public void parallelAfter(String arg0, Map<String, Object> arg1) throws IqbException {
        logger.debug("--并行汇聚节点回调处理类型dealType:{},业务参数procBizData:{}", arg0, arg1);
    }
}
