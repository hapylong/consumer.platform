package com.iqb.consumer.batch.util.encript;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.iqb.consumer.batch.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 签名工具类
 * 
 * @author jack
 * 
 */
public class SignUtil {

    private static Logger log = LoggerFactory.getLogger(SignUtil.class);

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public SignUtil() {

    }

    /**
     * 获取代签字符串
     * 
     * @return
     */
    public String getSignStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
        return sdf.format(new Date()) + new Random().nextInt(5000000);
    }

    /**
     * 
     * Description: 通用签名 如果 接收 form 用 SimpleHttpUtils.httpPost() ,接收json 用
     * SendHttpsUtil.postMsg4GetMap()
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author adam Create Date: 2017年5月9日 上午10:21:36
     */
    public static Map<String, Object> chatEncode(String json, String privateKey) throws Exception {
        log.info("chatEncode json string : {}", json);
        String PRIVATE_KEY = privateKey;
        byte[] data = json.getBytes();
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, PRIVATE_KEY);
        String data1 = Base64.encode(encodedData);
        String sign = RSAUtils.sign(encodedData, PRIVATE_KEY);
        Map<String, Object> params = new HashMap<>();
        params.put("data", data1);
        params.put("sign", sign);
        return params;
    }

}
