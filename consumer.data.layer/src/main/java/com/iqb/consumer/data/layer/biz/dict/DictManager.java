package com.iqb.consumer.data.layer.biz.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.common.constant.CommonConstant.DictTypeCodeEnum;
import com.iqb.consumer.common.constant.DictConvertCenter.DICT_TYPE_CODE;
import com.iqb.consumer.common.constant.DictConvertCenter.TARGET_COLUMN;
import com.iqb.consumer.common.constant.DictConvertCenter.WHERE_COLUMN;
import com.iqb.consumer.data.layer.bean.dict.IqbSysDictItemEntity;
import com.iqb.consumer.data.layer.biz.BasicManager;
import com.iqb.consumer.data.layer.dao.dict.DictRepository;

@Component
public class DictManager extends BasicManager {

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

    public String replace(DICT_TYPE_CODE type, WHERE_COLUMN from, TARGET_COLUMN to, Object value) {
        if (type == null || from == null || to == null || value == null) {
            throw new RuntimeException("注解配置异常，请检查相关配置。");
        }
        return analysis(type, from, to, value);
    }

    private String analysis(DICT_TYPE_CODE type, WHERE_COLUMN from, TARGET_COLUMN to, Object value) {
        DynamicSQL ds = new DynamicSQL(to, from, value, type);
        super.setDb(0, super.SLAVE);
        return dictRepository.analysis(ds);
    }

}
