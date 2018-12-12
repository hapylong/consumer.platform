package com.iqb.consumer.manage.front.order;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.business.service.IOrderService;

/**
 * 
 * Description: 蒲公英需求设计
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月15日    adam       1.0        1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/order")
public class OrderCenterController extends BasicService {

    protected static final Logger log = LoggerFactory.getLogger(OrderCenterController.class);
    private static final int SERVICE_CODE_QUERY_HOURSE_BILL = 1;
    private static final int SERVICE_CODE_CHAT_F_SERVER = 2; // 调账户系统，进行分期
    private static final int SERVICE_CODE_SAVE_OR_UPDATE_OTHER_AMT_SERVER = 3; // FINANCE-2317以租代购门店预处理节点增加线上收取GPS/保险费等费用
    private static final int SERVICE_CODE_SAVE_OR_GET_OTHER_AMT_SERVER = 4; // FINANCE-2317以租代购门店预处理节点增加线上收取GPS/保险费等费用

    /**
     * @author adam FINANCE-1700 房贷分期分页查询
     */
    @Autowired
    private IOrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/houseHandler/{type}/query", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_QUERY_HOURSE_BILL)
    public Object queryBillHouseHandler(@PathVariable Integer type, @RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        try {
            Object result = orderService
                    .houseHandlerBillQuery(type, requestMessage);
            return returnSuccessInfo(getSuccResponse(result));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_QUERY_HOURSE_BILL);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_QUERY_HOURSE_BILL);
        }
    }

    /**
     * 房贷分期接口
     * 
     * @param requestMessage
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/houseHandler/createBill", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_CHAT_F_SERVER)
    public Object createBillHouseHandler(@RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        try {
            orderService
                    .houseHandlerCreateBill(requestMessage);
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_QUERY_HOURSE_BILL);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_QUERY_HOURSE_BILL, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_QUERY_HOURSE_BILL);
        }
    }

    /**
     * FINANCE-2303
     * 
     * 以租代购门店预处理节点增加线上收取GPS/保险费等费用
     * 
     * @param requestMessage
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/otherAmt/saveOrUpdate/{isSave}", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_SAVE_OR_UPDATE_OTHER_AMT_SERVER)
    public Object saveOrUpdateOtherAmt(@RequestBody JSONObject requestMessage, @PathVariable Boolean isSave) {
        try {
            orderService
                    .saveOrUpdateOtherAmt(requestMessage, isSave, true);
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SAVE_OR_UPDATE_OTHER_AMT_SERVER);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_SAVE_OR_UPDATE_OTHER_AMT_SERVER, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SAVE_OR_UPDATE_OTHER_AMT_SERVER);
        }
    }

    /****
     * Description: FINANCE-2304
     * 
     * 车秒贷---门店额度确认节点增加支付GPS费用/支付保险费/支付购置税/支付其他费用等
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年11月6日 下午6:06:19
     */
    @ResponseBody
    @RequestMapping(value = "/otherAmt/saveOrUpdateCmd/{isSave}", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_SAVE_OR_UPDATE_OTHER_AMT_SERVER)
    public Object saveOrUpdateCmd(@RequestBody JSONObject requestMessage, @PathVariable Boolean isSave) {
        try {
            orderService
                    .saveOrUpdateOtherAmt(requestMessage, isSave, false);
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SAVE_OR_UPDATE_OTHER_AMT_SERVER);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_SAVE_OR_UPDATE_OTHER_AMT_SERVER, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SAVE_OR_UPDATE_OTHER_AMT_SERVER);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/otherAmt/getOtherAmtList/{isPage}", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_SAVE_OR_GET_OTHER_AMT_SERVER)
    public Object getOtherAmtList(@RequestBody JSONObject requestMessage, @PathVariable Boolean isPage) {
        try {
            Object result = orderService
                    .getOtherAmtList(requestMessage, isPage);
            return returnSuccessInfo(getSuccResponse(result));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SAVE_OR_GET_OTHER_AMT_SERVER);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_SAVE_OR_GET_OTHER_AMT_SERVER, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SAVE_OR_GET_OTHER_AMT_SERVER);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/unIntcpt-finishBill", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_SAVE_OR_GET_OTHER_AMT_SERVER)
    public Object finishBill(@RequestBody JSONObject requestMessage) {
        try {
            orderService
                    .finishBill(requestMessage);
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SAVE_OR_GET_OTHER_AMT_SERVER);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_SAVE_OR_GET_OTHER_AMT_SERVER, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SAVE_OR_GET_OTHER_AMT_SERVER);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_ORDER_CENTER;
    }

}
