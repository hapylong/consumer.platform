package com.iqb.consumer.manage.front.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.biz.wf.SettleApplyBeanBiz;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.workflow.constant.WfAttribute.WfProcDealType;
import com.iqb.etep.workflow.task.service.IWfProcTaskCallBackService;

@Component
public class ProcessCallback extends BaseService implements IWfProcTaskCallBackService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** 流程任务审批 */
    public static final String PROC_APPROVE = WfProcDealType.PROC_APPROVE;

    private static final String PASS = "1"; // 通过
    private static final String REFUSED = "0"; // 拒绝
    private static final String BACK = "2"; // 退回
    private static final String PROCENDEDYES = "1";

    @Resource
    private SettleApplyBeanBiz settleApplyBeanBiz;

    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws IqbException {
        logger.info("处理类型dealType:{},业务参数procBizData:{}", dealType, procBizData);
        String procBizId = (String) procBizData.get("procBizId"); // 业务参数:主要是订单号
        String procApprStatus = (String) procBizData.get("procApprStatus"); // 审批结论 1：通过；2：退回；0：拒绝
        String procCurrTask = (String) procBizData.get("procCurrTask");// 当前节点
        String procEnded = (String) procBizData.get("procEnded"); // 流程是否结束，1：是；0：否
        // 流程拒绝 将settleStatus修改为3
        // 流程审批通过且流程结束 将settleStatus修改为2
        if (PROC_APPROVE.equals(dealType)) {
            String id = procBizId.substring(procBizId.indexOf("-") + 1, procBizId.length());
            Map<String, Object> params = null;

            // 流程审批
            if (REFUSED.equals(procApprStatus)) { // 拒绝流程结束
                logger.info("订单:{}流程拒绝", procBizId);
                params = new HashMap<String, Object>();
                params.put("id", id);
                params.put("settleStatus", 3);
                settleApplyBeanBiz.updateSettleStatus(params);
            }
            if (PASS.equals(procApprStatus) && PROCENDEDYES.equals(procEnded)) { // 流程通过并结束
                logger.info("订单:{}流程通过并结束", procBizId);
                params = new HashMap<String, Object>();
                params.put("id", id);
                params.put("settleStatus", 2);
                settleApplyBeanBiz.updateSettleStatus(params);
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
