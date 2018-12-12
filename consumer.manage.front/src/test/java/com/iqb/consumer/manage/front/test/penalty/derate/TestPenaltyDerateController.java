package com.iqb.consumer.manage.front.test.penalty.derate;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.iqb.consumer.manage.front.test.penalty.derate.common.AbstractConstant;
import com.iqb.consumer.manage.front.test.penalty.derate.util.HttpClientUtil;

/**
 * 
 * Description: 罚息测试类
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月9日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class TestPenaltyDerateController extends AbstractConstant {

    /** 日志 **/
    protected static final Logger logger = LoggerFactory.getLogger(TestPenaltyDerateController.class);

    /**
     * 
     * Description: 测试获取罚息减免申请列表
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2017年3月10日 下午1:55:51
     */
    @Test
    public void testListPenaltyDerateApply() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bizId", "12345678");

        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "unIntcpt-penaltyDerate/listPenaltyDerateApply", json);
        logger.info("返回结果:{}", result);
    }

    /**
     * 
     * Description: 获取可进行罚息减免列表
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午5:23:57
     */
    @Test
    public void testListPenaltyDeratable() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        // params.put("getOverdueBill", "1");

        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "unIntcpt-penaltyDerate/listPenaltyDeratable", json);
        logger.info("返回结果:{}", result);
    }

}
