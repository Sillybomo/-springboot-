package com.config;

/**
 * @Author: 薄墨
 * @Description: TODO
 * @DateTime: 2025/9/16 9:47
 **/

import lombok.Data;

/**
 * 泛型版本的统一响应结果类
 * @param <T> 数据类型
 */
@Data
public class AjaxResult<T> {
    /** 操作是否成功 */
    private boolean success;

    /** 返回消息 */
    private String msg;

    /** 数据对象 */
    private T data;

    /** 状态码 */
    private int code;

    // 构造方法
    public AjaxResult() {
    }

    public AjaxResult(boolean success, String msg, T data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
        // 根据成功状态设置默认状态码
        this.code = success ? 200 : 500;
    }

    public AjaxResult(boolean success, String msg, T data, int code) {
        this.success = success;
        this.msg = msg;
        this.data = data;
        this.code = code;
    }

    // 静态工具方法，方便快速创建实例
    public static <T> AjaxResult<T> success() {
        return new AjaxResult<>(true, "操作成功", null);
    }

    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<>(true, "操作成功", data);
    }

    public static <T> AjaxResult<T> success(String msg, T data) {
        return new AjaxResult<>(true, msg, data);
    }

    public static <T> AjaxResult<T> error() {
        return new AjaxResult<>(false, "操作失败", null);
    }

    public static <T> AjaxResult<T> error(String msg) {
        return new AjaxResult<>(false, msg, null);
    }

    public static <T> AjaxResult<T> error(String msg, int code) {
        return new AjaxResult<>(false, msg, null, code);
    }
}

