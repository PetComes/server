package com.pet.comes.config.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/*
*[참고] https://bcp0109.tistory.com/326
* Docket: Swagger 설정의 핵심이 되는 Bean
* useDefaultResponseMessages: Swagger 에서 제공해주는 기본 응답 코드 (200, 401, 403, 404). false 로 설정하면 기본 응답 코드를 노출하지 않음
* apis: api 스펙이 작성되어 있는 패키지 (Controller) 를 지정
* paths: apis 에 있는 API 중 특정 path 를 선택
* apiInfo:Swagger UI 로 노출할 정보*/
@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket api() { // Swagger 설정
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.pet.comes.config.swagger"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Practice Swagger")
                .description("practice swagger config--Tony")
                .version("3.0")
                .build();
    }
}
