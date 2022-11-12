package com.milisong.scm.autoconfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger生成api配置
 * 
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty("swagger.conf.host")
@ConfigurationProperties("swagger.conf")
@Setter
@Getter
public class Swagger2AutoConfiguration {
	private String groupName;
	private String basePackage;
	private String title;
	private String host;
	private String desc;
	private String serviceUrl;
	private String version;

    @Bean
    public Docket createRestApi() {
    	// 添加header头
    	List<Parameter> pars = new ArrayList<Parameter>() {
			private static final long serialVersionUID = 1L;
			{ add(new ParameterBuilder().name("Access-Token").description("会话Token").modelRef(new ModelRef("string")).parameterType("header").required(false).build()); }
    	};  
        
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName(groupName)
                .apiInfo(apiInfo())
                .host(host)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(desc)
                .termsOfServiceUrl(serviceUrl)
                .version(version + LocalDateTime.now())
                .build();
    }
}