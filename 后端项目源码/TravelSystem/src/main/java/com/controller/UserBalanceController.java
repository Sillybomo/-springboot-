package com.controller;

import com.entity.vo.ResultVO;
import com.entity.po.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 用户余额控制器
 */
@RestController
@RequestMapping("/user")
public class UserBalanceController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户余额
     */
    @GetMapping("/balance/{userId}")
    public ResultVO getUserBalance(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResultVO.err(404, "用户不存在");
            }
            return ResultVO.ok(user.getBalance() != null ? user.getBalance() : 0);
        } catch (Exception e) {
            return ResultVO.err(500, "获取用户余额失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户余额
     */
    @PutMapping("/balance/{userId}")
    public ResultVO updateUserBalance(@PathVariable Long userId, @RequestBody BalanceUpdateRequest request) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResultVO.err(404, "用户不存在");
            }
            
            Integer currentBalance = user.getBalance() != null ? user.getBalance() : 0;
            Integer newBalance = currentBalance + request.getAmount();
            
            if (newBalance < 0) {
                return ResultVO.err(400, "余额不足，当前余额: " + currentBalance);
            }
            
            user.setBalance(newBalance);
            userService.updateById(user, userId);
            
            return ResultVO.ok(newBalance);
        } catch (Exception e) {
            return ResultVO.err(500, "更新用户余额失败: " + e.getMessage());
        }
    }

    /**
     * 充值余额
     */
    @PostMapping("/recharge")
    public ResultVO rechargeBalance(@RequestBody RechargeRequest request) {
        try {
            User user = userService.findById(request.getUserId());
            if (user == null) {
                return ResultVO.err(404, "用户不存在");
            }
            
            if (request.getAmount() <= 0) {
                return ResultVO.err(400, "充值金额必须大于0");
            }
            
            Integer currentBalance = user.getBalance() != null ? user.getBalance() : 0;
            Integer newBalance = currentBalance + request.getAmount();
            
            user.setBalance(newBalance);
            userService.updateById(user, request.getUserId());
            
            return ResultVO.ok(newBalance);
        } catch (Exception e) {
            return ResultVO.err(500, "充值失败: " + e.getMessage());
        }
    }

    /**
     * 余额更新请求
     */
    public static class BalanceUpdateRequest {
        private Integer amount;

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }

    /**
     * 充值请求
     */
    public static class RechargeRequest {
        private Long userId;
        private Integer amount;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }
}
