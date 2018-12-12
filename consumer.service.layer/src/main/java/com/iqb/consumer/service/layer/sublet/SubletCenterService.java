package com.iqb.consumer.service.layer.sublet;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.sublet.db.entity.InstSubletRecordEntity;
import com.iqb.consumer.data.layer.bean.sublet.pojo.GetSubletRecordPojo;
import com.iqb.consumer.data.layer.bean.sublet.pojo.SubletInfoByOidPojo;

public interface SubletCenterService {

    SubletInfoByOidPojo getSubletInfoByOid(JSONObject requestMessage) throws GenerallyException;

    boolean persistSubletInfo(JSONObject requestMessage) throws GenerallyException;

    GetSubletRecordPojo getSubletRecord(JSONObject requestMessage) throws GenerallyException;

    InstSubletRecordEntity getSubletEntity(JSONObject requestMessage) throws GenerallyException;

    /**
     * 以租代购-转租
     * 
     * @param params
     * @throws GenerallyException
     */
    Map<String, Object> getSubletInfo(Map<String, Object> params) throws GenerallyException;
}
