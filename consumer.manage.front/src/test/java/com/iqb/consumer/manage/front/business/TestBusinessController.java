/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月23日下午6:41:23 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.manage.front.business;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;

/**
 * @author crw
 * 
 */
public class TestBusinessController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    @Test
    public void testSend2RiskForDandelion() {
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("orderId", "CDHTC3001170517007");
        LinkedHashMap result2 =
                SendHttpsUtil.postMsg4GetMap(
                        // "http://101.201.151.38:8088/consumer.manage.front/business/send2RiskForDandelion",
                        "http://127.0.0.1:8080/consumer.manage.front/business/send2RiskForDandelion",
                        params2);
        logger.debug("调用风控结果:{}", result2);

    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testUnBindBankCard() {
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("userId", "13185905557");
        params2.put("bankCardNo", "6230580000128992216");
        LinkedHashMap result2 =
                SendHttpsUtil.postMsg4GetMap(
                        "http://127.0.0.1:8080/consumer.manage.front/business/unBindBankCard",
                        params2);
        logger.debug("银行卡解绑结果:{}", result2);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testGetBindBankCard() {
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("userId", "18911908439");
        LinkedHashMap result2 =
                SendHttpsUtil.postMsg4GetMap(
                        "http://127.0.0.1:8080/consumer.manage.front/business/getBindBankCard",
                        params2);
        logger.debug("查询银行卡绑定结果:{}", result2);
    }

    @Test
    public void testAutoSwitchUnBindBankCard() {
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("regId", "13681486576");
        params2.put("bankCardNo", "6226225200195078");
        LinkedHashMap result2 =
                SendHttpsUtil.postMsg4GetMap(
                        "http://127.0.0.1:8080/consumer.manage.front/business/autoSwitchUnBindBankCard",
                        params2);
        logger.debug("查询银行卡绑定结果:{}", result2);
    }
}
