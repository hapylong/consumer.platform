package com.iqb.consumer.data.layer.biz.pledge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.dao.credit_product.CreditProductRepository;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class PledgeRepayManager extends BaseBiz {
    protected static final Logger logger = LoggerFactory
            .getLogger(PledgeRepayManager.class);

    @Autowired
    private CreditProductRepository creditProductRepository;

    public InstOrderInfoEntity getOrderInfoEntityByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return creditProductRepository.getInstOrderInfoEntityByOrderId(orderId);
    }

}
