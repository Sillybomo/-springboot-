package com.service;

import java.util.List;
import com.entity.po.Favorite;
import com.entity.query.FavoriteQuery;
import com.entity.vo.PaginationResultVO;
/**
 *  业务接口
 * 
 * 对应数据库表: t_favorite
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
public interface FavoriteService {
	/** 
	 * 根据条件查询列表
	 */
	List<Favorite> findListByParam(FavoriteQuery param);
	/** 
	 * 根据条件查询数量
	 */
	Long findCountByParam(FavoriteQuery param);
	/** 
	 * 分页查询
	 */
	PaginationResultVO<Favorite> findListByPage(FavoriteQuery param);
	/** 
	 * 新增
	 */
	Long add(Favorite bean);
	/** 
	 * 批量新增
	 */
	Long batchAdd(List<Favorite> list);
	/** 
	 * 批量新增/修改
	 */
	Long batchAddOrUpdate(List<Favorite> list);
	/** 
	 * 根据Id查询
	 */
	Favorite findById(Long id);

	/** 
	 * 根据Id修改
	 */
	Long updateById(Favorite entity, Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(Long id);

	/** 
	 * 根据UserIdAndAttractionId查询
	 */
	Favorite findByUserIdAndAttractionId(Long userId,Long attractionId);

	/** 
	 * 根据UserIdAndAttractionId修改
	 */
	Long updateByUserIdAndAttractionId(Favorite entity, Long userId,Long attractionId);

	/** 
	 * 根据UserIdAndAttractionId删除
	 */
	Long deleteByUserIdAndAttractionId(Long userId,Long attractionId);

}