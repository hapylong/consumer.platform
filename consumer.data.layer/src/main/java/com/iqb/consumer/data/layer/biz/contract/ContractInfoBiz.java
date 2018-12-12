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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.contract.ContractInfoBean;
import com.iqb.consumer.data.layer.bean.contract.OrderContractListBean;
import com.iqb.consumer.data.layer.dao.ContractInfoDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Repository
public class ContractInfoBiz extends BaseBiz {

    @Resource
    private ContractInfoDao contractInfoDao;

    public int insertContractInfo(JSONObject objs) {
        ContractInfoBean contractInfoBean = new ContractInfoBean();
        try {
            contractInfoBean = JSONObject.parseObject(JSON.toJSONString(objs), ContractInfoBean.class);
        } catch (Exception e) {
            return 2;
        }
        setDb(0, super.MASTER);
        if (contractInfoBean.getId() != null) {
            return contractInfoDao.updateContractInfo(contractInfoBean);
        } else {
            return contractInfoDao.insertContractInfo(contractInfoBean);
        }
    }

    /**
     * 通过订单号查询合同信息
     * 
     * @param orderId
     * @return
     */
    public ContractInfoBean selContractInfo(String orderId) {
        setDb(0, super.SLAVE);
        return contractInfoDao.selContractInfo(orderId);
    }

    public List<OrderContractListBean> orderContractInit(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return this.contractInfoDao.orderContractInit(objs);
    }

    public void updataStatus(ContractInfoBean contractInfoBean) {
        super.setDb(0, super.SLAVE);
        this.contractInfoDao.updataStatus(contractInfoBean);

    }

    public List<OrderContractListBean> orderContractFinish(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return this.contractInfoDao.orderContractFinish(objs);
    }
}
