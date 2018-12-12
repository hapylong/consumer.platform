package com.iqb.consumer.data.layer.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.contract.ContractInfoBean;
import com.iqb.consumer.data.layer.bean.contract.OrderContractListBean;

/**
 * @author guojuan
 */
public interface ContractInfoDao {

    /**
     * 插入合同补录信息
     * 
     * @param
     * @return
     */
    int insertContractInfo(ContractInfoBean contractInfoBean);

    /**
     * 通过订单号查询合同信息
     * 
     * @param orderId
     * @return
     */
    ContractInfoBean selContractInfo(String orderId);

    /**
     * 查询待签约订单信息
     * 
     * @param objs
     * @return
     */
    List<OrderContractListBean> orderContractInit(JSONObject objs);

    /**
     * 更新合同补录信息
     * 
     * @param contractInfoBean
     * @return
     */
    int updateContractInfo(ContractInfoBean contractInfoBean);

    /**
     * 更新合同订单状态
     * 
     * @param contractInfoBean
     */
    void updataStatus(ContractInfoBean contractInfoBean);

    /**
     * 获取所有正在签约货已经签约的订单
     * 
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param objs
     * @return 设定文件 List<OrderContractListBean> 返回类型
     * @throws
     * @author guojuan 2017年10月11日下午5:38:48
     */
    List<OrderContractListBean> orderContractFinish(JSONObject objs);
}
