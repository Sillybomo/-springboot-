package com.entity.query;

import lombok.Data;


/**
 * BaseQuery 类是一个通用的查询基类，用于封装分页查询的相关参数。
 * 该类使用了 Lombok 的 @Data 注解，自动生成 getter、setter、toString 等方法。
 *
 * @param <T> 查询结果的数据类型
 */
@Data
public class BaseQuery<T> {
    /**
     * 分页信息对象，包含查询结果的分页数据。
     */
    private SimplePage<T> simplePage;

    /**
     * 当前页码，表示用户请求的页码。
     */
    private Integer currentPage;

    /**
     * 每页显示的记录数，表示用户请求的每页数据量。
     */
    private Integer pageSize;

    /**
     * 排序字段，表示查询结果的排序依据。
     */
    private String orderBy;
}
