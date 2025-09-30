package com.enums;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * 日期时间单位枚举（兼容Java 8+）
 * 包含基础单位、复合单位及特殊时间单位
 */
public enum DateTimeUnit {
    // 基础时间单位
    MILLISECOND("毫秒", ChronoUnit.MILLIS),
    SECOND("秒", ChronoUnit.SECONDS),
    MINUTE("分钟", ChronoUnit.MINUTES),
    HOUR("小时", ChronoUnit.HOURS),

    // 日期单位
    DAY("天", ChronoUnit.DAYS),
    WEEK("周", ChronoUnit.WEEKS),
    MONTH("月", ChronoUnit.MONTHS),
    QUARTER("季度", null) {
        @Override
        public TemporalUnit getTemporalUnit() {
            return ChronoUnit.MONTHS;
        }

        @Override
        public long getDuration() {
            return 3;
        }
    },
    YEAR("年", ChronoUnit.YEARS),
    DECADE("十年", ChronoUnit.DECADES),
    CENTURY("世纪", ChronoUnit.CENTURIES),

    // 特殊单位
    WORKDAY("工作日", null) {
        @Override
        public boolean isBusinessUnit() {
            return true;
        }
    },
    FISCAL_MONTH("财月", null),
    ERA("纪元", ChronoUnit.ERAS);

    private final String description;
    private final ChronoUnit chronoUnit;

    DateTimeUnit(String description, ChronoUnit chronoUnit) {
        this.description = description;
        this.chronoUnit = chronoUnit;
    }

    // 基础方法
    public String getDescription() {
        return description;
    }

    public TemporalUnit getTemporalUnit() {
        return chronoUnit;
    }

    public long getDuration() {
        return chronoUnit != null ? chronoUnit.getDuration().toMillis() : 0;
    }

    // 扩展方法
    public boolean isBusinessUnit() {
        return false;
    }

    // 单位转换示例
    public static long convertBetween(DateTimeUnit from, DateTimeUnit to, long value) {
        return from.chronoUnit.between(
            java.time.Instant.EPOCH,
            java.time.Instant.EPOCH.plus(value, to.chronoUnit)
        );
    }

    // 使用示例
    public static void main(String[] args) {
        // 遍历所有单位
        System.out.println("=== 支持的时间单位 ===");
        for (DateTimeUnit unit : values()) {
            System.out.printf("%-10s %-20s %s%n",
                unit.name(),
                unit.getDescription(),
                unit.getTemporalUnit() != null ? "标准单位" : "复合单位");
        }

        // 日期计算示例
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        System.out.println("\n当前时间: " + now);
        System.out.println("加2个季度: " +
            now.plus(2 * QUARTER.getDuration(), QUARTER.getTemporalUnit()));
    }
}
