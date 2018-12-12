package com.iqb.consumer.batch.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.batch.dao.DictRepository;
import com.iqb.consumer.batch.data.entity.IqbSysDictItemEntity;
import com.iqb.consumer.batch.util.Constant.DictTypeCodeEnum;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class DictManager extends BaseBiz {

    @Autowired
    DictRepository dictRepository;

    public List<IqbSysDictItemEntity> getDictListByDTC(DictTypeCodeEnum dtce) {
        super.setDb(0, super.SLAVE);
        return dictRepository.getDictListByDTC(dtce);
    }

    public IqbSysDictItemEntity getDictByDTCAndDC(DictTypeCodeEnum dtc, String dc) {
        super.setDb(0, super.SLAVE);
        return dictRepository.getDictByDTCAndDC(dtc, dc);
    }

}
