package com.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.CacheNamespace;
import com.entity.po.User;
import com.entity.query.UserQuery;
import java.util.List;
/**
 *  Mapper接口
 * 
 * 对应数据库表: t_user
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Mapper
@CacheNamespace
public interface UserMapper extends BaseMapperEx<User, UserQuery> {

	/** 
	 * 根据Id查询
	 */
	User selectById(@Param("id") Long id);

	/** 
	 * 根据Id更新
	 */
	Long updateById( @Param("bean") User entity, @Param("id") Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(@Param("id") Long id);


}