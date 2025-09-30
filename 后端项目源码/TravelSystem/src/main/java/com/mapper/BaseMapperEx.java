package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.MapKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseMapperEx<T,P> extends BaseMapper<T>  {
//public interface BaseMapperEx<T,P>  {
    // 插入（返回影响行数）
    Long insertEntity(@Param("bean")T entity);

    // 插入或更新（根据主键判断）兼容mybatis-plus返回值就用boolean
    Long insertOrUpdate(@Param("bean") T entity);
    //查询数据总条数
    Long selectCountByCollection();
    // 批量插入
    Long insertBatch(@Param("list") List<T> entityList);

    // 批量插入或更新（ON DUPLICATE KEY UPDATE）
    Long insertOrUpdateBatch(@Param("list") List<T> entityList);

    // 根据参数动态查询集合
    List<T> selectByParams(@Param("query") P p);

    // 根据集合条件查询数量
    Long selectCountByCollection(@Param("query") P p);

    // 新增根据条件删除方法
    Long deleteByParams(@Param("query") P query);
    
    
     /*
        public List<Map<String, Object>> getUserWithOrders() {
            // 多表关联查询
            return userMapper.selectJoin(
                "user u", // 主表
                "u.id, u.username, u.create_time, o.id as order_id, o.order_no, o.amount", // 需要查询的字段
                "LEFT JOIN orders o ON u.id = o.user_id", // 关联条件
                "u.status = 1 AND o.status != 'CANCELED'" // WHERE条件
            );
        }
            
        public List<Map<String, Object>> getActiveUserProductInfo() {
            // 三表关联查询
            return userMapper.selectJoin(
                "user u",
                "u.id, u.username, o.order_no, p.product_name, p.price",
                "LEFT JOIN orders o ON u.id = o.user_id LEFT JOIN product p ON o.product_id = p.id",
                "u.is_active = 1 AND o.create_time > DATE_SUB(NOW(), INTERVAL 30 DAY)"
            );
        }
    */  
    // 多表关联查询    
    @MapKey("id")
    List<Map<String, Object>> selectJoin(@Param("joinTables") String joinTables, 
                                         @Param("selectColumns") String selectColumns, 
                                         @Param("joinConditions") String joinConditions,
                                         @Param("whereConditions") String whereConditions);



    /*
    // 查询分页数据
    List<Map<String, Object>> records = userMapper.selectJoinPage(
        "user u",
        "u.id, u.username, o.order_no, o.amount, o.create_time",
        "LEFT JOIN orders o ON u.id = o.user_id",
        "u.status = 1",
        "o.create_time DESC", // 按订单创建时间倒序排列
        offset,
        size
    );

    // 查询总数
    Long total = userMapper.selectJoinCount(
        "user u",
        "LEFT JOIN orders o ON u.id = o.user_id",
        "u.status = 1"
    );
    */
    
    // 多表关联查询结果总数
    Long selectJoinCount(@Param("joinTables") String joinTables, 
                         @Param("joinConditions") String joinConditions,
                         @Param("whereConditions") String whereConditions);


    /*
    // 构建查询条件
    StringBuilder whereCondition = new StringBuilder();
    whereCondition.append("p.category_id = '").append(categoryId).append("' ");
    whereCondition.append("AND o.create_time BETWEEN '").append(formatDate(startDate)).append("' ");
    whereCondition.append("AND '").append(formatDate(endDate)).append("' ");
    whereCondition.append("AND o.status = 'COMPLETED'");

    // 构建JOIN条件
    String joinCondition = "INNER JOIN order_item oi ON p.id = oi.product_id " +
                        "INNER JOIN orders o ON oi.order_id = o.id " +
                        "INNER JOIN user u ON o.user_id = u.id";

    // 查询汇总数据
    List<Map<String, Object>> summary = productMapper.selectJoin(
        "product p",
        "p.id, p.product_name, p.price, SUM(oi.quantity) as total_sold, " +
        "SUM(oi.quantity * p.price) as total_sales",
        joinCondition,
        whereCondition.toString()
    );

    // 查询分页数据
    List<Map<String, Object>> details = productMapper.selectJoinPage(
        "product p",
        "p.id, p.product_name, u.username as buyer, o.order_no, " +
        "oi.quantity, p.price, (oi.quantity * p.price) as subtotal, o.create_time",
        joinCondition,
        whereCondition.toString(),
        "o.create_time DESC",
        (page - 1) * size,
        size
    );

    // 获取总记录数
    Long total = productMapper.selectJoinCount(
        "product p",
        joinCondition,
        whereCondition.toString()
    );
    */ 
    
    // 多表关联分页查询
    @MapKey("id")
    List<Map<String, Object>> selectJoinPage(@Param("joinTables") String joinTables, 
                                            @Param("selectColumns") String selectColumns, 
                                            @Param("joinConditions") String joinConditions,
                                            @Param("whereConditions") String whereConditions,
                                            @Param("orderBy") String orderBy,
                                            @Param("offset") Integer offset,
                                            @Param("limit") Integer limit);
}
