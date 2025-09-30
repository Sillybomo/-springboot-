package com.controller;

import com.entity.vo.ResultVO;
import com.entity.po.Payment;
import com.entity.query.PaymentQuery;
import com.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 控制器
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Resource
	private PaymentService service;

	@PostMapping("/list")
	public ResultVO list(@RequestBody PaymentQuery param) {
		return ResultVO.ok(service.findListByParam(param));
	}

	@PostMapping
	public ResultVO add(@RequestBody Payment entity) {
		return ResultVO.ok(service.add(entity));
	}

	@PostMapping("/batch")
	public ResultVO batchAdd(@RequestBody List<Payment> list) {
		return ResultVO.ok(service.batchAdd(list));
	}

	@GetMapping("/{id}")
	public ResultVO getById(@PathVariable Long id) {
		return ResultVO.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	public ResultVO updateById(@RequestBody Payment entity, @PathVariable Long id) {
		return ResultVO.ok(service.updateById(entity, id));
	}

	@DeleteMapping("/{id}")
	public ResultVO deleteById(@PathVariable Long id) {
		return ResultVO.ok(service.deleteById(id));
	}

	@GetMapping("/{orderId}")
	public ResultVO getByOrderId(@PathVariable Long orderId) {
		return ResultVO.ok(service.findByOrderId(orderId));
	}

	@PutMapping("/{orderId}")
	public ResultVO updateByOrderId(@RequestBody Payment entity, @PathVariable Long orderId) {
		return ResultVO.ok(service.updateByOrderId(entity, orderId));
	}

	@DeleteMapping("/{orderId}")
	public ResultVO deleteByOrderId(@PathVariable Long orderId) {
		return ResultVO.ok(service.deleteByOrderId(orderId));
	}

}