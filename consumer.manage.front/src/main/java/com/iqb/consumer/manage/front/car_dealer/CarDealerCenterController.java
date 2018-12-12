package com.iqb.consumer.manage.front.car_dealer;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.data.layer.bean.car_dealer.entity.InstDealerManagerEntity;
import com.iqb.consumer.data.layer.bean.car_dealer.pojo.CgetInfoResponsePojo;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.order.OrderBreakInfo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.business.service.IOrderService;
import com.iqb.consumer.service.layer.car_dealer.CarDealerCenterService;
import com.iqb.consumer.service.layer.common.CalculateService;

@RestController
@RequestMapping("/car_dealer")
public class CarDealerCenterController extends BasicService {

    private static Logger logger = LoggerFactory.getLogger(CarDealerCenterController.class);
    private static final int SERVICE_CODE_CGET_INFO = 1;
    private static final int SERVICE_CODE_CGET_CAR_DEALER_LIST = 2;
    private static final int SERVICE_CODE_RECULATE_AMT = 3;
    private static final int SERVICE_CODE_RESET_AMT = 4;

    @Autowired
    private CarDealerCenterService carDealerCenterServiceImpl;

    @Autowired
    private CalculateService calculateServiceImpl;

    @Autowired
    private IOrderService orderService;

    /**
     * 
     * Description: {"type":0}4.2查询客户渠道名称(方便检索使用) {"type":1}4.3车商名称检索
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月26日 下午5:12:24
     */
    @ResponseBody
    @RequestMapping(value = "/cget_info", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_CGET_INFO)
    public Object cgetInfo(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            List<CgetInfoResponsePojo> result = carDealerCenterServiceImpl.cgetInfo(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put(KEY_RESULT, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CGET_INFO);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CGET_INFO);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/cget_car_dealer_list", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_CGET_CAR_DEALER_LIST)
    public Object getCarDealerInfo(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            PageInfo<InstDealerManagerEntity> result = carDealerCenterServiceImpl.cgetCarDealerList(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put(KEY_RESULT, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CGET_CAR_DEALER_LIST);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_CGET_CAR_DEALER_LIST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/reculateAmt", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_RECULATE_AMT)
    public Object reculateAmt(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            String orderId = requestMessage.getString("orderId");
            BigDecimal amt = requestMessage.getBigDecimal("orderAmt");
            PlanBean pb = carDealerCenterServiceImpl.getPBByOid(orderId);
            if (pb == null || amt == null) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A);
            }
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put(KEY_RESULT, calculateServiceImpl.calculateAmt(pb, amt));
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_RECULATE_AMT);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_RECULATE_AMT);
        }
    }

    /**
     * 以租代购流程-抵质押物估价节点退回到门店预处理节点金额重算方法 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月6日
     */
    @ResponseBody
    @RequestMapping(value = "/resetAmt", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_RESET_AMT)
    public Object resetAmt(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            OrderBreakInfo obi = null;
            try {
                obi = JSONObject.toJavaObject(requestMessage, OrderBreakInfo.class);
                if (obi == null || !obi.checkEntity()) {
                    throw new Exception();
                }
            } catch (Exception e) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A);
            }
            logger.info("---以租代购流程-抵质押物估价节点退回到门店预处理节点金额重算方法--{}", JSONObject.toJSONString(obi));
            InstOrderInfoEntity ioie = orderService.getIOIEByOid(obi.getOrderId());
            if (ioie == null || ioie.getBackFlag() != 1) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.B);
            }
            int count = orderService.saveOrderBreakInfo(obi);
            if (count == 1) {
                String orderId = obi.getOrderId();
                BigDecimal amt = obi.getOrderAmt();
                PlanBean pb = carDealerCenterServiceImpl.getPBByOid(orderId);
                if (pb == null || amt == null) {
                    throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A);
                }
                carDealerCenterServiceImpl.resetAmt(calculateServiceImpl.calculateAmt(pb, amt), obi.getOrderId(),
                        obi.getOrderAmt());
            }
            return returnSuccessInfo(getSuccResponse(StatusEnum.success));
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_RESET_AMT);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_RESET_AMT);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_CAR_DEALER_CENTER;
    }

}
