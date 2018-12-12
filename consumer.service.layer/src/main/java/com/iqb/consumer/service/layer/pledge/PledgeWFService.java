package com.iqb.consumer.service.layer.pledge;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.BizConstant.BizPledgeProcDefKeyConstant;
import com.iqb.consumer.common.constant.BizConstant.WFConst;
import com.iqb.consumer.common.constant.CommonConstant;
import com.iqb.consumer.common.constant.CommonConstant.RiskStatusConst;
import com.iqb.consumer.common.constant.CommonConstant.WFKeyConst;
import com.iqb.consumer.common.constant.CommonConstant.WFStatusConst;
import com.iqb.consumer.data.layer.bean.Pledge.PledgeInfoBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.workflow.constant.WfAttribute.WfProcDealType;
import com.iqb.etep.workflow.task.service.IWfProcTaskCallBackService;

/**
 * 
 * Description: 质押车工作流回调服务接口
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月13日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Component
public class PledgeWFService implements IWfProcTaskCallBackService {

    /** 日志 **/
    private static final Logger logger = LoggerFactory.getLogger(PledgeWFService.class);

    @Autowired
    private IPledgeSerivce pledgeSerivceImpl;

    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws IqbException {
        logger.info("工作流回调前置接口,参数1:{},参数2:{}", dealType, JSONObject.toJSONString(procBizData));
        String procApprStatus = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCAPPRSTATUS_KEY));
        String procBizId = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCBIZID_KEY));
        String procCurrTask = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCCURRTASK_KEY));
        String procEnded = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCENDED_KEY));
        /** 参数校验 **/
        this.validateProcBizData(procApprStatus, procBizId, procCurrTask, procEnded);

        /** 判断回调类型是否是结束 **/
        /*
         * if (WFConst.PROCENDED_YES.equals(procEnded) &&
         * WfProcDealType.PROC_APPROVE.equals(dealType)) {
         * this.updatePledgeOrderByWF(procApprStatus, procBizId); return; }
         */

        /** 判断所在节点并进行业务处理 **/
        if (WfProcDealType.PROC_APPROVE.equals(dealType)) {
            this.dealProcApprove(procBizData, procCurrTask, procBizId);
            return;
        }
    }

    /**
     * 
     * Description: 处理审核通过业务逻辑
     * 
     * @param
     * @return void
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午9:11:09
     */
    private void dealProcApprove(Map<String, Object> procBizData, String procCurrTask, String procBizId)
            throws IqbException {
        logger.info("dealProcApprove接口,参数1:{},参数2:{}", JSONObject.toJSONString(procBizData), procCurrTask);
        OrderBean orderBean = new OrderBean();
        PledgeInfoBean bean = new PledgeInfoBean();
        orderBean.setOrderId(procBizId);
        bean.setOrderId(procBizId);
        /** 获取审批状态 **/
        String procApprStatus = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCAPPRSTATUS_KEY));
        switch (procCurrTask) {
            case BizPledgeProcDefKeyConstant.PLEDGE_ORDER_KEY:
                break;

            case BizPledgeProcDefKeyConstant.PLEDGE_DEPARTMENT_KEY:
                orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_31));
                break;
            /** 车帮风控审批 **/
            case BizPledgeProcDefKeyConstant.PLEDGE_RISK_KEY:
                if (WFConst.PROCAPPRSTATUS_SUCC.equals(procApprStatus)) {
                    orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_5));
                    this.pledgeSerivceImpl.dealRiskWFReturn(procBizId);
                    orderBean.setRiskStatus(Integer.parseInt(RiskStatusConst.RiskStatus_0));
                }
                if (WFConst.PROCAPPRSTATUS_REFUSE.equals(procApprStatus)) {
                    orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_0));
                    orderBean.setRiskStatus(Integer.parseInt(RiskStatusConst.RiskStatus_1));
                    bean.setStatus(CommonConstant.pledgeStatus.PLEDGESTATUS_2);
                    this.pledgeSerivceImpl.updatePledgeStatus(bean);
                }
                break;

            /** 项目信息维护 **/
            case BizPledgeProcDefKeyConstant.PLEDGE_STORE_KEY:
                if (WFConst.PROCAPPRSTATUS_SUCC.equals(procApprStatus)) {
                    orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_8));
                }
                break;

            /** 项目初审 **/
            case BizPledgeProcDefKeyConstant.PLEDGE_FIRST_PROJECT_KEY:
                if (WFConst.PROCAPPRSTATUS_SUCC.equals(procApprStatus)) {
                    orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_82));
                }
                if (WFConst.PROCAPPRSTATUS_BACK.equals(procApprStatus)) {
                    orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_5));
                }
                break;

            /** 入库确认 **/
            case BizPledgeProcDefKeyConstant.PLEDGE_STORAGE_KEY:
                if (WFConst.PROCAPPRSTATUS_SUCC.equals(procApprStatus)) {
                    orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_9));
                }
                if (WFConst.PROCAPPRSTATUS_BACK.equals(procApprStatus)) {
                    orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_5));
                }
                break;

            /** 项目终审 **/
            case BizPledgeProcDefKeyConstant.PLEDGE_FINAL_PROJECT_KEY:
                if (WFConst.PROCAPPRSTATUS_SUCC.equals(procApprStatus)) {
                    orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_81));
                }
                if (WFConst.PROCAPPRSTATUS_BACK.equals(procApprStatus)) {
                    orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_5));
                }
                break;

            default:
                break;
        }
        this.pledgeSerivceImpl.updatePledgeOrderByWF(orderBean);
    }

    /**
     * 
     * Description: 根据工作流返回进行业务处理(申请状态)
     * 
     * @param
     * @return void
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午9:01:34
     */
    @SuppressWarnings("unused")
    private void updatePledgeOrderByWF(String procApprStatus, String procBizId) throws IqbException {
        logger.info("updatePledgeOrderByWF接口,参数1:{},参数2:{}", procApprStatus, procBizId);
        if (WFConst.PROCAPPRSTATUS_SUCC.equals(procApprStatus)) {
            OrderBean orderBean = new OrderBean();
            orderBean.setOrderId(procBizId);
            orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_9));
            this.pledgeSerivceImpl.updatePledgeOrderByWF(orderBean);
            return;
        }
        if (WFConst.PROCAPPRSTATUS_REFUSE.equals(procApprStatus)) {
            OrderBean orderBean = new OrderBean();
            orderBean.setOrderId(procBizId);
            orderBean.setWfStatus(Integer.parseInt(WFStatusConst.PENALTY_DERATE_APPLY_STATUS_0));
            orderBean.setRiskStatus(Integer.parseInt(RiskStatusConst.RiskStatus_1));
            this.pledgeSerivceImpl.updatePledgeOrderByWF(orderBean);
            return;
        }
    }

    /**
     * 
     * Description: 校验流程业务数据
     * 
     * @param procEnded
     * @param
     * @return void
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午8:57:39
     */
    private void validateProcBizData(String procApprStatus, String procBizId, String procCurrTask, String procEnded)
            throws IqbException {}

    @Override
    public void before(String dealType, Map<String, Object> procBizData) throws IqbException {
        logger.info("工作流回调前置接口,参数1:{},参数2:{}", dealType, JSONObject.toJSONString(procBizData));
    }

}
