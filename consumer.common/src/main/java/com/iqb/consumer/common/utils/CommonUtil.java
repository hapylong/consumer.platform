/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月6日 上午10:10:56
 * @version V1.0
 */
package com.iqb.consumer.common.utils;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class CommonUtil {

    protected static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * The UUID make use of JDK's UUID to generate random UUID.
     * 
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String randomString(int length) {
        String rs = null;
        try {
            rs = RandomStringUtils.random(length, 0, 0, true, true, null, new SecureRandom());
        } catch (Exception e) {
            logger.error("calculate the random string failed", e);
        }
        return rs;
    }

    public static String checkMap(Map map) {
        Set keys = map.keySet();
        for (Object key : keys) {
            if (map.get(key) == null) {
                return key.toString();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(randomString(6));
    }
}
