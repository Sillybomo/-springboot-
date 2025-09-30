package com.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.CacheNamespace;
import com.entity.po.Order;
import com.entity.query.OrderQuery;
import java.util.List;
import java.util.Map;
/**
 *  Mapper接口
 * 
 * 对应数据库表: t_order
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Mapper
@CacheNamespace
public interface OrderMapper extends BaseMapperEx<Order, OrderQuery> {

	/** 
	 * 根据Id查询
	 */
	Order selectById(@Param("id") Long id);

	/** 
	 * 根据Id更新
	 */
	Long updateById( @Param("bean") Order entity, @Param("id") Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(@Param("id") Long id);

	/** 
	 * 根据userId删除
	 */
	Long deleteByUserId(Long userId);

	/** 
	 * 根据ticketId删除
	 */
	Long deleteByTicketId(Long ticketId);

	/** 
	 * 多表联查获取订单列表（包含景点和门票信息）
	 */
	List<Map<String, Object>> selectOrderWithAttractionAndTicket(@Param("param") OrderQuery param);

}