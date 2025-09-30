package com.entity.query;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.*;
import lombok.experimental.Accessors;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.entity.po.User;

/**
 *  查询对象
 * 
 * 对应数据库表: t_user
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@TableName("t_user")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserQuery extends BaseQuery<User> {

	/** 
	 * 
	 */
	private Long id;


	/** 
	 * 
	 */
	private String username;

	private String usernameFuzzy;

    private String avatar;

	/** 
	 * 
	 */
	private String password;

	private String passwordFuzzy;

	/** 
	 * 
	 */
	private String name;

	private String nameFuzzy;

	/** 
	 * 
	 */
	private String email;

	private String emailFuzzy;

	/** 
	 * 
	 */
	private String phone;

	private String phoneFuzzy;

	/** 
	 * 
	 */
	private String address;

	private String addressFuzzy;

	/** 
	 * 
	 */
	private String role;

	private String roleFuzzy;

	/** 
	 * 
	 */
	private Date createdAt;

	private String createdAtStart;

	private String createdAtEnd;

	/** 
	 * 
	 */
	private Date updatedAt;

	private String updatedAtStart;

	private String updatedAtEnd;

	/** 
	 * 用户余额
	 */
	private Integer balance;

}