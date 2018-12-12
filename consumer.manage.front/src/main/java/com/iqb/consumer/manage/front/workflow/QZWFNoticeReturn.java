/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年2月24日 下午6:00:15
 * @version V1.0
 */
package com.iqb.consumer.manage.front.workflow;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.service.layer.authoritycard.AuthorityCardService;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.workflow.constant.WfAttribute.WfProcDealType;
import com.iqb.etep.workflow.task.service.IWfProcTaskCallBackService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a> ChangeHistory: 2017-06-05
 *         权证资料管理增加GPS确认节点，并增加退回 haojinlong
 */
@Component
public class QZWFNoticeReturn extends BaseService implements IWfProcTaskCallBackService {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private AuthorityCardService authorityCardService;

    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws IqbException {
        logger.debug("处理类型dealType:{},业务参数procBizData:{}", dealType, procBizData);
        /** 流程启动并提交 */
        if (WfProcDealType.PROC_COMMIT.equalsIgnoreCase(dealType)) {
            String procBizId = (String) procBizData.get("procBizId");// 订单号
            // 修改权证资料
            int result = this.updateAuthorityCardStatus(procBizId, 2, 0);// 已上传
            logger.debug("修改权证资料结果:{}", result);
        }
        /** 流程任务审批 */
        if (WfProcDealType.PROC_APPROVE.equalsIgnoreCase(dealType)) {
            String procBizId = (String) procBizData.get("procBizId");// 订单号
            String procApprStatus = (String) procBizData.get("procApprStatus");
            String procCurrTask = (String) procBizData.get("procCurrTask");// 当前节点

            switch (procCurrTask) {
                case "warrants_input":
                    int result = this.updateAuthorityCardStatus(procBizId, 2, 0);// 通过
                    logger.debug("修改权证资料结果:{}", result);
                    break;
                case "warrants_confirm": // GPS确认
                    if ("1".equals(procApprStatus)) {
                        // 审批通过，回写status为审批通过
                        result = this.updateAuthorityCardStatus(procBizId, 2, 1);// 通过
                        logger.debug("修改权证资料结果:{}", result);
                    } else if ("2".equals(procApprStatus)) {
                        // 退回，回写status为拒绝或者审批通过
                        result = this.updateAuthorityCardStatus(procBizId, 2, 2);// 拒绝
                        logger.debug("修改权证资料结果:{}", result);
                    }
                    break;
                case "warrants_approve": // 权证资料审核
                    if ("1".equals(procApprStatus)) {
                        // 审批通过，回写status为审批通过
                        result = this.updateAuthorityCardStatus(procBizId, 3, 1);// 通过
                        logger.debug("修改权证资料结果:{}", result);
                    } else if ("2".equals(procApprStatus)) {
                        // 退回，回写status为拒绝或者审批通过
                        result = this.updateAuthorityCardStatus(procBizId, 2, 2);// 拒绝
                        logger.debug("修改权证资料结果:{}", result);
                    }
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

    private int updateAuthorityCardStatus(String procBizId, int uploadStatus, int status) {
        JSONObject params = new JSONObject();
        params.put("orderId", procBizId);
        params.put("uploadStatus", uploadStatus);
        params.put("status", status);
        int res = authorityCardService.updateAuthorityCardStatus(params);
        return res;
    }

}
