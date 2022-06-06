package ma.cam.kernal.config;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	public static final String AUTHORIZATION_HEADER = "Authorization";

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.forCodeGeneration(true)
	            .ignoredParameterTypes(java.sql.Date.class)
	            .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
	            .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
	            .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
	            .securityContexts(Lists.newArrayList(securityContext()))
	            .securitySchemes(Lists.newArrayList(apiKey()))
	            .useDefaultResponseMessages(false)
	            .apiInfo(apiInfo())
				.select()
				.apis( RequestHandlerSelectors.basePackage( "ma.cam" ))
//				.apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
	}

	private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.any())
            .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
            new SecurityReference("JWT", authorizationScopes));
    }
    
    private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Documentation ws-workflow")
				.description("Documentation technique des web services de l'api workflow")
				.termsOfServiceUrl("https://www.creditagricole.ma/fr")
				.license("workflow License")
				.licenseUrl("workflow@creditagricole.ma").version("1.0").build();
	}

}