/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @date 2016年9月20日 下午3:47:11
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;

import com.iqb.consumer.data.layer.bean.contract.ContractListBean;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
public interface ContractListDao {

    /**
     * 插入合同补录信息
     * 
     * @param
     * @return
     */
    int insertContractList(ContractListBean contractListBean);

    /**
     * 查询当前订单先全部合同列表
     * 
     * @param orderId
     * @return
     */
    List<ContractListBean> selContractList(String orderId);
}
