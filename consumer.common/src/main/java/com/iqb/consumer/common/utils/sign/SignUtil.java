package com.iqb.consumer.common.utils.sign;

import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.encript.RSAUtils;

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
     * 获取签名
     * 
     * @param signStr 待签名字符串
     * @return
     * @throws Exception
     */
    public static String sign(String signStr, String prikeyPath) {

        log.info((new StringBuilder()).append("待签名字符串：signStr=").append(signStr).toString());
        String sign = null;
        try {
            Signature sig = Signature.getInstance(SIGN_ALGORITHMS);
            sig.initSign(PkCertFactory.getPrivateKey(prikeyPath));
            sig.update((null == signStr ? "" : signStr).getBytes("UTF-8"));
            byte signData[] = sig.sign();
            sign = new String(Base64.encode(signData));
        } catch (Exception e) {
            log.error("获取签名异常", e.getMessage());
        }
        log.info((new StringBuilder()).append("签名：sign=").append(sign).toString());
        return sign;
    }

    /**
     * 签名校验
     * 
     * @param sign 签名
     * @param plain 约定口令 可为空
     * @return
     * @throws Exception
     */
    public static boolean verify(String sign, String signStr, String certPath) {
        log.info((new StringBuilder()).append("待签名字符串：signStr=").append(signStr).toString());
        log.info((new StringBuilder()).append("签名：sign=").append(sign).toString());
        java.security.cert.X509Certificate cert = PkCertFactory.getCert(certPath);
        log.info("获取证书成功");

        boolean b = false;
        try {
            byte signData[] = Base64.decode(sign);
            Signature sig = Signature.getInstance(SIGN_ALGORITHMS);
            sig.initVerify(cert);
            sig.update((null == signStr ? "" : signStr).getBytes("UTF-8"));
            b = sig.verify(signData);
        } catch (Exception e) {
            log.error("签名校验异常", e.getMessage());
        }
        log.info((new StringBuilder()).append("验证平台签名是否成功").append(b).toString());
        return b;
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
     * Description: 通用签名
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object chatDecode(JSONObject requestMessage, Class c, String publicKey) throws GenerallyException {
        try {
            String PUBLIC_KEY = publicKey;
            String sign = requestMessage.getString("sign");
            String data = requestMessage.getString("data");
            byte[] byteData = Base64.decode(data);
            boolean signFlag = RSAUtils.verify(byteData, PUBLIC_KEY, sign);
            if (signFlag) {
                byte[] decodedData =
                        RSAUtils.decryptByPublicKey(byteData, PUBLIC_KEY);
                String jsonStr = new String(decodedData);
                return JSONObject.parseObject(jsonStr, c);
            } else {
                throw new GenerallyException(Reason.CHAT_DECODE_ERROR, Layer.UTIL, Location.A);
            }
        } catch (Throwable e) {
            log.error("chatDecode error :" + e);
            throw new GenerallyException(Reason.CHAT_DECODE_ERROR, Layer.UTIL, Location.B);
        }
    }
}
