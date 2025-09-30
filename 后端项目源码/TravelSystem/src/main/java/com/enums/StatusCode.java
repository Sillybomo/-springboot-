package com.enums;

/**
 * @Author: Hendrix Xie
 * @CreateTime: 2025-03-23 18:20:08
 * @Description: 统状态码枚举（符合HTTP语义规范）
 * @Version: 1.0
 */
public enum StatusCode {
    // 成功状态码
    OK(200, "成功"),
    CREATED(201, "资源创建成功"),
    ACCEPTED(202, "请求已接受处理"),
    NO_CONTENT(204, "无内容返回"),

    // 客户端错误
    BAD_REQUEST(400, "参数错误"),
    UNAUTHORIZED(401, "用户未登录"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "未找到"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    REQUEST_TIMEOUT(408, "请求超时"),
    CONFLICT(409, "数据冲突"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    VALIDATION_FAILED(422, "数据校验失败"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    OPERATION_LIMITED(430, "操作次数超限"),

    // 服务端错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    NOT_IMPLEMENTED(501, "功能未实现"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    DATABASE_CONNECTION_FAILED(521, "数据库连接失败"),

    // 扩展业务状态
    EXPIRED_TOKEN(498, "令牌已过期"),
    THIRD_PARTY_ERROR(520, "第三方服务异常");


    private final int code;
    private final String description;

    StatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据状态码查找对应枚举（线程安全）
     * @param code 状态码
     * @return 匹配的枚举实例，未找到返回null
     */
    public static StatusCode fromCode(int code) {
        for (StatusCode status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
