package com.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.entity.po.Order;
import com.entity.query.OrderQuery;
import com.entity.vo.PaginationResultVO;
import com.service.OrderService;
import com.mapper.OrderMapper;
import com.entity.query.SimplePage;
/**
 *  业务接口
 * 
 * 对应数据库表: t_order
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderMapper orderMapper;
	/** 
	 * 根据条件查询列表
	 */
	@Override
	public List<Order> findListByParam(OrderQuery param){
		return orderMapper.selectByParams(param);
	}
	/** 
	 * 根据条件查询数量
	 */
	@Override
	public Long findCountByParam(OrderQuery param){
		return orderMapper.selectCountByCollection(param);
	}
	/** 
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<Order> findListByPage(OrderQuery param){
		// 参数校验
		if (param == null || param.getCurrentPage() <= 0 || param.getPageSize() <= 0) {
			throw new IllegalArgumentException("分页参数无效：currentPage 和 pageSize 必须大于 0");
		}
		PaginationResultVO<Order> page = new PaginationResultVO<>();
		try {

            // 构建多表联查参数
            String joinTables = "t_order o JOIN t_user u";
            String joinConditions = "ON o.user_id = u.id";

            // 构建模糊查询条件

            List<String> conditions = new ArrayList<>();

            // 1. 订单编号模糊查询
            if (param.getOrderNoFuzzy() != null && !param.getOrderNoFuzzy().isEmpty()) {
                String safeKeyword = param.getOrderNoFuzzy()
                        .replace("'", "''")
                        .replace("%", "\\%")
                        .replace("_", "\\_");
                conditions.add("o.order_no LIKE '%" + safeKeyword + "%'");
            }

            // 2. 用户ID模糊查询
            if (param.getUserIdFuzzy() != null && !param.getUserIdFuzzy().isEmpty()) {
                String safeKeyword = param.getUserIdFuzzy()
                        .replace("'", "''")
                        .replace("%", "\\%")
                        .replace("_", "\\_");
                conditions.add("o.user_id LIKE '%" + safeKeyword + "%'");
            }

            // 3. 订单状态精确查询
            if (param.getStatus() != null && !param.getStatus().isEmpty()) {
                // 等值查询不需要转义特殊字符
                conditions.add("o.status = '" + param.getStatus() + "'");
            }

            // 4. 联系人姓名模糊查询
            if (param.getContactNameFuzzy() != null && !param.getContactNameFuzzy().isEmpty()) {
                String safeKeyword = param.getContactNameFuzzy()
                        .replace("'", "''")
                        .replace("%", "\\%")
                        .replace("_", "\\_");
                conditions.add("o.contact_name LIKE '%" + safeKeyword + "%'");
            }


            // 组合所有条件
            System.out.println("conditions: " + conditions);
            String whereConditions = conditions.isEmpty() ? null : String.join(" AND ", conditions);
            System.out.println("whereConditions: " + whereConditions);

            // 查询总记录数
            Long total = orderMapper.selectJoinCount(joinTables,joinConditions,whereConditions);
            if (total == null) total = 0L;



            // 创建分页对象
			if(param.getSimplePage() == null) {
				SimplePage<Order> sPage = new SimplePage<>(param.getCurrentPage(), param.getPageSize(), total);
				param.setSimplePage(sPage);
			}
			// 设置分页结果
			page.setTotal(total);
			page.setCurrent(param.getCurrentPage());
			page.setPageSize(param.getPageSize());
			page.setTotalPages(param.getSimplePage().getTotalPages());

            // 执行多表联查（带分页）
            int offset = (param.getCurrentPage() - 1) * param.getPageSize();
            List<Map<String, Object>> result = orderMapper.selectJoinPage(
                    joinTables,
                    "o.*,  u.username", // 选择列：产品所有字段 + 分类名称
                    joinConditions,
                    whereConditions,
                    "o.id DESC", // 按商品ID倒序排列
                    offset,
                    param.getPageSize()
            );

            // 转换Map结果到Order对象
            List<Order> orderList = new ArrayList<>();
            for (Map<String, Object> map : result) {
                Order order = new Order();
                // 设置订单基础字段
                order.setId((Long) map.get("id"));
                order.setOrderNo((String) map.get("order_no"));
                order.setUserId((Long) map.get("user_id"));
                order.setTicketId((Long) map.get("ticket_id"));
                order.setQuantity((Integer) map.get("quantity"));
                order.setTotalAmount((BigDecimal) map.get("total_amount"));
                order.setStatus((String) map.get("status"));
                order.setVisitDate((Date) map.get("visit_date"));
                order.setContactName((String) map.get("contact_name"));
                order.setContactPhone((String) map.get("contact_phone"));
                order.setRemark((String) map.get("remark"));
                
                // 修复时间字段的类型转换 - 处理Timestamp到Date的转换
                Object createdAtObj = map.get("created_at");
                if (createdAtObj instanceof java.sql.Timestamp) {
                    order.setCreatedAt(new Date(((java.sql.Timestamp) createdAtObj).getTime()));
                } else if (createdAtObj instanceof java.sql.Date) {
                    order.setCreatedAt((Date) createdAtObj);
                }
                
                Object updatedAtObj = map.get("updated_at");
                if (updatedAtObj instanceof java.sql.Timestamp) {
                    order.setUpdatedAt(new Date(((java.sql.Timestamp) updatedAtObj).getTime()));
                } else if (updatedAtObj instanceof java.sql.Date) {
                    order.setUpdatedAt((Date) updatedAtObj);
                }
                
                // 设置关联的用户名
                order.setUsername((String) map.get("username"));

                orderList.add(order);
            }
            page.setList(orderList);


		} catch (Exception e) {
			e.printStackTrace(); // 打印详细异常信息
			System.out.println("分页查询失败: " + e.getMessage());
			return new PaginationResultVO<>();
		}
		return page;
	}

	/** 
	 * 新增
	 */
	@Override
	public Long add(Order bean){
		return orderMapper.insertEntity(bean);
	}
	/** 
	 * 批量新增
	 */
	@Override
	public Long batchAdd(List<Order> list){
		return orderMapper.insertBatch(list);
	}
	/** 
	 * 批量新增/修改
	 */
	@Override
	public Long batchAddOrUpdate(List<Order> list){
		return orderMapper.insertOrUpdateBatch(list);
	}
	/** 
	 * 根据Id查询
	 */
	@Override
	public Order findById(Long id){
		return orderMapper.selectById(id);
	}
	/** 
	 * 根据Id修改
	 */
	@Override
	public Long updateById( Order entity, Long id){
		return orderMapper.updateById(entity,id);
	}
	/** 
	 * 根据Id删除
	 */
	@Override
	public Long deleteById(Long id){
		return orderMapper.deleteById(id);
	}
	/** 
	 * 根据userId删除
	 */
	@Override
	public Long deleteByUserId(Long userId){
		return orderMapper.deleteByUserId(userId);
	}
	/** 
	 * 根据ticketId删除
	 */
	@Override
	public Long deleteByTicketId(Long ticketId){
		return orderMapper.deleteByTicketId(ticketId);
	}
	
	/** 
	 * 多表联查获取订单列表（包含景点和门票信息）
	 */
	@Override
	public List<Map<String, Object>> findListWithJoin(OrderQuery param) {
		return orderMapper.selectOrderWithAttractionAndTicket(param);
	}
}