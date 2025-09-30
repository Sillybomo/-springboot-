package com.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.CacheNamespace;
import com.entity.po.Payment;
import com.entity.query.PaymentQuery;
/**
 *  Mapper接口
 * 
 * 对应数据库表: t_payment
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Mapper
@CacheNamespace
public interface PaymentMapper extends BaseMapperEx<Payment, PaymentQuery> {

	/** 
	 * 根据Id查询
	 */
	Payment selectById(@Param("id") Long id);

	/** 
	 * 根据Id更新
	 */
	Long updateById( @Param("bean") Payment entity, @Param("id") Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(@Param("id") Long id);

	/** 
	 * 根据OrderId查询
	 */
	Payment selectByOrderId(@Param("orderId") Long orderId);

	/** 
	 * 根据OrderId更新
	 */
	Long updateByOrderId( @Param("bean") Payment entity, @Param("orderId") Long orderId);

	/** 
	 * 根据OrderId删除
	 */
	Long deleteByOrderId(@Param("orderId") Long orderId);


}