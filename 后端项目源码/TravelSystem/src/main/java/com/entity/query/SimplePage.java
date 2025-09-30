package com.entity.query;

import com.enums.PageSize;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单分页类，用于封装分页查询的结果。
 *
 * @param <T> 分页数据的类型
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimplePage<T> {
    /**
     * 该类用于管理分页数据，包含当前页码、每页记录数、总记录数、总页数、偏移量以及数据列表等信息。
     *
     * @param <T> 数据列表中存储的数据类型
     */
    private Integer currentPage; // 当前页码，表示当前显示的页数
    private Integer pageSize;    // 每页记录数，表示每页显示的数据条数
    private Long totalRecords;   // 总记录数，表示数据的总条数
    private Integer totalPages;  // 总页数，表示根据总记录数和每页记录数计算出的总页数
    private Integer offset;      // 偏移量，表示当前页数据在总数据中的起始位置
    private List<T> dataList;    // 数据列表，存储当前页的数据

    /**
     * 安全构造方法，用于初始化分页对象。
     *
     * @param currentPage 当前页码，如果为 null 或小于 1，则默认为 1
     * @param pageSize 每页大小，如果为 null 或小于 1，则默认为 10
     * @param totalRecords 总记录数，如果为 null 或小于 0，则默认为 0
     */
    public SimplePage(Integer currentPage, Integer pageSize, Long totalRecords) {
        this.currentPage = (currentPage == null || currentPage < 1) ? 1 : currentPage;
        this.pageSize = (pageSize == null || pageSize < 1) ? PageSize.SIZE10.getValue() : pageSize;
        this.totalRecords = (totalRecords == null || totalRecords < 0) ? 0 : totalRecords;
        computeInternal();
    }

    /**
     * MyBatis Plus 互操作构造方法，用于从 MyBatis Plus 的 Page 对象初始化分页对象。
     *
     * @param page MyBatis Plus 的 Page 对象
     */
    public SimplePage(Page<T> page) {
        this((int) page.getCurrent(), (int) page.getSize(), page.getTotal());
        this.dataList = page.getRecords();
    }

    /**
     * 内部计算逻辑，用于计算总页数和偏移量。
     */
    private void computeInternal() {
        this.totalPages = (pageSize == 0) ? 0 : (int) Math.ceil((double) totalRecords / pageSize);
        this.offset = (currentPage - 1) * pageSize;
        if (this.offset < 0) this.offset = 0;
    }

    /**
     * 检查是否有上一页。
     *
     * @return 如果当前页码大于 1，则返回 true，表示有上一页；否则返回 false
     */
    public boolean hasPrevious() {
        return currentPage > 1 && currentPage > 0;
    }

    /**
     * 检查是否还有下一页。
     *
     * @return 如果当前页码小于总页码，则返回 true，表示还有下一页；否则返回 false
     * @throws IllegalArgumentException 如果总页码为负数
     */
    public synchronized boolean hasNext() {
        if (totalPages < 0) {
            throw new IllegalArgumentException("totalPages must be non-negative");
        }
        return currentPage < totalPages;
    }

    /**
     * 获取分页显示范围的列表。
     *
     * @param maxDisplayPages 最大显示的页码数量，必须为正整数
     * @return 包含分页范围的整数列表
     * @throws IllegalArgumentException 如果 maxDisplayPages 小于等于零
     */
    public List<Integer> getPageRange(int maxDisplayPages) {
        if (maxDisplayPages <= 0) {
            throw new IllegalArgumentException("参数 maxDisplayPages 必须为正整数");
        }

        List<Integer> pageRange = new ArrayList<>();
        for (int i = 1; i <= maxDisplayPages; i++) {
            pageRange.add(i);
        }

        return pageRange;
    }
}
