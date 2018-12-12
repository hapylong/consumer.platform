package com.iqb.consumer.data.layer.dao.dandelion;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.dandelion.entity.InstCreditInfoEntity;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.GetInfoByOidResponsePojo;

public interface DandelionRepository {

    GetInfoByOidResponsePojo getInfoByOid(String orderId);

    void persistDesignPersion3elements(InstCreditInfoEntity icie);

    InstCreditInfoEntity getInstCreditInfoEntityByOid(String orderId);

    Integer updateToAddGuaranteeNo(InstCreditInfoEntity icie);

    int updateBorrowInfo(InstCreditInfoEntity icie);

    int updatePersionInfo(InstCreditInfoEntity icie);

    void persistSubletInfo(InstCreditInfoEntity icie);

    int updateDandelionEntityByOid(InstOrderInfoEntity ioie);

    void updateSubletInfo(InstCreditInfoEntity icie);

    int saveUpdateMortgageInfo(String mortgageInfo);

    int updateMortgateInfo(InstCreditInfoEntity icie);

}
