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
import java.math.BigDecimal;

/**
 *  实体类
 * 
 * 对应数据库表: t_order
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@TableName("t_order")
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;


	/** 
	 * 
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;


    private String username;
	/** 
	 * 
	 */
	@TableField(value = "order_no")
	private String orderNo;

	/** 
	 * 
	 */
	@TableField(value = "user_id")
	private Long userId;

	/** 
	 * 
	 */
	@TableField(value = "ticket_id")
	private Long ticketId;

	/** 
	 * 
	 */
	@TableField(value = "quantity")
	private Integer quantity;

	/** 
	 * 
	 */
	@TableField(value = "total_amount")
	private BigDecimal totalAmount;

	/** 
	 * 
	 */
	@TableField(value = "status")
	private String status;

	/** 
	 * 
	 */
	@TableField(value = "visit_date")
	@JsonFormat(pattern = "yyyy-MM-dd" ,timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date visitDate;

	/** 
	 * 
	 */
	@TableField(value = "contact_name")
	private String contactName;

	/** 
	 * 
	 */
	@TableField(value = "contact_phone")
	private String contactPhone;

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
     * 订单备注
     */
    @TableField(value = "remark")
    private String remark;

}