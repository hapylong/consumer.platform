package com.iqb.consumer.data.layer.dao.credit_product;

import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderBreakInfoEntity;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.credit_product.pojo.DetailsInfoPojo;
import com.iqb.consumer.data.layer.bean.credit_product.request.MerchantCertificateRequestMessage;
import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;

public interface CreditProductRepository {

    int merchantCertificate(MerchantCertificateRequestMessage mcrm);

    // InstOrderInfoEntity getPartialOrderInfo(String orderId);

    InstOrderBreakInfoEntity getOrderBreakInfoEntity(String orderId);

    InstOrderInfoEntity getInstOrderInfoEntityByOrderId(String orderId);

    void saveInstOrderInfoEntityX(InstOrderInfoEntity ioie);

    void updateOrderBreakInfoEntity(InstOrderBreakInfoEntity ioie);

    void updateInstOrderInfoEntityEQUAL(InstOrderInfoEntity ioie);

    void updateInstOrderInfoEntityUNVEN(InstOrderInfoEntity ioie);

    void updateInstOrderInfoEntityUPDATE(InstOrderInfoEntity ioie);

    DetailsInfoPojo getInfoDetailsX(String orderIdx);

    CreditorInfoBean getCreditorInfoBean(String orderId);

}
