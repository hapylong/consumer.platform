package com.iqb.consumer.service.layer.dict;

import java.util.List;

import com.iqb.consumer.common.constant.CommonConstant.DictTypeCodeEnum;
import com.iqb.consumer.data.layer.bean.plan.SysDictItem;

public interface DictService {

    /**
     * 
     * Description: 通过表字段DTC [DICT_TYPE_CODE] 获取list
     * 
     * @param
     * @return List<SysDictItem>
     * @throws
     * @Author adam Create Date: 2017年6月19日 下午2:16:50
     */
    List<SysDictItem> getDictListByDTC(DictTypeCodeEnum dtce);

    /**
     * 
     * Description: 通过表字段DTC [DICT_TYPE_CODE]和 DC [DICT_CODE] 获取一条记录 。
     * 
     * 例如： 通过DTC [bizType2OpenId]和DC [biztype]找到对应的openId
     * 
     * @param
     * @return String
     * @throws
     * @Author adam Create Date: 2017年6月19日 下午12:01:33
     */
    SysDictItem getDictByDTCAndDC(DictTypeCodeEnum dtc, String bizType);

    /**
     * 
     * Description: 通过openId 获取bizType
     * 
     * @param
     * @return String
     * @throws
     * @Author adam Create Date: 2017年6月19日 下午2:39:10
     */
    String getOpenIdByBizType(String bizType);

    String getOpenIdByOrderId(String orderId);

    String getJysOpenIdByOrderId(String orderId);

    String getOpenIdByPath(String path);

    String getOpenIdByHoId(String hoId);

    public <T> List<T> changeDictValue(List<T> list) throws Exception;

    SysDictItem getDictByDTCAndDN(DictTypeCodeEnum dtc, String bizType);
}
