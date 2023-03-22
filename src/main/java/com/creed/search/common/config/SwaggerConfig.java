package com.creed.search.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info()
                .title("블로그 검색 API")
                .version(appVersion)
                .description("블로그 키워드 검색 API 명세서입니다.");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
