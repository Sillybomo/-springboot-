package com.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.entity.po.Attraction;
import com.entity.query.AttractionQuery;
import com.entity.vo.PaginationResultVO;
import com.service.AttractionService;
import com.mapper.AttractionMapper;
import com.entity.query.SimplePage;
/**
 *  业务接口
 * 
 * 对应数据库表: t_attraction
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Service
public class AttractionServiceImpl implements AttractionService {
	@Autowired
	private AttractionMapper attractionMapper;
	/** 
	 * 根据条件查询列表
	 */
	@Override
	public List<Attraction> findListByParam(AttractionQuery param){
		return attractionMapper.selectByParams(param);
	}
	/** 
	 * 根据条件查询数量
	 */
	@Override
	public Long findCountByParam(AttractionQuery param){
		return attractionMapper.selectCountByCollection(param);
	}
	/** 
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<Attraction> findListByPage(AttractionQuery param){
		// 参数校验
		if (param == null || param.getCurrentPage() <= 0 || param.getPageSize() <= 0) {
			throw new IllegalArgumentException("分页参数无效：currentPage 和 pageSize 必须大于 0");
		}
		PaginationResultVO<Attraction> page = new PaginationResultVO<>();
		try {
			// 查询总记录数
			Long total = attractionMapper.selectCountByCollection(param);
			if (total == null) {
				total = 0L;
			}
			// 创建分页对象
			if(param.getSimplePage() == null) {
				SimplePage<Attraction> sPage = new SimplePage<>(param.getCurrentPage(), param.getPageSize(), total);
				param.setSimplePage(sPage);
			}
			// 设置分页结果
			page.setTotal(total);
			page.setCurrent(param.getCurrentPage());
			page.setPageSize(param.getPageSize());
			page.setTotalPages(param.getSimplePage().getTotalPages());
			page.setList(attractionMapper.selectByParams(param));
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
	public Long add(Attraction bean){
		return attractionMapper.insertEntity(bean);
	}
	/** 
	 * 批量新增
	 */
	@Override
	public Long batchAdd(List<Attraction> list){
		return attractionMapper.insertBatch(list);
	}
	/** 
	 * 批量新增/修改
	 */
	@Override
	public Long batchAddOrUpdate(List<Attraction> list){
		return attractionMapper.insertOrUpdateBatch(list);
	}
	/** 
	 * 根据Id查询
	 */
	@Override
	public Attraction findById(Long id){
		return attractionMapper.selectById(id);
	}
	/** 
	 * 根据Id修改
	 */
	@Override
	public Long updateById( Attraction entity, Long id){
		return attractionMapper.updateById(entity,id);
	}
	/** 
	 * 根据Id删除
	 */
	@Override
	public Long deleteById(Long id){
		return attractionMapper.deleteById(id);
	}
}