/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月12日 下午2:22:42
 * @version V1.0
 */
package com.iqb.consumer.service.layer.CRM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.common.utils.encript.Cryptography;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBizTypeBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.ServiceParaConfig;
import com.iqb.consumer.service.layer.domain.Req8005Bean;
import com.iqb.consumer.service.layer.merchant.service.IMerchantService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;

/**
 * @author gxy
 */
@Service
public class CRMServiceImpl implements CRMService {

	private static Logger log = LoggerFactory.getLogger(CRMServiceImpl.class);
    @Resource
    private ServiceParaConfig serviceParaConfig;

    @Resource
    private IMerchantService merchantService;

    @Resource
    private UserBeanBiz userBiz;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private MerchantBizTypeBiz merchantBizTypeBiz;

    @Override
    public int addMerchant(JSONObject objs) throws IqbException, IqbSqlException {

        String merchCode = objs.getString("merchCode");// 商户名称
        String enabled = objs.getString("enabled"); // 是否为虚商户 0 否 1是
        String merchShortName = objs.getString("merchShortName");// 商户简称
        String publicNo = objs.getString("publicNo");// 所属微信号 1 花花 2 新车 3 二手车
        String merchAddr = objs.getString("merchAddr");// 详细地址
        String riskType = objs.getString("riskType");// 风控类型 注 1 旅游 2 医美 3 车类等
        String parentId = objs.getString("parentId");// 父加盟商ID
        String level = objs.getString("level");// (非必要字段)层级 1 爱钱帮1级 2 爱车帮 3
        String overdueRate = objs.getString("overdueRate"); // 逾期利率
        String overdueFee = objs.getString("overdueFee");// 逾期固定手续费

        // (2001 以租代售新车 2002以租代售二手车 2100 抵押车 2200 质押车 1100 易安家 1000 医美 1200 旅游)

        String passwd = Cryptography.encrypt("a11111" + merchCode);// 商户密码加密

        int riskFlag = objs.getIntValue("riskFlag");// 风控系统是否配配置商户编码
        String riskCode = objs.getString("riskCode");// 密钥
        // String merchCode =
        // PinyinTool.converterToFirstSpell(objs.getString("merchCode"));// 商户名称
        // String id = objs.getString("id");// ID
        // String merchantFullName = objs.getString("merchantFullName");// 商户全称
        // String province = objs.getString("province"); // 省
        // String city = objs.getString("city");// 市
        // String instalNo = objs.getString("instalNo");// 分期计划
        // String overdueType = objs.getString("overdueType");// 逾期利率模式 0 按月 1
        // 按天
        // String actcode = objs.getString("actcode");// 请求类型 0 新增 2 修改 3 删除

        /* for all */
        objs.put("merchCode", merchCode);

        /* for merInfo */
        objs.put("merchantNo", merchCode);
        objs.put("password", passwd);
        objs.put("merchantShortName", merchShortName);
        objs.put("merchantAddr", merchAddr);
        objs.put("level", Integer.parseInt(level));
		objs.put("parentId", parentId);
        objs.put("publicNo", Integer.parseInt(publicNo));
        objs.put("riskType", Integer.parseInt(riskType));
        objs.put("fee", Double.parseDouble(overdueFee));
        objs.put("overdueFee", Double.parseDouble(overdueRate) * 100);
        objs.put("status", 1);
        objs.put("enabled", Integer.parseInt(enabled));
        objs.put("riskFlag", riskFlag);
        objs.put("riskCode", riskCode);
        if (this.selectMerchatInfoByID(objs)) {
            merchantService.delMerchantInfo(objs);
            merchantBizTypeBiz.delMerchantBizType(objs);
        }
        try {
            int merInfoStatus = merchantService.insertMerchantInfo(objs);
            // 帮帮手（增加业务类型） by guoliangdi 2017.3.21
            String businessType = objs.getString("businessType"); // 帮帮手（增加业务类型）
            if (!StringUtil.isEmpty(businessType)) {
                if (businessType.contains("[")) {
                    List<String> businessTypeList = JSONObject.parseArray(businessType, String.class);
                    if (!CollectionUtils.isEmpty(businessTypeList)) {
                        for (String bizType : businessTypeList) {
                            objs.put("bizType", bizType);
                            merchantBizTypeBiz.insertMerchantBizTypeBySync(objs);
                        }
                    }
                    if (merInfoStatus != 1) {
                        return -1;
                    }
                } else {
                    objs.put("bizType", businessType);
                    merchantBizTypeBiz.insertMerchantBizTypeBySync(objs);
                }

            } else {
                merchantBizTypeBiz.insertMerchantBizTypeBySync(objs);
            }

        } catch (Exception e) {
			log.error("insert merchant error: ", e);
			return -1;
        }
        return 1;
    }

    /**
     * 效验推送接口是否重复推送。yes : 删除老数据，新增. no：直接新增
     * 
     * @param id
     * @return
     */
    private boolean selectMerchatInfoByID(JSONObject objs) {
        objs.put("id", objs.getString("id"));
        MerchantBean merchantBeanObj = merchantService.getMerByID(objs);
        if (null != merchantBeanObj) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private Map addMerchantVact(JSONObject objs) {
        // 需要通过reset接口新增商户信息
        Req8005Bean bean8005 = new Req8005Bean();
        bean8005.setAppType("pc");
        bean8005.setVersion("1.1.1");
        bean8005.setLgnMerchCode("iqb");
        bean8005.setLgnUserCode("iqb");
        bean8005.setRtype("");
        bean8005.setActCode("0");
        bean8005.setTraceNo(getTraceNo());

        bean8005.setMerchCode(objs.getString("merchCode"));
        bean8005.setMerchShortName(objs.getString("merchShortName"));
        bean8005.setMerchAddr(objs.getString("merchAddr"));
        bean8005.setPasswd(objs.getString("passwd"));
        bean8005.setInstalNo(objs.getString("instalNo"));
        bean8005.setOverdueRate(objs.getString("overdueRate"));
        bean8005.setOverdueFee(objs.getString("overdueFee"));
        bean8005.setOverdueType(objs.getString("overdueType"));

        // 签名
		LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("appType", bean8005.getAppType());
        linkedHashMap.put("version", bean8005.getVersion());
        linkedHashMap.put("lgnmerchcode", bean8005.getLgnMerchCode());
        linkedHashMap.put("lgnusercode", bean8005.getLgnUserCode());
        linkedHashMap.put("rtype", bean8005.getRtype());
        linkedHashMap.put("actCode", bean8005.getActCode());
        linkedHashMap.put("traceno", bean8005.getTraceNo());

        linkedHashMap.put("merchCode", bean8005.getMerchCode());
        linkedHashMap.put("merchShortName", bean8005.getMerchShortName());
        linkedHashMap.put("merchAddr", bean8005.getMerchAddr());
        linkedHashMap.put("passwd", bean8005.getPasswd());
        linkedHashMap.put("instalNo", bean8005.getInstalNo());
        linkedHashMap.put("overdueRate", bean8005.getOverdueRate());
        linkedHashMap.put("overdueFee", bean8005.getOverdueFee());
        linkedHashMap.put("overdueType", bean8005.getOverdueType());
        String reqSignStr = mergeMap(linkedHashMap);
        String sign = SignUtil.sign(reqSignStr, serviceParaConfig.getPriKeyPath());
        bean8005.setSign(sign);
        return getResMap(serviceParaConfig.getUrl_8005(), bean8005);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map getResMap(String uri, Object obj) {
        // rest请求
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=utf-8"));
        headers.set("Accept-Charset", "UTF-8");

        // https协议
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
			public boolean verify(String urlHostName, SSLSession session) {
                System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                return true;
            }
        };
        try {
            trustAllHttpsCertificates();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hv);

        HttpEntity entity = new HttpEntity(JSON.toJSON(obj), headers);
        ResponseEntity response;
        response = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
        LinkedHashMap responseMap = (LinkedHashMap) response.getBody();
        return responseMap;
    }

    protected static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        @Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        @Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

    @SuppressWarnings("unchecked")
    private static String mergeMap(@SuppressWarnings("rawtypes") Map map) {
        String requestMerged = "";
        StringBuffer buff = new StringBuffer();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            if (!"sign".equals(entry.getKey())) {
                if (entry.getValue() == null) {
                    entry.setValue("");
                    buff.append("");
                } else {
                    buff.append(String.valueOf(entry.getValue()).trim());
                }
            }
        }
        requestMerged = buff.toString();
        return requestMerged;
    }

    private String getTraceNo() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-");
        String traceNo = sdf.format(date) + (int) ((Math.random() * 9 + 1) * 100000);
        return traceNo;
    }
}
