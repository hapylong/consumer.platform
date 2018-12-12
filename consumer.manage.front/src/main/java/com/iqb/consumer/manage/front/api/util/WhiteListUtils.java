package com.iqb.consumer.manage.front.api.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.etep.common.exception.IqbException;

/**
 * 白名单效验，为了安全，添加一个白名单限制
 * 
 * @author Yeoman
 * 
 */
public class WhiteListUtils {

    private static final Logger logger = LoggerFactory.getLogger(WhiteListUtils.class);

    /**
     * 访问方是否在白名单范围内
     * 
     * @param request
     * @param ips
     * @return
     * @throws IqbException
     */
    public static boolean valid(HttpServletRequest request, List<String> ips) throws IqbException {
        Map<String, String> param = new HashMap<String, String>();
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
        logger.info("接收到白名单数据{}", param);
        boolean flag = false;
        try {
            // 合法性效验
            flag = decode(param.get("ciphertext"), param.get("express"), ips);
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_NOTINWHITELIST_90000001);
        }
        return flag;
    }

    /**
     * @param ciphertext
     * @param express
     * @param zxfIP
     * @return
     */
    private static boolean decode(String ciphertext, String express, List<String> ips) {
        String number = "";
        if (express.length() == 21) {
            number = express.substring(20, 21);
        }
        if (express.length() == 22) {
            number = express.substring(20, 22);
        }
        int randomNumber = Integer.parseInt(number);
        String[] ds = ciphertext.split("-");
        byte[] bytes = new byte[40];
        int count = 0;
        for (String s : ds) {
            int xor = Integer.parseInt(s);
            Integer xorMid = xor ^ randomNumber;
            byte b = xorMid.byteValue();
            bytes[count] = b;
            count++;
        }
        String s1 = new String(bytes);

        String[] ds1 = s1.split("-");
        String ip = ds1[0];
        if (ips.contains(ip))
            return true;
        return false;
    }
}
