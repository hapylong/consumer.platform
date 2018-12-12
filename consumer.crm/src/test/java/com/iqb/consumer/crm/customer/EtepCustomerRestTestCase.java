package com.iqb.consumer.crm.customer;

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

/**
 * 
 * Description: 测试用例
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月21日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class EtepCustomerRestTestCase {

    private String uri = "http://192.168.1.194/consumer.manage.front";

    private String xfjrUri = "http://192.168.1.155:8080/consumer.manage.front";

    @Test
    public void saveCustomerInfo() {
        uri = uri + "/customer/saveCustomerInfoFromEtep";

        System.out.println(uri);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");

        Map map = new HashMap();
        map.put("customerCode", "2");
        map.put("customerName", "2");
        map.put("customerShortName", "2");
        map.put("customerFullName", "2");
        map.put("customerType", "2");
        map.put("province", "2");
        map.put("city", "2");
        map.put("addressDetail", "2");
        map.put("customerStatus", "2");
        map.put("createTime", "2");
        map.put("deleteFlag", "2");
        map.put("remark", "2");
        map.put("version", "2");

        HttpEntity entity = new HttpEntity(JSON.toJSON(map), headers);
        ResponseEntity response = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
        LinkedHashMap responseMap = (LinkedHashMap) response.getBody();
        System.out.print("*******************" + responseMap);
        assertTrue(true);
    }

    @Test
    public void pushCustomerInfoToXFJR() {
        uri = uri + "/etepCustomer/pushCustomerInfoToXFJR";

        System.out.println(uri);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");

        Map map = new HashMap();
        map.put("customerCode", "2");
        map.put("customerName", "2");
        map.put("customerShortName", "2");
        map.put("customerFullName", "2");
        map.put("customerType", "2");
        map.put("province", "2");
        map.put("city", "2");
        map.put("addressDetail", "2");
        map.put("customerStatus", "2");
        map.put("createTime", "2");
        map.put("deleteFlag", "2");
        map.put("remark", "2");
        map.put("version", "2");

        HttpEntity entity = new HttpEntity(JSON.toJSON(map), headers);
        ResponseEntity response = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
        LinkedHashMap responseMap = (LinkedHashMap) response.getBody();
        System.out.print("*******************" + responseMap);
        assertTrue(true);
    }

    @Test
    public void testXFJRInter() {
        uri = xfjrUri + "/merchant/add";

        System.out.println(uri);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");

        Map map = new HashMap();
        map.put("customerCode", "2");
        map.put("customerName", "2");
        map.put("customerShortName", "2");
        map.put("customerFullName", "2");
        map.put("customerType", "2");
        map.put("province", "2");
        map.put("city", "2");
        map.put("addressDetail", "2");
        map.put("customerStatus", "2");
        map.put("createTime", "2");
        map.put("deleteFlag", "2");
        map.put("remark", "2");
        map.put("version", "2");

        HttpEntity entity = new HttpEntity(JSON.toJSON(map), headers);
        ResponseEntity response = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
        LinkedHashMap responseMap = (LinkedHashMap) response.getBody();
        System.out.print("*******************" + responseMap);
        assertTrue(true);
    }

    @Test
    public void getCustomerInfoByCustomerType() {
        uri = uri + "/customer/getCustomerInfoByCustomerType";

        System.out.println(uri);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");

        Map map = new HashMap();
        map.put("customerType", "5");

        HttpEntity entity = new HttpEntity(JSON.toJSON(map), headers);
        ResponseEntity response = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
        LinkedHashMap responseMap = (LinkedHashMap) response.getBody();
        System.out.print("*******************" + responseMap);
        assertTrue(true);
    }

    @Test
    public void getCustomerStoreInfoByCode() {
        uri = uri + "/customer/getCustomerStoreInfoByCode";

        System.out.println(uri);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");

        Map map = new HashMap();
        map.put("customerCode", "1002001");

        HttpEntity entity = new HttpEntity(JSON.toJSON(map), headers);
        ResponseEntity response = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
        LinkedHashMap responseMap = (LinkedHashMap) response.getBody();
        System.out.print("*******************" + responseMap);
        assertTrue(true);
    }

}
