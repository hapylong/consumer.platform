package com.iqb.consumer.service.layer.penalty.derate;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.BizConstant.BizProcDefKeyConstant;
import com.iqb.consumer.common.constant.BizConstant.WFConst;
import com.iqb.consumer.common.constant.CommonConstant.StatusConst;
import com.iqb.consumer.common.constant.CommonConstant.WFKeyConst;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.data.layer.bean.penalty.derate.PenaltyDerateBean;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.BeanUtil;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.etep.workflow.constant.WfAttribute.WfProcDealType;
import com.iqb.etep.workflow.task.service.IWfProcTaskCallBackService;

/**
 * 
 * Description: 罚息减免工作流服务接口
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
public class PenaltyDerateWFService implements IWfProcTaskCallBackService {

    /** 日志 **/
    private static final Logger logger = LoggerFactory.getLogger(PenaltyDerateWFService.class);

    @Autowired
    private SysUserSession sysUserSession;

    @Autowired
    private IPenaltyDerateService penaltyDerateServiceImpl;

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
        if (WFConst.PROCENDED_YES.equals(procEnded) && WfProcDealType.PROC_APPROVE.equals(dealType)) {
            this.updatePenaltyDerateStatusByWF(procApprStatus, procBizId);
        }

        /** 判断所在节点并进行业务处理 **/
        if (WfProcDealType.PROC_APPROVE.equals(dealType) || WfProcDealType.PROC_COMMIT.equals(dealType)) {
            this.dealProcApprove(procBizData, procCurrTask);
            return;
        } else if ("retrieve".equals(dealType)) { // 撤回
            this.penaltyDerateServiceImpl.retrievePenaltyDerate(procBizId);
        } else if ("end".equals(dealType)) {
            PenaltyDerateBean penaltyDerateBean = new PenaltyDerateBean();
            penaltyDerateBean.setUuid(procBizId);
            penaltyDerateBean.setApplyStatus(StatusConst.PENALTY_DERATE_APPLY_STATUS_INTERRUPT);
            this.penaltyDerateServiceImpl.updatePenaltyDeratableApplyStatus(penaltyDerateBean);
        }
    }

    public static void main(String[] args) {
        System.out.println(WfProcDealType.PROC_CANCEL);
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
    private void dealProcApprove(Map<String, Object> procBizData, String procCurrTask) throws IqbException {
        switch (procCurrTask) {
            case BizProcDefKeyConstant.PENALTYDERATE_KEY:
                /** 如果提交失败直接回退 **/
                String procApprStatus = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCAPPRSTATUS_KEY));
                if (!WFConst.PROCAPPRSTATUS_SUCC.equals(procApprStatus)) {
                    break;
                }
                /** 提交成功，数据入库 **/
                String procInstId = ObjectUtils.toString(procBizData.get(WFConst.ProcInstIdKey));
                String procBizId = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCBIZID_KEY));
                String penaltyDerateInfo = ObjectUtils.toString(procBizData.get(WFKeyConst.PENALTYDERATEINFO_KEY));
                PenaltyDerateBean penaltyDerateBean = new PenaltyDerateBean();
                try {
                    penaltyDerateBean = BeanUtil.toJavaObject(penaltyDerateInfo, PenaltyDerateBean.class);
                } catch (Exception e) {
                    throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000002);
                }
                penaltyDerateBean.setProcInstId(procInstId);
                penaltyDerateBean.setUuid(procBizId);
                this.penaltyDerateServiceImpl.savePenaltyDeratable(JSONObject.parseObject(JSONObject
                        .toJSONString(penaltyDerateBean)));
                break;

            default:
                break;
        }
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
    private void updatePenaltyDerateStatusByWF(String procApprStatus, String procBizId) throws IqbException {
        if (WFConst.PROCAPPRSTATUS_SUCC.equals(procApprStatus)) {
            PenaltyDerateBean penaltyDerateBean = new PenaltyDerateBean();
            penaltyDerateBean.setUuid(procBizId);
            penaltyDerateBean.setApplyStatus(StatusConst.PENALTY_DERATE_APPLY_STATUS_SUCC);
            this.penaltyDerateServiceImpl.doPenaltyDerate(penaltyDerateBean);
            return;
        }
        if (WFConst.PROCAPPRSTATUS_REFUSE.equals(procApprStatus)) {
            PenaltyDerateBean penaltyDerateBean = new PenaltyDerateBean();
            penaltyDerateBean.setUuid(procBizId);
            penaltyDerateBean.setApplyStatus(StatusConst.PENALTY_DERATE_APPLY_STATUS_FAIL);
            this.penaltyDerateServiceImpl.updatePenaltyDeratableApplyStatus(penaltyDerateBean);
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
        /** 判断所在节点并进行业务处理 **/
        if (WfProcDealType.PROC_START.equals(dealType) || WfProcDealType.PROC_COMMIT.equals(dealType)) {
            String procBizId = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCBIZID_KEY));
            String procOrgCode = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCORGCODE_KEY));
            if (StringUtil.isEmpty(procBizId)) {
                procBizData.put(WFKeyConst.PROCBIZID_KEY, UUID.randomUUID().toString());
            }
            if (StringUtil.isEmpty(procOrgCode)) {
                procBizData.put(WFKeyConst.PROCORGCODE_KEY, this.sysUserSession.getOrgCode());
            }
        }
        logger.info("工作流回调前置接口处理完成,参数1:{},参数2:{}", dealType, JSONObject.toJSONString(procBizData));
    }

}
