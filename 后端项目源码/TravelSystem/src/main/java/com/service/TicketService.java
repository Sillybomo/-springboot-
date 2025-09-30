package com.service;

import java.util.List;
import com.entity.po.Ticket;
import com.entity.query.TicketQuery;
import com.entity.vo.PaginationResultVO;
/**
 *  业务接口
 * 
 * 对应数据库表: t_ticket
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
public interface TicketService {
	/** 
	 * 根据条件查询列表
	 */
	List<Ticket> findListByParam(TicketQuery param);
	/** 
	 * 根据条件查询数量
	 */
	Long findCountByParam(TicketQuery param);
	/** 
	 * 分页查询
	 */
	PaginationResultVO<Ticket> findListByPage(TicketQuery param);
	/** 
	 * 新增
	 */
	Long add(Ticket bean);
	/** 
	 * 批量新增
	 */
	Long batchAdd(List<Ticket> list);
	/** 
	 * 批量新增/修改
	 */
	Long batchAddOrUpdate(List<Ticket> list);
	/** 
	 * 根据Id查询
	 */
	Ticket findById(Long id);

	/** 
	 * 根据Id修改
	 */
	Long updateById(Ticket entity, Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(Long id);

	/** 
	 * 根据attractionId删除
	 */
	Long deleteByAttractionId(Long attractionId);
}