package com.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum PageSize {
    SIZE10(10),       // 默认分页大小
    SIZE20(20),         // 中等数据量
    SIZE50(50),        // 大范围数据
    SIZE100(100),        // 大数据量（如报表场景）
    CUSTOM(-1);        // 自定义分页尺寸（需单独处理）

    private final int value;

    // 预定义的分页尺寸列表
    private static final List<Integer> PREDEFINED_OPTIONS = Arrays.stream(values())
            .filter(e -> e != CUSTOM)
            .map(PageSize::getValue)
            .collect(Collectors.toList());

    PageSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // 校验并返回合法分页大小（若输入非法则返回默认值）
    public static int getValidated(int inputSize) {
        if (inputSize <= 0) return SIZE10.value;
        return Arrays.stream(PageSize.values())
                .filter(e -> e != PageSize.CUSTOM && e.value >= inputSize)
                .findFirst()
                .orElse(SIZE100)
                .getValue();
    }

    // 获取所有预定义的分页尺寸（用于生成下拉菜单或文档）
    public static List<Integer> getPredefinedOptions() {
        return PREDEFINED_OPTIONS;
    }
}
