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
 * 对应数据库表: t_attraction
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@TableName("t_attraction")
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Attraction implements Serializable {

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
	@TableField(value = "name")
	private String name;

	/** 
	 * 
	 */
	@TableField(value = "city")
	private String city;

	/** 
	 * 
	 */
	@TableField(value = "province")
	private String province;

	/** 
	 * 
	 */
	@TableField(value = "description")
	private String description;

	/** 
	 * 
	 */
	@TableField(value = "opening_hours")
	private String openingHours;

	/** 
	 * 
	 */
	@TableField(value = "address")
	private String address;

	/** 
	 * 
	 */
	@TableField(value = "rating")
	private BigDecimal rating;

	/** 
	 * 
	 */
	@TableField(value = "image_url")
	private String imageUrl;

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