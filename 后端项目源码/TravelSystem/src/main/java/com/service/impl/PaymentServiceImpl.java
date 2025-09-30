package com.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.entity.po.Payment;
import com.entity.query.PaymentQuery;
import com.entity.vo.PaginationResultVO;
import com.service.PaymentService;
import com.mapper.PaymentMapper;
import com.entity.query.SimplePage;
/**
 *  业务接口
 * 
 * 对应数据库表: t_payment
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private PaymentMapper paymentMapper;
	/** 
	 * 根据条件查询列表
	 */
	@Override
	public List<Payment> findListByParam(PaymentQuery param){
		return paymentMapper.selectByParams(param);
	}
	/** 
	 * 根据条件查询数量
	 */
	@Override
	public Long findCountByParam(PaymentQuery param){
		return paymentMapper.selectCountByCollection(param);
	}
	/** 
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<Payment> findListByPage(PaymentQuery param){
		// 参数校验
		if (param == null || param.getCurrentPage() <= 0 || param.getPageSize() <= 0) {
			throw new IllegalArgumentException("分页参数无效：currentPage 和 pageSize 必须大于 0");
		}
		PaginationResultVO<Payment> page = new PaginationResultVO<>();
		try {
			// 查询总记录数
			Long total = paymentMapper.selectCountByCollection(param);
			if (total == null) {
				total = 0L;
			}
			// 创建分页对象
			if(param.getSimplePage() == null) {
				SimplePage<Payment> sPage = new SimplePage<>(param.getCurrentPage(), param.getPageSize(), total);
				param.setSimplePage(sPage);
			}
			// 设置分页结果
			page.setTotal(total);
			page.setCurrent(param.getCurrentPage());
			page.setPageSize(param.getPageSize());
			page.setTotalPages(param.getSimplePage().getTotalPages());
			page.setList(paymentMapper.selectByParams(param));
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
	public Long add(Payment bean){
		return paymentMapper.insertEntity(bean);
	}
	/** 
	 * 批量新增
	 */
	@Override
	public Long batchAdd(List<Payment> list){
		return paymentMapper.insertBatch(list);
	}
	/** 
	 * 批量新增/修改
	 */
	@Override
	public Long batchAddOrUpdate(List<Payment> list){
		return paymentMapper.insertOrUpdateBatch(list);
	}
	/** 
	 * 根据Id查询
	 */
	@Override
	public Payment findById(Long id){
		return paymentMapper.selectById(id);
	}
	/** 
	 * 根据Id修改
	 */
	@Override
	public Long updateById( Payment entity, Long id){
		return paymentMapper.updateById(entity,id);
	}
	/** 
	 * 根据Id删除
	 */
	@Override
	public Long deleteById(Long id){
		return paymentMapper.deleteById(id);
	}
	/** 
	 * 根据OrderId查询
	 */
	@Override
	public Payment findByOrderId(Long orderId){
		return paymentMapper.selectByOrderId(orderId);
	}
	/** 
	 * 根据OrderId修改
	 */
	@Override
	public Long updateByOrderId( Payment entity, Long orderId){
		return paymentMapper.updateByOrderId(entity,orderId);
	}
	/** 
	 * 根据OrderId删除
	 */
	@Override
	public Long deleteByOrderId(Long orderId){
		return paymentMapper.deleteByOrderId(orderId);
	}
}