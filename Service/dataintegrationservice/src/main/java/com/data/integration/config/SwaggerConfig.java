package com.data.integration.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This is configuration class for generating REST API documentation
 * @author Aniket
 *
 */
@Configuration
@EnableSwagger2 
public class SwaggerConfig {

	@Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Data Integration Service")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/Integration.*"))
                .build();
    }
     
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Data Integration API")
                .description("Data Integration API")
                .contact("name of contact")
                .license("License Version 2.0")
                .licenseUrl("License url")
                .version("1.0")
                .build();
    }
    

    
}
