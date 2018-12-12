package com.iqb.consumer.manage.front.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.RBParamConfig;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.manage.front.ParamConfig;
import com.iqb.etep.common.base.service.BaseService;

/**
 * 鉴权接口，作为API接口方便第三方使用
 * 
 * @author Yeoman
 * 
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseService {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Resource
    private EncryptUtils encryptUtils;
    @Resource
    private RBParamConfig rbParamConfig;
    /** 成功 **/
    public static final String SUCC = "success";
    @Autowired
    private ParamConfig paramConfig;

    protected static final String THREEYARDS = "threeYards"; // 三码鉴权
    protected static final String FOURYARDS = "fourYards"; // 四码鉴权

    // /**
    // * {\
    // *
    // "bank_card_type\":\"0\",\"bank_code\":\"BOC\",\"bank_name\":\"中国银行\",\"card_last\":\"7367\",\"merchant_id\":\"100000000254474\",\"phone\":\"\",\"result_code\":\"0000\",\"result_msg\":\"鉴权成功\",\"sign\":\"1b31d900c4fff0ea3e870
    // * 6 5 1 9 7 0 b 2 f 4 c \ " }
    // *
    // * @param type
    // * @param request
    // * @param response
    // * @return
    // */
    // @RequestMapping(value = {"/auth{type}"}, method = {RequestMethod.GET, RequestMethod.POST})
    // public Object auth(@PathVariable("type") String type, HttpServletRequest request,
    // HttpServletResponse response) {
    // Map<String, Object> result = new HashMap<String, Object>();
    // // 加密
    // String jsonStr = null;
    // try {
    // jsonStr = encryptUtils.decode(request, "");
    // } catch (IqbException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // AuthDto authDto = JSONObject.parseObject(jsonStr, AuthDto.class);
    // Map<String, String> params = new HashMap<String, String>();
    // params.put("card_no", authDto.getCardNo()); // 卡号
    // params.put("owner", authDto.getOwner()); // 姓名
    // params.put("cert_no", authDto.getCertNo()); // 证件号
    // params.put("member_id", authDto.getMemberId());
    // Map<?, ?> map = null;
    // if (THREEYARDS.equalsIgnoreCase(type)) {// 三码鉴权
    // map = IdentifyOperation.checkThreeElements(params, rbParamConfig);
    // } else if (FOURYARDS.equalsIgnoreCase(type)) { // 四码鉴权
    //
    // } else {
    // result.put("result_code", "9999");
    // result.put("result_msg", "不支持的鉴权方式");
    // }
    // return map;
    // }

    /**
     * 内部使用的鉴权接口 三码
     * 
     * @param objs
     * @param request
     * @return
     */
    private String randomInt(int max) {
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        for (int i = 0; i < max; i++) {
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }

    /*
     * @RequestMapping(value = {"/innerAuth{type}"}, method = {RequestMethod.GET,
     * RequestMethod.POST}) public Object innerAuth(@PathVariable("type") String type, @RequestBody
     * JSONObject objs, HttpServletRequest request) { Map<String, Object> result = new
     * HashMap<String, Object>(); try { InnerAuthRequestPojo iarp = JSONObject.toJavaObject(objs,
     * InnerAuthRequestPojo.class); if (iarp == null || !iarp.checkRequest(type)) {
     * result.put("retCode", "9999"); result.put("retMsg", "请求参数不完整"); return result; } Map<String,
     * String> params = new HashMap<String, String>();
     * 
     * params.put("bankCardNum", iarp.getCard_no()); // 卡号 params.put("bizChannelCode", "zhongge");
     * // 中阁CODE params.put("userName", iarp.getOwner()); // 姓名 params.put("idNum",
     * iarp.getCert_no()); // 证件号 params.put("member_id", randomInt(11)); if
     * (THREEYARDS.equalsIgnoreCase(type)) {// 三码鉴权 params.put("bankReservedPhoneNum", ""); //
     * 银行预留手机号 } else if (FOURYARDS.equalsIgnoreCase(type)) { // 四码鉴权
     * params.put("bankReservedPhoneNum", iarp.getPhone()); // 银行预留手机号 } else {
     * result.put("retCode", "9999"); result.put("retMsg", "不支持的鉴权方式"); } Map<?, ?> map =
     * IdentifyOperation.checkThreeElements(params, rbParamConfig); if (map != null) { if
     * ("0000".equals(map.get("result_code"))) { // 鉴权成功 result.put("retCode", "0000");
     * result.put("retMsg", "鉴权成功"); } else { result.put("retCode", "9999"); result.put("retMsg",
     * map.get("result_msg")); } } else { result.put("retCode", "9999"); result.put("retMsg",
     * "鉴权异常"); return result; } return result; } catch (Throwable e) { logger.error("鉴权异常: ", e);
     * result.put("retCode", "9999"); result.put("retMsg", "鉴权异常"); return result; } }
     */

    @RequestMapping(value = {"/innerAuth{type}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object innerAuth(@PathVariable("type") String type, @RequestBody JSONObject objs, HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, Object> result = new HashMap<String, Object>();
        map.put("bizChannelCode", "zhongge");
        map.put("bankCardNum", objs.getString("card_no"));
        map.put("userName", objs.getString("owner"));
        map.put("idNum", objs.getString("cert_no"));
        if (THREEYARDS.equalsIgnoreCase(type)) {// 三码鉴权
            map.put("bankReservedPhoneNum", ""); // 银行预留手机号
        } else if (FOURYARDS.equalsIgnoreCase(type)) { // 四码鉴权
            map.put("bankReservedPhoneNum", objs.getString("phone")); // 银行预留手机号
        } else {
            result.put("retCode", "9999");
            result.put("retMsg", "不支持的鉴权方式");
        }
        String httpStr = null;
        try {
            logger.info("TOWN:中阁鉴权入参{}",map);
            logger.info("TOWN:中阁鉴权URL{}",paramConfig.getAuthInfoUrl());
            httpStr = SimpleHttpUtils.httpPost(paramConfig.getAuthInfoUrl(), map);
            logger.info("TOWN:中阁鉴权返回值{}",httpStr);
        } catch (Exception e) {
            logger.error("调用鉴权接口发生错误参数:{}", objs, e);
        }
        if (SUCC.equals(httpStr)) {
            result.put("retCode", "1");
            result.put("retMsg", "鉴权成功");
        } else {
            result.put("retCode", "2");
            result.put("retMsg", "鉴权失败");
        }
        return result;
    }

    @RequestMapping(value = {"/unIntcpt-scallback"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object scallback(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("AuthController.scallback :" + JSONObject.toJSONString(objs));
        } catch (Exception e) {
            logger.error("AuthController.scallback error:", e);
        }
        return "{\"result_code\":\"00\",\"result_msg\":\"iqianbang\"}";
    }

    @RequestMapping(value = {"/unIntcpt-fcallback"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Object fcallback(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("AuthController.fcallback :" + JSONObject.toJSONString(objs));
        } catch (Exception e) {
            logger.error("AuthController.fcallback error:", e);
        }
        return "";
    }

    public static void main1(String[] args) {
        try {
            JSONObject json = new JSONObject();
            json.put("owner", "4321");
            json.put("cert_no", "4321");
            json.put("card_no", "4321");
            json.put("phone", "4321");
            AuthController.InnerAuthRequestPojo iarp = JSONObject.toJavaObject(json, InnerAuthRequestPojo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class InnerAuthRequestPojo {
        private String card_no;
        private String owner;
        private String cert_no;
        private String phone;

        public String getCard_no() {
            return card_no;
        }

        public void setCard_no(String card_no) {
            this.card_no = card_no;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getCert_no() {
            return cert_no;
        }

        public void setCert_no(String cert_no) {
            this.cert_no = cert_no;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public boolean checkRequest(String type) {
            logger.info("checkRequest :" + toString());
            if (StringUtil.isEmpty(card_no) ||
                    StringUtil.isEmpty(owner) ||
                    StringUtil.isEmpty(cert_no)) {
                return false;
            }
            if (THREEYARDS.equalsIgnoreCase(type)) {
                return true;
            } else if (FOURYARDS.equalsIgnoreCase(type)) {
                return !StringUtil.isEmpty(phone);
            }
            return false;
        }

        @Override
        public String toString() {
            return "InnerAuthRequestPojo [card_no=" + card_no + ", owner=" + owner + ", cert_no=" + cert_no
                    + ", phone=" + phone + "]";
        }

    }

}
