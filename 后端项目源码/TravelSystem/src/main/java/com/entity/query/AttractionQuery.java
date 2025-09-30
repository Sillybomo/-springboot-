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
import com.entity.po.Attraction;

/**
 *  查询对象
 * 
 * 对应数据库表: t_attraction
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@TableName("t_attraction")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class AttractionQuery extends BaseQuery<Attraction> {

	/** 
	 * 
	 */
	private Long id;

	/** 
	 * 
	 */
	private String name;

	private String nameFuzzy;

	/** 
	 * 
	 */
	private String city;

	private String cityFuzzy;

	/** 
	 * 
	 */
	private String province;

	private String provinceFuzzy;

	/** 
	 * 
	 */
	private String description;

	private String descriptionFuzzy;

	/** 
	 * 
	 */
	private String openingHours;

	private String openingHoursFuzzy;

	/** 
	 * 
	 */
	private String address;

	private String addressFuzzy;

	/** 
	 * 
	 */
	private BigDecimal rating;

	/** 
	 * 
	 */
	private String imageUrl;

	private String imageUrlFuzzy;

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