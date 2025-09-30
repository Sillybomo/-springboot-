package com.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.CacheNamespace;
import com.entity.po.Cart;
import com.entity.query.CartQuery;
import java.util.List;
import java.util.Map;
/**
 * 购物车表：存储用户待购买的门票信息 Mapper接口
 * 
 * 对应数据库表: t_cart
 * @author Bomo
 * @date 2025-09-19 14:19:41
 */
@Mapper
@CacheNamespace
public interface CartMapper extends BaseMapperEx<Cart, CartQuery> {

	/** 
	 * 根据Id查询
	 */
	Cart selectById(@Param("id") Long id);

	/** 
	 * 根据Id更新
	 */
	Long updateById( @Param("bean") Cart entity, @Param("id") Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(@Param("id") Long id);

	/** 
	 * 根据UserIdAndTicketId查询
	 */
	Cart selectByUserIdAndTicketId(@Param("userId") Long userId, @Param("ticketId") Long ticketId);

	/** 
	 * 根据UserIdAndTicketId更新
	 */
	Long updateByUserIdAndTicketId( @Param("bean") Cart entity, @Param("userId") Long userId, @Param("ticketId") Long ticketId);

	/** 
	 * 根据UserIdAndTicketId删除
	 */
	Long deleteByUserIdAndTicketId(@Param("userId") Long userId, @Param("ticketId") Long ticketId);

	/** 
	 * 多表联查获取购物车列表（包含景点和门票信息）
	 */
	List<Map<String, Object>> selectCartWithAttractionAndTicket(@Param("param") CartQuery param);

}