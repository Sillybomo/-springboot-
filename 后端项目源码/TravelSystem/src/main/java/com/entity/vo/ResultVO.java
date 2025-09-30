package com.entity.vo;

import com.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * @Author: Hendrix Xie
 * @CreateTime: 2025-03-23 17:42:51
 * @Description: 响应结果封装类
 * @Version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResultVO {

    //成员属性
    private int code;//状态码

    private boolean success;//成功响应为true,失败响应为false

    private String message;//响应信息

    private Object data;//响应数据

    //成功响应的方法 -- 返回的Result中只封装了成功状态码
    public static ResultVO ok(){
        return new ResultVO(StatusCode.OK.getCode(), true,null, null);
    }
    //成功响应的方法 -- 返回的Result中封装了成功状态码和响应信息
    public static ResultVO ok(String message){
        return new ResultVO(StatusCode.OK.getCode(),true,message, null);
    }
    //成功响应的方法 -- 返回的Result中封装了成功状态码和响应数据
    public static ResultVO ok(Object data){
        return new ResultVO(StatusCode.OK.getCode(),true,null, data);
    }
    //成功响应的方法 -- 返回的Result中封装了成功状态码和响应信息和响应数据
    public static ResultVO ok(String message, Object data){
        return new ResultVO(StatusCode.OK.getCode(),true,message, data);
    }
    //失败响应的方法 -- 返回的Result中封装了失败状态码和响应信息
    public static ResultVO err(int errCode, String message){
        return new ResultVO(errCode,false, message, null);
    }
    //失败响应的方法 -- 返回的Result中封装了失败状态码和响应信息和响应数据
    public static ResultVO err(int errCode,String message,Object data){
        return new ResultVO(errCode,false,message, data);
    }
}
