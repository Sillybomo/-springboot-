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
import com.entity.po.Ticket;

/**
 *  查询对象
 * 
 * 对应数据库表: t_ticket
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@TableName("t_ticket")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TicketQuery extends BaseQuery<Ticket> {

	/** 
	 * 
	 */
	private Long id;

	/** 
	 * 
	 */
	private Long attractionId;

	/** 
	 * 
	 */
	private String ticketType;

	private String ticketTypeFuzzy;

	/** 
	 * 
	 */
	private BigDecimal price;

	/** 
	 * 
	 */
	private BigDecimal discountPrice;

	/** 
	 * 
	 */
	private Integer validDays;

	/** 
	 * 
	 */
	private String description;

	private String descriptionFuzzy;

	/** 
	 * 
	 */
	private Integer stock;

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