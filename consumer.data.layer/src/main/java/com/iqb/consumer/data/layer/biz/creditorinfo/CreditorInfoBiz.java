package com.iqb.consumer.data.layer.biz.creditorinfo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;
import com.iqb.consumer.data.layer.dao.CreditorInfoDao;
import com.iqb.etep.common.base.biz.BaseBiz;

@Component
public class CreditorInfoBiz extends BaseBiz {

    @Resource
    private CreditorInfoDao creditorInfoDao;

    public int insertCreditorInfo(CreditorInfoBean creditorInfoBean) {
        setDb(0, super.MASTER);
        return creditorInfoDao.insertCreditorInfo(creditorInfoBean);
    }

    public CreditorInfoBean selectOneByOrderId(String orderId) {
        setDb(0, super.SLAVE);
        return creditorInfoDao.selectOneByOrderId(orderId);
    }

    public int updateCreditorInfo(CreditorInfoBean creditorInfoBean) {
        setDb(0, super.MASTER);
        return creditorInfoDao.updateCreditorInfo(creditorInfoBean);
    }

}
