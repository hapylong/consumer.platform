/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 工作流回调接口
 * @version V1.0
 */
package com.iqb.consumer.manage.front.workflow;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.BizConstant.PledgeInquiryWFCode;
import com.iqb.consumer.common.constant.CommonConstant.WFKeyConst;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.data.layer.bean.Pledge.PledgeInfoBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.pledge.PledgeInquiryBiz;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.consumer.service.layer.pledge.PledgeInquiryWFService;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.BeanUtil;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.etep.common.utils.https.SendHttpsUtil;
import com.iqb.etep.workflow.constant.WfAttribute.WfProcDealType;
import com.iqb.etep.workflow.task.service.IWfProcTaskCallBackService;

/**
 * 质押车 guojuan
 */
@Component
public class WFPledgeInquiryReturnController extends BaseService implements IWfProcTaskCallBackService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(WFPledgeInquiryReturnController.class);

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

    @Autowired
    private PledgeInquiryWFService pledgeInquiryWFService;
    @Resource
    private PledgeInquiryBiz pledgeInquiryBiz;

    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws IqbException {
        // 审核状态 1：通过；2：退回；0：拒绝
        String procApprStatus = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCAPPRSTATUS_KEY));
        String procBizId = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCBIZID_KEY));
        // 当前流程任务代码
        String procCurrTask = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCCURRTASK_KEY));
        // 流程是否结束 1：是；0：否
        String procEnded = ObjectUtils.toString(procBizData.get(WFKeyConst.PROCENDED_KEY));

        logger.info("iqb 回调-质押车");
        if (WfProcDealType.PROC_APPROVE.equals(dealType) || WfProcDealType.PROC_COMMIT.equals(dealType)) {
            switch (procCurrTask) {
                case PledgeInquiryWFCode.PLEDGE_INQUIRY_PRETREATMENT:
                    String pledgeInquiryInfo = ObjectUtils.toString(procBizData.get("pledgeInquiryInfo"));
                    PledgeInfoBean pledgeInfoBean = new PledgeInfoBean();
                    try {
                        pledgeInfoBean = BeanUtil.toJavaObject(pledgeInquiryInfo, PledgeInfoBean.class);
                    } catch (Exception e) {
                        throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000002);
                    }
                    /** 提交成功，数据入库 **/
                    JSONObject object = new JSONObject();
                    object.put("orderId", procBizId);
                    object.put("wfStatus", "12");
                    object.put("applyAmt", pledgeInfoBean.getApplyAmt().multiply(BigDecimal.valueOf(10000)));
                    object.put("assessPrice", String.valueOf((new BigDecimal(pledgeInfoBean.getAssessPrice()))
                            .multiply(BigDecimal.valueOf(10000))));
                    object.put("remark", pledgeInfoBean.getRemark());
                    pledgeInquiryWFService.after(object);
                    break;
                case PledgeInquiryWFCode.PLEDGE_INQUIRY_REVIEW:
                    /** 提交成功，数据入库 **/
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("orderId", procBizId);
                    params.put("status", "2");
                    // 退回状态处理
                    if ("2".equals(procApprStatus)) {
                        params.put("status", "1");
                    }
                    // LinkedHashMap result =
                    // SendHttpsUtil.postMsg4GetMap(
                    // paramConfig.getManagerSend2RiskYZDSurl(),
                    // params);
                    pledgeInquiryBiz.updatePledgeStatus(params);
                    logger.debug("车辆复评结果:{}", params);
                    break;
                default:
                    break;
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
