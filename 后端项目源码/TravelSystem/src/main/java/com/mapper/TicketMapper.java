package com.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.CacheNamespace;
import com.entity.po.Ticket;
import com.entity.query.TicketQuery;
/**
 *  Mapper接口
 * 
 * 对应数据库表: t_ticket
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Mapper
@CacheNamespace
public interface TicketMapper extends BaseMapperEx<Ticket, TicketQuery> {

	/** 
	 * 根据Id查询
	 */
	Ticket selectById(@Param("id") Long id);

	/** 
	 * 根据Id更新
	 */
	Long updateById( @Param("bean") Ticket entity, @Param("id") Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(@Param("id") Long id);

	/** 
	 * 根据attractionId删除
	 */
	Long deleteByAttractionId(Long attractionId);


}