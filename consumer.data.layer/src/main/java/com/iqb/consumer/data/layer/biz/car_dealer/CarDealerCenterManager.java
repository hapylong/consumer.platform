package com.iqb.consumer.data.layer.biz.car_dealer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.car_dealer.entity.InstDealerManagerEntity;
import com.iqb.consumer.data.layer.bean.car_dealer.pojo.CgetInfoResponsePojo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.dao.car_dealer.CarDealerCenterRepository;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class CarDealerCenterManager extends BaseBiz {

    @Autowired
    private CarDealerCenterRepository carDealerCenterRepository;

    public List<CgetInfoResponsePojo> getCustomerChannal() {
        super.setDb(0, super.SLAVE);
        return carDealerCenterRepository.getCustomerChannal();
    }

    public List<CgetInfoResponsePojo> getCarDealer() {
        super.setDb(0, super.SLAVE);
        return carDealerCenterRepository.getCarDealer();
    }

    public InstDealerManagerEntity getCarDealerInfoByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return carDealerCenterRepository.getCarDealerInfoByOid(orderId);
    }

    public List<InstDealerManagerEntity> cgetCarDealerList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        List<InstDealerManagerEntity> list = carDealerCenterRepository.cgetCarDealerList(requestMessage);
        return list;
    }

    public PlanBean getPBByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return carDealerCenterRepository.getPBByOid(orderId);
    }

}
