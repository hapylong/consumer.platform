package com.iqb.consumer.data.layer.dao.car_dealer;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.car_dealer.entity.InstDealerManagerEntity;
import com.iqb.consumer.data.layer.bean.car_dealer.pojo.CgetInfoResponsePojo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;

public interface CarDealerCenterRepository {

    List<CgetInfoResponsePojo> getCustomerChannal();

    List<CgetInfoResponsePojo> getCarDealer();

    InstDealerManagerEntity getCarDealerInfoByOid(String orderId);

    List<InstDealerManagerEntity> cgetCarDealerList(JSONObject requestMessage);

    PlanBean getPBByOid(String orderId);

}
