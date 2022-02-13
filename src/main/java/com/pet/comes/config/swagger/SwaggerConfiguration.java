package com.pet.comes.config.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
*[참고] https://bcp0109.tistory.com/326
* Docket: Swagger 설정의 핵심이 되는 Bean
* useDefaultResponseMessages: Swagger 에서 제공해주는 기본 응답 코드 (200, 401, 403, 404). false 로 설정하면 기본 응답 코드를 노출하지 않음
* apis: api 스펙이 작성되어 있는 패키지 (Controller) 를 지정
* paths: apis 에 있는 API 중 특정 path 를 선택
* apiInfo:Swagger UI 로 노출할 정보*/
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    /*===================== home 화면 ====================== */
    @Bean
    public Docket home() { // Swagger 설정
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("AA-Petcomes API Swagger")
                .useDefaultResponseMessages(false)
                .select()
//                .apis(RequestHandlerSelectors.any()) // Swagger가 어디를 기준으로 RestController를 스캔할지 지정.
                .apis(RequestHandlerSelectors.basePackage("com.pet.comes")) // java 밑에 base package를 주로함.
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Petcomes APIs")
                .description("Spring boot Server")
                .version("1.0")
                .build();
    }

    /*===================== certification ====================== */
    @Bean
    public Docket certificationApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Certification Group")
                .useDefaultResponseMessages(false)
                .select()
//                .apis(RequestHandlerSelectors.any()) // Swagger가 어디를 기준으로 RestController를 스캔할지 지정.
                .apis(RequestHandlerSelectors.basePackage("com.pet.comes.controller.certification")) // java 밑에 base package를 주로함.
                .paths(PathSelectors.any())
                .build()
                .apiInfo(certificationApiInfo());

    }

    private ApiInfo certificationApiInfo() {
        return new ApiInfoBuilder()
                .title("Certification APIs")
                .description("Account, Outh, SignController")
                .version("1.0")
                .build();
    }

    /*===================== comment ====================== */
    @Bean
    public Docket commentApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Comment Group")
                .useDefaultResponseMessages(false)
                .select()
//                .apis(RequestHandlerSelectors.any()) // Swagger가 어디를 기준으로 RestController를 스캔할지 지정.
                .apis(RequestHandlerSelectors.basePackage("com.pet.comes.controller.comment")) // java 밑에 base package를 주로함.
                .paths(PathSelectors.any())
                .build()
                .apiInfo(commentApiInfo());

    }

    private ApiInfo commentApiInfo() {
        return new ApiInfoBuilder()
                .title("Comment APIs")
                .description("CommentController")
                .version("1.0")
                .build();
    }

    /*===================== diary ====================== */
    @Bean
    public Docket diaryApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Diary Group")
                .useDefaultResponseMessages(false)
                .select()
//                .apis(RequestHandlerSelectors.any()) // Swagger가 어디를 기준으로 RestController를 스캔할지 지정.
                .apis(RequestHandlerSelectors.basePackage("com.pet.comes.controller.diary")) // java 밑에 base package를 주로함.
                .paths(PathSelectors.any())
                .build()
                .apiInfo(diaryApiInfo());

    }

    private ApiInfo diaryApiInfo() {
        return new ApiInfoBuilder()
                .title("Diary APIs")
                .description("DiaryController")
                .version("1.0")
                .build();
    }

    /*===================== dog ====================== */
    @Bean
    public Docket dogApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Dog Group")
                .useDefaultResponseMessages(false)
                .select()
//                .apis(RequestHandlerSelectors.any()) // Swagger가 어디를 기준으로 RestController를 스캔할지 지정.
                .apis(RequestHandlerSelectors.basePackage("com.pet.comes.controller.dog")) // java 밑에 base package를 주로함.
                .paths(PathSelectors.any())
                .build()
                .apiInfo(dogApiInfo());

    }

    private ApiInfo dogApiInfo() {
        return new ApiInfoBuilder()
                .title("Dog APIs")
                .description("DogController")
                .version("1.0")
                .build();
    }

    /*===================== icon ====================== */
    @Bean
    public Docket iconApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Icon Group")
                .useDefaultResponseMessages(false)
                .select()
//                .apis(RequestHandlerSelectors.any()) // Swagger가 어디를 기준으로 RestController를 스캔할지 지정.
                .apis(RequestHandlerSelectors.basePackage("com.pet.comes.controller.icon")) // java 밑에 base package를 주로함.
                .paths(PathSelectors.any())
                .build()
                .apiInfo(iconApiInfo());

    }

    private ApiInfo iconApiInfo() {
        return new ApiInfoBuilder()
                .title("Icon APIs")
                .description("IconController")
                .version("1.0")
                .build();
    }

    /*===================== s3 ====================== */
    @Bean
    public Docket s3Apis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("AWS-S3 Group")
                .useDefaultResponseMessages(false)
                .select()
//                .apis(RequestHandlerSelectors.any()) // Swagger가 어디를 기준으로 RestController를 스캔할지 지정.
                .apis(RequestHandlerSelectors.basePackage("com.pet.comes.controller.s3")) // java 밑에 base package를 주로함.
                .paths(PathSelectors.any())
                .build()
                .apiInfo(s3ApiInfo());

    }

    private ApiInfo s3ApiInfo() {
        return new ApiInfoBuilder()
                .title("S3 APIs")
                .description("FileUploadController")
                .version("1.0")
                .build();
    }

    /*===================== social ====================== */
    @Bean
    public Docket socialApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Social Group")
                .useDefaultResponseMessages(false)
                .select()
//                .apis(RequestHandlerSelectors.any()) // Swagger가 어디를 기준으로 RestController를 스캔할지 지정.
                .apis(RequestHandlerSelectors.basePackage("com.pet.comes.controller.social")) // java 밑에 base package를 주로함.
                .paths(PathSelectors.any())
                .build()
                .apiInfo(socialApiInfo());

    }

    private ApiInfo socialApiInfo() {
        return new ApiInfoBuilder()
                .title("Social APIs")
                .description("SocialController")
                .version("1.0")
                .build();
    }

    /*===================== user ====================== */
    @Bean
    public Docket userApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("User Group")
                .useDefaultResponseMessages(false)
                .select()
//                .apis(RequestHandlerSelectors.any()) // Swagger가 어디를 기준으로 RestController를 스캔할지 지정.
                .apis(RequestHandlerSelectors.basePackage("com.pet.comes.controller.user")) // java 밑에 base package를 주로함.
                .paths(PathSelectors.any())
                .build()
                .apiInfo(userApiInfo());

    }

    private ApiInfo userApiInfo() {
        return new ApiInfoBuilder()
                .title("User APIs")
                .description("UserController")
                .version("1.0")
                .build();
    }




}
