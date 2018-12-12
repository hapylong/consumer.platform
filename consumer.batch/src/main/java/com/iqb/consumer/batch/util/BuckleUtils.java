package com.iqb.consumer.batch.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class BuckleUtils {
    /**
     * <p>
     * RSA公钥/私钥/签名工具包
     * </p>
     * <p>
     * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
     * </p>
     * <p>
     * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
     * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
     * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
     * </p>
     * <P>
     * 逻辑认证：数字信封（数字信封用加密技术来保证只有特定的收信人才能阅读信的内容）。
     * </P>
     * <p>
     * 流程解析(1).甲方构建密钥对儿，将公钥公布给乙方，将私钥保留。
     * </p>
     * <p>
     * 流程解析(2).甲方使用私钥加密数据，然后用私钥对加密后的数据签名，发送给乙方签名以及加密后的数据；乙方使用公钥、签名来验证待解密数据是否有效，如果有效使用公钥对数据解密。
     * </p>
     * <p>
     * 流程解析(3).乙方使用公钥加密数据，向甲方发送经过加密后的数据；甲方获得加密数据，通过私钥解密。
     * </p>
     * <p>
     * RSA加密明文最大长度117字节，解密要求密文最大长度为128字节，所以在加密和解密的过程中需要分块进行。
     * </p>
     * 
     * @author : guoliangdi
     * @date : Created in 11:46 2017/6/1
     * @modified by :
     */
    public static class RSAUtils {

        /**
         * 加密算法RSA
         */
        public static final String KEY_ALGORITHM = "RSA";

        /**
         * 签名算法
         */
        public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

        /**
         * 获取公钥的key
         */
        private static final String PUBLIC_KEY = "RSAPublicKey";

        /**
         * 获取私钥的key
         */
        private static final String PRIVATE_KEY = "RSAPrivateKey";

        /**
         * RSA最大加密明文大小
         */
        private static final int MAX_ENCRYPT_BLOCK = 117;

        /**
         * RSA最大解密密文大小
         */
        private static final int MAX_DECRYPT_BLOCK = 128;

        /**
         * <p>
         * 生成密钥对(公钥和私钥)
         * </p>
         * 
         * @return
         * @throws Exception
         */
        public static Map<String, Object> genKeyPair() throws Exception {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> keyMap = new HashMap<>(2);
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        }

        /**
         * <p>
         * 用私钥对信息生成数字签名
         * </p>
         * 
         * @param data 已加密数据
         * @param privateKey 私钥(BASE64编码)
         * 
         * @return
         * @throws Exception
         */
        public static String sign(byte[] data, String privateKey) throws Exception {
            byte[] keyBytes = Base64Utils.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(data);
            return Base64Utils.encode(signature.sign());
        }

        /**
         * <p>
         * 校验数字签名
         * </p>
         * 
         * @param data 已加密数据
         * @param publicKey 公钥(BASE64编码)
         * @param sign 数字签名
         * 
         * @return
         * @throws Exception
         * 
         */
        public static boolean verify(byte[] data, String publicKey, String sign)
                throws Exception {
            byte[] keyBytes = Base64Utils.decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicK);
            signature.update(data);
            return signature.verify(Base64Utils.decode(sign));
        }

        /**
         * <P>
         * 私钥解密
         * </p>
         * 
         * @param encryptedData 已加密数据
         * @param privateKey 私钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
                throws Exception {
            byte[] keyBytes = Base64Utils.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }

        /**
         * <p>
         * 公钥解密
         * </p>
         * 
         * @param encryptedData 已加密数据
         * @param publicKey 公钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
                throws Exception {
            byte[] keyBytes = Base64Utils.decode(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }

        /**
         * <p>
         * 公钥加密
         * </p>
         * 
         * @param data 源数据
         * @param publicKey 公钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static byte[] encryptByPublicKey(byte[] data, String publicKey)
                throws Exception {
            byte[] keyBytes = Base64Utils.decode(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        }

        /**
         * <p>
         * 私钥加密
         * </p>
         * 
         * @param data 源数据
         * @param privateKey 私钥(BASE64编码)
         * @return
         * @throws Exception
         */
        public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
                throws Exception {
            byte[] keyBytes = Base64Utils.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        }

        /**
         * <p>
         * 获取私钥
         * </p>
         * 
         * @param keyMap 密钥对
         * @return
         * @throws Exception
         */
        public static String getPrivateKey(Map<String, Object> keyMap)
                throws Exception {
            Key key = (Key) keyMap.get(PRIVATE_KEY);
            return Base64Utils.encode(key.getEncoded());
        }

        /**
         * <p>
         * 获取公钥
         * </p>
         * 
         * @param keyMap 密钥对
         * @return
         * @throws Exception
         */
        public static String getPublicKey(Map<String, Object> keyMap)
                throws Exception {
            Key key = (Key) keyMap.get(PUBLIC_KEY);
            return Base64Utils.encode(key.getEncoded());
        }
    }

    /**
     * <p>
     * BASE64编码解码工具包
     * </p>
     * <p>
     * 依赖Base64 DIY实现类
     * </p>
     * 
     * @author : guoliangdi
     * @date : Created in 11:49 2017/6/1
     * @modified by :
     */
    public static class Base64Utils {

        /**
         * 文件读取缓冲区大小
         */
        private static final int CACHE_SIZE = 1024;

        /**
         * <p>
         * BASE64字符串解码为二进制数据
         * </p>
         * 
         * @param base64
         * @return
         * @throws Exception
         */
        public static byte[] decode(String base64) throws Exception {
            return Base64.decode(base64);
        }

        /**
         * <p>
         * 二进制数据编码为BASE64字符串
         * </p>
         * 
         * @param bytes
         * @return
         * @throws Exception
         */
        public static String encode(byte[] bytes) throws Exception {
            return new String(Base64.encode(bytes));
        }

        /**
         * <p>
         * 将文件编码为BASE64字符串
         * </p>
         * <p>
         * 大文件慎用，可能会导致内存溢出
         * </p>
         * 
         * @param filePath 文件绝对路径
         * @return
         * @throws Exception
         */
        public static String encodeFile(String filePath) throws Exception {
            byte[] bytes = fileToByte(filePath);
            return encode(bytes);
        }

        /**
         * <p>
         * BASE64字符串转回文件
         * </p>
         * 
         * @param filePath 文件绝对路径
         * @param base64 编码字符串
         * @throws Exception
         */
        public static void decodeToFile(String filePath, String base64) throws Exception {
            byte[] bytes = decode(base64);
            byteArrayToFile(bytes, filePath);
        }

        /**
         * <p>
         * 文件转换为二进制数组
         * </p>
         * 
         * @param filePath 文件路径
         * @return
         * @throws Exception
         */
        public static byte[] fileToByte(String filePath) throws Exception {
            byte[] data = new byte[0];
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream in = new FileInputStream(file);
                ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
                byte[] cache = new byte[CACHE_SIZE];
                int nRead = 0;
                while ((nRead = in.read(cache)) != -1) {
                    out.write(cache, 0, nRead);
                    out.flush();
                }
                out.close();
                in.close();
                data = out.toByteArray();
            }
            return data;
        }

        /**
         * <p>
         * 二进制数据写文件
         * </p>
         * 
         * @param bytes 二进制数据
         * @param filePath 文件生成目录
         */
        public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
            InputStream in = new ByteArrayInputStream(bytes);
            File destFile = new File(filePath);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            destFile.createNewFile();
            OutputStream out = new FileOutputStream(destFile);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            in.close();
        }
    }

    /**
     * <p>
     * 依赖Base64 DIY实现类
     * </p>
     * <p>
     * 参照it.sauronsoftware.base64.Base64
     * </p>
     * 
     * @author : guoliangdi
     * @date : Created in 13:36 2017/6/1
     * @modified by :
     */
    public static class Base64 {

        private static char[] Base64Code =
        {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                'j',
                'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
                '4',
                '5',
                '6', '7', '8', '9', '+', '/'
        };

        private static byte[] Base64Decode =
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                62, -1,
                63,
                -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, 0, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6,
                7, 8,
                9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28,
                29, 30,
                31,
                32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1,
                -1
        };

        public static String encode(byte[] b) {
            int code = 0;
            if (b == null)
                return null;
            StringBuffer sffer = new StringBuffer((b.length - 1) / 3 << 6);
            for (int i = 0; i < b.length; i++) {
                code |= b[i] << 16 - i % 3 * 8 & 255 << 16 - i % 3 * 8;
                if ((i % 3 != 2) && (i != b.length - 1))
                    continue;
                sffer.append(Base64Code[((code & 0xFC0000) >>> 18)]);
                sffer.append(Base64Code[((code & 0x3F000) >>> 12)]);
                sffer.append(Base64Code[((code & 0xFC0) >>> 6)]);
                sffer.append(Base64Code[(code & 0x3F)]);
                code = 0;
            }

            if (b.length % 3 > 0)
                sffer.setCharAt(sffer.length() - 1, '=');
            if (b.length % 3 == 1)
                sffer.setCharAt(sffer.length() - 2, '=');
            return sffer.toString();
        }

        public static byte[] decode(String code) {
            if (code == null)
                return null;
            int len = code.length();
            if (len % 4 != 0)
                throw new IllegalArgumentException("Base64 string length must be 4*n");
            if (code.length() == 0)
                return new byte[0];
            int pad = 0;
            if (code.charAt(len - 1) == '=')
                pad++;
            if (code.charAt(len - 2) == '=')
                pad++;
            int retLen = len / 4 * 3 - pad;
            byte[] ret = new byte[retLen];
            for (int i = 0; i < len; i += 4) {
                int j = i / 4 * 3;
                char ch1 = code.charAt(i);
                char ch2 = code.charAt(i + 1);
                char ch3 = code.charAt(i + 2);
                char ch4 = code.charAt(i + 3);
                int tmp =
                        Base64Decode[ch1] << 18 | Base64Decode[ch2] << 12 | Base64Decode[ch3] << 6 | Base64Decode[ch4];
                ret[j] = (byte) ((tmp & 0xFF0000) >> 16);
                if (i < len - 4) {
                    ret[(j + 1)] = (byte) ((tmp & 0xFF00) >> 8);
                    ret[(j + 2)] = (byte) (tmp & 0xFF);
                } else {
                    if (j + 1 < retLen)
                        ret[(j + 1)] = (byte) ((tmp & 0xFF00) >> 8);
                    if (j + 2 < retLen)
                        ret[(j + 2)] = (byte) (tmp & 0xFF);
                }
            }
            return ret;
        }
    }

    public static class Chat {
        public static String post(String url, Map<String, String> paramMap)
                throws ClientProtocolException, IOException {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            // 设置请求和传输超时时间
            RequestConfig requestConfig =
                    RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
            httpPost.setConfig(requestConfig);
            List<NameValuePair> formparams = setHttpParams(paramMap);
            UrlEncodedFormEntity param = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(param);
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println("************" + response);
            String httpEntityContent = getHttpEntityContent(response);
            System.out.println("************" + httpEntityContent);
            httpPost.abort();
            System.out.println("************" + httpEntityContent);
            return httpEntityContent;

        }

        /**
         * 封装HTTP POST方法
         * 
         * @param
         * @param （如JSON串）
         * @return
         * @throws ClientProtocolException
         * @throws java.io.IOException
         */
        public static String post(String url, String data) throws ClientProtocolException, IOException {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            // 设置请求和传输超时时间
            RequestConfig requestConfig =
                    RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("Content-Type", "text/json; charset=utf-8");
            httpPost.setEntity(new StringEntity(URLEncoder.encode(data, "UTF-8")));
            HttpResponse response = httpClient.execute(httpPost);
            String httpEntityContent = getHttpEntityContent(response);
            httpPost.abort();
            return httpEntityContent;
        }

        /**
         * 封装HTTP GET方法
         * 
         * @param
         * @return
         * @throws ClientProtocolException
         * @throws java.io.IOException
         */
        public static String get(String url) throws ClientProtocolException, IOException {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet();
            // 设置请求和传输超时时间
            RequestConfig requestConfig =
                    RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
            httpGet.setConfig(requestConfig);
            httpGet.setURI(URI.create(url));
            HttpResponse response = httpClient.execute(httpGet);
            String httpEntityContent = getHttpEntityContent(response);
            httpGet.abort();
            return httpEntityContent;
        }

        /**
         * 封装HTTP GET方法
         * 
         * @param
         * @param
         * @return
         * @throws ClientProtocolException
         * @throws java.io.IOException
         */
        public static String get(String url, Map<String, String> paramMap) throws ClientProtocolException, IOException {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet();
            // 设置请求和传输超时时间
            RequestConfig requestConfig =
                    RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
            httpGet.setConfig(requestConfig);
            List<NameValuePair> formparams = setHttpParams(paramMap);
            String param = URLEncodedUtils.format(formparams, "UTF-8");
            httpGet.setURI(URI.create(url + "?" + param));
            HttpResponse response = httpClient.execute(httpGet);
            String httpEntityContent = getHttpEntityContent(response);
            httpGet.abort();
            return httpEntityContent;
        }

        /**
         * 封装HTTP PUT方法
         * 
         * @param
         * @param
         * @return
         * @throws ClientProtocolException
         * @throws java.io.IOException
         */
        public static String put(String url, Map<String, String> paramMap) throws ClientProtocolException, IOException {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(url);
            // 设置请求和传输超时时间
            RequestConfig requestConfig =
                    RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
            httpPut.setConfig(requestConfig);
            List<NameValuePair> formparams = setHttpParams(paramMap);
            UrlEncodedFormEntity param = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPut.setEntity(param);
            HttpResponse response = httpClient.execute(httpPut);
            String httpEntityContent = getHttpEntityContent(response);
            httpPut.abort();
            return httpEntityContent;
        }

        /**
         * 封装HTTP DELETE方法
         * 
         * @param
         * @return
         * @throws ClientProtocolException
         * @throws java.io.IOException
         */
        public static String delete(String url) throws ClientProtocolException, IOException {
            HttpClient httpClient = new DefaultHttpClient();
            HttpDelete httpDelete = new HttpDelete();
            // 设置请求和传输超时时间
            RequestConfig requestConfig =
                    RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
            httpDelete.setConfig(requestConfig);
            httpDelete.setURI(URI.create(url));
            HttpResponse response = httpClient.execute(httpDelete);
            String httpEntityContent = getHttpEntityContent(response);
            httpDelete.abort();
            return httpEntityContent;
        }

        /**
         * 封装HTTP DELETE方法
         * 
         * @param
         * @param
         * @return
         * @throws ClientProtocolException
         * @throws java.io.IOException
         */
        public static String delete(String url, Map<String, String> paramMap) throws ClientProtocolException,
                IOException {
            HttpClient httpClient = new DefaultHttpClient();
            HttpDelete httpDelete = new HttpDelete();
            // 设置请求和传输超时时间
            RequestConfig requestConfig =
                    RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
            httpDelete.setConfig(requestConfig);
            List<NameValuePair> formparams = setHttpParams(paramMap);
            String param = URLEncodedUtils.format(formparams, "UTF-8");
            httpDelete.setURI(URI.create(url + "?" + param));
            HttpResponse response = httpClient.execute(httpDelete);
            String httpEntityContent = getHttpEntityContent(response);
            httpDelete.abort();
            return httpEntityContent;
        }

        /**
         * 设置请求参数
         * 
         * @param
         * @return
         */
        private static List<NameValuePair> setHttpParams(Map<String, String> paramMap) {
            List<NameValuePair> formparams = new ArrayList<>();
            Set<Map.Entry<String, String>> set = paramMap.entrySet();
            for (Map.Entry<String, String> entry : set) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            return formparams;
        }

        /**
         * 获得响应HTTP实体内容
         * 
         * @param response
         * @return
         * @throws java.io.IOException
         * @throws java.io.UnsupportedEncodingException
         */
        private static String getHttpEntityContent(HttpResponse response) throws IOException,
                UnsupportedEncodingException {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = br.readLine();
                StringBuilder sb = new StringBuilder();
                while (line != null) {
                    sb.append(line + "\n");
                    line = br.readLine();
                }
                return sb.toString();
            }
            return "";
        }
    }

}
