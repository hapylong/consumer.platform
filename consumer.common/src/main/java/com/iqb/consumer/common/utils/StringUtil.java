package com.iqb.consumer.common.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {

    /**
     * 随机生成count个大写字母
     * 
     * @param count 数量
     * @return
     */
    public static String randomUpLetter(int count) {
        return RandomStringUtils.randomAlphabetic(count).toUpperCase();
    }

    /**
     * 判断字符串是否匹配regexs，只要匹配任何一个，则返回true
     * 
     * @param source 源字符串
     * @param regexs 正则表达式数组
     * @return
     */
    public static boolean matches(String source, String... regexs) {
        for (String regex : regexs) {
            if (source.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

    /**
     * @param htmlStr
     * @return 删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
        if (htmlStr == null) {
            return null;
        }
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 判断字符串是否为空字符串，如果str为"null"，认为其不为空字符串
     * 
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return (str == null || str.isEmpty());
    }

    /**
     * 
     * Description: list转字符串
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2016年10月25日 下午3:40:36
     */
    public static String listToStr(final Iterable<?> iterable, final String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    /**
     * 
     * Description: 字符串转list
     * 
     * @param
     * @return List<T>
     * @throws
     * @Author wangxinbang Create Date: 2016年10月25日 下午3:42:07
     */
    public static List<String> strToList(String str, String sp) {
        return Arrays.asList(split(str, sp));
    }

    public static String getGenderByIdCardNo(String idNO) {
        if (isEmpty(idNO)) {
            return "不详";
        }
        int leh = idNO.length();
        String sexMark = idNO.substring(leh - 2, leh - 1);
        if (Integer.parseInt(sexMark) % 2 == 0) {
            return "女";
        } else {
            return "男";
        }
    }

    public static int getAgeByIdCardNo(String IdNO) {
        int leh = IdNO.length();
        String dates = "";
        if (leh == 18) {
            dates = IdNO.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year = df.format(new Date());
            int u = Integer.parseInt(year) - Integer.parseInt(dates);
            return u;
        } else {
            dates = IdNO.substring(6, 8);
            return Integer.parseInt(dates);
        }
    }

}
