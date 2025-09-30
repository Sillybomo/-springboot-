package com.controller;

import com.entity.vo.ResultVO;
import com.entity.po.Review;
import com.entity.query.ReviewQuery;
import com.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 控制器
 */
@RestController
@RequestMapping("/review")
public class ReviewController {

	@Resource
	private ReviewService service;

	@PostMapping("/list")
	public ResultVO list(@RequestBody ReviewQuery param) {
		return ResultVO.ok(service.findListByParam(param));
	}

	@PostMapping
	public ResultVO add(@RequestBody Review entity) {
		return ResultVO.ok(service.add(entity));
	}

	@PostMapping("/batch")
	public ResultVO batchAdd(@RequestBody List<Review> list) {
		return ResultVO.ok(service.batchAdd(list));
	}

	@GetMapping("/{id}")
	public ResultVO getById(@PathVariable Long id) {
		return ResultVO.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	public ResultVO updateById(@RequestBody Review entity, @PathVariable Long id) {
		return ResultVO.ok(service.updateById(entity, id));
	}

	@DeleteMapping("/{id}")
	public ResultVO deleteById(@PathVariable Long id) {
		return ResultVO.ok(service.deleteById(id));
	}

}