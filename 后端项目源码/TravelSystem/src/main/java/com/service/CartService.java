package com.service;

import java.util.List;
import java.util.Map;
import com.entity.po.Cart;
import com.entity.query.CartQuery;
import com.entity.vo.PaginationResultVO;
/**
 * 购物车表：存储用户待购买的门票信息 业务接口
 * 
 * 对应数据库表: t_cart
 * @author Bomo
 * @date 2025-09-19 14:19:41
 */
public interface CartService {
	/** 
	 * 根据条件查询列表
	 */
	List<Cart> findListByParam(CartQuery param);
	/** 
	 * 根据条件查询数量
	 */
	Long findCountByParam(CartQuery param);
	
	/** 
	 * 多表联查获取购物车列表（包含景点和门票信息）
	 */
	List<Map<String, Object>> findListWithJoin(CartQuery param);
	/** 
	 * 分页查询
	 */
	PaginationResultVO<Cart> findListByPage(CartQuery param);
	/** 
	 * 新增
	 */
	Long add(Cart bean);
	/** 
	 * 批量新增
	 */
	Long batchAdd(List<Cart> list);
	/** 
	 * 批量新增/修改
	 */
	Long batchAddOrUpdate(List<Cart> list);
	/** 
	 * 根据Id查询
	 */
	Cart findById(Long id);

	/** 
	 * 根据Id修改
	 */
	Long updateById(Cart entity, Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(Long id);

	/** 
	 * 根据UserIdAndTicketId查询
	 */
	Cart findByUserIdAndTicketId(Long userId,Long ticketId);

	/** 
	 * 根据UserIdAndTicketId修改
	 */
	Long updateByUserIdAndTicketId(Cart entity, Long userId,Long ticketId);

	/** 
	 * 根据UserIdAndTicketId删除
	 */
	Long deleteByUserIdAndTicketId(Long userId,Long ticketId);

}