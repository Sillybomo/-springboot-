package com.controller;

import com.entity.vo.ResultVO;
import com.entity.po.Order;
import com.entity.po.User;
import com.service.OrderService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 退票控制器
 */
@RestController
@RequestMapping("/order")
public class RefundController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * 申请退票
     */
    @PostMapping("/refund")
    public ResultVO applyRefund(@RequestBody RefundRequest request) {
        try {
            Order order = orderService.findById(request.getOrderId());
            if (order == null) {
                return ResultVO.err(404, "订单不存在");
            }

            // 检查订单状态
            if (!"PAID".equals(order.getStatus())) {
                return ResultVO.err(400, "只有已支付的订单才能申请退票");
            }

            // 更新订单状态为退票中
            order.setStatus("REFUND");
            order.setRemark(order.getRemark() + " | 退票原因: " + request.getReason());
            orderService.updateById(order, order.getId());

            return ResultVO.ok("退票申请已提交，等待管理员审核");
        } catch (Exception e) {
            return ResultVO.err(500, "申请退票失败: " + e.getMessage());
        }
    }

    /**
     * 处理退票申请（管理员）
     */
    @PutMapping("/refund/{orderId}")
    public ResultVO processRefund(@PathVariable Long orderId, @RequestBody ProcessRefundRequest request) {
        try {
            Order order = orderService.findById(orderId);
            if (order == null) {
                return ResultVO.err(404, "订单不存在");
            }

            if (!"REFUND".equals(order.getStatus())) {
                return ResultVO.err(400, "订单状态不是退票中");
            }

            if ("APPROVED".equals(request.getStatus())) {
                // 审核通过，执行退款
                User user = userService.findById(order.getUserId());
                if (user != null) {
                    Integer currentBalance = user.getBalance() != null ? user.getBalance() : 0;
                    Integer refundAmount = order.getTotalAmount().intValue();
                    Integer newBalance = currentBalance + refundAmount;
                    
                    user.setBalance(newBalance);
                    userService.updateById(user, user.getId());
                    
                    // 记录退款日志
                    System.out.println("退票成功 - 订单ID: " + orderId + 
                                     ", 用户ID: " + order.getUserId() + 
                                     ", 退款金额: " + refundAmount + 
                                     ", 原余额: " + currentBalance + 
                                     ", 新余额: " + newBalance);
                } else {
                    return ResultVO.err(404, "用户不存在，无法退款");
                }

                // 更新订单状态为已取消
                order.setStatus("CANCELLED");
                order.setRemark(order.getRemark() + " | 退票审核通过，已退款: " + request.getRemark());
            } else if ("REJECTED".equals(request.getStatus())) {
                // 审核拒绝
                order.setStatus("REFUND_REJECTED");
                order.setRemark(order.getRemark() + " | 退票审核拒绝: " + request.getRemark());
            }

            orderService.updateById(order, order.getId());

            return ResultVO.ok("退票申请处理完成");
        } catch (Exception e) {
            return ResultVO.err(500, "处理退票申请失败: " + e.getMessage());
        }
    }

    /**
     * 获取退票申请列表
     */
    @PostMapping("/refund/list")
    public ResultVO getRefundList(@RequestBody RefundQueryRequest request) {
        try {
            // 这里可以添加更复杂的查询逻辑
            // 暂时返回简单的成功响应
            return ResultVO.ok("退票申请列表功能开发中");
        } catch (Exception e) {
            return ResultVO.err(500, "获取退票申请列表失败: " + e.getMessage());
        }
    }

    /**
     * 退票申请请求
     */
    public static class RefundRequest {
        private Long orderId;
        private String reason;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    /**
     * 处理退票申请请求
     */
    public static class ProcessRefundRequest {
        private String status; // APPROVED, REJECTED
        private String remark;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    /**
     * 退票查询请求
     */
    public static class RefundQueryRequest {
        private Long userId;
        private String status;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
