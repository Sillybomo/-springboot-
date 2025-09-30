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
import com.entity.po.Order;

/**
 *  查询对象
 * 
 * 对应数据库表: t_order
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@TableName("t_order")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class OrderQuery extends BaseQuery<Order> {

	/** 
	 * 
	 */
	private Long id;

	/** 
	 * 
	 */
	private String orderNo;

	private String orderNoFuzzy;

    private String userIdFuzzy;

    private String statusFuzzy;

    private String userNameFuzzy;



	/** 
	 * 
	 */
	private Long userId;

	/** 
	 * 
	 */
	private Long ticketId;

	/** 
	 * 
	 */
	private Integer quantity;

	/** 
	 * 
	 */
	private BigDecimal totalAmount;

	/** 
	 * 
	 */
	private String status;


	/** 
	 * 
	 */
	private Date visitDate;

	private String visitDateStart;

	private String visitDateEnd;

	/** 
	 * 
	 */
	private String contactName;

	private String contactNameFuzzy;

	/** 
	 * 
	 */
	private String contactPhone;

	private String contactPhoneFuzzy;

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
     * 订单备注
     */
    private String remark;

    private String remarkFuzzy;

    /**
     * 是否需要多表联查
     */
    private Boolean needJoin;

    /**
     * 开始时间（用于时间范围查询）
     */
    private String startTime;

    /**
     * 结束时间（用于时间范围查询）
     */
    private String endTime;

}