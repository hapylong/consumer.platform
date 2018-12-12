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
import com.iqb.consumer.data.layer.biz.order.JysOrderBiz;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.workflow.constant.WfAttribute.WfProcDealType;
import com.iqb.etep.workflow.task.service.IWfProcTaskCallBackService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Component
public class JYSNoticeReturn extends BaseService implements IWfProcTaskCallBackService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private JysOrderBiz jysOrderBiz;

    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws IqbException {
        logger.debug("处理类型dealType:{},业务参数procBizData:{}", dealType, procBizData);
        /** 流程启动并提交 */
        if (WfProcDealType.PROC_COMMIT.equalsIgnoreCase(dealType)) {
            // WF->2 status->2
            String procBizId = (String) procBizData.get("procBizId");// 订单号
            // 修改权证资料
            int result = 0;
            if (procBizId.indexOf("-") != -1) {// 说明有包含id传进来
                result = this.updateJysOrderStatus1(procBizId, 2, 2);
            } else {
                result = this.updateJysOrderStatus(procBizId, 2, 2);// 带复核
            }

            logger.debug("修改交易所订单：{},结果:{}", procBizId, result);
        }
        /** 流程任务审批 */
        if (WfProcDealType.PROC_APPROVE.equalsIgnoreCase(dealType)) {
            String procBizId = (String) procBizData.get("procBizId");// 订单号
            String procApprStatus = (String) procBizData.get("procApprStatus");
            String procCurrTask = (String) procBizData.get("procCurrTask");// 当前节点

            if (procCurrTask.equals("asset_import")) {
                // 修改权证资料
                /*
                 * int result = this.updateJysOrderStatus(procBizId, 2, 2);// 通过
                 * logger.debug("修改交易所订单：{},结果:{}", procBizId, result);
                 */
                int result = 0;
                if (procBizId.indexOf("-") != -1) {// 说明有包含id传进来
                    result = this.updateJysOrderStatus1(procBizId, 2, 2);
                } else {
                    result = this.updateJysOrderStatus(procBizId, 2, 2);// 带复核
                }
            } else {
                if ("1".equals(procApprStatus)) {
                    // 修改权证资料
                    /*
                     * int result = this.updateJysOrderStatus(procBizId, 3, 3);// 通过
                     * logger.debug("修改交易所订单：{},结果:{}", procBizId, result);
                     */
                    int result = 0;
                    if (procBizId.indexOf("-") != -1) {// 说明有包含id传进来
                        result = this.updateJysOrderStatus1(procBizId, 3, 3);
                    } else {
                        result = this.updateJysOrderStatus(procBizId, 3, 3);// 带复核
                    }
                } else {
                    // 审批通过，回写status为拒绝或者审批通过
                    // 修改权证资料
                    /*
                     * int result = this.updateJysOrderStatus(procBizId, 2, 2);// 拒绝
                     * logger.debug("修改交易所订单：{},结果:{}", procBizId, result);
                     */
                    int result = 0;
                    if (procBizId.indexOf("-") != -1) {// 说明有包含id传进来
                        result = this.updateJysOrderStatus1(procBizId, 2, 2);
                    } else {
                        result = this.updateJysOrderStatus(procBizId, 2, 2);// 带复核
                    }
                }
            }

        }

    }

    @Override
    public void before(String arg0, Map<String, Object> arg1) throws IqbException {

    }

    // 1，导入完毕，2待复核，3复核完毕待打包，4打包完毕待拆分，5拆分完毕
    private int updateJysOrderStatus(String procBizId, int wfStatus, int status) {
        JSONObject objs = new JSONObject();
        objs.put("orderId", procBizId);
        objs.put("id", null);
        objs.put("wfStatus", wfStatus);
        objs.put("status", status);
        int res = jysOrderBiz.updateJYSOrderInfo(objs);
        return res;
    }

    // 根据"-"去拆分
    private int updateJysOrderStatus1(String procBizId, int wfStatus, int status) {
        int index = procBizId.indexOf("-");
        JSONObject objs = new JSONObject();
        objs.put("orderId", procBizId.substring(0, index));
        objs.put("id", procBizId.substring(index + 1, procBizId.length()));
        objs.put("wfStatus", wfStatus);
        objs.put("status", status);
        int res = jysOrderBiz.updateJYSOrderInfo(objs);
        return res;
    }

    public void parallelAfter(String arg0, Map<String, Object> arg1) throws IqbException {
        logger.debug("--并行汇聚节点回调处理类型dealType:{},业务参数procBizData:{}", arg0, arg1);
    }
}
