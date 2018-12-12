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
 * 2017年8月16日下午4:23:19 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.manage.front.house;

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
public class HouseAssetControllerTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 查询房贷资产分配信息(带分页)
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testSelectInstSettleConfigResultByParams() {
        Map<String, Object> params2 = new HashMap<String, Object>();
        params2.put("pageNum", "1");
        params2.put("pageSize", "10");

        LinkedHashMap result2 =
                SendHttpsUtil.postMsg4GetMap(
                        "http://127.0.0.1:8080/consumer.manage.front/houseAsset/unIntcpt-query",
                        params2);
        logger.debug("车辆状态跟踪-保存评估金额结果:{}", result2);
    }

    /**
     * Description:查询房贷资产分配详情 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testSelectHouseAssetDetailByParams() {
        Map<String, Object> params2 = new HashMap<String, Object>();
        params2.put("orderId", "DZ20170814001");

        LinkedHashMap result2 =
                SendHttpsUtil.postMsg4GetMap(
                        "http://127.0.0.1:8080/consumer.manage.front/houseAsset/unIntcpt-queryDetail",
                        params2);
        logger.debug("车辆状态跟踪-保存评估金额结果:{}", result2);
    }

}
