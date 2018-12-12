/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年10月11日 上午11:25:26
 * @version V1.0
 */
package com.iqb.consumer.manage.front.api;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.RBParamConfig;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.manage.front.api.onigiri.Decipher;
import com.iqb.consumer.manage.front.api.onigiri.Md5Utils;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class IdentifyOperation {

    private static Logger logger = LoggerFactory.getLogger(IdentifyOperation.class);

    public static Map<?, ?> checkThreeElements(Map<String, String> params, RBParamConfig rbParamConfig) {
        params.put("merchant_id", rbParamConfig.getRbMerchantId()); // 商户ID
        params.put("cert_type", "01"); // 证件类型
        params.put("cvv2", ""); // 信用卡背后的三位数字
        params.put("validthru", ""); // 卡有效期
        params.put("identify_id", getUUID()); // 指纹ID
        params.put("version", rbParamConfig.getRbVersion());
        Map<?, ?> result = null;
        try {
            String retResult = buildSubmit(params, rbParamConfig);
            result = JSONObject.parseObject(retResult);
            return result;
        } catch (Throwable e) {
            logger.error("IdentifyOperation.checkThreeElements exception :", e);
            return result;
        }
    }

    // {
    // "bank_card_type": "0",
    // "bank_name": "建设银行",
    // "bind_id": "1994",
    // "card_last": "0306",
    // "member_id": "12345678900",
    // "merchant_id": "100000000009085",
    // "phone": "15211413775",
    // "result_code": "0000",
    // "result_msg": "鉴权成功",
    // "sign": "e1bf09d24cc12d6a872219ae4a5e2178"
    // }

    // 传入参数：

    /**
     * 融宝鉴权接口
     * 
     * @param para
     * @param url
     * @return
     * @throws Exception
     */
    private static String buildSubmit(Map<String, String> para, RBParamConfig rbParamConfig)
            throws Exception {
        // 生成签名
        String mysign = Md5Utils.BuildMysign(para, rbParamConfig.getRbKey());// key
                                                                             // 交易安全检验码，由数字和字母组成的64位字符串
        para.put("sign_type", "MD5");
        para.put("sign", mysign);
        // 将map转化为json串
        String json = JSON.toJSONString(para);
        // 加密数据
        Map<String, String> maps = Decipher.encryptData(json, rbParamConfig);
        maps.put("merchant_id", rbParamConfig.getRbMerchantId());
        // 发送请求 并接收
        String post = SimpleHttpUtils.httpPost(rbParamConfig.getRbNewPayUrl() + "/identify/doIdentify", maps);
        // String post = HttpClientUtil.post(ReapalConfig.rongpay_api + url, maps);
        // 解密返回的数据
        String res = Decipher.decryptData(post, rbParamConfig);
        return res;
    }

    private static String getUUID() {
        String s = UUID.randomUUID().toString();
        // 去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
}
