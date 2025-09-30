package com.controller;

import com.entity.vo.ResultVO;
import com.entity.po.Favorite;
import com.entity.query.FavoriteQuery;
import com.service.FavoriteService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 控制器
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

	@Resource
	private FavoriteService service;

	@PostMapping("/list")
	public ResultVO list(@RequestBody FavoriteQuery param) {
		return ResultVO.ok(service.findListByParam(param));
	}

	@PostMapping
	public ResultVO add(@RequestBody Favorite entity) {
		return ResultVO.ok(service.add(entity));
	}

	@PostMapping("/batch")
	public ResultVO batchAdd(@RequestBody List<Favorite> list) {
		return ResultVO.ok(service.batchAdd(list));
	}

	@GetMapping("/{id}")
	public ResultVO getById(@PathVariable Long id) {
		return ResultVO.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	public ResultVO updateById(@RequestBody Favorite entity, @PathVariable Long id) {
		return ResultVO.ok(service.updateById(entity, id));
	}

	@DeleteMapping("/{id}")
	public ResultVO deleteById(@PathVariable Long id) {
		return ResultVO.ok(service.deleteById(id));
	}

	@GetMapping("/{userId}/{attractionId}")
	public ResultVO getByUserIdAndAttractionId(@PathVariable Long userId, @PathVariable Long attractionId) {
		return ResultVO.ok(service.findByUserIdAndAttractionId(userId, attractionId));
	}

	@PutMapping("/{userId}/{attractionId}")
	public ResultVO updateByUserIdAndAttractionId(@RequestBody Favorite entity, @PathVariable Long userId, @PathVariable Long attractionId) {
		return ResultVO.ok(service.updateByUserIdAndAttractionId(entity, userId, attractionId));
	}

	@DeleteMapping("/{userId}/{attractionId}")
	public ResultVO deleteByUserIdAndAttractionId(@PathVariable Long userId, @PathVariable Long attractionId) {
		return ResultVO.ok(service.deleteByUserIdAndAttractionId(userId, attractionId));
	}

}