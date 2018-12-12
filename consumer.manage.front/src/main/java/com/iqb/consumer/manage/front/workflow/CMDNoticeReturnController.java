/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 工作流回调接口
 * @date 2016年9月19日 上午11:24:17
 * @version V1.0
 */
package com.iqb.consumer.manage.front.workflow;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.etep.workflow.constant.WfAttribute.WfProcDealType;
import com.iqb.etep.workflow.task.service.IWfProcTaskCallBackService;

/**
 * 车秒贷
 */
@Component
public class CMDNoticeReturnController extends BaseService implements IWfProcTaskCallBackService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(CMDNoticeReturnController.class);

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

    @SuppressWarnings({"rawtypes"})
    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws IqbException {
        logger.debug("处理类型dealType:{},业务参数procBizData:{}", dealType, procBizData);
        if (WfProcDealType.PROC_APPROVE.equalsIgnoreCase(dealType)) {
            try {
                // 工作流处理后需要同步order表状态,根据约定的接口状态去维护riskStatus
                String procBizId = (String) procBizData.get("procBizId");
                String procApprStatus = (String) procBizData.get("procApprStatus");
                String procCurrTask = (String) procBizData.get("procCurrTask");// 当前节点
                String wfStatus = null;
                switch (procCurrTask) {
                    case "credit_store": // 项目信息维护
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "6";
                            // 生成对应的项目信息数据
                            Map<String, String> params2 = new HashMap<String, String>();
                            params2.put("orderId", procBizId);
                            SendHttpsUtil.postMsg4GetMap(paramConfig.getCmdCopyInfoUrl(), params2);
                            logger.debug("WFNoticeReturnController credit_store passed!");
                            break;
                        } else {
                            logger.debug("WFNoticeReturnController credit_store refused!");
                            break;
                        }
                    case "credit_audit": // 内控审批
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "8";
                            logger.debug("WFNoticeReturnController credit_audit passed!");
                            break;
                        } else {
                            wfStatus = "5";
                            logger.debug("WFNoticeReturnController credit_audit refused!");
                            break;
                        }
                    case "credit_project": // 项目初审
                        if ("1".equals(procApprStatus)) {
                            wfStatus = "9";
                            logger.debug("WFNoticeReturnController credit_project passed!");
                            break;
                        } else {
                            wfStatus = "5";
                            logger.debug("WFNoticeReturnController credit_project refused!");
                            break;
                        }
                }

                // 修改工作流状态
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", procBizId);
                params.put("wfStatus", wfStatus);
                LinkedHashMap result =
                        SendHttpsUtil
                                .postMsg4GetMap(
                                        paramConfig.getManagerModifyOrderUrl(),
                                        params);
                logger.debug("修改订单状态结果:{}", result);

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
