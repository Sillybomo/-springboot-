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
 * 购物车表：存储用户待购买的门票信息 实体类
 * 
 * 对应数据库表: t_cart
 * @author Bomo
 * @date 2025-09-19 14:19:41
 */
@TableName("t_cart")
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cart implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/** 
	 * 
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/** 
	 * 用户ID，关联t_user.id
	 */
	@TableField(value = "user_id")
	private Long userId;

	/** 
	 * 门票ID，关联t_ticket.id（间接关联t_attraction）
	 */
	@TableField(value = "ticket_id")
	private Long ticketId;

	/** 
	 * 购票数量（至少1张）
	 */
	@TableField(value = "quantity")
	private Integer quantity;

	/** 
	 * 加入购物车时间
	 */
	@TableField(value = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdAt;

	/** 
	 * 更新时间（如修改数量）
	 */
	@TableField(value = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedAt;

}