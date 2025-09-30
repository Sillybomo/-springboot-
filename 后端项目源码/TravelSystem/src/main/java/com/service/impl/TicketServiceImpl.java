package com.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.entity.po.Ticket;
import com.entity.query.TicketQuery;
import com.entity.vo.PaginationResultVO;
import com.service.TicketService;
import com.mapper.TicketMapper;
import com.entity.query.SimplePage;
/**
 *  业务接口
 * 
 * 对应数据库表: t_ticket
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Service
public class TicketServiceImpl implements TicketService {
	@Autowired
	private TicketMapper ticketMapper;
	/** 
	 * 根据条件查询列表
	 */
	@Override
	public List<Ticket> findListByParam(TicketQuery param){
		return ticketMapper.selectByParams(param);
	}
	/** 
	 * 根据条件查询数量
	 */
	@Override
	public Long findCountByParam(TicketQuery param){
		return ticketMapper.selectCountByCollection(param);
	}
	/** 
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<Ticket> findListByPage(TicketQuery param){
		// 参数校验
		if (param == null || param.getCurrentPage() <= 0 || param.getPageSize() <= 0) {
			throw new IllegalArgumentException("分页参数无效：currentPage 和 pageSize 必须大于 0");
		}
		PaginationResultVO<Ticket> page = new PaginationResultVO<>();
		try {
			// 查询总记录数
			Long total = ticketMapper.selectCountByCollection(param);
			if (total == null) {
				total = 0L;
			}
			// 创建分页对象
			if(param.getSimplePage() == null) {
				SimplePage<Ticket> sPage = new SimplePage<>(param.getCurrentPage(), param.getPageSize(), total);
				param.setSimplePage(sPage);
			}
			// 设置分页结果
			page.setTotal(total);
			page.setCurrent(param.getCurrentPage());
			page.setPageSize(param.getPageSize());
			page.setTotalPages(param.getSimplePage().getTotalPages());
			page.setList(ticketMapper.selectByParams(param));
		} catch (Exception e) {
			System.out.println("分页查询失败");
			return new PaginationResultVO<>();
		}
		return page;
	}

	/** 
	 * 新增
	 */
	@Override
	public Long add(Ticket bean){
		return ticketMapper.insertEntity(bean);
	}
	/** 
	 * 批量新增
	 */
	@Override
	public Long batchAdd(List<Ticket> list){
		return ticketMapper.insertBatch(list);
	}
	/** 
	 * 批量新增/修改
	 */
	@Override
	public Long batchAddOrUpdate(List<Ticket> list){
		return ticketMapper.insertOrUpdateBatch(list);
	}
	/** 
	 * 根据Id查询
	 */
	@Override
	public Ticket findById(Long id){
		return ticketMapper.selectById(id);
	}
	/** 
	 * 根据Id修改
	 */
	@Override
	public Long updateById( Ticket entity, Long id){
		return ticketMapper.updateById(entity,id);
	}
	/** 
	 * 根据Id删除
	 */
	@Override
	public Long deleteById(Long id){
		return ticketMapper.deleteById(id);
	}
	/** 
	 * 根据attractionId删除
	 */
	@Override
	public Long deleteByAttractionId(Long attractionId){
		return ticketMapper.deleteByAttractionId(attractionId);
	}
}