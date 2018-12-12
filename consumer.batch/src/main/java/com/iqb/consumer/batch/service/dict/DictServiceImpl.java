package com.iqb.consumer.batch.service.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iqb.consumer.batch.biz.DictManager;
import com.iqb.consumer.batch.data.entity.IqbSysDictItemEntity;
import com.iqb.consumer.batch.util.Constant.DictTypeCodeEnum;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictManager dictManager;

    @Override
    public List<IqbSysDictItemEntity> getDictListByDTC(DictTypeCodeEnum dtce) {
        if (dtce != null) {
            return dictManager.getDictListByDTC(dtce);
        } else {
            return null;
        }
    }

    @Override
    public IqbSysDictItemEntity getDictByDTCAndDC(DictTypeCodeEnum dtc, String dc) {
        return dictManager.getDictByDTCAndDC(dtc, dc);
    }

    @Override
    public String getOpenIdByBizType(String bizType) {
        IqbSysDictItemEntity sdi = dictManager.getDictByDTCAndDC(DictTypeCodeEnum.bizType2OpenId, bizType);
        if (sdi == null) {
            throw new RuntimeException("not found in db.");
        }
        return sdi.getDictValue();
    }
}
