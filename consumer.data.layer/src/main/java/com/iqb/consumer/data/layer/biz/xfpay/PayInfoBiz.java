/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月18日 下午2:32:14
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.xfpay;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.data.layer.bean.xfpay.PayInfoBean;
import com.iqb.consumer.data.layer.dao.PayInfoDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class PayInfoBiz extends BaseBiz {

    @Resource
    private PayInfoDao payInfoDao;

    public List<PayInfoBean> getByOrgId(int orgId) {
        setDb(0, super.SLAVE);
        return payInfoDao.getByOrgId(orgId);
    }

    public long insertPayInfo(PayInfoBean payInfoBean) {
        setDb(0, super.MASTER);
        return payInfoDao.insertPayInfo(payInfoBean);
    }

    public PayInfoBean getByCardNo(String cardNo, String orgId) {
        setDb(0, super.SLAVE);
        return payInfoDao.getByCardNo(cardNo, orgId);
    }

    public PayInfoBean getByIdAndOrgId(Map<String, Object> params) {
        setDb(0, super.SLAVE);
        return payInfoDao.getByIdAndOrgId(params);
    }

    public void unBindCardStatus(Map<String, Object> params) {
        setDb(0, super.MASTER);
        payInfoDao.unBindCardStatus(params);
    }

    public int updateBindCardInfo(PayInfoBean payInfoBean) {
        setDb(0, super.MASTER);
        return payInfoDao.updateBindCardInfo(payInfoBean);
    }

    public void delBindCardInfo(int bankId) {
        setDb(0, super.MASTER);
        payInfoDao.delBindCardInfo(bankId);
    }
}
