package com.iqb.consumer.service.layer.pledge;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description: 质押车回调接口
 * 
 * @author guojuan
 */
@Component
public class PledgeInquiryWFService {

    /** 日志 **/
    private static final Logger logger = LoggerFactory.getLogger(PledgeInquiryWFService.class);

    @Autowired
    private IPledgeInquiryService pledgeInquiryService;

    public void after(JSONObject object) throws IqbException {
        logger.info("质押车更新状态");
        pledgeInquiryService.updateOrderInfo(object);
    }

    public void updateOrderInfoRiskStatus(Map<String, String> params) {
        pledgeInquiryService.updateOrderInfoRiskStatus(params);
    }
}
