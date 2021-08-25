package ru.solyanin.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@EnableOpenApi
@EnableSwagger2
@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        Docket result = new Docket(DocumentationType.OAS_30)
                .apiInfo(new ApiInfoBuilder()
                        .title("SwaggerService")
                        .description("Общий сваггер для сервисов")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
        return result;
    }

    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider(
            @Value("#{'${applications.api.names}'.split(',')}") List<String> apiNames) {
        return () -> {
            List<SwaggerResource> resources = new ArrayList<>();
            for (String apiName : apiNames) {
                SwaggerResource wsResource = new SwaggerResource();
                wsResource.setName(apiName);
                wsResource.setSwaggerVersion("3.0");
                wsResource.setLocation(String.format("/%s/v3/api-docs", apiName));
                resources.add(wsResource);
            }
            return resources;
        };
    }
}
