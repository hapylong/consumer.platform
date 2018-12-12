package com.iqb.consumer.manage.front.carowner;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.business.service.IOrderService;
import com.iqb.consumer.service.layer.user.UserService;

/**
 * 
 * @Description:车主贷
 * @author adam
 * @date 2017年11月9日 下午3:25:41
 * @version
 */
@Controller
@RequestMapping("/carOwner")
public class CarOwnerCenterController extends BasicService {

    protected static final Logger log = LoggerFactory.getLogger(CarOwnerCenterController.class);
    private static final int SERVICE_CODE_SAVE_OR_UPDATE_USER_INFO = 1;
    private static final int SERVICE_CODE_CAR_OWNER_BASIC_INFO = 1;
    private static final int SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO = 2;
    private static final int SERVICE_CODE_EXPORT_CAR_OWNER_ORDER_INFO = 3;
    private static final int SERVICE_CODE_CAR_INFO = 4;
    private static final int SERVICE_CODE_EXPORT_CAR_INFO = 5;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private AdminService adminServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/saveOrUpdateUserInfo", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_SAVE_OR_UPDATE_USER_INFO)
    public Object queryBillHouseHandler(@PathVariable Integer type, @RequestBody JSONObject requestMessage,
            HttpServletRequest request) {
        try {
            userServiceImpl.saveOrUpdateUserInfo(requestMessage);
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SAVE_OR_UPDATE_USER_INFO);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_SAVE_OR_UPDATE_USER_INFO, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_SAVE_OR_UPDATE_USER_INFO);
        }
    }

    /****
     * 
     * Description: FINANCE-2355 车主贷：流程配置
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年11月14日 下午4:46:45
     */
    @ResponseBody
    @RequestMapping(value = "/carOwnerBasicInfo", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_CAR_OWNER_BASIC_INFO)
    public Object carOwnerBasicInfo(@RequestBody JSONObject requestMessage) {
        try {
            userServiceImpl.carOwnerBasicInfo(requestMessage);
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CAR_OWNER_BASIC_INFO);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_CAR_OWNER_BASIC_INFO, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CAR_OWNER_BASIC_INFO);
        }
    }

    /****
     * 
     * Description: FINANCE-2440 订单查询-后台接口
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年11月15日 下午4:35:27
     */
    @ResponseBody
    @RequestMapping(value = "/getCarOwnerOrderInfo", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO)
    public Object getCarOwnerOrderInfo(@RequestBody JSONObject requestMessage) {
        try {
            adminServiceImpl.merchantNos(requestMessage);
            return returnSuccessInfo(getSuccResponse(orderService.getCarOwnerOrderInfo(requestMessage)));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO);
        }
    }

    /**
     * 
     * Description: FINANCE-2440 订单查询-后台接口 导出
     * 
     * @param
     * @return void
     * @throws @Author adam Create Date: 2017年11月20日 上午11:38:10
     */
    @ResponseBody
    @RequestMapping(value = "/exportCarOwnerOrderInfo", method = RequestMethod.GET)
    @ServiceCode(SERVICE_CODE_EXPORT_CAR_OWNER_ORDER_INFO)
    public void exportCarOwnerOrderInfo(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            JSONObject requestMessage = new JSONObject();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                requestMessage.put(paraName, new String(para.trim()));
            }
            adminServiceImpl.merchantNos(requestMessage);
            orderService.exportCarOwnerOrderInfo(requestMessage, response);
        } catch (Throwable e) {
            log.error("exportCarOwnerOrderInfo :", e);
        }
    }

    /**
     * 
     * Description:FINANCE-2441 订单分期-后台接口 （查询已分期订单）
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年11月15日 下午6:18:14
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderBreakInfo", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO)
    public Object getOrderBreakInfo(@RequestBody JSONObject requestMessage) {
        try {
            adminServiceImpl.merchantNos(requestMessage);
            /** 已放款状态，可分期 **/
            requestMessage.put("bizType", "2400");
            return returnSuccessInfo(getSuccResponse(orderService.getOrderBreakInfo(requestMessage)));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO);
        }
    }

    /**
     * 
     * Description: FINANCE-2440 订单查询-后台接口 导出（导出已分期订单）
     * 
     * @param
     * @return void
     * @throws @Author adam Create Date: 2017年11月20日 上午11:38:10
     */
    @ResponseBody
    @RequestMapping(value = "/exportOrderBreakInfo", method = RequestMethod.GET)
    @ServiceCode(SERVICE_CODE_EXPORT_CAR_OWNER_ORDER_INFO)
    public void exportOrderBreakInfo(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            JSONObject requestMessage = new JSONObject();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                requestMessage.put(paraName, new String(para.trim()));
            }
            requestMessage.put("riskStatus", 7);
            adminServiceImpl.merchantNos(requestMessage);
            orderService.exportCarOwnerOrderInfo(requestMessage, response);
        } catch (Throwable e) {
            log.error("exportCarOwnerOrderInfo :", e);
        }
    }

    /**
     * 
     * Description: FINANCE-2441 订单分期-后台接口
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年11月15日 下午4:36:39
     */
    @ResponseBody
    @RequestMapping(value = "/orderBreak", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO)
    public Object orderBreak(@RequestBody JSONObject requestMessage) {
        try {
            log.info("调用分期参数:{}", requestMessage);
            Map<String, Object> result = orderService.orderBreak(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_CAR_OWNER_ORDER_INFO);
        }
    }

    /**
     * 
     * Description: FINANCE-2442 车辆查询-后台接口
     * 
     * @param
     * @return Object
     * @throws @Author adam Create Date: 2017年11月15日 下午4:36:39
     */
    @ResponseBody
    @RequestMapping(value = "/getCarOwnerCarInfo", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_CAR_INFO)
    public Object getCarInfo(@RequestBody JSONObject requestMessage) {
        try {
            adminServiceImpl.merchantNos(requestMessage);
            return returnSuccessInfo(getSuccResponse(orderService.getCarOwnerCarInfoPage(requestMessage)));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CAR_INFO);
        } catch (DevDefineErrorMsgException e) {
            return generateFailResponseMessage(
                    new GenerallyException(Reason.ERR_MSG_SELF_DEFINE, Layer.CONTROLLER, Location.A),
                    SERVICE_CODE_CAR_INFO, e.getErrMsg());
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CAR_INFO);
        }
    }

    /****
     * 
     * Description: FINANCE-2442 车辆查询-后台接口 导出
     * 
     * @param
     * @return void
     * @throws @Author adam Create Date: 2017年11月20日 上午11:37:54
     */
    @ResponseBody
    @RequestMapping(value = "/exportCarOwnerCarInfo", method = RequestMethod.GET)
    @ServiceCode(SERVICE_CODE_EXPORT_CAR_INFO)
    public void exportCarOwnerCarInfo(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            JSONObject requestMessage = new JSONObject();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                requestMessage.put(paraName, new String(para.trim()));
            }
            adminServiceImpl.merchantNos(requestMessage);
            orderService.exportCarOwnerCarInfo(requestMessage, response);
        } catch (Throwable e) {
            log.error("exportCarOwnerOrderInfo :", e);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_CAR_OWNER_CENTER;
    }

}
