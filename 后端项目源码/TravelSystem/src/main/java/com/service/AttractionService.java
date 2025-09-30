package com.service;

import java.util.List;
import com.entity.po.Attraction;
import com.entity.query.AttractionQuery;
import com.entity.vo.PaginationResultVO;
/**
 *  业务接口
 * 
 * 对应数据库表: t_attraction
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
public interface AttractionService {
	/** 
	 * 根据条件查询列表
	 */
	List<Attraction> findListByParam(AttractionQuery param);
	/** 
	 * 根据条件查询数量
	 */
	Long findCountByParam(AttractionQuery param);
	/** 
	 * 分页查询
	 */
	PaginationResultVO<Attraction> findListByPage(AttractionQuery param);
	/** 
	 * 新增
	 */
	Long add(Attraction bean);
	/** 
	 * 批量新增
	 */
	Long batchAdd(List<Attraction> list);
	/** 
	 * 批量新增/修改
	 */
	Long batchAddOrUpdate(List<Attraction> list);
	/** 
	 * 根据Id查询
	 */
	Attraction findById(Long id);

	/** 
	 * 根据Id修改
	 */
	Long updateById(Attraction entity, Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(Long id);

}