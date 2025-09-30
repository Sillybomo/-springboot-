package com.service;

import java.util.List;
import com.entity.po.Payment;
import com.entity.query.PaymentQuery;
import com.entity.vo.PaginationResultVO;
/**
 *  业务接口
 * 
 * 对应数据库表: t_payment
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
public interface PaymentService {
	/** 
	 * 根据条件查询列表
	 */
	List<Payment> findListByParam(PaymentQuery param);
	/** 
	 * 根据条件查询数量
	 */
	Long findCountByParam(PaymentQuery param);
	/** 
	 * 分页查询
	 */
	PaginationResultVO<Payment> findListByPage(PaymentQuery param);
	/** 
	 * 新增
	 */
	Long add(Payment bean);
	/** 
	 * 批量新增
	 */
	Long batchAdd(List<Payment> list);
	/** 
	 * 批量新增/修改
	 */
	Long batchAddOrUpdate(List<Payment> list);
	/** 
	 * 根据Id查询
	 */
	Payment findById(Long id);

	/** 
	 * 根据Id修改
	 */
	Long updateById(Payment entity, Long id);

	/** 
	 * 根据Id删除
	 */
	Long deleteById(Long id);

	/** 
	 * 根据OrderId查询
	 */
	Payment findByOrderId(Long orderId);

	/** 
	 * 根据OrderId修改
	 */
	Long updateByOrderId(Payment entity, Long orderId);

	/** 
	 * 根据OrderId删除
	 */
	Long deleteByOrderId(Long orderId);

}