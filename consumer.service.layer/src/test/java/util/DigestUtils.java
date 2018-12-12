/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年5月3日 下午4:47:15
 * @version V1.0
 */

package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class DigestUtils {

    public static String getSignData(String signName, Map<String, String> params) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if (signName.equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            value = null == value ? "" : value;
            content.append("&" + key + "=" + value);
        }
        String str = content.toString();
        if (isEmpty(str)) {
            return str;
        }
        if (str.startsWith("&")) {
            return str.substring("&".length());
        }
        return str;
    }

    private static boolean isEmpty(String str) {
        return (str == null) || (str.length() == 0);
    }

}
