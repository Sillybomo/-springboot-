package com.controller;

import com.entity.vo.PaginationResultVO;
import com.entity.vo.ResultVO;
import com.entity.po.Order;
import com.entity.query.OrderQuery;
import com.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * 控制器
 */
@RestController
@RequestMapping("/order")
public class OrderController {

	@Resource
	private OrderService service;

    @Autowired
    private OrderService orderService;


	@PostMapping("/list")
	public ResultVO list(@RequestBody OrderQuery param) {
		// 如果前端需要多表联查数据，使用多表联查方法
		if (param.getNeedJoin() != null && param.getNeedJoin()) {
			return ResultVO.ok(service.findListWithJoin(param));
		}
		return ResultVO.ok(service.findListByParam(param));
	}

	@PostMapping("/page")
	public ResultVO page(@RequestBody OrderQuery param) {
		return ResultVO.ok(service.findListByPage(param));
	}

    @Operation(summary = "分页查询订单列表")
    @RequestMapping("/orderList")
    public ResultVO getOrderList(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) Long ticketId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String remark
    ) {

        try {
            System.out.println("=== 订单查询接口被调用 ===");

            // 构建查询参数对象
            OrderQuery param = new OrderQuery();
            param.setCurrentPage(currentPage);
            param.setPageSize(pageSize);

            // 设置查询条件（与前端搜索表单对应）
            if (orderNo != null && !orderNo.trim().isEmpty()) {
                param.setOrderNoFuzzy(orderNo.trim());
                System.out.println("订单编号查询: " + orderNo.trim());
            }
            if (userId != null) {
                param.setUserIdFuzzy(userId.toString());
                System.out.println("用户ID查询: " + userId);
            }
            if (userName != null) {
                param.setContactNameFuzzy(userName);
                System.out.println("联系人名查询: " + userName);
            }
            if (ticketId != null) {
                param.setTicketId(ticketId);
                System.out.println("票务ID查询: " + ticketId);
            }
            if (status != null && !status.trim().isEmpty()) {
                param.setStatus(status.trim()); // 使用精确查询
                System.out.println("订单状态查询: " + status.trim());
            }


            // 调用Service层分页查询
            PaginationResultVO<Order> pageResult = orderService.findListByPage(param);



            return ResultVO.ok("查询成功", pageResult);
        } catch (IllegalArgumentException e) {
            return ResultVO.err(400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.err(500, "服务器内部错误，查询失败");
        }
    }

	@PostMapping
	public ResultVO add(@RequestBody Order entity) {
		return ResultVO.ok(service.add(entity));
	}

	@PostMapping("/batch")
	public ResultVO batchAdd(@RequestBody List<Order> list) {
		return ResultVO.ok(service.batchAdd(list));
	}

	@GetMapping("/{id}")
	public ResultVO getById(@PathVariable Long id) {
		return ResultVO.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	public ResultVO updateById(@RequestBody Order entity, @PathVariable Long id) {
		return ResultVO.ok(service.updateById(entity, id));
	}

	/**
	 * 订单发货接口
	 */
	@PutMapping("/{id}/delivery")
	public ResultVO deliveryOrder(@PathVariable Long id, @RequestBody Order o) {
		try {
			// 获取订单信息
			Order order = service.findById(id);
			if (order == null) {
				return ResultVO.err(404, "订单不存在");
			}

			// 检查订单状态，只有已支付的订单才能发货
			if (!"PAID".equals(order.getStatus())) {
				return ResultVO.err(400, "只有已支付的订单才能发货");
			}

			// 更新订单状态为已发货
			order.setStatus("DELIVERED");
			order.setUpdatedAt(new Date());

			// 如果有发货备注，可以存储到remark字段）
            order.setRemark(o.getRemark()); // 如果需要

            service.updateById(order, id);

			Long result = service.updateById(order, id);
			if (result > 0) {
				return ResultVO.ok("订单发货成功");
			} else {
				return ResultVO.err(500, "订单发货失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultVO.err(500, "服务器内部错误，发货失败");
		}
	}

	/**
	 * 删除订单接口（只有未支付的订单才能删除）
	 */
	@DeleteMapping("/{id}")
	public ResultVO deleteById(@PathVariable Long id) {
		try {
			// 获取订单信息
			Order order = service.findById(id);
			if (order == null) {
				return ResultVO.err(404, "订单不存在");
			}

			// 检查订单状态，只有未支付的订单才能删除
			if (!"UNPAID".equals(order.getStatus())) {
				return ResultVO.err(400, "只有未支付的订单才能删除");
			}

			Long result = service.deleteById(id);
			if (result > 0) {
				return ResultVO.ok("订单删除成功");
			} else {
				return ResultVO.err(500, "订单删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultVO.err(500, "服务器内部错误，删除失败");
		}
	}

	/**
	 * 退票处理接口
	 */
	@PutMapping("/{id}/refund")
	public ResultVO refundOrder(@PathVariable Long id, @RequestBody Map<String, String> refundData) {
		try {
			// 获取订单信息
			Order order = service.findById(id);
			if (order == null) {
				return ResultVO.err(404, "订单不存在");
			}

			// 检查订单状态，只有退票中的订单才能处理
			if (!"REFUND".equals(order.getStatus())) {
				return ResultVO.err(400, "只有退票中的订单才能处理");
			}

			String processResult = refundData.get("processResult");
			String processRemark = refundData.get("processRemark");

			if (processResult == null || (!"APPROVED".equals(processResult) && !"REJECTED".equals(processResult))) {
				return ResultVO.err(400, "无效的处理结果");
			}

			// 更新订单状态
			if ("APPROVED".equals(processResult)) {
				order.setStatus("CANCELLED");
			} else {
				order.setStatus("REFUND_REJECTED");

                order.setRemark(processRemark); // 如果需要
			}
			order.setUpdatedAt(new Date());


			Long result = service.updateById(order, id);
			if (result > 0) {
				return ResultVO.ok("退票处理成功");
			} else {
				return ResultVO.err(500, "退票处理失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultVO.err(500, "服务器内部错误，退票处理失败");
		}
	}

}