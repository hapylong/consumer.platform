package com.iqb.consumer.manage.front.dealer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.manage.front.AbstractController;

public class TestDealerManageRest extends AbstractController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testSaveDealer() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", "Test201705260001");
        params.put("custChannels", "客户渠道");
        params.put("custChannelsName", "客户渠道名称");
        params.put("sourceCar", "车源");
        params.put("sourceCarName", "车源名称");
        params.put("address", "北京");
        params.put("contactMethod", "联系方式");
        params.put("maritalStatus", "婚姻状态");
        params.put("contactName", "联系名称");
        params.put("contactMobile", "联系电话");
        params.put("contactAddr", "联系地址");
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap(BASEURL + "/dealer/saveDealerInfo", params);
        logger.info("" + returnMap);
    }

    @Test
    public void selDealerInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("custChannels", "update渠道");
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap(BASEURL + "/dealer/getDealerInfo", params);
        logger.info("" + returnMap);
    }

    @Test
    public void testJudgeBlackInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("custChannelsName", "客户渠道名称1");
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap(BASEURL + "/dealer/judgeBlack", params);
        logger.info("" + returnMap);
    }

    @Test
    public void updateSaveDealer() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", "Test201705260001");
        params.put("custChannels", "update渠道");
        params.put("custChannelsName", "update客户渠道名称");
        params.put("sourceCar", "update车源");
        params.put("sourceCarName", "update车源名称");
        params.put("contactMethod", "update联系方式");
        params.put("maritalStatus", "update婚姻状态");
        params.put("contactName", "update联系名称");
        params.put("contactMobile", "update联系电话");
        params.put("contactAddr", "update联系地址");
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap(BASEURL + "/dealer/updateDealerInfo", params);
        logger.info("" + returnMap);
    }

    @Test
    public void updateDealerInfoStatus() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ids", "1,3");
        params.put("flag", "1");
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap(BASEURL + "/dealer/updateDealerInfoStatus", params);
        logger.info("" + returnMap);
    }
}
