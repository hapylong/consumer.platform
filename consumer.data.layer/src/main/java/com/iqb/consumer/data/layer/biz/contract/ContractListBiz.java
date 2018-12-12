/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午4:01:57
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.contract;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.data.layer.bean.contract.ContractListBean;
import com.iqb.consumer.data.layer.dao.ContractListDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Repository
public class ContractListBiz extends BaseBiz {

    @Resource
    private ContractListDao contractListDao;

    public int insertContractList(ContractListBean contractListBean) {
        setDb(0, super.MASTER);
        return contractListDao.insertContractList(contractListBean);
    }

    public List<ContractListBean> selContractList(String orderId) {
        setDb(0, super.MASTER);
        return contractListDao.selContractList(orderId);
    }
}
