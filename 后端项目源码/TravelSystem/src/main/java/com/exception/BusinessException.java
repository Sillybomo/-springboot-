package com.exception;

/**
 * @Author: Hendrix Xie
 * @CreateTime: 2025-03-23 18:05:52
 * @Description: 用于表示用户操作不当、业务规则违反等可预期的异常场景
 * @Version: 1.0
 */

public class BusinessException extends RuntimeException {
    // 显式声明序列化版本号
    private static final long serialVersionUID = 20240325L;

    /**
     * 创建包含错误信息的业务异常
     * @param message 业务异常描述信息（用户可见提示）
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * 创建包含错误信息和根本原因的异常
     * @param message 业务异常描述信息（用户可见提示）
     * @param cause 原始异常（用于异常链追踪）
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 快速创建业务异常的静态工厂方法
     * @param message 异常描述信息
     * @return 封装好的业务异常对象
     */
    public static BusinessException of(String message) {
        return new BusinessException(message);
    }
}
