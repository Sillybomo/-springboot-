package com.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.entity.po.Review;
import com.entity.query.ReviewQuery;
import com.entity.vo.PaginationResultVO;
import com.service.ReviewService;
import com.mapper.ReviewMapper;
import com.entity.query.SimplePage;
/**
 *  业务接口
 * 
 * 对应数据库表: t_review
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Service
public class ReviewServiceImpl implements ReviewService {
	@Autowired
	private ReviewMapper reviewMapper;
	/** 
	 * 根据条件查询列表
	 */
	@Override
	public List<Review> findListByParam(ReviewQuery param){
		return reviewMapper.selectByParams(param);
	}
	/** 
	 * 根据条件查询数量
	 */
	@Override
	public Long findCountByParam(ReviewQuery param){
		return reviewMapper.selectCountByCollection(param);
	}
	/** 
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<Review> findListByPage(ReviewQuery param){
		// 参数校验
		if (param == null || param.getCurrentPage() <= 0 || param.getPageSize() <= 0) {
			throw new IllegalArgumentException("分页参数无效：currentPage 和 pageSize 必须大于 0");
		}
		PaginationResultVO<Review> page = new PaginationResultVO<>();
		try {
			// 查询总记录数
			Long total = reviewMapper.selectCountByCollection(param);
			if (total == null) {
				total = 0L;
			}
			// 创建分页对象
			if(param.getSimplePage() == null) {
				SimplePage<Review> sPage = new SimplePage<>(param.getCurrentPage(), param.getPageSize(), total);
				param.setSimplePage(sPage);
			}
			// 设置分页结果
			page.setTotal(total);
			page.setCurrent(param.getCurrentPage());
			page.setPageSize(param.getPageSize());
			page.setTotalPages(param.getSimplePage().getTotalPages());
			page.setList(reviewMapper.selectByParams(param));
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
	public Long add(Review bean){
		return reviewMapper.insertEntity(bean);
	}
	/** 
	 * 批量新增
	 */
	@Override
	public Long batchAdd(List<Review> list){
		return reviewMapper.insertBatch(list);
	}
	/** 
	 * 批量新增/修改
	 */
	@Override
	public Long batchAddOrUpdate(List<Review> list){
		return reviewMapper.insertOrUpdateBatch(list);
	}
	/** 
	 * 根据Id查询
	 */
	@Override
	public Review findById(Long id){
		return reviewMapper.selectById(id);
	}
	/** 
	 * 根据Id修改
	 */
	@Override
	public Long updateById( Review entity, Long id){
		return reviewMapper.updateById(entity,id);
	}
	/** 
	 * 根据Id删除
	 */
	@Override
	public Long deleteById(Long id){
		return reviewMapper.deleteById(id);
	}
	/** 
	 * 根据attractionId删除
	 */
	@Override
	public Long deleteByAttractionId(Long attractionId){
		return reviewMapper.deleteByAttractionId(attractionId);
	}
	/** 
	 * 根据userId删除
	 */
	@Override
	public Long deleteByUserId(Long userId){
		return reviewMapper.deleteByUserId(userId);
	}
	/** 
	 * 根据orderId删除
	 */
	@Override
	public Long deleteByOrderId(Long orderId){
		return reviewMapper.deleteByOrderId(orderId);
	}
}