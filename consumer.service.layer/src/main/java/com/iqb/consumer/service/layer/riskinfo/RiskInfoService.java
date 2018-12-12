package com.iqb.consumer.service.layer.riskinfo;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.user.pojo.GetUserRiskInfoResponsePojo;

public interface RiskInfoService {

    GetUserRiskInfoResponsePojo getUserRiskInfoByRT(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description:以租代售-发送风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    Map<String, Object> send2Risk(Map<String, String> map);

    /**
     * 
     * Description:抵押车-发送风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    Map<String, Object> send2Risk4Pledge(Map<String, String> map);

    /**
     * 
     * Description:蒲公英行-发送风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    Map<String, Object> send2RiskForDandelion(Map<String, String> map);

    /**
     * 
     * Description:车主贷-发送风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月14日
     */
    Map<String, Object> send3Risk(Map<String, String> map);
}
