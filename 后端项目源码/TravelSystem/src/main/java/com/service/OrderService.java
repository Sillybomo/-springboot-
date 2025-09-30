package com.service;

import java.util.List;
import java.util.Map;
import com.entity.po.Order;
import com.entity.query.OrderQuery;
import com.entity.vo.PaginationResultVO;
/**
 *  业务接口
 * 
 * 对应数据库表: t_order
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
public interface OrderService {
	/** 
	 * 根据条件查询列表
	 */
	List<Order> findListByParam(OrderQuery param);
	/** 
	 * 根据条件查询数量
	 */
	Long findCountByParam(OrderQuery param);
	/** 
	 * 分页查询
	 */
	PaginationResultVO<Order> findListByPage(OrderQuery param);
	/** 
	 * 新增
	 */
	Long add(Order bean);
	/** 
	 * 批量新增
	 */
	Long batchAdd(List<Order> list);
	/** 
	 * 批量新增/修改
	 */
	Long batchAddOrUpdate(List<Order> list);
	/** 
	 * 根据Id查询
	 */
	Order findById(Long id);

	/** 
	 * 根据Id修改
	 */
	Long updateById(Order entity, Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(Long id);

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
	List<Map<String, Object>> findListWithJoin(OrderQuery param);
}