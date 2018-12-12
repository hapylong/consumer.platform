package com.iqb.consumer.data.layer.dao.sublet;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.sublet.db.entity.InstSubletRecordEntity;
import com.iqb.consumer.data.layer.bean.sublet.pojo.GetSubletRecordPojo;
import com.iqb.consumer.data.layer.bean.sublet.pojo.SubletInfoByOidPojo;

public interface SubletCenterRepository {

    SubletInfoByOidPojo getSubletInfoByOid(String orderId);

    void persistSubletInfo(InstSubletRecordEntity isre);

    GetSubletRecordPojo getSubletRecord(@Param("orderId") String orderId, @Param("subletOrderId") String subletOrderId);

    InstSubletRecordEntity getSubletEntityByOid(String orderId);

}
