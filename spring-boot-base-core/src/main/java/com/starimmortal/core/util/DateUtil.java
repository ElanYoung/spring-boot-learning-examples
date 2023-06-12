package com.starimmortal.core.util;

import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具函数
 *
 * @author pedro@TaleLin
 */
public class DateUtil {
    /**
     * 例如：2018-12-28
     */
    public static final String DATE = "yyyy-MM-dd";

    /**
     * 例如：2018-12-28 10:00:00
     */
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 例如：10:00:00
     */
    public static final String TIME = "HH:mm:ss";

    /**
     * 例如：10_00_00
     */
    public static final String TIME_WITH_UNDERLINE = "HH_mm_ss";

    /**
     * 例如：10:00
     */
    public static final String TIME_WITHOUT_SECOND = "HH:mm";

    /**
     * 例如：2018-12-28 10:00
     */
    public static final String DATE_TIME_WITHOUT_SECONDS = "yyyy-MM-dd HH:mm";

    /**
     * 例如：2018-06-08T10:34:56+08:00
     */
    public static final String RFC3339 = "yyyy-MM-dd'T'HH:mm:ssXXX";

    /**
     * 生肖
     */
    public static final String[] ZODIAC_ARRAY = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};

    /**
     * 星座
     */
    public static final String[] CONSTELLATION_ARRAY = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};

    /**
     * 月份-天
     */
    public static final int[] CONSTELLATION_EDGE_DAY = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};

    /**
     * 获取年
     *
     * @return 年
     */
    public static int getYear() {
        LocalTime localTime = LocalTime.now();
        return localTime.get(ChronoField.YEAR);
    }

    /**
     * 获取月份
     *
     * @return 月份
     */
    public static int getMonth() {
        LocalTime localTime = LocalTime.now();
        return localTime.get(ChronoField.MONTH_OF_YEAR);
    }

    /**
     * 获取某月的第几天
     *
     * @return 几号
     */
    public static int getMonthOfDay() {
        LocalTime localTime = LocalTime.now();
        return localTime.get(ChronoField.DAY_OF_MONTH);
    }

    /**
     * 格式化日期为字符串
     *
     * @param date    date
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return Date
     */
    public static Date parse(String dateStr, String pattern) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 为Date增加秒数,减传负数
     *
     * @param date        日期
     * @param plusSeconds 要增加的秒数
     * @return 新的日期
     */
    public static Date addSeconds(Date date, Long plusSeconds) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime newDateTime = dateTime.plusSeconds(plusSeconds);
        return Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 增加分钟
     *
     * @param date        日期
     * @param plusMinutes 分钟数
     * @return 新的日期
     */
    public static Date addMinutes(Date date, Long plusMinutes) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime newDateTime = dateTime.plusMinutes(plusMinutes);
        return Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 增加小时
     *
     * @param date date
     * @param hour 小时数
     * @return new date
     */
    public static Date addHours(Date date, Long hour) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime localDateTime = dateTime.plusHours(hour);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 增加秒
     *
     * @param duration 延时时间，单位s
     * @return Instant
     */
    public static Instant addSeconds(long duration) {
        LocalDateTime now = LocalDateTime.now();
        return now.plusSeconds(duration).toInstant(ZoneOffset.ofHours(8));
    }

    /**
     * @return 返回当天的起始时间
     */
    public static Date getStartTime() {
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return localDateTimeToDate(now);
    }

    /**
     * @return 返回当天的结束时间
     */
    public static Date getEndTime() {
        LocalDateTime now = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999);
        return localDateTimeToDate(now);
    }

    /**
     * 减月份
     *
     * @param monthsToSubtract 月份
     * @return Date
     */
    public static Date minusMonths(long monthsToSubtract) {
        LocalDate localDate = LocalDate.now().minusMonths(monthsToSubtract);
        return localDateToDate(localDate);
    }

    /**
     * 减秒
     *
     * @param seconds 秒
     * @return 相差秒数
     */
    public static long minusSeconds(long seconds) {
        Instant timestamp = Instant.ofEpochSecond(seconds);
        Instant now = Instant.now();
        Duration duration = Duration.between(now, timestamp);
        return duration.getSeconds();
    }

    /**
     * LocalDate类型转为Date
     *
     * @param localDate LocalDate object
     * @return Date object
     */
    public static Date localDateToDate(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * LocalDateTime类型转为Date
     *
     * @param localDateTime LocalDateTime object
     * @return Date object
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 查询当前年的第一天
     *
     * @param pattern 格式，默认格式yyyyMMdd
     * @return 20190101
     */
    public static String getFirstDayOfCurrentYear(String pattern) {
        LocalDateTime localDateTime = LocalDateTime.now().withMonth(1).withDayOfMonth(1);
        if (!StringUtils.hasText(pattern)) {
            pattern = "yyyyMMdd";
        }

        return format(localDateTimeToDate(localDateTime), pattern);
    }

    /**
     * 查询前一年最后一个月第一天
     *
     * @param pattern 格式，默认格式yyyyMMdd
     * @return 20190101
     */
    public static String getLastMonthFirstDayOfPreviousYear(String pattern) {
        LocalDateTime localDateTime = LocalDateTime.now().minusYears(1L).withMonth(12).withDayOfMonth(1);
        if (!StringUtils.hasText(pattern)) {
            pattern = "yyyyMMdd";
        }
        return format(localDateTimeToDate(localDateTime), pattern);
    }

    /**
     * 查询前一年最后一个月第一天
     *
     * @param pattern 格式，默认格式yyyyMMdd
     * @return 20190101
     */
    public static String getLastMonthLastDayOfPreviousYear(String pattern) {
        LocalDateTime localDateTime = LocalDateTime.now().minusYears(1L).with(TemporalAdjusters.lastDayOfYear());
        if (!StringUtils.hasText(pattern)) {
            pattern = "yyyyMMdd";
        }
        return format(localDateTimeToDate(localDateTime), pattern);
    }

    /**
     * 获取当前日期
     *
     * @param pattern 格式，默认格式yyyyMMdd
     * @return 20190101
     */
    public static String getCurrentDay(String pattern) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.of("+8"));
        if (!StringUtils.hasText(pattern)) {
            pattern = "yyyyMMdd";
        }
        return format(localDateTimeToDate(localDateTime), pattern);
    }

    /**
     * 获取本月所对应的月初时间
     *
     * @return 月初时间
     */
    public static Date getFirstDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDate = now.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        return localDateTimeToDate(firstDate);
    }

    /**
     * 获取本月所对应的月末时间
     *
     * @return 月末时间
     */
    public static Date getLastDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusMonths(1L).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS).plus(-1L, ChronoUnit.MILLIS);
        return localDateTimeToDate(endDate);
    }

    /**
     * 根据日期获取生肖
     *
     * @param date 日期
     * @return 生肖
     */
    public static String getZodiac(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return ZODIAC_ARRAY[calendar.get(Calendar.YEAR) % 12];
    }

    /**
     * 根据日期获取星座
     *
     * @param date 日期
     * @return 星座
     */
    public static String getConstellation(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < CONSTELLATION_EDGE_DAY[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return CONSTELLATION_ARRAY[month];
        }
        return CONSTELLATION_ARRAY[11];
    }

    /**
     * 判断当前时间是否位于开始时间与结束时间之间
     *
     * @param date  当前时间
     * @param start 开始时间
     * @param end   结束时间
     * @return true || false
     */
    public static Boolean isInTimeLine(Date date, Date start, Date end) {
        long time = date.getTime();
        long startTime = start.getTime();
        long endTime = end.getTime();
        return time > startTime && time < endTime;
    }

    /**
     * 判断某个时间是否超过当前时间
     *
     * @param date   开始时间
     * @param period 秒数
     * @return true || false
     */
    public static Boolean isOutOfDate(Date date, Long period) {
        long time = addSeconds(date, period).getTime();
        long now = Calendar.getInstance().getTimeInMillis();
        return now >= time;
    }

    /**
     * 判断过期时间是否超过当前时间
     *
     * @param expiredTime 过期时间
     * @return true || false
     */
    public static Boolean isOutOfDate(Date expiredTime) {
        long time = expiredTime.getTime();
        long now = Calendar.getInstance().getTimeInMillis();
        return now >= time;
    }
}
