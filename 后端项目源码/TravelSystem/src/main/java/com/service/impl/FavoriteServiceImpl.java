package com.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.entity.po.Favorite;
import com.entity.query.FavoriteQuery;
import com.entity.vo.PaginationResultVO;
import com.service.FavoriteService;
import com.mapper.FavoriteMapper;
import com.entity.query.SimplePage;
/**
 *  业务接口
 * 
 * 对应数据库表: t_favorite
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {
	@Autowired
	private FavoriteMapper favoriteMapper;
	/** 
	 * 根据条件查询列表
	 */
	@Override
	public List<Favorite> findListByParam(FavoriteQuery param){
		return favoriteMapper.selectByParams(param);
	}
	/** 
	 * 根据条件查询数量
	 */
	@Override
	public Long findCountByParam(FavoriteQuery param){
		return favoriteMapper.selectCountByCollection(param);
	}
	/** 
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<Favorite> findListByPage(FavoriteQuery param){
		// 参数校验
		if (param == null || param.getCurrentPage() <= 0 || param.getPageSize() <= 0) {
			throw new IllegalArgumentException("分页参数无效：currentPage 和 pageSize 必须大于 0");
		}
		PaginationResultVO<Favorite> page = new PaginationResultVO<>();
		try {
			// 查询总记录数
			Long total = favoriteMapper.selectCountByCollection(param);
			if (total == null) {
				total = 0L;
			}
			// 创建分页对象
			if(param.getSimplePage() == null) {
				SimplePage<Favorite> sPage = new SimplePage<>(param.getCurrentPage(), param.getPageSize(), total);
				param.setSimplePage(sPage);
			}
			// 设置分页结果
			page.setTotal(total);
			page.setCurrent(param.getCurrentPage());
			page.setPageSize(param.getPageSize());
			page.setTotalPages(param.getSimplePage().getTotalPages());
			page.setList(favoriteMapper.selectByParams(param));
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
	public Long add(Favorite bean){
		return favoriteMapper.insertEntity(bean);
	}
	/** 
	 * 批量新增
	 */
	@Override
	public Long batchAdd(List<Favorite> list){
		return favoriteMapper.insertBatch(list);
	}
	/** 
	 * 批量新增/修改
	 */
	@Override
	public Long batchAddOrUpdate(List<Favorite> list){
		return favoriteMapper.insertOrUpdateBatch(list);
	}
	/** 
	 * 根据Id查询
	 */
	@Override
	public Favorite findById(Long id){
		return favoriteMapper.selectById(id);
	}
	/** 
	 * 根据Id修改
	 */
	@Override
	public Long updateById( Favorite entity, Long id){
		return favoriteMapper.updateById(entity,id);
	}
	/** 
	 * 根据Id删除
	 */
	@Override
	public Long deleteById(Long id){
		return favoriteMapper.deleteById(id);
	}
	/** 
	 * 根据UserIdAndAttractionId查询
	 */
	@Override
	public Favorite findByUserIdAndAttractionId(Long userId,Long attractionId){
		return favoriteMapper.selectByUserIdAndAttractionId(userId,attractionId);
	}
	/** 
	 * 根据UserIdAndAttractionId修改
	 */
	@Override
	public Long updateByUserIdAndAttractionId( Favorite entity, Long userId,Long attractionId){
		return favoriteMapper.updateByUserIdAndAttractionId(entity,userId,attractionId);
	}
	/** 
	 * 根据UserIdAndAttractionId删除
	 */
	@Override
	public Long deleteByUserIdAndAttractionId(Long userId,Long attractionId){
		return favoriteMapper.deleteByUserIdAndAttractionId(userId,attractionId);
	}
}