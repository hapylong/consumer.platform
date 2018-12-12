package com.iqb.consumer.service.layer.biz;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("rawtypes")
public class WfProcTaskRestTestCase {
    private String baseUrl = "http://101.201.151.38:8088/consumer.manage.front/";

    @Test
    public void startAndCommitProcess() {
        // int type = 10; // 启动流程
        // int type = 11; // 通过Token启动流程
        // int type = 20; // 启动并提交流程
        int type = 21; // 通过Token启动并提交流程
        // int type = 30; // 通过流程任务ID签收
        // int type = 31; // 通过流程任务代码签收
        // int type = 40; // 通过流程任务ID取消签收
        // int type = 41; // 通过流程任务代码取消签收
        // int type = 50; // 通过流程任务ID审批流程任务
        // int type = 51; // 通过流程任务代码审批流程任务
        // int type = 60; // 通过流程任务ID删除流程任务
        // int type = 61; // 通过流程任务代码删除流程任务

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");
        Object data = JSON.toJSON(getRequestData(type));
        System.out.println(data);
        HttpEntity entity = new HttpEntity<Object>(data, headers);
        ResponseEntity<Map> response =
                restTemplate.exchange(getRequestUrl(type), HttpMethod.POST, entity, Map.class);
        System.out.println("&&&&&&&&&&&&&&&&&&&" + response);
        LinkedHashMap responseMap = (LinkedHashMap) response.getBody();
        System.out.print("*******************" + responseMap);
        assertTrue(true);
    }

    private String getRequestUrl(int type) {
        switch (type) {
            case 10:
                return baseUrl + "WfTask/startProcess";
            case 11:
                return baseUrl + "WfTask/startProcessByToken";
            case 20:
                return baseUrl + "WfTask/startAndCommitProcess";
            case 21:
                return baseUrl + "WfTask/startAndCommitProcessByToken";
            case 30:
                return baseUrl + "WfTask/claimProcess";
            case 31:
                return baseUrl + "WfTask/claimProcessByTaskCode";
            case 40:
                return baseUrl + "WfTask/unclaimProcess";
            case 41:
                return baseUrl + "WfTask/unclaimProcessByTaskCode";
            case 50:
                return baseUrl + "WfTask/completeProcess";
            case 51:
                return baseUrl + "WfTask/completeProcessByTaskCode";
            case 60:
                return baseUrl + "WfTask/deleteProcess";
            case 61:
                return baseUrl + "WfTask/deleteProcessByTaskCode";
            default:
                return null;
        }
    }

    private Map getRequestData(int type) {
        Map map = new HashMap();

        switch (type) {
            case 10:
                return getStartProcessData();
            case 11:
                return getStartProcessDataByToken();
            case 20:
                return getStartAndCompleteProcessData();
            case 21:
                return getStartAndCompleteProcessDataByToken();
            case 30:
                return getClaimProcessDataById();
            case 31:
                return getClaimProcessDataByCode();
            case 40:
                return getUnclaimProcessData();
            case 41:
                return getUnclaimProcessDataByCode();
            case 50:
                return getcompleteProcessData();
            case 51:
                return getcompleteProcessDataByCode();
            case 60:
                return getDeleteProcessData();
            case 61:
                return getDeleteProcessDataByCode();
            default:
                break;
        }

        return map;
    }

    private Map<String, Map<String, Object>> getStartProcessData() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procDefKey", "test_asset");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "2"); // 1：token认证；2：session认证
        // hmVariables.put("procTaskUser", "user1");
        // hmVariables.put("procTaskRole", "role1");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "10000");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("procBizMemo", "业务流程测试");
        hmBizData.put("money1", 10);
        hmBizData.put("money2", 20);
        hmBizData.put("amount", 50001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getStartProcessDataByToken() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procDefKey", "test_asset");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "1"); // 1：token认证；2：session认证
        hmVariables.put("procTokenUser", "f53674938794c432e1021584ffd963a6");
        hmVariables.put("procTokenPass", "331493b0b9d8815135f6361bd1f83a7c");
        hmVariables.put("procTaskUser", "user1");
        hmVariables.put("procTaskRole", "role1");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "10001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("procBizMemo", "业务流程测试");
        hmBizData.put("money1", 10);
        hmBizData.put("money2", 20);
        hmBizData.put("amount", 50001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getStartAndCompleteProcessData() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procDefKey", "test_asset");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "2"); // 1：token认证；2：session认证
        // hmVariables.put("procTaskUser", "user1");
        // hmVariables.put("procTaskRole", "role1");
        hmVariables.put("procApprStatus", "1");
        hmVariables.put("procApprOpinion", "同意");
        hmVariables.put("procAssignee", "");
        hmVariables.put("procAppointTask", "");

        hmVariables.put("amount", 500001);
        hmVariables.put("Test1", "Test1");
        hmVariables.put("Test2", "Test2");
        hmVariables.put("Test3", "Test3");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "1001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("procBizMemo", "业务流程测试");
        hmBizData.put("money1", 10);
        hmBizData.put("money2", 20);
        hmBizData.put("amount", 5001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getStartAndCompleteProcessDataByToken() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procDefKey", "leaseprocess");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "1"); // 1：token认证；2：session认证
        hmVariables.put("procTokenUser", "f53674938794c432e1021584ffd963a6");
        hmVariables.put("procTokenPass", "331493b0b9d8815135f6361bd1f83a7c");
        hmVariables.put("procTaskUser", "13701259346");
        hmVariables.put("procTaskRole", "iqb_vc1");

        hmVariables.put("procApprStatus", "1");
        hmVariables.put("procApprOpinion", "同意");
        hmVariables.put("procAssignee", "");
        hmVariables.put("procAppointTask", "");

        hmVariables.put("amount", 500001);
        hmVariables.put("Test1", "Test1");
        hmVariables.put("Test2", "Test2");
        hmVariables.put("Test3", "Test3");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "1001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("procBizMemo", "业务流程测试");
        hmBizData.put("money1", 10);
        hmBizData.put("money2", 20);
        hmBizData.put("amount", 5001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getClaimProcessDataById() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procTaskId", "6b2824ae-83b9-11e6-be39-64006a272fcf");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "2"); // 1：token认证；2：session认证
        // hmVariables.put("procTaskUser", "user5");
        // hmVariables.put("procTaskRole", "role5");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "1001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("amount", 5001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getClaimProcessDataByCode() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procInstId", "6b2824ae-83b9-11e6-be39-64006a272fcf");
        hmProcData.put("procTaskCode", "lease_approve");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "1"); // 1：token认证；2：session认证
        hmVariables.put("procTokenUser", "f53674938794c432e1021584ffd963a6");
        hmVariables.put("procTokenPass", "331493b0b9d8815135f6361bd1f83a7c");
        hmVariables.put("procTaskUser", "user6");
        hmVariables.put("procTaskRole", "role6");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "1001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("amount", 5001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getUnclaimProcessData() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procTaskId", "ececfcb8-80c7-11e6-ad4a-00ff507ac4ce");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "2"); // 1：token认证；2：session认证
        // hmVariables.put("procTaskUser", "user2");
        // hmVariables.put("procTaskRole", "role2");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "1001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("amount", 5001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getUnclaimProcessDataByCode() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procInstId", "c249ead4-80c7-11e6-ad4a-00ff507ac4ce");
        hmProcData.put("procTaskCode", "comprehensive_approve");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "1"); // 1：token认证；2：session认证
        hmVariables.put("procTokenUser", "f53674938794c432e1021584ffd963a6");
        hmVariables.put("procTokenPass", "331493b0b9d8815135f6361bd1f83a7c");
        hmVariables.put("procTaskUser", "user2");
        hmVariables.put("procTaskRole", "role2");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "1001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("amount", 5001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getcompleteProcessData() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procTaskId", "b583505f-8174-11e6-a52a-00ff62b6d916");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "2"); // 1：token认证；2：session认证
        // hmVariables.put("procTaskUser", "user2");
        // hmVariables.put("procTaskRole", "role2");

        hmVariables.put("procApprStatus", "0");
        hmVariables.put("procApprOpinion", "同意");
        hmVariables.put("procAssignee", "");
        hmVariables.put("procAppointTask", "");

        hmVariables.put("amount", 500001);
        hmVariables.put("Test1", "Test1");
        hmVariables.put("Test2", "Test2");
        hmVariables.put("Test3", "Test3");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "1001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("amount", 500001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getcompleteProcessDataByCode() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procInstId", "c12f6f80-80c6-11e6-ad4a-00ff507ac4ce");
        hmProcData.put("procTaskCode", "lease_approve");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "1"); // 1：token认证；2：session认证
        hmVariables.put("procTokenUser", "f53674938794c432e1021584ffd963a6");
        hmVariables.put("procTokenPass", "331493b0b9d8815135f6361bd1f83a7c");
        hmVariables.put("procTaskUser", "user6");
        hmVariables.put("procTaskRole", "role6");

        hmVariables.put("procApprStatus", "1");
        hmVariables.put("procApprOpinion", "同意");
        hmVariables.put("procAssignee", "");
        hmVariables.put("procAppointTask", "");
        hmVariables.put("amount", 500001);

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "1001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("amount", 500001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getDeleteProcessData() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procTaskId", "f0cb6682-8174-11e6-a52a-00ff62b6d916");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "2"); // 1：token认证；2：session认证
        // hmVariables.put("procTaskUser", "user1");
        // hmVariables.put("procTaskRole", "role1");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "1001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("amount", 500001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }

    private Map<String, Map<String, Object>> getDeleteProcessDataByCode() {
        Map<String, Map<String, Object>> reqData = new HashMap<String, Map<String, Object>>();

        Map<String, Object> hmProcData = new HashMap<String, Object>();
        hmProcData.put("procInstId", "25696c55-80c4-11e6-ad4a-00ff507ac4ce");
        hmProcData.put("procTaskCode", "risk_approve");

        Map<String, Object> hmVariables = new HashMap<String, Object>();
        hmVariables.put("procAuthType", "1"); // 1：token认证；2：session认证
        hmVariables.put("procTokenUser", "f53674938794c432e1021584ffd963a6");
        hmVariables.put("procTokenPass", "331493b0b9d8815135f6361bd1f83a7c");
        hmVariables.put("procTaskUser", "user1");
        hmVariables.put("procTaskRole", "role1");

        Map<String, Object> hmBizData = new HashMap<String, Object>();
        hmBizData.put("procBizId", "1001");
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", "1001001");
        hmBizData.put("amount", 500001);

        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        return reqData;
    }
}
