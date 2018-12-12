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
 * 2018年3月30日下午3:51:37 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
public interface HuayiLoanService {
    /**
     * 
     * Description:华益周转贷订单提交、流程启动
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月30日
     */
    Map<String, String> startHuayiLoanProcess(JSONObject objs) throws IqbException, GenerallyException;
}
