/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午8:50:49
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.bank;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.riskinfo.LocalRiskInfoBean;
import com.iqb.consumer.data.layer.dao.BankCardBeanDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class BankCardBeanBiz extends BaseBiz {

    @Resource
    private BankCardBeanDao bankCardBeanDao;

    /**
     * 通过用户Id查询开户绑卡信息
     * 
     * @param userId
     * @return
     */
    public BankCardBean getOpenBankCardByRegId(String userId) {
        setDb(0, super.SLAVE);
        return bankCardBeanDao.getOpenBankCardByRegId(userId);
    }

    /**
     * 保存用户银行卡信息
     * 
     * @param bankCardBean
     * @return
     */
    public long saveBankCard(BankCardBean bankCardBean) {
        setDb(0, super.MASTER);
        return bankCardBeanDao.saveBankCard(bankCardBean);
    }

    /**
     * 根据userId修改银行卡号
     * 
     * @param bankCardBean
     * @return
     */
    public long updateBankCardNoByUserId(@Param("userId") String userId, @Param("bankCardNo") String bankCardNo,
            @Param("bankName") String bankName) {
        setDb(0, super.MASTER);
        return bankCardBeanDao.updateBankCardNoByUserId(userId, bankCardNo, bankName);
    }

    /**
     * 
     * Description:批量插入银行卡信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月12日
     */
    public long batchSaveBankCard(List<BankCardBean> list) {
        setDb(0, super.MASTER);
        return bankCardBeanDao.batchSaveBankCard(list);
    }

    /**
     * 根据用户id 卡状态查询卡信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年2月1日
     */
    public BankCardBean selectBankCardInfoByUserId(@Param("userId") String userId, @Param("status") String status) {
        setDb(1, super.MASTER);
        return bankCardBeanDao.selectBankCardInfoByUserId(userId, status);
    }

    /**
     * 根据商户简拼查询商户号
     * 
     * @param @param merchanNo
     * @param @return
     * @return Long
     * @author chengzhen
     * @data 2018年3月26日 10:51:42
     */
    public String getMerchanIdByMerchanNo(String merchanNo) {
        setDb(1, super.SLAVE);
        return bankCardBeanDao.getMerchanIdByMerchanNo(merchanNo);
    }

    public void saveReprotContent(JSONObject objs) {
        setDb(1, super.MASTER);
        bankCardBeanDao.saveReprotContent(objs);
    }

    public LocalRiskInfoBean selectLocalInfoByNoAndReportName(String tradeNo, String reportType) {
        setDb(1, super.MASTER);
        return bankCardBeanDao.selectLocalInfoByNoAndReportName(tradeNo, reportType);
    }

    public void updateLocalRiskInfoBean(LocalRiskInfoBean localBean) {
        setDb(1, super.MASTER);
        bankCardBeanDao.updateLocalRiskInfoBean(localBean);
    }

    public Map getCodeAndKeyByMerchanNo(String merchantNo) {
        setDb(1, super.SLAVE);
        return bankCardBeanDao.getCodeAndKeyByMerchanNo(merchantNo);
    }
}
