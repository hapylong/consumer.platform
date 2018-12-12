package com.iqb.consumer.batch.data.pojo;

import java.util.HashMap;
import java.util.Map;

public class EndProcessPojo {
    public static EndProcessPojo init() {
        EndProcessPojo epp = new EndProcessPojo();

        epp.getHmAuthData().put("procAuthType", "1");
        epp.getHmAuthData().put("procTokenUser", "f53674938794c432e1021584ffd963a6");
        epp.getHmAuthData().put("procTokenPass", "331493b0b9d8815135f6361bd1f83a7c");
        epp.getHmAuthData().put("procTaskUser", "batchStop");

        epp.getHmVariables().put("procSpecialDesc", "业务取消，流程强制终止");

        epp.getHmBizData().put("procBizType", "");
        return epp;
    }

    private Map<String, Object> hmProcData = new HashMap<>();
    private Map<String, Object> hmAuthData = new HashMap<>();
    private Map<String, Object> hmVariables = new HashMap<>();
    private Map<String, Object> hmBizData = new HashMap<>();

    public void setProcInstId(String id) {
        hmProcData.put("procInstId", id);
    }

    public void setProcBizId(String id) {
        hmBizData.put("procBizId", id);// √
    }

    public void setProcOrgCode(String code) {
        hmBizData.put("procOrgCode", code);// √
    }

    /****/

    public Map<String, Map<String, Object>> createRequestMap() {
        Map<String, Map<String, Object>> requestMap = new HashMap<>();
        requestMap.put("procData", hmProcData);
        requestMap.put("variableData", hmVariables);
        requestMap.put("authData", hmAuthData);
        requestMap.put("bizData", hmBizData);
        return requestMap;
    }

    public Map<String, Object> getHmProcData() {
        return hmProcData;
    }

    public Map<String, Object> getHmAuthData() {
        return hmAuthData;
    }

    public Map<String, Object> getHmVariables() {
        return hmVariables;
    }

    public Map<String, Object> getHmBizData() {
        return hmBizData;
    }

}
