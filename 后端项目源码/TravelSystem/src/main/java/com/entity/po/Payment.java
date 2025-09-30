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
 * 对应数据库表: t_payment
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@TableName("t_payment")
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment implements Serializable {

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
	@TableField(value = "order_id")
	private Long orderId;

	/** 
	 * 
	 */
	@TableField(value = "payment_no")
	private String paymentNo;

	/** 
	 * 
	 */
	@TableField(value = "payment_method")
	private String paymentMethod;

	/** 
	 * 
	 */
	@TableField(value = "amount")
	private BigDecimal amount;

	/** 
	 * 
	 */
	@TableField(value = "status")
	private String status;

	/** 
	 * 
	 */
	@TableField(value = "payment_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date paymentTime;

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

}