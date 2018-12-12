package com.iqb.consumer.manage.front.api;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.manage.front.AbstractController;

import util.EncryptionAlgorithm;

public class TestIntoAssetController extends AbstractController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自测类，测试资产进件API接口蒲公英接口
     */
    @Test
    public void testIntoAsset() {

        // 生成IP认证
        String random = RandomStringUtils.random(20, true, true);
        Integer randomInt = (int) (Math.random() * 16 + 1);
        String ciphertext = EncryptionAlgorithm.encrypte("123.56.201.182", random, randomInt, new Date());
        String express = random + randomInt.toString();
        String url = "https://www.shandianx.com/consumer.manage.front/api/intopgymortgageAsset";
        String uri = url + "?ciphertext=" + ciphertext + "&express=" + express;

        Map<String, String> params = new HashMap<>();
        params.put("merchantId", "cdhtc");
        params.put("realName", "名字4");
        params.put("regId", "13535262555");
        params.put("proType", "产品类型");
        params.put("idCard", "362204198703287412");
        params.put("address", "北京");
        params.put("companyName", "爱钱帮");
        params.put("companyAddress", "南新仓");
        params.put("companyPhone", "010-25450213");
        params.put("creditPasswd", "1234");
        params.put("creditNo", "111111111");
        params.put("creditCode", "验证码");
        String privatekey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL5iajtijIPlEd+e+0hIWtBTdfF1Y7wmFiGh3sLKBrMsUzlxbDrGPUpyZmLSoeFMQUAuoz60A9VXcjXA6CoRm7WSr74WOU/SwXYTyVyhXRqjyHDFK36yT93BOckwyEeLEhRfL9Fb9CcwSDOBSuCDqP1WSuLJXdFDa+yCtHarOMXhAgMBAAECgYBUVnSI6hclNoy8a0kVtDfGobJVzJLp/lU/gN2VnLlrFe+oOr6fKsJsAFGq5XQgwg8Fx6M5W7V3BM3rUhGG0VYQTYaOG8X5WzENcXp2xbR65uKgF6b9Dui723OF4cZoROoWPdaWejmbyzacrBHJOWT0xSqRvOAd9KQCfRSQte5rgQJBAOniPxW/TCFFpH9thAN0mERoxDjstwVDt1TjCBVsLgRJjvR96pVZkHxcx3ePx7cNiYDB8wYtj1zy8hygA43TLjUCQQDQYyfuc8Lst+pjZyF8cTdJCJAHR7KIGjv4763xa2O1Uh9XiQTj8u0BNp8objqhu7TtLC6xCupQjTtJ3v9nDR59AkEAlQxG0bAgbw+3vjFnsOAL7YSqj/BizmsSGXm5fBV92eOaw7q+1UDHsElcIJizWLe6yiBPpL9CHqUAoDdlLGvJhQJBAIAPDzD4Lqdt9joT3H4uzPyyqF/w7YO6K3S1Rb2AcwuwZdZTO5Ahbwg52uDrrFhLk8nURvVBcfECeZbR1T4sz7kCQCy0VN4xCPV7VFB4W7xxQJdeqQNbrlTN9t3+YfvHD7DydgO9Ht1ZlCJJpUBKeIrrxA2+bBd2Z+zR6AwPfQw6wWw=";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "sxpgy");
        LinkedHashMap resultStr = SendHttpsUtil.postMsg4GetMap(uri, encrypt);
        logger.info("资产进件返回值:{}", resultStr);
    }

    @Test
    public void testIntoCarAsset() {
        Map<String, String> params = new HashMap<>();
        params.put("orderAmt", "10000.00");
        params.put("orderName", "宝马-i320");
        params.put("planId", "90");
        params.put("merchantNo", "cdhtc");
        params.put("bizType", "2001");
        params.put("regId", "18515262757");
        params.put("realName", "张三");
        params.put("idCardNo", "362204198703287412");
        params.put("bankCardNo", "6220512458745215625");
        params.put(
                "riskInfo",
                "{\"phone\":\"18503681123\",\"addprovince\":\"黑龙江省哈尔滨市香坊区\",\"marriedstatus\":\"未婚\",\"contactname1\":\"张梦林\",\"contactphone1\":\"13244655722\",\"contactname2\":\"李涛\",\"contactphone2\":\"15846000501\"}");
        String privatekey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "finance");
        LinkedHashMap resultStr =
                SendHttpsUtil.postMsg4GetMap(BASEURL + "api/intoCarAssetAsset", encrypt);
        logger.info("资产进件返回值:{}", resultStr);
    }

    @SuppressWarnings({"rawtypes"})
    @Test
    public void testSelPlanByMerchantNo() {
        // 生成IP认证
        String random = RandomStringUtils.random(20, true, true);
        Integer randomInt = (int) (Math.random() * 16 + 1);
        String ciphertext = EncryptionAlgorithm.encrypte("127.0.0.1", random, randomInt, new Date());
        String express = random + randomInt.toString();
        String url = "http://localhost:8080/consumer.manage.front/api/selPlanByMerch";
        String uri = url + "?ciphertext=" + ciphertext + "&express=" + express;

        Map<String, String> params = new HashMap<>();
        params.put("merchantNo", "tjym");
        params.put("bizType", "2001");
        String privatekey =
                "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKEO+ErTmX/NkuqZJYRQ4zsN9eRwEk+HzzT7vtX/mHnlaN48e0+vQwv4DFIr5vcnKTEnrTHDjm8nLxv9hWEyvNqqNvn88C1evGY7YS7UHF/rIlIHk5yqkhLmI4hMK4A09cZE3H//SvtarfbXNalgpllakBYw0uMTG3LIVBbjolqDAgMBAAECgYBx/9qXfDcGU0hObaA0i9yLDFKkIv+S85oi8p4dcxFGFq+nvj+6I1/dtPFjqFePUVTCyka5FqZW3vFlaoliRzuMmrTnMwHP0FFb3zk0KOoScgwBzKb+B4KwvWqmGvDtBSGr55txOvq4hMnN7F/l1gd1P5JUeTSkOwp132Bk/AkdQQJBAN00U6c030FJAEAMlLiIr3V8o/rKlA5LrHKI0+MHrZyswajjyPU7ZAesGLHGkqozDkDsMv8B8YwODyhbKWcVYqkCQQC6ZKDy/G6TPBmqmPLWEtIlFUpQG9IpFdtK0E3/XQpYGWtWdkVoV5RTJiTARxlq0L1MPNe35EA5u2BvnceEBbtLAkBQ9KKc8jO23/Gwfyo6swOL+vbEhTlUVguhy0PItfmq+mrR1bOpVHgSesB655KrqY3Q4uDzBRIS0N5pRZvMdz4pAkEAsei+328mtA5XiUg38TFBt/ecWN5VOLYN/FjbOfU6nGCW3Y5CLZf6kWsFBSpYDmZzaMNiO7n3u6MoJyF4gIFgBQJAXtTyeTdMwCTIT7kvbtXF+nc/zK5CYGvla9ib+2EC1pAZjeAfybFVYktOwt4MwxEu8yvypzFSRcdD1gepdnvnhQ==";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "tjym");
        // String result = HttpClientUtil.httpPost("http://localhost:9080/consumer.manage.front/" +
        // "auth/auththreeYards", json);
        LinkedHashMap postMsg4GetMap =
                SendHttpsUtil.postMsg4GetMap(uri, encrypt);
        logger.info("资产进件返回值:{}", JSONObject.toJSON(postMsg4GetMap));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testReCalAmt() {
        Map<String, String> params = new HashMap<>();
        params.put("orderAmt", "10002.32");
        params.put("planId", "90");
        String privatekey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "cdhtc");
        // String result = HttpClientUtil.httpPost("http://localhost:9080/consumer.manage.front/" +
        // "auth/auththreeYards", json);
        LinkedHashMap postMsg4GetMap =
                SendHttpsUtil.postMsg4GetMap("http://localhost:9080/consumer.manage.front/api/recalAmt",
                        encrypt);
        logger.info("计算明细返回值:{}", JSONObject.toJSON(postMsg4GetMap));
    }

    @SuppressWarnings({"unused", "rawtypes"})
    @Test
    public void testAuth() {
        Map<String, String> params = new HashMap<>();
        params.put("cardNo", "6013820100017717367");
        params.put("owner", "刘欢");
        params.put("certNo", "110223198912248170");
        params.put("memberId", "0232125412");
        params.put("phone", "");
        String privatekey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnE46JuuTH9kqT1WYXUmaSsYg9Fwgb1O47a942oEgnDdBogEUmi/ojqZycuYitOIhYMHS4cenn1x7V14+axE6f/dzZIqTY+Hv5Vt81hAV+g300s8oHi4aGhB8SbNC9h46d2U64r0XO/9dudI7cphaJ8K+VneyQ1CRAlwSs2++T/AgMBAAECgYEAiX4I8eOEn2UDYA/IyieNq51k6YOQiXwRGThjp1lICpo+LG5uMdoNFSvqEQRywynLV1nGRDMSSjXw4RiyU4J0LJalwA8MFKrDLo6ynyUXq1MedB3N9sWdjV6FIRe3AOkrFoyqMcYwaDgflppUEn/PQF8RJ7/DwIMq1W0y7KIyl/kCQQDnccJbtqrAV3G1C0nbmC2DjNtjaStNxJMtbZdxA40yOlpV+WVlwTwcVqonPAY/yUXyUocvXWjfSd3XygDU+qXLAkEAu8f5INvRxoYAMQ9Ni9nLtbOu+ksrYOG3iF0Xuzj6vSxpQt0gG4sxKLfthE3GR8FZB83Oya6iHqeh5L3EA9m3HQJBANTupXkJkwkIqnV9YEMnO+CGzCUm0g9nZlzqMeVo9hOa+heVLwOyB2KvHTahk8JFpBOwd+1MzDp6VB6/G4rW24cCQER8VHIVedarvJon+KJO1qr4U5LZo4J8EBHd0e/oghpIfkNynmktRMqS+j9MlkpJTA06lHaeCGahwLvKMhktaEkCQGWqy4WYmSAxz6kZ9ua4z/i1lJGbJlIZbWdkZohJQp87A0RKLBuI+5CCcWde3SavuVEsWKcmaNt1aw8VLSsbl2w=";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "cdhtc");
        // String result = HttpClientUtil.httpPost("http://localhost:9080/consumer.manage.front/" +
        // "auth/auththreeYards", json);
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap("http://localhost:9080/consumer.manage.front/api/auththreeYards",
                        encrypt);

    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testGetLastThreeOrderInfo() {

        String random = RandomStringUtils.random(20, true, true);
        Integer randomInt = (int) (Math.random() * 16 + 1);
        String ciphertext = EncryptionAlgorithm.encrypte("168.0.1.123", random, randomInt, new Date());
        String express = random + randomInt.toString();
        String url = "http://localhost:8080/consumer.manage.front/api/getLastThreeOrderInfo";
        String uri = url + "?ciphertext=" + ciphertext + "&express=" + express;

        Map<String, String> params = new HashMap<>();
        params.put("regId", "13522580626");
        params.put("orderId", "ASXR2002170627001");
        params.put("merchantNo", "syqs");
        params.put("bizType", "2002");
        String privatekey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ7wSmUOdwP7OBBMm+tEO0OepkRaoY8DE5Rja9sSlzjCCslAb5Mu8d58/hq1ISOHXMcmIYkLy70879+HrDGz52fQJ/mVFgNqwrg3fzLLn7t0csYHK1Kb777xQXOmsQs/e0v8xEftagEtGI3lexvGx7rx6SMZB3GNFXUVCKasuXOBAgMBAAECgYEAj7GpYPIR8fyHdFz+vlaCyiC+K6BQ3k4mvoqgcYC+TWdJfygNd+ECdWdGCPlnS4rO+5Hi0ddjTOSx7cLokEsZUxLpVHEmCdxL16RpULsz+fxy7v+MojKvAVDyCl4nZ/CnO0cYY9liEICPrh4M/rNkgJJ3Cq1Ms4AwPkFFTx/ts70CQQD28WcvVAzsWsc0BXC16iyxmrxRpRDhBcAh3MclQkfQl8s6ExZSgWfk9C2it5oYAMEb8a/YRniUo1IcTDUOy4OrAkEApMSY4Fb3qcgmCO/h/pl9foA+5MNfbbeLLrD1ASRahSFiB+5mUCiBWgLlpchj0kUmjvn5CxGVN4caQlJ5msw5gwJBANupp0jhclF4+sQapsUbVsBxzVyvAZG5JMy6cGeDSxCRRlUVj4C63ek7D7Ezsx3w5dZuqiYvoalOkobbf0L249sCQDaSTrfu1fRLsB1Bpi43FBNz0mDePJsQMW7zeRI5wilZ1ygdU5G0+LKhRwJTjow0DwNH065Q0oUjoBTbUB9vCJUCQFYPa9OlzfGfUjhfF6N+4NlgkKpQUKn/dNkdZwCkSSOWgGJJGOrqdK1egTY6C0lfJusXy/X0z9n/MxT3UAKiUJQ=";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "syqs");
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap(uri,
                        encrypt);
        System.out.println("---------------------" + returnMap);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testGetAllOrderInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", "SYQS2002170210004");

        String privatekey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ7wSmUOdwP7OBBMm+tEO0OepkRaoY8DE5Rja9sSlzjCCslAb5Mu8d58/hq1ISOHXMcmIYkLy70879+HrDGz52fQJ/mVFgNqwrg3fzLLn7t0csYHK1Kb777xQXOmsQs/e0v8xEftagEtGI3lexvGx7rx6SMZB3GNFXUVCKasuXOBAgMBAAECgYEAj7GpYPIR8fyHdFz+vlaCyiC+K6BQ3k4mvoqgcYC+TWdJfygNd+ECdWdGCPlnS4rO+5Hi0ddjTOSx7cLokEsZUxLpVHEmCdxL16RpULsz+fxy7v+MojKvAVDyCl4nZ/CnO0cYY9liEICPrh4M/rNkgJJ3Cq1Ms4AwPkFFTx/ts70CQQD28WcvVAzsWsc0BXC16iyxmrxRpRDhBcAh3MclQkfQl8s6ExZSgWfk9C2it5oYAMEb8a/YRniUo1IcTDUOy4OrAkEApMSY4Fb3qcgmCO/h/pl9foA+5MNfbbeLLrD1ASRahSFiB+5mUCiBWgLlpchj0kUmjvn5CxGVN4caQlJ5msw5gwJBANupp0jhclF4+sQapsUbVsBxzVyvAZG5JMy6cGeDSxCRRlUVj4C63ek7D7Ezsx3w5dZuqiYvoalOkobbf0L249sCQDaSTrfu1fRLsB1Bpi43FBNz0mDePJsQMW7zeRI5wilZ1ygdU5G0+LKhRwJTjow0DwNH065Q0oUjoBTbUB9vCJUCQFYPa9OlzfGfUjhfF6N+4NlgkKpQUKn/dNkdZwCkSSOWgGJJGOrqdK1egTY6C0lfJusXy/X0z9n/MxT3UAKiUJQ=";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "syqs");
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap("http://localhost:8080/consumer.manage.front/api/getAllOrderInfo",
                        encrypt);
        System.out.println("---------------------" + returnMap);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testSendPaySms() {
        Map<String, String> params = new HashMap<>();
        params.put("traceNo", "SYQS2002170210004");
        params.put("amount", "3");
        // params.put("bankCardNo", "6226225200195079"); //民生银行
        params.put("bankCardNo", "6226225200195079"); // 北京银行
        params.put("cardNo", "140621198208115539");
        params.put("regId", "13681486576");
        params.put("flag", "1");
        params.put("repayNo", "1");
        params.put("realName", "郝金龙");
        params.put("callbackUrl", "http://127.0.0.1:8080");
        String privatekey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ7wSmUOdwP7OBBMm+tEO0OepkRaoY8DE5Rja9sSlzjCCslAb5Mu8d58/hq1ISOHXMcmIYkLy70879+HrDGz52fQJ/mVFgNqwrg3fzLLn7t0csYHK1Kb777xQXOmsQs/e0v8xEftagEtGI3lexvGx7rx6SMZB3GNFXUVCKasuXOBAgMBAAECgYEAj7GpYPIR8fyHdFz+vlaCyiC+K6BQ3k4mvoqgcYC+TWdJfygNd+ECdWdGCPlnS4rO+5Hi0ddjTOSx7cLokEsZUxLpVHEmCdxL16RpULsz+fxy7v+MojKvAVDyCl4nZ/CnO0cYY9liEICPrh4M/rNkgJJ3Cq1Ms4AwPkFFTx/ts70CQQD28WcvVAzsWsc0BXC16iyxmrxRpRDhBcAh3MclQkfQl8s6ExZSgWfk9C2it5oYAMEb8a/YRniUo1IcTDUOy4OrAkEApMSY4Fb3qcgmCO/h/pl9foA+5MNfbbeLLrD1ASRahSFiB+5mUCiBWgLlpchj0kUmjvn5CxGVN4caQlJ5msw5gwJBANupp0jhclF4+sQapsUbVsBxzVyvAZG5JMy6cGeDSxCRRlUVj4C63ek7D7Ezsx3w5dZuqiYvoalOkobbf0L249sCQDaSTrfu1fRLsB1Bpi43FBNz0mDePJsQMW7zeRI5wilZ1ygdU5G0+LKhRwJTjow0DwNH065Q0oUjoBTbUB9vCJUCQFYPa9OlzfGfUjhfF6N+4NlgkKpQUKn/dNkdZwCkSSOWgGJJGOrqdK1egTY6C0lfJusXy/X0z9n/MxT3UAKiUJQ=";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "syqs");
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap("http://localhost:8080/consumer.manage.front/api/sendPaySms",
                        encrypt);
        System.out.println("---------------------" + returnMap);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testPCpayConfirm() {
        Map<String, String> params = new HashMap<>();
        params.put("traceNo", "SYQS2002170210004");
        params.put("smsCode", "577994");
        params.put("paymentId", "201706091216081100210168707554");

        String privatekey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ7wSmUOdwP7OBBMm+tEO0OepkRaoY8DE5Rja9sSlzjCCslAb5Mu8d58/hq1ISOHXMcmIYkLy70879+HrDGz52fQJ/mVFgNqwrg3fzLLn7t0csYHK1Kb777xQXOmsQs/e0v8xEftagEtGI3lexvGx7rx6SMZB3GNFXUVCKasuXOBAgMBAAECgYEAj7GpYPIR8fyHdFz+vlaCyiC+K6BQ3k4mvoqgcYC+TWdJfygNd+ECdWdGCPlnS4rO+5Hi0ddjTOSx7cLokEsZUxLpVHEmCdxL16RpULsz+fxy7v+MojKvAVDyCl4nZ/CnO0cYY9liEICPrh4M/rNkgJJ3Cq1Ms4AwPkFFTx/ts70CQQD28WcvVAzsWsc0BXC16iyxmrxRpRDhBcAh3MclQkfQl8s6ExZSgWfk9C2it5oYAMEb8a/YRniUo1IcTDUOy4OrAkEApMSY4Fb3qcgmCO/h/pl9foA+5MNfbbeLLrD1ASRahSFiB+5mUCiBWgLlpchj0kUmjvn5CxGVN4caQlJ5msw5gwJBANupp0jhclF4+sQapsUbVsBxzVyvAZG5JMy6cGeDSxCRRlUVj4C63ek7D7Ezsx3w5dZuqiYvoalOkobbf0L249sCQDaSTrfu1fRLsB1Bpi43FBNz0mDePJsQMW7zeRI5wilZ1ygdU5G0+LKhRwJTjow0DwNH065Q0oUjoBTbUB9vCJUCQFYPa9OlzfGfUjhfF6N+4NlgkKpQUKn/dNkdZwCkSSOWgGJJGOrqdK1egTY6C0lfJusXy/X0z9n/MxT3UAKiUJQ=";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "syqs");
        LinkedHashMap returnMap =
                SendHttpsUtil.postMsg4GetMap("http://localhost:8080/consumer.manage.front/api/iqbPay",
                        encrypt);
        System.out.println("---------------------" + returnMap);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testintoNoticePrePayResult() {
        Map<String, String> params = new HashMap<>();
        params.put("amount", "300");
        params.put("tradeTime", "2017-06-15 11:27:15");
        params.put("tradeNo", "201706151123291031610022065944");
        params.put("merchantId", "M200001523");
        params.put("orderStatus", "00");
        params.put("outOrderId", "SYQS2002170614001_465726");
        params.put(
                "sign",
                "Td+0cz6I51yHJCKMlbdcxdnF67h+/1jZqeTyzftI+awvmWO5q+CDI/rq9/GtkTzRaSRe+9Hi4sfmpPwJDrOBZ7miWgZW8oJ/o+JRjD0VA2XPxQ2b404O0CizTL+keT0EBtI8biI2En8Kvye+9GgCpNJeQzK7sA5j8B3tgaWtnvI=");

        String privatekey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ7wSmUOdwP7OBBMm+tEO0OepkRaoY8DE5Rja9sSlzjCCslAb5Mu8d58/hq1ISOHXMcmIYkLy70879+HrDGz52fQJ/mVFgNqwrg3fzLLn7t0csYHK1Kb777xQXOmsQs/e0v8xEftagEtGI3lexvGx7rx6SMZB3GNFXUVCKasuXOBAgMBAAECgYEAj7GpYPIR8fyHdFz+vlaCyiC+K6BQ3k4mvoqgcYC+TWdJfygNd+ECdWdGCPlnS4rO+5Hi0ddjTOSx7cLokEsZUxLpVHEmCdxL16RpULsz+fxy7v+MojKvAVDyCl4nZ/CnO0cYY9liEICPrh4M/rNkgJJ3Cq1Ms4AwPkFFTx/ts70CQQD28WcvVAzsWsc0BXC16iyxmrxRpRDhBcAh3MclQkfQl8s6ExZSgWfk9C2it5oYAMEb8a/YRniUo1IcTDUOy4OrAkEApMSY4Fb3qcgmCO/h/pl9foA+5MNfbbeLLrD1ASRahSFiB+5mUCiBWgLlpchj0kUmjvn5CxGVN4caQlJ5msw5gwJBANupp0jhclF4+sQapsUbVsBxzVyvAZG5JMy6cGeDSxCRRlUVj4C63ek7D7Ezsx3w5dZuqiYvoalOkobbf0L249sCQDaSTrfu1fRLsB1Bpi43FBNz0mDePJsQMW7zeRI5wilZ1ygdU5G0+LKhRwJTjow0DwNH065Q0oUjoBTbUB9vCJUCQFYPa9OlzfGfUjhfF6N+4NlgkKpQUKn/dNkdZwCkSSOWgGJJGOrqdK1egTY6C0lfJusXy/X0z9n/MxT3UAKiUJQ=";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "syqs");
        String resultStr =
                SimpleHttpUtils.httpPost("http://localhost:8080/consumer.manage.front/api/intoNoticePrePayResult",
                        params);
        System.out.println("---------------------" + resultStr);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testintoNoticeNormalPaymentResult() {
        Map<String, String> params = new HashMap<>();
        params.put("amount", "300");
        params.put("tradeTime", "2017-06-15 11:27:15");
        params.put("tradeNo", "201706151123291031610022065944");
        params.put("merchantId", "M200001523");
        params.put("orderStatus", "00");
        params.put("outOrderId", "SYQS2002170614001_465726");
        params.put(
                "sign",
                "Td+0cz6I51yHJCKMlbdcxdnF67h+/1jZqeTyzftI+awvmWO5q+CDI/rq9/GtkTzRaSRe+9Hi4sfmpPwJDrOBZ7miWgZW8oJ/o+JRjD0VA2XPxQ2b404O0CizTL+keT0EBtI8biI2En8Kvye+9GgCpNJeQzK7sA5j8B3tgaWtnvI=");

        String privatekey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ7wSmUOdwP7OBBMm+tEO0OepkRaoY8DE5Rja9sSlzjCCslAb5Mu8d58/hq1ISOHXMcmIYkLy70879+HrDGz52fQJ/mVFgNqwrg3fzLLn7t0csYHK1Kb777xQXOmsQs/e0v8xEftagEtGI3lexvGx7rx6SMZB3GNFXUVCKasuXOBAgMBAAECgYEAj7GpYPIR8fyHdFz+vlaCyiC+K6BQ3k4mvoqgcYC+TWdJfygNd+ECdWdGCPlnS4rO+5Hi0ddjTOSx7cLokEsZUxLpVHEmCdxL16RpULsz+fxy7v+MojKvAVDyCl4nZ/CnO0cYY9liEICPrh4M/rNkgJJ3Cq1Ms4AwPkFFTx/ts70CQQD28WcvVAzsWsc0BXC16iyxmrxRpRDhBcAh3MclQkfQl8s6ExZSgWfk9C2it5oYAMEb8a/YRniUo1IcTDUOy4OrAkEApMSY4Fb3qcgmCO/h/pl9foA+5MNfbbeLLrD1ASRahSFiB+5mUCiBWgLlpchj0kUmjvn5CxGVN4caQlJ5msw5gwJBANupp0jhclF4+sQapsUbVsBxzVyvAZG5JMy6cGeDSxCRRlUVj4C63ek7D7Ezsx3w5dZuqiYvoalOkobbf0L249sCQDaSTrfu1fRLsB1Bpi43FBNz0mDePJsQMW7zeRI5wilZ1ygdU5G0+LKhRwJTjow0DwNH065Q0oUjoBTbUB9vCJUCQFYPa9OlzfGfUjhfF6N+4NlgkKpQUKn/dNkdZwCkSSOWgGJJGOrqdK1egTY6C0lfJusXy/X0z9n/MxT3UAKiUJQ=";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "syqs");
        String resultStr =
                SimpleHttpUtils.httpPost(
                        "http://localhost:8080/consumer.manage.front/api/intoNoticeNormalPaymentResult",
                        params);
        System.out.println("---------------------" + resultStr);
    }

    public static void main(String[] args) {

        TestIntoAssetController controller = new TestIntoAssetController();

        controller.testintoNoticeNormalPaymentResult();

        /*
         * Map<String, String> params = new HashMap<String, String>(); params.put("traceNo",
         * "SYQS2002170614001_465726"); params.put("smsCode", "478478"); params.put("paymentId",
         * "201706151123291100210169886223");
         * 
         * String privatekey =
         * "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ7wSmUOdwP7OBBMm+tEO0OepkRaoY8DE5Rja9sSlzjCCslAb5Mu8d58/hq1ISOHXMcmIYkLy70879+HrDGz52fQJ/mVFgNqwrg3fzLLn7t0csYHK1Kb777xQXOmsQs/e0v8xEftagEtGI3lexvGx7rx6SMZB3GNFXUVCKasuXOBAgMBAAECgYEAj7GpYPIR8fyHdFz+vlaCyiC+K6BQ3k4mvoqgcYC+TWdJfygNd+ECdWdGCPlnS4rO+5Hi0ddjTOSx7cLokEsZUxLpVHEmCdxL16RpULsz+fxy7v+MojKvAVDyCl4nZ/CnO0cYY9liEICPrh4M/rNkgJJ3Cq1Ms4AwPkFFTx/ts70CQQD28WcvVAzsWsc0BXC16iyxmrxRpRDhBcAh3MclQkfQl8s6ExZSgWfk9C2it5oYAMEb8a/YRniUo1IcTDUOy4OrAkEApMSY4Fb3qcgmCO/h/pl9foA+5MNfbbeLLrD1ASRahSFiB+5mUCiBWgLlpchj0kUmjvn5CxGVN4caQlJ5msw5gwJBANupp0jhclF4+sQapsUbVsBxzVyvAZG5JMy6cGeDSxCRRlUVj4C63ek7D7Ezsx3w5dZuqiYvoalOkobbf0L249sCQDaSTrfu1fRLsB1Bpi43FBNz0mDePJsQMW7zeRI5wilZ1ygdU5G0+LKhRwJTjow0DwNH065Q0oUjoBTbUB9vCJUCQFYPa9OlzfGfUjhfF6N+4NlgkKpQUKn/dNkdZwCkSSOWgGJJGOrqdK1egTY6C0lfJusXy/X0z9n/MxT3UAKiUJQ="
         * ; Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
         * encrypt.put("merchantNo", "syqs");
         * 
         * System.out.println("---------------------" + JSONObject.toJSONString(encrypt));
         */
    }

    @Test
    public void testGetInfoByRegId() {
        // 生成IP认证
        String random = RandomStringUtils.random(20, true, true);
        Integer randomInt = (int) (Math.random() * 16 + 1);
        String ciphertext = EncryptionAlgorithm.encrypte("127.0.0.1", random, randomInt, new Date());
        String express = random + randomInt.toString();
        String url = "http://localhost:8080/consumer.manage.front/api/getInfoByRegId";
        String uri = url + "?ciphertext=" + ciphertext + "&express=" + express;

        Map<String, String> params = new HashMap<>();
        // params.put("merchantNo", "tjym");
        params.put("regId", "13020079696");
        String privatekey =
                "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKEO+ErTmX/NkuqZJYRQ4zsN9eRwEk+HzzT7vtX/mHnlaN48e0+vQwv4DFIr5vcnKTEnrTHDjm8nLxv9hWEyvNqqNvn88C1evGY7YS7UHF/rIlIHk5yqkhLmI4hMK4A09cZE3H//SvtarfbXNalgpllakBYw0uMTG3LIVBbjolqDAgMBAAECgYBx/9qXfDcGU0hObaA0i9yLDFKkIv+S85oi8p4dcxFGFq+nvj+6I1/dtPFjqFePUVTCyka5FqZW3vFlaoliRzuMmrTnMwHP0FFb3zk0KOoScgwBzKb+B4KwvWqmGvDtBSGr55txOvq4hMnN7F/l1gd1P5JUeTSkOwp132Bk/AkdQQJBAN00U6c030FJAEAMlLiIr3V8o/rKlA5LrHKI0+MHrZyswajjyPU7ZAesGLHGkqozDkDsMv8B8YwODyhbKWcVYqkCQQC6ZKDy/G6TPBmqmPLWEtIlFUpQG9IpFdtK0E3/XQpYGWtWdkVoV5RTJiTARxlq0L1MPNe35EA5u2BvnceEBbtLAkBQ9KKc8jO23/Gwfyo6swOL+vbEhTlUVguhy0PItfmq+mrR1bOpVHgSesB655KrqY3Q4uDzBRIS0N5pRZvMdz4pAkEAsei+328mtA5XiUg38TFBt/ecWN5VOLYN/FjbOfU6nGCW3Y5CLZf6kWsFBSpYDmZzaMNiO7n3u6MoJyF4gIFgBQJAXtTyeTdMwCTIT7kvbtXF+nc/zK5CYGvla9ib+2EC1pAZjeAfybFVYktOwt4MwxEu8yvypzFSRcdD1gepdnvnhQ==";
        Map<String, Object> encrypt = new EncryptUtils().encrypt(params, privatekey);
        encrypt.put("merchantNo", "tjym");
        // String result = HttpClientUtil.httpPost("http://localhost:9080/consumer.manage.front/" +
        // "auth/auththreeYards", json);
        LinkedHashMap postMsg4GetMap =
                SendHttpsUtil.postMsg4GetMap(uri, encrypt);
        logger.info("资产进件返回值:{}", JSONObject.toJSON(postMsg4GetMap));
    }
}
