package com.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.entity.po.Cart;
import com.entity.query.CartQuery;
import com.entity.vo.PaginationResultVO;
import com.service.CartService;
import com.mapper.CartMapper;
import com.entity.query.SimplePage;
/**
 * 购物车表：存储用户待购买的门票信息 业务接口
 * 
 * 对应数据库表: t_cart
 * @author Bomo
 * @date 2025-09-19 14:19:41
 */
@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartMapper cartMapper;
	/** 
	 * 根据条件查询列表
	 */
	@Override
	public List<Cart> findListByParam(CartQuery param){
		return cartMapper.selectByParams(param);
	}
	/** 
	 * 根据条件查询数量
	 */
	@Override
	public Long findCountByParam(CartQuery param){
		return cartMapper.selectCountByCollection(param);
	}
	
	/** 
	 * 多表联查获取购物车列表（包含景点和门票信息）
	 */
	@Override
	public List<Map<String, Object>> findListWithJoin(CartQuery param) {
		return cartMapper.selectCartWithAttractionAndTicket(param);
	}
	/** 
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<Cart> findListByPage(CartQuery param){
		// 参数校验
		if (param == null || param.getCurrentPage() <= 0 || param.getPageSize() <= 0) {
			throw new IllegalArgumentException("分页参数无效：currentPage 和 pageSize 必须大于 0");
		}
		PaginationResultVO<Cart> page = new PaginationResultVO<>();
		try {
			// 查询总记录数
			Long total = cartMapper.selectCountByCollection(param);
			if (total == null) {
				total = 0L;
			}
			// 创建分页对象
			if(param.getSimplePage() == null) {
				SimplePage<Cart> sPage = new SimplePage<>(param.getCurrentPage(), param.getPageSize(), total);
				param.setSimplePage(sPage);
			}
			// 设置分页结果
			page.setTotal(total);
			page.setCurrent(param.getCurrentPage());
			page.setPageSize(param.getPageSize());
			page.setTotalPages(param.getSimplePage().getTotalPages());
			page.setList(cartMapper.selectByParams(param));
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
	public Long add(Cart bean){
		return cartMapper.insertEntity(bean);
	}
	/** 
	 * 批量新增
	 */
	@Override
	public Long batchAdd(List<Cart> list){
		return cartMapper.insertBatch(list);
	}
	/** 
	 * 批量新增/修改
	 */
	@Override
	public Long batchAddOrUpdate(List<Cart> list){
		return cartMapper.insertOrUpdateBatch(list);
	}
	/** 
	 * 根据Id查询
	 */
	@Override
	public Cart findById(Long id){
		return cartMapper.selectById(id);
	}
	/** 
	 * 根据Id修改
	 */
	@Override
	public Long updateById( Cart entity, Long id){
		return cartMapper.updateById(entity,id);
	}
	/** 
	 * 根据Id删除
	 */
	@Override
	public Long deleteById(Long id){
		return cartMapper.deleteById(id);
	}
	/** 
	 * 根据UserIdAndTicketId查询
	 */
	@Override
	public Cart findByUserIdAndTicketId(Long userId,Long ticketId){
		return cartMapper.selectByUserIdAndTicketId(userId,ticketId);
	}
	/** 
	 * 根据UserIdAndTicketId修改
	 */
	@Override
	public Long updateByUserIdAndTicketId( Cart entity, Long userId,Long ticketId){
		return cartMapper.updateByUserIdAndTicketId(entity,userId,ticketId);
	}
	/** 
	 * 根据UserIdAndTicketId删除
	 */
	@Override
	public Long deleteByUserIdAndTicketId(Long userId,Long ticketId){
		return cartMapper.deleteByUserIdAndTicketId(userId,ticketId);
	}
}