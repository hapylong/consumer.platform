/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年7月19日 上午11:20:17
 * @version V1.0
 */

package com.iqb.consumer.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class BigDecimalUtil {

    public static BigDecimal add(BigDecimal... v) {
        BigDecimal initBigDecimal = new BigDecimal(0);
        for (int i = 0; i < v.length; i++) {
            initBigDecimal = initBigDecimal.add(v[i]);
        }
        return format(initBigDecimal);
    }

    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return format(v1.subtract(v2));
    }

    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        return format(v1.multiply(v2));
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return v1.divide(v2, 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal format(BigDecimal v1) {
        DecimalFormat df = new DecimalFormat("#.00");
        return new BigDecimal(df.format(v1)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal formatFloat(float v1) {
        DecimalFormat df = new DecimalFormat("#.00");
        return new BigDecimal(df.format(v1)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal formatInt(float v1) {
        DecimalFormat df = new DecimalFormat("#.00");
        return new BigDecimal(df.format(v1)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 缩小100倍
     * 
     * @param v1
     * @return
     */
    public static BigDecimal narrow(BigDecimal v1) {
        return div(v1, new BigDecimal(100));
    }

    /**
     * 扩大100倍
     * 
     * @return
     */
    public static BigDecimal expand(BigDecimal v1) {
        return mul(v1, new BigDecimal(100));
    }

    public static BigDecimal div100(BigDecimal d) {
        if (d == null) {
            return null;
        }
        return d.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
    }

}
