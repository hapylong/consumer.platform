package com.iqb.consumer.batch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.batch.data.entity.IqbSysDictItemEntity;
import com.iqb.consumer.batch.util.Constant.DictTypeCodeEnum;

public interface DictRepository {

    List<IqbSysDictItemEntity> getDictListByDTC(DictTypeCodeEnum dtce);

    IqbSysDictItemEntity getDictByDTCAndDC(@Param("dtc") DictTypeCodeEnum dtc, @Param("dc") String dc);

}
