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
import com.entity.po.Cart;

/**
 * 购物车表：存储用户待购买的门票信息 查询对象
 * 
 * 对应数据库表: t_cart
 * @author Bomo
 * @date 2025-09-19 14:19:41
 */
@TableName("t_cart")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CartQuery extends BaseQuery<Cart> {

	/** 
	 * 
	 */
	private Long id;

	/** 
	 * 用户ID，关联t_user.id
	 */
	private Long userId;

	/** 
	 * 门票ID，关联t_ticket.id（间接关联t_attraction）
	 */
	private Long ticketId;

	/** 
	 * 购票数量（至少1张）
	 */
	private Integer quantity;

	/** 
	 * 加入购物车时间
	 */
	private Date createdAt;

	private String createdAtStart;

	private String createdAtEnd;

	/** 
	 * 更新时间（如修改数量）
	 */
	private Date updatedAt;

	private String updatedAtStart;

	private String updatedAtEnd;

	/** 
	 * 是否需要多表联查（包含景点和门票信息）
	 */
	private Boolean needJoin;

	/** 
	 * 景点名称（用于搜索）
	 */
	private String attractionName;

}