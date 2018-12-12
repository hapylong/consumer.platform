package com.iqb.consumer.data.layer.biz.credit_product;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderBreakInfoEntity;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity.OrderInfoType;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException.Layer;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException.Location;
import com.iqb.consumer.data.layer.bean.credit_product.exception.CreditProductlInvalidException.Reason;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.DetailsInfoPojo;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.GuaranteePledgeInfoBridge;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.GuaranteePledgeInfoBridge.StatusEnum;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.PlanDetailsPojo;
import com.iqb.consumer.data.layer.bean.credit_product.request.MerchantCertificateRequestMessage;
import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.dao.credit_product.CreditProductRepository;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class CreditProductManager extends BaseBiz {
    protected static final Logger logger = LoggerFactory
            .getLogger(CreditProductRepository.class);

    @Autowired
    private CreditProductRepository creditProductRepository;

    public boolean merchantCertificate(MerchantCertificateRequestMessage mcrm) {
        return creditProductRepository.merchantCertificate(mcrm) == 1;
    }

    public GuaranteePledgeInfoBridge checkBreakOrderUpdate(String orderId,
            BigDecimal carAmt) {
        super.setDb(0, super.MASTER);
        InstOrderBreakInfoEntity ioie = creditProductRepository
                .getOrderBreakInfoEntity(orderId);
        if (ioie == null || !ioie.checkEntity()) {
            return new GuaranteePledgeInfoBridge(StatusEnum.FAIL, null);
        }
        if (ioie.getCarAmt().compareTo(carAmt) != 0) {
            ioie.updateEntity(carAmt);
            return new GuaranteePledgeInfoBridge(StatusEnum.UNVEN, ioie);
        } else {
            return new GuaranteePledgeInfoBridge(StatusEnum.EQUAL, ioie);
        }
    }

    public InstOrderInfoEntity getInstOrderInfoEntityByOrderId(String orderId) {
        super.setDb(0, super.MASTER);
        return creditProductRepository.getInstOrderInfoEntityByOrderId(orderId);
    }

    public void saveInstOrderInfoEntityX(InstOrderInfoEntity ioie) {
        super.setDb(0, super.MASTER);
        creditProductRepository.saveInstOrderInfoEntityX(ioie);
    }

    public void updateInstOrderInfoEntityByType(InstOrderInfoEntity ioie,
            OrderInfoType type) throws CreditProductlInvalidException {
        super.setDb(0, super.MASTER);
        if (type == OrderInfoType.EQUAL) {
            creditProductRepository.updateInstOrderInfoEntityEQUAL(ioie);
        } else if (type == OrderInfoType.UNVEN) {
            creditProductRepository.updateInstOrderInfoEntityUNVEN(ioie);
        } else if (type == OrderInfoType.UPDATE) {
            creditProductRepository.updateInstOrderInfoEntityUPDATE(ioie);
        } else {
            throw new CreditProductlInvalidException(Reason.UNKNOW_TYPE,
                    Layer.MANAGER, Location.A);
        }
    }

    public DetailsInfoPojo getInfoDetailsX(String orderIdx) {
        super.setDb(0, super.MASTER);
        return creditProductRepository.getInfoDetailsX(orderIdx);
    }

    public CreditorInfoBean getCreditorInfoBean(String orderId) {
        super.setDb(0, super.MASTER);
        return creditProductRepository.getCreditorInfoBean(orderId);
    }

}
