package com.iqb.consumer.data.layer.biz.sublet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.sublet.db.entity.InstSubletRecordEntity;
import com.iqb.consumer.data.layer.bean.sublet.pojo.GetSubletRecordPojo;
import com.iqb.consumer.data.layer.bean.sublet.pojo.SubletInfoByOidPojo;
import com.iqb.consumer.data.layer.dao.sublet.SubletCenterRepository;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 
 * Description: 转租
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月8日    adam       1.0        1.0 Version 
 * </pre>
 */
@Component
public class SubletCenterManager extends BaseBiz {

    @Autowired
    private SubletCenterRepository subletCenterRepository;

    public SubletInfoByOidPojo getSubletInfoByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return subletCenterRepository.getSubletInfoByOid(orderId);
    }

    public void persistSubletInfo(InstSubletRecordEntity isre) {
        super.setDb(0, super.MASTER);
        subletCenterRepository.persistSubletInfo(isre);
    }

    public GetSubletRecordPojo getSubletRecord(String orderId, String subletOrderId) {
        super.setDb(0, super.SLAVE);
        return subletCenterRepository.getSubletRecord(orderId, subletOrderId);
    }

    public InstSubletRecordEntity getSubletEntityByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return subletCenterRepository.getSubletEntityByOid(orderId);
    }

}
