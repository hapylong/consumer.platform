package com.iqb.consumer.service.layer.car_dealer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.car_dealer.entity.InstDealerManagerEntity;
import com.iqb.consumer.data.layer.bean.car_dealer.pojo.CgetInfoResponsePojo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;

public interface CarDealerCenterService {

    List<CgetInfoResponsePojo> cgetInfo(JSONObject requestMessage) throws GenerallyException;

    PageInfo<InstDealerManagerEntity> cgetCarDealerList(JSONObject requestMessage) throws GenerallyException;

    PlanBean getPBByOid(String orderId) throws GenerallyException;

    void resetAmt(Map<String, BigDecimal> calculateAmt, String orderId, BigDecimal orderAmt) throws GenerallyException;

}
