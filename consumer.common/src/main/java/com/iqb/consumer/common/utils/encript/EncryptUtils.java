package com.iqb.consumer.common.utils.encript;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.JSONUtil;

/**
 * 
 * Description: 加密
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年11月18日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Component
public class EncryptUtils {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtils.class);

    @Resource
    private ConsumerConfig consumerConfig;

    /**
     * 
     * Description: 加密
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年11月17日 上午10:56:48
     */
    public Map<String, Object> encrypt(JSONObject objs) {
        String source = objs.toJSONString();
        byte[] data = source.getBytes();
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            byte[] encodedData = RSAUtils.encryptByPrivateKey(data, consumerConfig.getCommonPrivateKey());
            String data1 = encodeBase64(encodedData);
            String sign = RSAUtils.sign(encodedData, consumerConfig.getCommonPrivateKey());
            params.put("data", data1);
            params.put("sign", sign);
        } catch (Exception e) {
            logger.error("加密异常：" + objs.toJSONString(), e);
        }
        return params;
    }

    /**
     * 
     * Description: 加密
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2016年12月6日 上午11:21:06
     */
    public Map<String, Object> encrypt(Map<String, String> map) {
        String source = JSONUtil.objToJson(map);
        byte[] data = source.getBytes();
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            byte[] encodedData = RSAUtils.encryptByPrivateKey(data, consumerConfig.getCommonPrivateKey());
            String data1 = encodeBase64(encodedData);
            String sign = RSAUtils.sign(encodedData, consumerConfig.getCommonPrivateKey());
            params.put("data", data1);
            params.put("sign", sign);
        } catch (Exception e) {
            logger.error("加密异常：" + source, e);
        }
        return params;
    }

    /**
     * 带密钥加密
     * 
     * @param map
     * @param privateKey
     * @return
     */
    public Map<String, Object> encrypt(Map<String, String> map, String privateKey) {
        String source = JSONUtil.objToJson(map);
        byte[] data = source.getBytes();
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
            String data1 = encodeBase64(encodedData);
            String sign = RSAUtils.sign(encodedData, privateKey);
            params.put("data", data1);
            params.put("sign", sign);
        } catch (Exception e) {
            logger.error("加密异常：" + source, e);
        }
        return params;
    }

    /**
     * 加密
     * 
     * @param source
     * @return
     */
    public Map<String, Object> encrypt(String source) {
        byte[] data = source.getBytes();
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            byte[] encodedData = RSAUtils.encryptByPrivateKey(data, consumerConfig.getCommonPrivateKey());
            String data1 = encodeBase64(encodedData);
            String sign = RSAUtils.sign(encodedData, consumerConfig.getCommonPrivateKey());
            params.put("data", data1);
            params.put("sign", sign);
        } catch (Exception e) {
            logger.error("加密异常：" + source, e);
        }
        return params;
    }

    static String encodeBase64(byte[] source) throws Exception {
        return new String(Base64.encodeBase64(source), "UTF-8");
    }

    /**
     * 解密数据接口
     * 
     * @param request
     * @return
     * @throws IqbException
     */
    public String decode(HttpServletRequest request, String publicKey) throws IqbException {
        Map<String, String> param = new HashMap<String, String>();
        String jsonStr = null;
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paraName = paramNames.nextElement();
            String para = request.getParameter(paraName);
            param.put(paraName, para.trim());
        }
        try {
            // 验签
            String sign = param.get("sign");
            String data = param.get("data");
            byte[] byteData = Base64.decodeBase64(data);
            boolean signFlag = RSAUtils.verify(byteData, publicKey, sign);
            if (signFlag) {
                // 解密
                byte[] decodedData =
                        RSAUtils.decryptByPublicKey(byteData, publicKey);
                jsonStr = new String(decodedData);
                logger.info("存量统计查询接口，解析传参:{}", jsonStr);
            } else {
                logger.info("存量统计查询接口验签失败");
                throw new IqbException(ApiReturnInfo.API_DECRYPTION_10000002);
            }
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_DECRYPTION_10000002);
        }
        return jsonStr;
    }

    /**
     * 解密数据接口
     * 
     * @param parseObj
     * @param publicKey
     * @return
     * @throws IqbException
     */
    public String decode(JSONObject parseObj, String publicKey) throws IqbException {
        String jsonStr = null;
        try {
            // 验签
            String sign = parseObj.getString("sign");
            String data = parseObj.getString("data");
            byte[] byteData = Base64.decodeBase64(data);
            boolean signFlag = RSAUtils.verify(byteData, publicKey, sign);
            if (signFlag) {
                // 解密
                byte[] decodedData =
                        RSAUtils.decryptByPublicKey(byteData, publicKey);
                jsonStr = new String(decodedData);
                logger.info("解密后的传参:{}", jsonStr);
            } else {
                logger.info("解密接口验签失败");
                throw new IqbException(ApiReturnInfo.API_DECRYPTION_10000002);
            }
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_DECRYPTION_10000002);
        }
        return jsonStr;
    }

    /**
     * 
     * Description:车e估生成签名
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public static String getSign(Map<String, String> map, String carKey) {

        String result = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });

            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (!(val == "" || val == null)) {
                        sb.append(key + "=" + val + "&");
                    }
                }

            }
            result = sb.toString();
            result = result + "key=" + carKey;
            // 进行MD5加密
            result = DigestUtils.md5Hex(result).toUpperCase();
        } catch (Exception e) {
            return null;
        }
        return result;
    }
}
