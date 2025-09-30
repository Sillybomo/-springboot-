package com.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.CacheNamespace;
import com.entity.po.Favorite;
import com.entity.query.FavoriteQuery;
/**
 *  Mapper接口
 * 
 * 对应数据库表: t_favorite
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Mapper
@CacheNamespace
public interface FavoriteMapper extends BaseMapperEx<Favorite, FavoriteQuery> {

	/** 
	 * 根据Id查询
	 */
	Favorite selectById(@Param("id") Long id);

	/** 
	 * 根据Id更新
	 */
	Long updateById( @Param("bean") Favorite entity, @Param("id") Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(@Param("id") Long id);

	/** 
	 * 根据UserIdAndAttractionId查询
	 */
	Favorite selectByUserIdAndAttractionId(@Param("userId") Long userId, @Param("attractionId") Long attractionId);

	/** 
	 * 根据UserIdAndAttractionId更新
	 */
	Long updateByUserIdAndAttractionId( @Param("bean") Favorite entity, @Param("userId") Long userId, @Param("attractionId") Long attractionId);

	/** 
	 * 根据UserIdAndAttractionId删除
	 */
	Long deleteByUserIdAndAttractionId(@Param("userId") Long userId, @Param("attractionId") Long attractionId);


}