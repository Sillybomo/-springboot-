package com.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serial;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *  实体类
 * 
 * 对应数据库表: t_user
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@TableName("t_user")
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/** 
	 * 
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/** 
	 * 
	 */
	@TableField(value = "username")
	private String username;

	/** 
	 * 
	 */
	@TableField(value = "password")
	private String password;

	/** 
	 * 
	 */
	@TableField(value = "name")
	private String name;

	/** 
	 * 
	 */
	@TableField(value = "email")
	private String email;

	/** 
	 * 
	 */
	@TableField(value = "phone")
	private String phone;

	/** 
	 * 
	 */
	@TableField(value = "address")
	private String address;

	/** 
	 * 
	 */
	@TableField(value = "role")
	private String role;

	/** 
	 * 
	 */
	@TableField(value = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdAt;

	/** 
	 * 
	 */
	@TableField(value = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedAt;

	/** 
	 * 用户余额
	 */
	@TableField(value = "balance")
	private Integer balance;

	/** 
	 * 用户头像
	 */
	@TableField(value = "avatar")
	private String avatar;

}