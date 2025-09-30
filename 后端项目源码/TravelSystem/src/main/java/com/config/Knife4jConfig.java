package com.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j + SpringDoc 接口文档配置类
 * 文件名：Knife4jConfig.java（必须与公共类名一致）
 */
@Configuration
public class Knife4jConfig { // 公共类名与文件名一致：Knife4jConfig

    /**
     * 配置文档全局信息（标题、版本、作者等）
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("旅游系统API文档")
                        .description("包含用户、景点、订单、支付、收藏、评论等模块接口")
                        .version("v1.0.0")
                        // 作者信息
                        .contact(new Contact()
                                .name("薄墨")
                                .email("bomo@example.com")
                                .url("https://example.com"))
                        // 许可证信息
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }

    /**
     * 1. 用户模块接口分组
     */
    @Bean
    public GroupedOpenApi userApiGroup() {
        return GroupedOpenApi.builder()
                .group("1. 用户模块")
                .pathsToMatch("/user/**") // 匹配所有/user开头的接口
                .build();
    }

    /**
     * 2. 景点模块接口分组
     */
    @Bean
    public GroupedOpenApi attractionApiGroup() {
        return GroupedOpenApi.builder()
                .group("2. 景点模块")
                .pathsToMatch("/attraction/**")
                .build();
    }

    /**
     * 3. 订单与支付模块接口分组
     */
    @Bean
    public GroupedOpenApi orderPaymentApiGroup() {
        return GroupedOpenApi.builder()
                .group("3. 订单与支付模块")
                .pathsToMatch("/order/**", "/payment/**") // 多路径匹配
                .build();
    }

    /**
     * 4. 收藏与评论模块接口分组
     */
    @Bean
    public GroupedOpenApi favoriteReviewApiGroup() {
        return GroupedOpenApi.builder()
                .group("4. 收藏与评论模块")
                .pathsToMatch("/favorite/**", "/review/**")
                .build();
    }
}