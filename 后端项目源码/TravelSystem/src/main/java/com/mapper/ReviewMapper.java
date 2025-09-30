package com.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.CacheNamespace;
import com.entity.po.Review;
import com.entity.query.ReviewQuery;
/**
 *  Mapper接口
 * 
 * 对应数据库表: t_review
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Mapper
@CacheNamespace
public interface ReviewMapper extends BaseMapperEx<Review, ReviewQuery> {

	/** 
	 * 根据Id查询
	 */
	Review selectById(@Param("id") Long id);

	/** 
	 * 根据Id更新
	 */
	Long updateById( @Param("bean") Review entity, @Param("id") Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(@Param("id") Long id);

	/** 
	 * 根据attractionId删除
	 */
	Long deleteByAttractionId(Long attractionId);

	/** 
	 * 根据userId删除
	 */
	Long deleteByUserId(Long userId);

	/** 
	 * 根据orderId删除
	 */
	Long deleteByOrderId(Long orderId);


}