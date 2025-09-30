package com.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: Hendrix Xie
 * @CreateTime: 2025-03-23 17:47:09
 * @Description: 存储用户登录信息的User类
 * @Version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginUserVO {

    private String userCode;//用户名

    private String userPwd;//密码

    private String userState;//用户状态

    private String verificationCode;//验证码
}
