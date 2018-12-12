/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 日期工具
 * @date 2016年7月18日 上午11:43:51
 * @version V1.0
 */

package com.iqb.consumer.common.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class DateUtil {

    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    public static final String CREDIT_CARD_DATE_FORMAT = "MM/yyyy";
    public static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String SHORT_DATE_FORMAT_CN = "yyyy年MM月dd日";
    public static final String SHORT_DATE_DOT_FORMAT = "yyyy.MM.dd";
    public static final String SHORT_DATE_FORMAT_NO_DASH = "yyyyMMdd";
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SIMPLE_DATE_FORMAT_WITH_ZONE = "yyyy-MM-dd HH:mm:ss.SSSZ";
    public static final String ZONE_DATE_FORMAT_WITH_WEEK_ZONE = "EEE yyyy-MM-dd HH:mm:ss zzz";
    public static final String SIMPLE_DATE_FORMAT_NO_DASH = "yyyyMMddHHmmss";
    public static final String LOG_DATE_FORMAT = "yyyyMMdd_HH00";
    public static final String ZONE_DATE_FORMAT = "EEE yyyy-MM-dd HH:mm:ss zzz";
    public static final String DATE_FORMAT = "yyyy/MM/dd EEE";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String SIMPLE_TIME_FORMAT = "HH:mm:ss";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-ddTHH:mm:ss.SSSZ";

    public static final long ONE_MIN = 60 * 1000;
    public static final long ONE_HOUR = 60 * ONE_MIN;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long SCHEDULE_INTERVAL = 15 * ONE_MIN;

    public static int daysBetween(Calendar startTime, Calendar endTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("startTime is null");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("endTime is null");
        }
        if (startTime.compareTo(endTime) > 0) {
            throw new IllegalArgumentException("endTime is before the startTime");
        }
        return (int) ((endTime.getTimeInMillis() - startTime.getTimeInMillis()) / (1000 * 60 * 60 * 24));
    }

    public static Calendar startOfDayTomorrow() {
        Calendar calendar = Calendar.getInstance();
        truncateDay(calendar);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    /**
     * create a calendar for start of day yesterday.
     * 
     * @return
     */
    public static Calendar startOfDayYesterday() {
        Calendar yesterday = Calendar.getInstance();
        truncateDay(yesterday);
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        return yesterday;
    }

    /**
     * Truncate the calendar's Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND
     * to ZERO.
     * 
     * @param calendar
     * @return
     */
    public static Calendar truncateDay(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("input is null");
        }
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Calendar truncateMonth(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("input is null");
        }
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * format a calendar using {@link SimpleDateFormat}, with default pattern. if a null calendar is
     * passed in, empty string is returned.
     * 
     * @param calendar
     * @return
     */
    public static String format(Calendar calendar) {
        String formatted = "";
        if (calendar != null) {
            formatted = new SimpleDateFormat().format(calendar.getTime());
        }
        return formatted;
    }

    /**
     * format a Time using {@link SimpleDateFormat}, with default pattern. if a null calendar is
     * passed in, empty string is returned.
     * 
     * @param time
     * @return
     */
    public static String format(Time time) {
        String formatted = "";
        if (time != null) {
            formatted = new SimpleDateFormat(TIME_FORMAT).format(time.getTime());
        }
        return formatted;
    }

    /**
     * Return the String representation of the Calendar against the given format.
     * 
     * @param date the date to format, such as 'yyyy-MM-dd HH:mm:ss' for long date format with 24H
     * @param format the date format pattern
     * @return the format Date String.
     */
    public static String getDateString(Calendar calendar, String format) {
        if (calendar == null) {
            return null;
        }
        return getDateString(calendar.getTime(), format);
    }

    /**
     * Return the String representation of the Date against the given format.
     * 
     * @param date the date to format
     * @param format the date format pattern
     * @return the format Date String.
     */
    public static String getDateString(Date date, String format) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Return the String representation of the Date against the given format.
     * 
     * @param date the date to format
     * @param format the date format pattern
     * @return the format Date String.
     */
    public static String getDateString(Date date, String format, Locale locale) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(date);
    }

    /**
     * Return the String representation of the Calendar for the given format.
     * 
     * @param cal the Calendar to format
     * @param format the date format pattern, e.g., {@code "EEE yyyy-MM-dd HH:mm:ss zzz"}
     * @param timeZone the timeZone for formatter, e.g., {@code TimeZone.getTimeZone("UTC")}
     * @param locale the timeZone for formatter, e.g., {@code Locale.CHINA}, should be original from
     *        user profile
     * 
     * @return the format Date String.
     */
    public static String getDateStringWithZone(Calendar cal, String format, TimeZone timeZone, Locale locale) {
        if (cal == null) {
            return null;
        }
        if (format == null) {
            format = ZONE_DATE_FORMAT;
        }
        if (timeZone == null) {
            TimeZone.getTimeZone("UTC");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);

        sdf.setTimeZone(timeZone);
        return sdf.format(cal.getTime());
    }

    /**
     * Parses the <tt>Date</tt> from the given date string and the format pattern.
     * 
     * @param dateString
     * @param pattern the date format
     * @throws {@link IllegalArgumentException} if date format error
     * @return
     */
    public static Date parseDate(String dateString, String pattern) {
        if (dateString == null) {
            return null;
        }

        Date date = null;
        try {
            DateFormat format = new SimpleDateFormat(pattern);
            date = format.parse(dateString);
        } catch (ParseException ex) {
            log.error("Invalid date string: " + dateString, ex);
            throw new IllegalArgumentException("Invalid date string: " + dateString, ex);
        }

        return date;
    }

    public static Calendar parseCalendarShort(String dateString) {
        if (!StringUtils.hasText(dateString)) {
            return null;
        }
        return parseCalendar(dateString, SHORT_DATE_FORMAT);
    }

    public static Calendar parseCalendar(String dateString) {
        if (!StringUtils.hasText(dateString)) {
            return null;
        }
        return parseCalendar(dateString, SIMPLE_DATE_FORMAT);
    }

    /**
     * Parses the <tt>Date</tt> from the given date string and the format pattern and return it as a
     * {@link Calendar} instance.
     * 
     * @param dateString
     * @param pattern the date format
     * @throws {@link IllegalArgumentException} if date format error
     * @return
     */
    public static Calendar parseCalendar(String dateString, String pattern) {
        if (dateString == null) {
            return null;
        }
        Date date = parseDate(dateString, pattern);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    /**
     * Parses the <tt>Date</tt> from the given date string with the format pattern 'yyyy-MM-dd'.
     * 
     * @param dateString
     * @throws {@link IllegalArgumentException} if date format error
     * @return
     */
    public static Date parseShortDate(String dateString) {
        if (!StringUtils.hasText(dateString)) {
            return null;
        }
        Date date = null;
        try {
            DateFormat format = new SimpleDateFormat(SHORT_DATE_FORMAT);
            date = format.parse(dateString);
        } catch (ParseException ex) {
            log.error("Invalid date string: " + dateString, ex);
            throw new IllegalArgumentException("Invalid date string: " + dateString, ex);
        }

        return date;
    }

    /**
     * return one day before the given date.
     * 
     * @param date the given date
     * @return the adjusted date.
     */
    public static Calendar afterNDay(Calendar date, int amount) {
        Calendar cal = (Calendar) date.clone();
        cal.add(Calendar.DATE, amount);
        return cal;
    }

    public static Calendar lastBillDay(Calendar date) {
        Calendar cal = (Calendar) date.clone();
        cal.add(Calendar.MONTH, 1);
        return afterNDay(cal, -1);
    }

    /**
     * return one day after the given Date
     * 
     * @param date
     * @return
     */
    public static Calendar afterOneMonth(Calendar date, int amount) {
        Calendar cal = (Calendar) date.clone();
        cal.add(Calendar.MONTH, amount);
        return cal;
    }

    /**
     * return one day before the given date.
     * 
     * @param date the given date
     * @return the adjusted date.
     */
    public static Calendar backOneYear(Calendar date) {
        Calendar cal = (Calendar) date.clone();
        cal.add(Calendar.YEAR, -1);
        return cal;
    }

    /**
     * Get how many days in current month.
     * 
     */
    public static int daysForCurrentMonth() {
        Calendar c = Calendar.getInstance();
        int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        return days;
    }

    /**
     * Return the Calendar for the give date.
     * 
     * @param date
     * @return
     */
    public static Calendar fromDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    /**
     * return the EPOCH = "1970-01-01 00:00:00"
     * 
     * @param dateString
     * @param pattern
     * @return
     */
    public static Calendar epoch() {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(0);
        return c;
    }

    public static long getSimpleDateTimeMillis(long timeMillis) {
        Date date = new Date(timeMillis);
        String dateStr = getDateString(date, SHORT_DATE_FORMAT);
        Date transformDate = parseDate(dateStr, SHORT_DATE_FORMAT);
        return transformDate.getTime();
    }

    /**
     * get the date from a day with days
     * 
     * @param from which day
     * @param days interval days, 0: today; positive: future; negative: history.
     * @return
     */
    public static Calendar getDateFromDate(Date from, long days) {
        long froml = from.getTime();
        // 时间间隔。
        long interval = days * 24l * 60l * 60l * 1000l;
        long millis = froml + interval;
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(millis);
        return now;
    }

    /**
     * get the date from a day with days
     * 
     * @param from which day
     * @param days interval days, 0: today; positive: future; negative: history.
     * @return String
     * 
     */
    public static String getDateFromDate(Date from, long days, String format) {
        Calendar c = getDateFromDate(from, days);
        return getDateString(c, format);
    }

    public static String getDateFromDate(String from, long days, String format) {
        Date d = parseDate(from, format);
        Calendar c = getDateFromDate(d, days);
        return getDateString(c, format);
    }

    public static String getDetailTime(long ms) {
        return getDetailTime(ms, null);
    }

    public static String getDetailTime(long ms, String format) {
        if (format == null || "".equals(format.trim())) {
            return getDetailDay(ms, "dd天hh小时mm分钟ss秒");
        } else if (format.contains("dd")) {
            return getDetailDay(ms, format);
        } else if (format.contains("hh")) {
            return getDetailHour(ms, format);
        } else if (format.contains("mm")) {
            return getDetailMinute(ms, format);
        } else if (format.contains("ss")) {
            return getDetailSecond(ms, format);
        }
        return format.replace("dd", "");
    }

    /**
     * @date 2011-12-20
     * @param ms
     * @param type
     * @return
     */
    public static String getDetailDay(long ms, String format) {
        long dd = 1000l * 60l * 60l * 24l;
        long day = ms / dd;
        format = getDetailHour(ms % dd, format);
        return format.replace("dd", day + "");
    }

    public static long getDetailDay(long ms) {
        String d = getDetailDay(ms, "dd");
        return Long.parseLong(d);
    }

    public static String getDetailHour(long ms, String format) {
        long hh = 1000l * 60l * 60l;
        long hour = ms / hh;
        format = getDetailMinute(ms % hh, format);
        return format.replace("hh", hour + "");
    }

    public static long getDetailHour(long ms) {
        String d = getDetailHour(ms, "hh");
        return Long.parseLong(d);
    }

    public static String getDetailMinute(long ms, String format) {
        long mi = 1000l * 60l;
        long minute = ms / mi;
        format = getDetailSecond(ms % mi, format);
        return format.replace("mm", minute + "");
    }

    public static long getDetailMinute(long ms) {
        String d = getDetailMinute(ms, "mm");
        return Long.parseLong(d);
    }

    public static String getDetailSecond(long ms, String format) {
        long ss = 1000l;
        String ssStr = (ms / ss) + "";
        return format.replace("ss", ssStr);
    }

    public static long getDetailSecond(long ms) {
        String d = getDetailSecond(ms, "ss");
        return Long.parseLong(d);
    }

    /**
     * return Calendar after add the given days. if it is negative, will back the time, otherwise,
     * if it is positive , it will forward the time.
     * 
     * @param date the given date
     * @param the days to add
     * @return the adjusted date.
     */
    public static Calendar addDays(Calendar date, int days) {
        Calendar cal = (Calendar) date.clone();
        cal.add(Calendar.DATE, days);
        return cal;
    }

    public static Calendar addDays(Date date, int days) {
        return addDays(fromDate(date), days);
    }

    public static Date addDayFromDate(Date date, int days) {
        Calendar c = fromDate(date);
        return addDays(c, days).getTime();
    }

    /**
     * return Calendar after add the given seconds. if it is negative, will back the time,
     * otherwise, if it is positive , it will forward the time.
     * 
     * @param date the given date
     * @param seconds
     * @return the adjusted date.
     */
    public static Calendar addSeconds(Calendar date, int seconds) {
        Calendar cal = (Calendar) date.clone();
        cal.add(Calendar.SECOND, seconds);

        return cal;
    }

    public static Calendar addSeconds(Date date, int seconds) {
        return addSeconds(fromDate(date), seconds);
    }

    public static Calendar addHours(Calendar date, int hours) {
        Calendar cal = (Calendar) date.clone();
        cal.add(Calendar.HOUR, hours);

        return cal;
    }

    public static Calendar addHours(Date date, int seconds) {
        return addHours(fromDate(date), seconds);
    }

    public static Calendar addMinutes(Calendar date, int minutes) {
        Calendar cal = (Calendar) date.clone();
        cal.add(Calendar.MINUTE, minutes);

        return cal;
    }

    public static Calendar addMinutes(Date date, int minutes) {
        return addMinutes(fromDate(date), minutes);
    }

    public static Calendar startDateOfYear(Calendar cal) {
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, 0); // 0 = january
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return cal;
    }

    public static Calendar endDateOfYear(Calendar cal) {
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, 11); // 11 = december
        cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve

        return cal;
    }

    public static Calendar startDateOfMonth(Calendar cal) {
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return cal;
    }

    public static Calendar startDateOfWeek(Calendar cal) {
        cal.set(Calendar.DAY_OF_WEEK, 1);

        return cal;
    }

    public static Calendar startTimeOfDay(Calendar cal) {
        return truncateDay(cal);
    }

    public static String format(Calendar calendar, String format) {
        String timeStr = null;
        if (calendar != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            timeStr = sdf.format(calendar.getTime());
        }
        return timeStr;
    }

    public static Calendar fromTimeStamp(Timestamp time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time.getTime());

        return c;
    }

    public static Calendar now() {
        Calendar c = Calendar.getInstance();
        return c;
    }

    /**
     * Calculate the difference between two dates.
     * 
     * @param fronter the fronter date
     * @param later the later date
     * @return
     */
    public static long diffHours(Date fronter, Date later) {
        long diff = later.getTime() - fronter.getTime();

        long days = TimeUnit.MILLISECONDS.toHours(diff);

        return days;
    }

    /**
     * Calculate the difference between two dates.
     * 
     * @param fronter the fronter date
     * @param later the later date
     * @return
     */
    public static long diffDays(Date fronter, Date later) {
        long diff = later.getTime() - fronter.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        return days;
    }

    /**
     * 相差月份
     * 
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static int getMonthSpace(String frontDate, String endDate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(sdf.parse(frontDate));
        c2.setTime(sdf.parse(endDate));
        int i = 0;
        if (c1.compareTo(c2) > 0) {
            return -1;
        }
        while (true) {
            if (isSameMonth(c1, c2)) {
                break;
            } else {
                i++;
                c1.add(Calendar.MONTH, 1);
            }
        }
        return i;
    }

    /**
     * 
     * Description: 是否同一个月
     * 
     * @param
     * @return boolean
     * @throws
     * @Author wangxinbang Create Date: 2016年11月7日 下午7:47:49
     */
    public static boolean isSameMonth(Calendar c1, Calendar c2) {
        return c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);

    }

    /**
     * 
     * Description: 赋值Calendar
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2016年11月7日 下午9:30:08
     */
    public static Calendar copyCal(Calendar c1) {
        Date d = c1.getTime();
        Calendar tCal = Calendar.getInstance();
        tCal.setTime(d);
        return tCal;
    }

    /**
     * 
     * Description: 获取区间日期
     * 
     * @param
     * @return List<Date>
     * @throws
     * @Author wangxinbang Create Date: 2016年11月14日 下午5:14:11
     */
    public static List<Date> getDateListBetween(Date d1, Date d2) {
        List<Date> list = new ArrayList<>();
        Long oneDay = 1000 * 60 * 60 * 24l;
        if (d1 == null || d2 == null) {
            return null;
        }
        Long t1 = d1.getTime();
        Long t2 = d2.getTime();
        t2 = t2 + 1000 * 60 * 60 * 24l - 1l;
        while (t1 <= t2) {
            list.add(new Date(t1));
            t1 += oneDay;
        }
        return list;
    }

    /**
     * 
     * Description: 获取昨天日期
     * 
     * @param
     * @return Date
     * @throws
     * @Author wangxinbang Create Date: 2016年11月29日 下午3:09:06
     */
    public static Date getYesterdayDate() {
        return getDateFromDate(new Date(), -1).getTime();
    }

    /**
     * 根据日期获取对应的月份 天数
     * */
    public static int getDaysByDate(Date date) {
        Calendar a = Calendar.getInstance();
        a.setTime(date);
        int maxDate = a.getActualMaximum(Calendar.DATE);
        return maxDate;
    }

    /**
     * 
     * Description: 通过开始时间和时间差，计算结束时间：end = start + interval ;
     * 
     * @param
     * @return Calendar
     * @throws
     * @Author adam Create Date: 2017年6月6日 下午5:58:31
     */
    public static Date getEndCalendarByStartAndInterval(Date start, long interval) {
        long ends = start.getTime() + interval;
        Date end = new Date();
        end.setTime(ends);
        return end;
    }

    public static void main(String[] args) throws ParseException {
        String str = "20160208";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(str);
        System.out.println(DateUtil.getDaysByDate(date));
    }

    public static String parseYYYYMMDD(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtil.getDateString(date, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
    }

    public static long differenceDays(String startDate, String endDate) {
        long days = 0;
        SimpleDateFormat format = new SimpleDateFormat(DateUtil.SHORT_DATE_FORMAT);
        format.setLenient(false);
        Date date1;
        Date date2;
        try {
            date1 = format.parse(startDate);
            date2 = format.parse(endDate);
            days = (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static Map<String, Long> getMonthAndDays(long days) {
        Map<String, Long> map = new HashMap<>();
        long months = Math.abs(days / 30);
        long day = days - (months * 30);
        map.put("months", months);
        map.put("day", day);
        return map;
    }
}
