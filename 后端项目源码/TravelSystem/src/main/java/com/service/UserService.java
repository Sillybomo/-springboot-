package com.service;

import java.util.List;
import com.entity.po.User;
import com.entity.query.UserQuery;
import com.entity.vo.PaginationResultVO;
/**
 *  业务接口
 * 
 * 对应数据库表: t_user
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
public interface UserService {


    public User login(User user);
	/** 
	 * 根据条件查询列表
	 */
	List<User> findListByParam(UserQuery param);
	/** 
	 * 根据条件查询数量
	 */
	Long findCountByParam(UserQuery param);
	/** 
	 * 分页查询
	 */
	PaginationResultVO<User> findListByPage(UserQuery param);
	/** 
	 * 新增
	 */
	Long add(User bean);
	/** 
	 * 批量新增
	 */
	Long batchAdd(List<User> list);
	/** 
	 * 批量新增/修改
	 */
	Long batchAddOrUpdate(List<User> list);
	/** 
	 * 根据Id查询
	 */
	User findById(Long id);

	/** 
	 * 根据Id修改
	 */
	Long updateById(User entity, Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(Long id);

	/**
	 * 根据用户名查找用户
	 */
	User findByUsername(String username);

	/**
	 * 根据邮箱查找用户
	 */
	User findByEmail(String email);

	/**
	 * 根据手机号查找用户
	 */
	User findByPhone(String phone);

}