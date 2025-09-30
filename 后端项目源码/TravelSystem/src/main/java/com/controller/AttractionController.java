package com.controller;

import com.entity.vo.ResultVO;
import com.entity.po.Attraction;
import com.entity.query.AttractionQuery;
import com.service.AttractionService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 控制器
 */
@RestController
@RequestMapping("/attraction")
public class AttractionController {

	@Resource
	private AttractionService service;

	@PostMapping("/list")
	public ResultVO list(@RequestBody AttractionQuery param) {

        return ResultVO.ok(service.findListByParam(param));
	}

	@PostMapping
	public ResultVO add(@RequestBody Attraction entity) {
		return ResultVO.ok(service.add(entity));
	}

	@PostMapping("/batch")
	public ResultVO batchAdd(@RequestBody List<Attraction> list) {
		return ResultVO.ok(service.batchAdd(list));
	}

	@GetMapping("/{id}")
	public ResultVO getById(@PathVariable Long id) {
		return ResultVO.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	public ResultVO updateById(@RequestBody Attraction entity, @PathVariable Long id) {
		return ResultVO.ok(service.updateById(entity, id));
	}

	@DeleteMapping("/{id}")
	public ResultVO deleteById(@PathVariable Long id) {
		return ResultVO.ok(service.deleteById(id));
	}

}