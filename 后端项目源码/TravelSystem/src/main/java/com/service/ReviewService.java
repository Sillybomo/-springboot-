package com.service;

import java.util.List;
import com.entity.po.Review;
import com.entity.query.ReviewQuery;
import com.entity.vo.PaginationResultVO;
/**
 *  业务接口
 * 
 * 对应数据库表: t_review
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
public interface ReviewService {
	/** 
	 * 根据条件查询列表
	 */
	List<Review> findListByParam(ReviewQuery param);
	/** 
	 * 根据条件查询数量
	 */
	Long findCountByParam(ReviewQuery param);
	/** 
	 * 分页查询
	 */
	PaginationResultVO<Review> findListByPage(ReviewQuery param);
	/** 
	 * 新增
	 */
	Long add(Review bean);
	/** 
	 * 批量新增
	 */
	Long batchAdd(List<Review> list);
	/** 
	 * 批量新增/修改
	 */
	Long batchAddOrUpdate(List<Review> list);
	/** 
	 * 根据Id查询
	 */
	Review findById(Long id);

	/** 
	 * 根据Id修改
	 */
	Long updateById(Review entity, Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(Long id);

	/** 
	 * 根据attractionId删除
	 */
	Long deleteByAttractionId(Long attractionId);
	/** 
	 * 根据userId删除
	 */
	Long deleteByUserId(Long userId);
	/** 
	 * 根据orderId删除
	 */
	Long deleteByOrderId(Long orderId);
}