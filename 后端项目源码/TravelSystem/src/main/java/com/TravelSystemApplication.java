package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Bomo
 * @CreateTime: 2025-09-17 08:52:36
 * @Description: Spring Boot启动类
 * @Version: 1.0.0
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.mapper"})
public class TravelSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(TravelSystemApplication.class, args);
	}
}
