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
import com.entity.po.Review;

/**
 *  查询对象
 * 
 * 对应数据库表: t_review
 * @author Bomo
 * @date 2025-09-17 13:17:05
 */
@TableName("t_review")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ReviewQuery extends BaseQuery<Review> {

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
	private Long userId;

	/** 
	 * 
	 */
	private Long orderId;

	/** 
	 * 
	 */
	private Integer rating;

	/** 
	 * 
	 */
	private String comment;

	private String commentFuzzy;

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