package com.iqb.consumer.manage.front.finance;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.BillQueryRequestPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.BillRefundRequestPojo;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.manage.front.order.OrderCenterController;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.finance.FinanceService;

/****
 * 
 * Description: 所有需要查询账务系统提供服务的接口群组
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年8月16日    adam       1.0        1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/finance")
public class FinanceCenterController extends BasicService {

    protected static final Logger log = LoggerFactory.getLogger(OrderCenterController.class);
    private static final int SERVICE_CODE_BILLHANDLER_QUERY = 1;
    private static final int SERVICE_CODE_BILLHANDLER_VERIFY_PAYMENT = 2;
    private static final int SERVICE_CODE_BILLHANDLER_REFUND = 3;
    private static final int SERVICE_CODE_BILLHANDLER_EXPORT = 4;

    @Autowired
    private FinanceService financeServiceImpl;

    @Autowired
    private AdminService adminServiceImpl;

    /****
     * 
     * Description: 账务系统 账单查询
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年8月16日 下午1:57:38
     */
    @ResponseBody
    @RequestMapping(value = "/billHandler/houseQuery", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_BILLHANDLER_QUERY)
    public Object queryBillHandler(@RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        BillQueryRequestPojo bqr = null;
        try {
            adminServiceImpl.getMerchantNos(requestMessage);
            bqr = JSONObject.toJavaObject(requestMessage, BillQueryRequestPojo.class);
        } catch (Exception e) {
            log.error("queryBillHandler error :", e);
            return generateFailResponseMessage(
                    new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_BILLHANDLER_QUERY);
        }
        try {
            Object result = financeServiceImpl.queryBillHandler(bqr);
            return returnSuccessInfo(getSuccResponse(result));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_BILLHANDLER_QUERY);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_BILLHANDLER_QUERY, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_BILLHANDLER_QUERY);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/billHandler/houseQuery/export", method = RequestMethod.GET)
    @ServiceCode(SERVICE_CODE_BILLHANDLER_EXPORT)
    public Object queryBillHandlerExport(HttpServletRequest request, HttpServletResponse response) {

        BillQueryRequestPojo bqr = null;
        try {
            request.setCharacterEncoding("UTF-8");
            JSONObject requestMessage = new JSONObject();
            Enumeration<String> keys = request.getParameterNames();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String value = request.getParameter(key);
                requestMessage.put(key, value.trim());
            }
            adminServiceImpl.getMerchantNos(requestMessage);
            bqr = JSONObject.toJavaObject(requestMessage, BillQueryRequestPojo.class);
            // you are not to understand (偶数是分页list,奇数是非分页)
            if (bqr != null && bqr.getType() != null && bqr.getType() % 2 == 0) {
                bqr.setType(bqr.getType() - 1);
            }
        } catch (Exception e) {
            log.error("queryBillHandlerExport error :", e);
            return generateFailResponseMessage(
                    new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_BILLHANDLER_EXPORT);
        }
        try {
            String result = financeServiceImpl.queryBillHandlerExportXlsx(bqr, response);
            return returnSuccessInfo(getSuccResponse(result));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_BILLHANDLER_EXPORT);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_BILLHANDLER_QUERY, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_BILLHANDLER_EXPORT);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/billHandler/verifyPayment", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_BILLHANDLER_VERIFY_PAYMENT)
    public Object verifyPayment(@RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        BillQueryRequestPojo bqr = null;
        try {
            bqr = JSONObject.toJavaObject(requestMessage, BillQueryRequestPojo.class);
        } catch (Exception e) {
            log.error("queryBillHandler error :", e);
            return generateFailResponseMessage(
                    new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_BILLHANDLER_QUERY);
        }

        try {
            Object result = financeServiceImpl
                    .verifyPayment(bqr);
            return returnSuccessInfo(getSuccResponse(result));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_BILLHANDLER_VERIFY_PAYMENT);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_BILLHANDLER_VERIFY_PAYMENT, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_BILLHANDLER_VERIFY_PAYMENT);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/billHandler/refund", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_BILLHANDLER_REFUND)
    public Object refund(@RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        BillRefundRequestPojo brrq = null;
        try {
            brrq = JSONObject.toJavaObject(requestMessage, BillRefundRequestPojo.class);
            if (!brrq.checkEntity()) {
                throw new Exception();
            }
        } catch (Exception e) {
            log.error("refundBillHandler error :", e);
            return generateFailResponseMessage(
                    new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_BILLHANDLER_REFUND);
        }

        try {
            financeServiceImpl
                    .billHandlerRefund(brrq);
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_BILLHANDLER_REFUND);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_BILLHANDLER_REFUND, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_BILLHANDLER_REFUND);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_FINANCE_CENTER;
    }

}
