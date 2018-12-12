package com.iqb.consumer.service.layer.car_dealer;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.data.layer.bean.car_dealer.entity.InstDealerManagerEntity;
import com.iqb.consumer.data.layer.bean.car_dealer.pojo.CgetInfoResponsePojo;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.car_dealer.CarDealerCenterManager;

import jodd.util.StringUtil;

@Service
public class CarDealerCenterServiceImpl implements CarDealerCenterService {

    private static Logger logger = LoggerFactory.getLogger(CarDealerCenterServiceImpl.class);
    @Autowired
    private CarDealerCenterManager carDealerCenterManager;

    @Autowired
    private OrderBiz orderBiz;

    private static final String KEY_TYPE = "type";

    @Override
    public List<CgetInfoResponsePojo> cgetInfo(JSONObject requestMessage) throws GenerallyException {
        try {
            switch (requestMessage.getInteger(KEY_TYPE)) {
                case CgetInfoResponsePojo.CUSTOMER_CHANNAL:
                    return uniqList(carDealerCenterManager.getCustomerChannal());
                case CgetInfoResponsePojo.CAR_DEALER:
                    return uniqList(carDealerCenterManager.getCarDealer());
                default:
                    throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
            }
        } catch (Throwable e) {
            logger.error("CarDealerCenterServiceImpl.cgetInfo error", e);
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
        }
    }

    @Override
    public PageInfo<InstDealerManagerEntity> cgetCarDealerList(JSONObject requestMessage) throws GenerallyException {
        return new PageInfo<>(
                carDealerCenterManager.cgetCarDealerList(requestMessage));
    }

    @Override
    public PlanBean getPBByOid(String orderId) throws GenerallyException {
        if (StringUtil.isEmpty(orderId)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        return carDealerCenterManager.getPBByOid(orderId);
    }

    private List<CgetInfoResponsePojo> uniqList(List<CgetInfoResponsePojo> request) {
        HashSet<CgetInfoResponsePojo> data = new HashSet<>();
        Iterator<CgetInfoResponsePojo> rs = request.iterator();
        while (rs.hasNext()) {
            if (!data.add(rs.next())) {
                rs.remove();
            }
        }
        return request;
    }

    @Override
    public void resetAmt(Map<String, BigDecimal> amts, String orderId, BigDecimal orderAmt) throws GenerallyException {
        if (amts == null || amts.isEmpty()) {
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
        }
        InstOrderInfoEntity ioie = new InstOrderInfoEntity();
        ioie.setDownPayment(amts.get("downPayment"));
        ioie.setFeeAmount(amts.get("feeAmount"));
        ioie.setMargin(amts.get("margin"));
        ioie.setPreAmt(amts.get("preAmount"));
        ioie.setMonthInterest(amts.get("monthMake"));// 月供
        ioie.setServiceFee(amts.get("serviceFee"));// 服务费
        ioie.setOrderAmt(orderAmt);// 服务费
        ioie.setOrderId(orderId);
        orderBiz.updateIOIEResetAmt(ioie);
    }

}
