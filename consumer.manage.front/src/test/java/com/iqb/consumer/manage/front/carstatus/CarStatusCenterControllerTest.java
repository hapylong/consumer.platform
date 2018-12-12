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
 * 2017年7月10日下午4:48:55 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.manage.front.carstatus;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;

/**
 * @author crw
 * 
 */
public class CarStatusCenterControllerTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Test method for
     * {@link com.iqb.consumer.manage.front.carstatus.CarStatusCenterController#getGroupCode()}.
     */
    @Test
    public void testGetGroupCode() {
        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.iqb.consumer.manage.front.carstatus.CarStatusCenterController#cgetInfo(com.alibaba.fastjson.JSONObject, javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testCgetInfo() {
        fail("Not yet implemented");
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testSelOrderInfo() {
        Map<String, Object> params2 = new HashMap<String, Object>();
        params2.put("orderId", "CDHTC2002170630007");

        LinkedHashMap result2 =
                SendHttpsUtil.postMsg4GetMap(
                        "http://127.0.0.1:8080/consumer.manage.front/carstatus/unIntcpt-selOrderInfo",
                        params2);
        logger.debug("车辆状态跟踪-保存评估金额结果:{}", result2);
    }

    /**
     * Test method for
     * {@link com.iqb.consumer.manage.front.carstatus.CarStatusCenterController#saveAssessInfo(com.alibaba.fastjson.JSONObject, javax.servlet.http.HttpServletResponse)}
     * .
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testSaveAssessInfo() {
        Map<String, String> params1 = new HashMap<String, String>();
        params1.put("dealerAmt", "10000");
        params1.put("dealerName", "郝金龙");
        params1.put("dealerMobile", "13681486576");

        Map<String, Object> params2 = new HashMap<String, Object>();
        params2.put("orderId", "CDHTC2002170630007");
        params2.put("assessAmt", "10000");
        params2.put("dealerEvaluatesInfo", JSONObject.toJSONString(params1));
        params2.put("saleRemark", "车辆出售备注");
        LinkedHashMap result2 =
                SendHttpsUtil.postMsg4GetMap(
                        "http://127.0.0.1:8080/consumer.manage.front/carstatus/unIntcpt-saveAssessInfo",
                        params2);
        logger.debug("车辆状态跟踪-保存评估金额结果:{}", result2);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testSaveSubleaseInfo() {
        Map<String, String> params1 = new HashMap<String, String>();
        params1.put("subleaseOrderId", "SYQS2002170629001");
        params1.put("subleaseRealName", "郝金龙");
        params1.put("subleaseOrderItems", "36");
        params1.put("subleaseRegId", "13681486576");
        params1.put("subleaseAmount", "130000");
        params1.put("subleasePlan", "上收息(24个月)");
        params1.put("subleasePreAmount", "26520");
        params1.put("subleaseMonthInterest", "3979.44");

        Map<String, Object> params2 = new HashMap<String, Object>();
        params2.put("orderId", "CDHTC2002170630007");
        params2.put("subleaseInfo", JSONObject.toJSONString(params1));

        LinkedHashMap result2 =
                SendHttpsUtil.postMsg4GetMap(
                        "http://127.0.0.1:8080/consumer.manage.front/carstatus/saveSubleaseInfo",
                        params2);
        logger.debug("车辆状态跟踪-保存承租人结果:{}", result2);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testSaveReturnInfo() {
        Map<String, String> params1 = new HashMap<String, String>();
        params1.put("isCharge", "SYQS2002170629001");
        params1.put("feePlan", "上收息(24个月)");
        params1.put("returnAmonut", "26520");

        Map<String, Object> params2 = new HashMap<String, Object>();
        params2.put("orderId", "CDHTC2002170630007");
        params2.put("returnInfo", JSONObject.toJSONString(params1));

        LinkedHashMap result2 =
                SendHttpsUtil.postMsg4GetMap(
                        "http://127.0.0.1:8080/consumer.manage.front/carstatus/unIntcpt-saveReturnInfo",
                        params2);
        logger.debug("车辆状态跟踪-保存车辆返还结果:{}", result2);
    }

    public static void main(String[] args) {
        Map<String, Object> params1 = new HashMap<String, Object>();
        params1.put("subleaseOrderId", "SYQS2002170629001");
        params1.put("subleaseRealName", "郝金龙");
        params1.put("subleaseOrderItems", "36");
        params1.put("subleaseRegId", "13681486576");
        params1.put("subleaseAmount", "130000");
        params1.put("subleasePlan", "上收息(24个月)");
        params1.put("subleasePreAmount", "26520");
        params1.put("subleaseMonthInterest", "3979.44");

        Map<String, Object> params2 = new HashMap<String, Object>();
        params2.put("orderId", "CDHTC2002170630007");
        params2.put("subleaseInfo", params1);
        System.out.println(JSONObject.toJSONString(params2));
    }
}
