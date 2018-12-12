/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午8:53:47
 * @version V1.0
 */
package com.iqb.consumer.service.layer.business.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.riskinfo.LocalRiskInfoBean;
import com.iqb.consumer.data.layer.biz.bank.BankCardBeanBiz;
import com.iqb.consumer.service.layer.business.service.IBankCardService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service("bankCardService")
public class BankCardServiceImpl implements IBankCardService {
    protected static final Logger logger = LoggerFactory.getLogger(BankCardServiceImpl.class);
    @Resource
    private BankCardBeanBiz bankCardBeanBiz;

    @Override
    public BankCardBean getOpenBankCardByRegId(String userId) {
        return bankCardBeanBiz.getOpenBankCardByRegId(userId);
    }

    @Override
    public String getMerchanIdByMerchanNo(String merchanNo) {
        return bankCardBeanBiz.getMerchanIdByMerchanNo(merchanNo);
    }

    @Override
    public void saveReprotContent(JSONObject objs) {
        // 保存报告内容
        logger.info("回调参数:{}", objs);
        JSONArray jsonArray = objs.getJSONArray("result");
        for (int index = 0; index < jsonArray.size(); index++) {
            // 保存
            String reportType = jsonArray.getJSONObject(index).getString("reportType");
            // 根绝交易号和reportName查询出对应的Bean
            if ("BaseReport".equals(reportType)) {
                reportType = "1";
            } else {
                reportType = "2";
            }
            LocalRiskInfoBean localBean =
                    bankCardBeanBiz.selectLocalInfoByNoAndReportName(objs.get("tradeNo").toString(), reportType);
            logger.info("根据回调参数查询的Bean:{}", localBean);
            if ("1".equals(reportType)) {
                localBean.setReportNo(jsonArray.getJSONObject(index).getString("reportNo"));// 添加报告编号
                localBean.setReportContent(jsonArray.getJSONObject(index).getString("personResult"));// 添加报告内容
                localBean.setContentCreateTime1(jsonArray.getJSONObject(index).getString("reportFinishTime"));
            } else {
                localBean.setReportNo(jsonArray.getJSONObject(index).getString("reportNo"));// 添加报告内容
                localBean.setReportContent(jsonArray.getJSONObject(index).getString("loanResult"));// 添加报告内容
                localBean.setContentCreateTime2(jsonArray.getJSONObject(index).getString("reportFinishTime"));
            }
            // 更新Bean
            this.updateLocalRiskInfoBean(localBean);
        }
    }

    private void updateLocalRiskInfoBean(LocalRiskInfoBean localBean) {
        bankCardBeanBiz.updateLocalRiskInfoBean(localBean);
    }

    @Override
    public Map getCodeAndKeyByMerchanNo(String merchantNo) {
        return bankCardBeanBiz.getCodeAndKeyByMerchanNo(merchantNo);
    }

}
