/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午4:01:57
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.paylog;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.paylog.PaymentLogBean;
import com.iqb.consumer.data.layer.dao.PaymentLogDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class PaymentLogBiz extends BaseBiz {

    @Resource
    private PaymentLogDao paymentLogDao;

    public long insertPaymentLog(Map<String, String> params) {
        setDb(0, super.MASTER);
        return paymentLogDao.insertPaymentLog(params);
    }

    public Integer selSumAmount(JSONObject objs) {
        setDb(0, super.MASTER);
        return paymentLogDao.selSumAmount(objs);
    }

    public PaymentLogBean getPayLogByTradeNo(String tradeNo) {
        setDb(0, super.SLAVE);
        return paymentLogDao.getPayLogByTradeNo(tradeNo);
    }

    public List<PaymentLogBean> getPayLogByOrderId(String orderId, String flag) {
        setDb(0, super.SLAVE);
        return paymentLogDao.getPayLogByOrderId(orderId, flag);
    }

    public List<PaymentLogBean> getMersPaymentLogList(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return paymentLogDao.getMersPaymentLogList(objs);
    }

    public int getMersPaymentLogNum(JSONObject objs) {
        setDb(0, super.SLAVE);
        return paymentLogDao.getMersPaymentLogNum(objs);
    }

    public Long getMersPaymentLogAmt(JSONObject objs) {
        setDb(0, super.SLAVE);
        return paymentLogDao.getMersPaymentLogAmt(objs);
    }

    public List<PaymentLogBean> getMersPaymentLogListForSave(JSONObject objs) {
        setDb(0, super.SLAVE);
        return paymentLogDao.getMersPaymentLogList(objs);
    }

    public void persistLog(PaymentLogBean plb) {
        setDb(0, super.MASTER);
        paymentLogDao.persistLog(plb);
    }

    public PaymentLogBean getPayLogByTNo(String tradeNo) {
        setDb(0, super.SLAVE);
        return paymentLogDao.getPayLogByTNo(tradeNo);
    }
}
