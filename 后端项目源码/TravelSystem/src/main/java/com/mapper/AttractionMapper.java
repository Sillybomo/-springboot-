package com.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.CacheNamespace;
import com.entity.po.Attraction;
import com.entity.query.AttractionQuery;
/**
 *  Mapper接口
 * 
 * 对应数据库表: t_attraction
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Mapper
@CacheNamespace
public interface AttractionMapper extends BaseMapperEx<Attraction, AttractionQuery> {

	/** 
	 * 根据Id查询
	 */
	Attraction selectById(@Param("id") Long id);

	/** 
	 * 根据Id更新
	 */
	Long updateById( @Param("bean") Attraction entity, @Param("id") Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(@Param("id") Long id);


}