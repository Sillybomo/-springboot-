package com.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Hendrix Xie
 * @CreateTime: 2025-03-23 13:58:33
 * @Description: 分页查询结果封装VO
 * @Version: 1.0
 */
@Data
public class PaginationResultVO<T> implements Serializable {
    // 当前页码（从1开始计数）
    private Integer current;
    // 每页记录数
    private Integer pageSize;
    // 总记录数
    private Long total;
    // 总页数（自动计算）
    private Integer totalPages;
    // 当前页数据列表
    private List<T> list;

    public PaginationResultVO() {
        this.list = Collections.emptyList();
    }

    public PaginationResultVO(Integer current, Integer pageSize, Long total, List<T> list) {
        this.current = current;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list != null ? list : Collections.emptyList();
        this.totalPages = calculateTotalPages();
    }

    // 自动计算总页数
    private Integer calculateTotalPages() {
        if (pageSize == null || pageSize <= 0) return 0;
        return (int) Math.ceil((double) total / pageSize);
    }

    
}
