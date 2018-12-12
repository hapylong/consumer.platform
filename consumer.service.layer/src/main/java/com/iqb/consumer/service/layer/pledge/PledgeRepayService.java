package com.iqb.consumer.service.layer.pledge;

import com.alibaba.fastjson.JSONObject;

/**
 * Description:
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年4月10日    adam       1.0        1.0 Version 
 * </pre>
 */
public interface PledgeRepayService {

    Object repayByType(JSONObject requestMessage);

}
