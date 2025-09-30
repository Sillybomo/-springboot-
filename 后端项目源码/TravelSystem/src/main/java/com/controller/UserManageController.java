package com.controller;

import com.entity.po.User;
import com.entity.vo.ResultVO;
import com.entity.query.UserQuery;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/user")
public class UserManageController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表（分页+筛选）
     */
    @PostMapping("/userList")
    public ResultVO getUserList(@RequestBody UserQuery query) {
        try {
            // 使用分页查询方法
            var pageResult = userService.findListByPage(query);
            
            // 构建返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("list", pageResult.getList());
            result.put("total", pageResult.getTotal());
            result.put("current", pageResult.getCurrent());
            result.put("size", pageResult.getPageSize());
            
            return ResultVO.ok(result);
        } catch (Exception e) {
            return ResultVO.err(500, "获取用户列表失败: " + e.getMessage());
        }
    }

    /**
     * 创建用户
     */
    @PostMapping("/create")
    public ResultVO createUser(@RequestBody User user) {
        try {
            // 检查用户名是否已存在
            User existingUser = userService.findByUsername(user.getUsername());
            if (existingUser != null) {
                return ResultVO.err(400, "用户名已存在");
            }

            // 检查邮箱是否已存在
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                User existingEmail = userService.findByEmail(user.getEmail());
                if (existingEmail != null) {
                    return ResultVO.err(400, "邮箱已被使用");
                }
            }

            // 检查手机号是否已存在
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                User existingPhone = userService.findByPhone(user.getPhone());
                if (existingPhone != null) {
                    return ResultVO.err(400, "手机号已被使用");
                }
            }

            // 设置默认值
            if (user.getBalance() == null) {
                user.setBalance(0);
            }
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }

            Long result = userService.add(user);
            if (result != null && result > 0) {
                return ResultVO.ok("用户创建成功");
            } else {
                return ResultVO.err(500, "用户创建失败");
            }
        } catch (Exception e) {
            return ResultVO.err(500, "创建用户失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/update/{userId}")
    public ResultVO updateUser(@PathVariable Long userId, @RequestBody User user) {
        try {
            User existingUser = userService.findById(userId);
            if (existingUser == null) {
                return ResultVO.err(404, "用户不存在");
            }

            // 检查用户名是否被其他用户使用
            if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
                User usernameUser = userService.findByUsername(user.getUsername());
                if (usernameUser != null && !usernameUser.getId().equals(userId)) {
                    return ResultVO.err(400, "用户名已被其他用户使用");
                }
            }

            // 检查邮箱是否被其他用户使用
            if (user.getEmail() != null && !user.getEmail().isEmpty() && 
                !user.getEmail().equals(existingUser.getEmail())) {
                User emailUser = userService.findByEmail(user.getEmail());
                if (emailUser != null && !emailUser.getId().equals(userId)) {
                    return ResultVO.err(400, "邮箱已被其他用户使用");
                }
            }

            // 检查手机号是否被其他用户使用
            if (user.getPhone() != null && !user.getPhone().isEmpty() && 
                !user.getPhone().equals(existingUser.getPhone())) {
                User phoneUser = userService.findByPhone(user.getPhone());
                if (phoneUser != null && !phoneUser.getId().equals(userId)) {
                    return ResultVO.err(400, "手机号已被其他用户使用");
                }
            }

            // 更新用户信息
            user.setId(userId);
            Long result = userService.updateById(user, userId);
            if (result != null && result > 0) {
                return ResultVO.ok("用户更新成功");
            } else {
                return ResultVO.err(500, "用户更新失败");
            }
        } catch (Exception e) {
            return ResultVO.err(500, "更新用户失败: " + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{userId}")
    public ResultVO deleteUser(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResultVO.err(404, "用户不存在");
            }

            // 不能删除管理员账户
            if ("ADMIN".equals(user.getRole())) {
                return ResultVO.err(400, "不能删除管理员账户");
            }

            Long result = userService.deleteById(userId);
            if (result != null && result > 0) {
                return ResultVO.ok("用户删除成功");
            } else {
                return ResultVO.err(500, "用户删除失败");
            }
        } catch (Exception e) {
            return ResultVO.err(500, "删除用户失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/batchDelete")
    public ResultVO batchDeleteUsers(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> userIds = (List<Long>) request.get("userIds");
            
            if (userIds == null || userIds.isEmpty()) {
                return ResultVO.err(400, "请选择要删除的用户");
            }

            // 检查是否包含管理员账户
            for (Long userId : userIds) {
                User user = userService.findById(userId);
                if (user != null && "ADMIN".equals(user.getRole())) {
                    return ResultVO.err(400, "不能删除管理员账户");
                }
            }

            // 逐个删除用户
            for (Long userId : userIds) {
                userService.deleteById(userId);
            }
            
            return ResultVO.ok("批量删除成功");
        } catch (Exception e) {
            return ResultVO.err(500, "批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/detail/{userId}")
    public ResultVO getUserDetail(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResultVO.err(404, "用户不存在");
            }
            return ResultVO.ok(user);
        } catch (Exception e) {
            return ResultVO.err(500, "获取用户详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户名查找用户
     */
    @GetMapping("/findByUsername/{username}")
    public ResultVO findByUsername(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            return ResultVO.ok(user);
        } catch (Exception e) {
            return ResultVO.err(500, "查找用户失败: " + e.getMessage());
        }
    }

    /**
     * 根据邮箱查找用户
     */
    @GetMapping("/findByEmail/{email}")
    public ResultVO findByEmail(@PathVariable String email) {
        try {
            User user = userService.findByEmail(email);
            return ResultVO.ok(user);
        } catch (Exception e) {
            return ResultVO.err(500, "查找用户失败: " + e.getMessage());
        }
    }

    /**
     * 根据手机号查找用户
     */
    @GetMapping("/findByPhone/{phone}")
    public ResultVO findByPhone(@PathVariable String phone) {
        try {
            User user = userService.findByPhone(phone);
            return ResultVO.ok(user);
        } catch (Exception e) {
            return ResultVO.err(500, "查找用户失败: " + e.getMessage());
        }
    }
}
