package com.iqb.consumer.data.layer.dao.dict;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.common.constant.CommonConstant.DictTypeCodeEnum;
import com.iqb.consumer.data.layer.bean.dict.IqbSysDictItemEntity;
import com.iqb.consumer.data.layer.biz.dict.DynamicSQL;

public interface DictRepository {

    List<IqbSysDictItemEntity> getDictListByDTC(DictTypeCodeEnum dtce);

    IqbSysDictItemEntity getDictByDTCAndDC(@Param("dtc") DictTypeCodeEnum dtc, @Param("dc") String dc);

    String analysis(DynamicSQL ds);

}
