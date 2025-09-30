package com.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 日期时间工具类（线程安全）
 * 功能覆盖：格式化、解析、日期计算、转换、判断等
 */
public class DateUtils {

    // 常用日期格式常量
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD = "yyyy-MM-dd";
    public static final String HMS = "HH:mm:ss";
    public static final String COMPACT_YMD = "yyyyMMdd";
    public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    // 缓存常用格式的DateTimeFormatter（线程安全）
    private static final Map<String, DateTimeFormatter> FORMATTER_CACHE = new HashMap<>();

    static {
        FORMATTER_CACHE.put(YMD_HMS, DateTimeFormatter.ofPattern(YMD_HMS));
        FORMATTER_CACHE.put(YMD, DateTimeFormatter.ofPattern(YMD));
        FORMATTER_CACHE.put(HMS, DateTimeFormatter.ofPattern(HMS));
    }

    /**
     * 格式化日期为字符串（支持LocalDateTime和Date）
     */
    public static String format(Object dateObj, String pattern) {
        if (dateObj instanceof LocalDateTime) {
            return ((LocalDateTime) dateObj).format(getFormatter(pattern));
        } else if (dateObj instanceof Date) {
            return getFormatter(pattern).format(toLocalDateTime((Date) dateObj));
        }
        throw new IllegalArgumentException("Unsupported date type");
    }

    /**
     * 解析字符串为LocalDateTime（支持多格式自动匹配）
     */
    public static LocalDateTime parse(String dateStr, String... patterns) {
        for (String pattern : patterns) {
            try {
                return LocalDateTime.parse(dateStr, getFormatter(pattern));
            } catch (Exception e) {
                // 尝试下一个格式
            }
        }
        throw new IllegalArgumentException("无法解析日期: " + dateStr);
    }

    /**
     * 日期增减操作（天、月、年、小时等）
     */
    public static LocalDateTime add(LocalDateTime date, int amount, ChronoUnit unit) {
        return date.plus(amount, unit);
    }

    /**
     * 计算两个日期之间的天数差
     */
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 判断是否为同一天
     */
    public static boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        return date1.toLocalDate().equals(date2.toLocalDate());
    }

    /**
     * 获取当天的开始时间（00:00:00）
     */
    public static LocalDateTime startOfDay(LocalDateTime date) {
        return date.with(LocalTime.MIN);
    }

    /**
     * 获取当月的最后一天
     */
    public static LocalDateTime lastDayOfMonth(LocalDateTime date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * Date与LocalDateTime互转
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 获取缓存的DateTimeFormatter
    private static DateTimeFormatter getFormatter(String pattern) {
        return FORMATTER_CACHE.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
    }
}
