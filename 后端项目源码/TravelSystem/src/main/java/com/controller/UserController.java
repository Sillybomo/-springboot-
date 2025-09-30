package com.controller;

import com.config.AjaxResult;
import com.entity.po.Order;
import com.entity.query.OrderQuery;
import com.entity.vo.PaginationResultVO;
import com.entity.vo.ResultVO;
import com.entity.po.User;
import com.entity.query.UserQuery;
import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserService service;


    @Autowired
    private UserService userService;

    //登录
    @Operation(description = "登录")
    @RequestMapping("/login")
    public AjaxResult<User> login(User user) {
        System.out.println("----login----");
        System.out.println("user:"+user);

        //远程调用,实现根据用户名和密码查询用户信息
        User user1 = userService.login(user);
        System.out.println("user1:"+user1);

        //配置返回结果
        AjaxResult<User> ajaxResult = new AjaxResult<>();

        if (user1 != null) {
            ajaxResult.setSuccess(true);

            ajaxResult.setMsg("登录成功");

            ajaxResult.setData(user1);

        }else  {
            ajaxResult.setSuccess(false);
            ajaxResult.setMsg("用户名或密码错误");
            ajaxResult.setData(null);
        }
        return ajaxResult;

    }

    //注册
    @Operation(description = "用户注册")
    @PostMapping("/register")
    public AjaxResult<User> register(@RequestBody User user) {
        System.out.println("----register----");
        System.out.println("user:"+user);

        //配置返回结果
        AjaxResult<User> ajaxResult = new AjaxResult<>();

        try {
            // 检查用户名是否已存在
            User existingUser = userService.findByUsername(user.getUsername());
            if (existingUser != null) {
                ajaxResult.setSuccess(false);
                ajaxResult.setMsg("用户名已存在");
                return ajaxResult;
            }

            // 检查邮箱是否已存在
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                User existingEmail = userService.findByEmail(user.getEmail());
                if (existingEmail != null) {
                    ajaxResult.setSuccess(false);
                    ajaxResult.setMsg("邮箱已被使用");
                    return ajaxResult;
                }
            }

            // 检查手机号是否已存在
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                User existingPhone = userService.findByPhone(user.getPhone());
                if (existingPhone != null) {
                    ajaxResult.setSuccess(false);
                    ajaxResult.setMsg("手机号已被使用");
                    return ajaxResult;
                }
            }

            // 设置默认值
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }
            if (user.getBalance() == null) {
                user.setBalance(0);
            }
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());

            // 保存用户
            Long result = userService.add(user);
            if (result != null && result > 0) {
                ajaxResult.setSuccess(true);
                ajaxResult.setMsg("注册成功");
                ajaxResult.setData(user);
            } else {
                ajaxResult.setSuccess(false);
                ajaxResult.setMsg("注册失败");
            }
        } catch (Exception e) {
            ajaxResult.setSuccess(false);
            ajaxResult.setMsg("注册失败: " + e.getMessage());
            System.err.println("注册失败: " + e.getMessage());
            e.printStackTrace();
        }

        return ajaxResult;
    }


	@PostMapping("/list")
	public ResultVO list(@RequestBody UserQuery param) {
		return ResultVO.ok(service.findListByParam(param));
	}

    @PostMapping("/page")
    public ResultVO page(@RequestBody UserQuery param) {
        return ResultVO.ok(service.findListByPage(param));
    }

    @Operation(summary = "分页查询订单列表")
    @RequestMapping("/userList")
    public ResultVO getUserList(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long ticketId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Date createdAt,
            @RequestParam(required = false) Date updatedAt,
            @RequestParam(required = false) BigDecimal balance,
            @RequestParam(required = false) Long avatar
    ) {

        try {
            System.out.println("=== 用户查询接口被调用 ===");

            // 构建查询参数对象
            UserQuery param = new UserQuery();
            param.setCurrentPage(currentPage);
            param.setPageSize(pageSize);

            // 设置查询条件（与前端搜索表单对应）
            if (id != null) {
                param.setUsernameFuzzy(userName);
                System.out.println("用户名查询: " + id);
            }
            if (name != null) {
                param.setNameFuzzy(name);
                System.out.println("用户姓名查询: " + name);
            }
            if (phone != null) {
                param.setPhoneFuzzy(phone);
                System.out.println("用户手机号查询: " + phone);
            }

            if (role != null && !role.trim().isEmpty()) {
                param.setRoleFuzzy(role.trim()); // 使用精确查询
                System.out.println("订单状态查询: " + role.trim());
            }


            // 调用Service层分页查询
            PaginationResultVO<User> pageResult = userService.findListByPage(param);



            return ResultVO.ok("查询成功", pageResult);
        } catch (IllegalArgumentException e) {
            return ResultVO.err(400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.err(500, "服务器内部错误，查询失败");
        }
    }

	@PostMapping
	public ResultVO add(@RequestBody User entity) {
		return ResultVO.ok(service.add(entity));
	}

	@PostMapping("/batch")
	public ResultVO batchAdd(@RequestBody List<User> list) {
		return ResultVO.ok(service.batchAdd(list));
	}

	@GetMapping("/{id}")
	public ResultVO getById(@PathVariable Long id) {
		return ResultVO.ok(service.findById(id));
	}

	@PutMapping("/{id}")
	public ResultVO updateById(@RequestBody User entity, @PathVariable Long id) {
		return ResultVO.ok(service.updateById(entity, id));
	}

	@DeleteMapping("/{id}")
	public ResultVO deleteById(@PathVariable Long id) {
		return ResultVO.ok(service.deleteById(id));
	}

}