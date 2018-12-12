package com.iqb.consumer.service.layer.dandelion;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年5月14日上午11:57:40 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface HouseLoanService {
    /**
     * 
     * Description:启动房融贷工作流
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月14日
     */
    Map<String, String> startHouseLoanProcess(JSONObject objs) throws GenerallyException, IqbException;

    /**
     * 
     * Description:用户信息三码鉴权(姓名 身份证 银行卡)
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月14日
     */
    public Map<String, String> userInfoAuthByThree(String type, JSONObject objs);
}
