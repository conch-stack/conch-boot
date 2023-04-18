package com.nabob.conch.tools.utils;

import com.nabob.conch.tools.time.FastDateFormat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class DateUtils {

    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    public static final String YMD = "yyyy-MM-dd";


    private static final ConcurrentMap<Pattern, String> regPatternMap = new ConcurrentHashMap<Pattern, String>();

    static {
        regPatternMap.put(Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$"), "yyyy-MM-dd");
        regPatternMap.put(Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$"), "yyyy-MM-dd HH:mm:ss");
        regPatternMap.put(Pattern.compile("^\\d{4}\\d{1,2}\\d{1,2}$"), "yyyyMMdd");
        regPatternMap.put(Pattern.compile("^\\d{4}\\d{1,2}$"), "yyyyMM");
        regPatternMap.put(Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2}$"), "yyyy/MM/dd");
        regPatternMap.put(Pattern.compile("^\\d{4}年\\d{1,2}月\\d{1,2}日$"), "yyyy年MM月dd日");
        regPatternMap.put(Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$"), "yyyy/MM/dd HH:mm:ss");
        regPatternMap.put(Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1}$"), "yyyy/MM/dd HH:mm:ss.S");
        regPatternMap.put(Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1}$"), "yyyy-MM-dd HH:mm:ss.S");
    }

    public static Date convert(Long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 方法描述：获取当前时间的
     */
    public static Date now() {
        return new Date();
    }

    public static Long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 方法描述：格式化日期
     */
    public static String formatDate(Date d, String fmt) {
        return FastDateFormat.getInstance(fmt).format(d);
    }

    public static String formatYMD(Date d) {
        return FastDateFormat.getInstance(YMD).format(d);
    }

    public static String formatYMD_HMS(Date d) {
        return FastDateFormat.getInstance(YMD_HMS).format(d);
    }

    public static String formatDate(Long date, String fmt) {
        return FastDateFormat.getInstance(fmt).format(date);
    }

    public static String format(Date date, String fmt) {
        return FastDateFormat.getInstance(fmt).format(date);
    }

    public static Date parse(String date, String fmt) {
        try {
            return FastDateFormat.getInstance(fmt).parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parse(String date) {
        try {
            for (Map.Entry<Pattern, String> entry : regPatternMap.entrySet()) {
                boolean isMatch = entry.getKey().matcher(date).matches();
                if (isMatch) {
                    return FastDateFormat.getInstance(entry.getValue()).parse(date);
                }
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("can't support this pattern , date is " + date);
        }
        throw new IllegalArgumentException("can't support this pattern , date is " + date);
    }

    public static Date parseYMD(String date) {
        try {
            return FastDateFormat.getInstance(YMD).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseYMD_HMS(String date) {
        try {
            return FastDateFormat.getInstance(YMD_HMS).parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("the date pattern is error!");
        }
    }

    /**
     * 是否是同一天
     */
    public static boolean isSameDay(Date date, Date date2) {
        if (date == null || date2 == null) {
            return false;
        }
        FastDateFormat df = FastDateFormat.getInstance(YMD);
        return df.format(date).equals(df.format(date2));
    }

    public static Date addMonth(Date date, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, interval);
        return calendar.getTime();
    }

    public static Date addDay(Date date, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, interval);
        return calendar.getTime();
    }

    public static Date addHour(Date date, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, interval);
        return calendar.getTime();
    }

    public static Date addMinute(Date date, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, interval);
        return calendar.getTime();
    }

    public static Date dayStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date dayEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }


    public static Date getCurrentDay() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getCurrentDay(int index) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, index);

        return cal.getTime();
    }

    public static Date getCurrentDay(long timestamp) {
        return getCurrentDay(timestamp, 0);
    }

    public static Date getCurrentDay(long timestamp, int index) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, index);
        return cal.getTime();
    }

    public static Date getCurrentHour() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getCurrentHour(int index) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.HOUR_OF_DAY, index);

        return cal.getTime();
    }

    public static Date getCurrentMinute() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date getCurrentMonth() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    // get lastest sarterday
    public static Date getCurrentWeek() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == 7) {
            return cal.getTime();
        } else {
            cal.add(Calendar.DATE, -dayOfWeek);
        }
        return cal.getTime();
    }

}
