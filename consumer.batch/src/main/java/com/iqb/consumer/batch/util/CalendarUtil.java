package com.iqb.consumer.batch.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jodd.util.StringUtil;

public class CalendarUtil {

    public static final long ONE_MIN = 60 * 1000;
    public static final long ONE_HOUR = 60 * ONE_MIN;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long SCHEDULE_INTERVAL = 15 * CalendarUtil.ONE_MIN;

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

    public static Date getDayStartTime(Calendar c) {
        Calendar c2 = Calendar.getInstance();
        c2.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 0, 0, 0);

        Date d = new Date();
        d.setTime(c2.getTimeInMillis());
        return d;
    }

    public static Date getDayEndTime(Calendar c) {
        Calendar c2 = Calendar.getInstance();
        c2.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 24, 0, 0);
        Date d = new Date();
        d.setTime(c2.getTimeInMillis());
        return d;
    }

    public static Date getStartOfXDaysBefore(Calendar c, int x) {
        Calendar c2 = Calendar.getInstance();
        c2.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE) - x, 0, 0, 0);
        Date d = new Date();
        d.setTime(c2.getTimeInMillis());
        return d;
    }

    private static final String NROMAL_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static SimpleDateFormat getSimpleDateFormat(String format) {
        if (StringUtil.isEmpty(format)) {
            return new SimpleDateFormat(NROMAL_FORMAT);
        } else {
            return new SimpleDateFormat(format);
        }
    }

}
