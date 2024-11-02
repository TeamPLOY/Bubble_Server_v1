package com.ploy.bubble_server_v1.common.config;

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
                .info(new Info().title("BUBBLE"))  // API의 기본 정보를 설정합니다.

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))  // 보안 요구 사항을 추가합니다.
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)  // 보안 스키마의 유형을 HTTP로 설정합니다.
                                .scheme("bearer")  // Bearer 토큰 인증 방식으로 설정합니다.
                                .bearerFormat("JWT")  // 토큰의 형식을 JWT로 설정합니다.
                                .in(SecurityScheme.In.HEADER)  // 토큰이 HTTP 헤더에 포함됨을 나타냅니다.
                                .name("Authorization")));  // Bearer 토큰을 보낼 헤더의 이름을 설정합니다.
    }
}
