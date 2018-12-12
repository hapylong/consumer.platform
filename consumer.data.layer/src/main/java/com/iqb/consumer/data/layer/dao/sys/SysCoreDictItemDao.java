package com.iqb.consumer.data.layer.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.common.constant.CommonConstant.DictTypeCodeEnum;
import com.iqb.consumer.data.layer.bean.plan.SysDictItem;

public interface SysCoreDictItemDao {

    /**
     * 通过字典类型查询所有字典信息
     * 
     * @param dictDypeCode
     * @return
     */
    List<SysDictItem> selDictItem(@Param("dictDypeCode") String dictDypeCode);

    /**
     * 通过业务类型名称查询业务是否存在
     * 
     * @param dictName
     * @return
     */
    List<SysDictItem> selBizType(@Param("dictName") String dictName);

    SysDictItem getDictByDTCAndDC(@Param("dtc") DictTypeCodeEnum dtc, @Param("dc") String dc);

    SysDictItem getDictByDTCAndDN(@Param("dtc") DictTypeCodeEnum dtc, @Param("dName") String dName);

}
