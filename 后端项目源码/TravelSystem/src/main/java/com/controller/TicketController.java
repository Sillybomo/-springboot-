package com.controller;

import com.entity.vo.ResultVO;
import com.entity.po.Ticket;
import com.entity.query.TicketQuery;
import com.service.TicketService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 控制器
 */
@RestController
@RequestMapping("/ticket")
public class TicketController {

	@Resource
	private TicketService service;

	@PostMapping("/list")
	public ResultVO list(@RequestBody TicketQuery param) {
		return ResultVO.ok(service.findListByParam(param));
	}


	@PostMapping
	public ResultVO add(@RequestBody Ticket entity) {
		return ResultVO.ok(service.add(entity));
	}

	@PostMapping("/batch")
	public ResultVO batchAdd(@RequestBody List<Ticket> list) {
		return ResultVO.ok(service.batchAdd(list));
	}

	@GetMapping("/{id}")
	public ResultVO getById(@PathVariable Long id) {
		return ResultVO.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	public ResultVO updateById(@RequestBody Ticket entity, @PathVariable Long id) {
		return ResultVO.ok(service.updateById(entity, id));
	}

	@DeleteMapping("/{id}")
	public ResultVO deleteById(@PathVariable Long id) {
		return ResultVO.ok(service.deleteById(id));
	}

}