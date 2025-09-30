package com.controller;

import com.entity.vo.ResultVO;
import com.entity.po.Cart;
import com.entity.query.CartQuery;
import com.service.CartService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 购物车表：存储用户待购买的门票信息控制器
 */
@RestController
@RequestMapping("/cart")
public class CartController {

	@Resource
	private CartService service;

	@PostMapping("/list")
	public ResultVO list(@RequestBody CartQuery param) {
		// 如果前端需要多表联查数据，使用多表联查方法
		if (param.getNeedJoin() != null && param.getNeedJoin()) {
			return ResultVO.ok(service.findListWithJoin(param));
		}
		return ResultVO.ok(service.findListByParam(param));
	}

	@PostMapping
	public ResultVO add(@RequestBody Cart entity) {
		return ResultVO.ok(service.add(entity));
	}

	@PostMapping("/batch")
	public ResultVO batchAdd(@RequestBody List<Cart> list) {
		return ResultVO.ok(service.batchAdd(list));
	}

	@GetMapping("/{id}")
	public ResultVO getById(@PathVariable Long id) {
		return ResultVO.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	public ResultVO updateById(@RequestBody Cart entity, @PathVariable Long id) {
		return ResultVO.ok(service.updateById(entity, id));
	}

	@DeleteMapping("/{id}")
	public ResultVO deleteById(@PathVariable Long id) {
		return ResultVO.ok(service.deleteById(id));
	}

	@GetMapping("/{userId}/{ticketId}")
	public ResultVO getByUserIdAndTicketId(@PathVariable Long userId, @PathVariable Long ticketId) {
		return ResultVO.ok(service.findByUserIdAndTicketId(userId, ticketId));
	}

	@PutMapping("/{userId}/{ticketId}")
	public ResultVO updateByUserIdAndTicketId(@RequestBody Cart entity, @PathVariable Long userId, @PathVariable Long ticketId) {
		return ResultVO.ok(service.updateByUserIdAndTicketId(entity, userId, ticketId));
	}

	@DeleteMapping("/{userId}/{ticketId}")
	public ResultVO deleteByUserIdAndTicketId(@PathVariable Long userId, @PathVariable Long ticketId) {
		return ResultVO.ok(service.deleteByUserIdAndTicketId(userId, ticketId));
	}

	/**
	 * 获取购物车数量
	 */
	@GetMapping("/count")
	public ResultVO getCartCount(@RequestParam(required = false) Long userId) {
		CartQuery query = new CartQuery();
		if (userId != null) {
			query.setUserId(userId);
		}
		Long count = service.findCountByParam(query);
		return ResultVO.ok(count);
	}

	/**
	 * 更新购物车商品数量
	 */
	@PostMapping("/update")
	public ResultVO updateCart(@RequestBody Cart entity) {
		return ResultVO.ok(service.updateById(entity, entity.getId()));
	}

	/**
	 * 删除购物车商品
	 */
	@PostMapping("/delete")
	public ResultVO deleteCart(@RequestBody CartQuery query) {
		if (query.getId() != null) {
			return ResultVO.ok(service.deleteById(query.getId()));
		}
		return ResultVO.err(400, "商品ID不能为空");
	}

	/**
	 * 批量删除购物车商品
	 */
	@PostMapping("/batchDelete")
	public ResultVO batchDeleteCart(@RequestBody List<Long> ids) {
		int count = 0;
		for (Long id : ids) {
			service.deleteById(id);
			count++;
		}
		return ResultVO.ok(count);
	}

	/**
	 * 清空购物车
	 */
	@PostMapping("/clear")
	public ResultVO clearCart(@RequestBody CartQuery query) {
		if (query.getUserId() != null) {
			// 根据用户ID清空购物车
			List<Cart> cartList = service.findListByParam(query);
			for (Cart cart : cartList) {
				service.deleteById(cart.getId());
			}
			return ResultVO.ok("购物车已清空");
		}
		return ResultVO.err(400, "用户ID不能为空");
	}

	/**
	 * 购物车结算
	 */
	@PostMapping("/checkout")
	public ResultVO checkoutCart(@RequestBody CartQuery query) {
		// 这里可以添加结算逻辑，比如创建订单等
		// 暂时返回成功
		return ResultVO.ok("结算功能待实现");
	}

}