package com.iqb.consumer.service.layer.contract;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.contract.ContractInfoBean;

/**
 * 合同接口 guojuan
 */
public interface ContractInfoService {

    /**
     * 插入合同补录信息
     * 
     * @param
     * @return
     */
    int insertContractInfo(JSONObject objs);

    /**
     * 查询商户父ID
     * 
     * @param objs
     * @return
     */
    JSONObject justMerchantType(JSONObject objs);

    /**
     * 通过订单查询合同信息
     * 
     * @param orderId
     * @return
     */
    ContractInfoBean selContractInfo(String orderId);

    /**
     * 生成电子合同还款明细信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月19日
     */
    Map<String, String> createRepaymentDetail(String orderId);

}
