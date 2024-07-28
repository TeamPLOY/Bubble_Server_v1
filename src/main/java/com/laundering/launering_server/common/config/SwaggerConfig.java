package com.laundering.launering_server.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // OpenAPI 객체를 생성하고 설정합니다.
        return new OpenAPI()
                .info(new Info().title("LAUNDERING"))  // API의 기본 정보를 설정합니다.

                .addSecurityItem(new SecurityRequirement().addList("jwtAuth"))  // 보안 요구 사항을 추가합니다.
                .components(new Components()
                        .addSecuritySchemes("jwtAuth", new io.swagger.v3.oas.models.security.SecurityScheme()
                                .name("access_token")  // 보안 스키마의 이름을 설정합니다.
                                .type(SecurityScheme.Type.APIKEY)  // 보안 스키마의 유형을 API 키로 설정합니다.
                                .in(SecurityScheme.In.HEADER)));  // API 키를 HTTP 헤더에 포함시킵니다.
    }
}