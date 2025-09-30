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
import java.math.BigDecimal;
import com.entity.po.Payment;

/**
 *  查询对象
 * 
 * 对应数据库表: t_payment
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@TableName("t_payment")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PaymentQuery extends BaseQuery<Payment> {

	/** 
	 * 
	 */
	private Long id;

	/** 
	 * 
	 */
	private Long orderId;

	/** 
	 * 
	 */
	private String paymentNo;

	private String paymentNoFuzzy;

	/** 
	 * 
	 */
	private String paymentMethod;

	private String paymentMethodFuzzy;

	/** 
	 * 
	 */
	private BigDecimal amount;

	/** 
	 * 
	 */
	private String status;

	private String statusFuzzy;

	/** 
	 * 
	 */
	private Date paymentTime;

	private String paymentTimeStart;

	private String paymentTimeEnd;

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

}