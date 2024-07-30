package com.laundering.laundering_server.common.config;


import com.laundering.laundering_server.common.exception.CustomAccessDeniedHandler;
import com.laundering.laundering_server.common.exception.CustomAuthenticationEntryPoint;
import com.laundering.laundering_server.common.filter.CustomAuthenticationFilter;
import com.laundering.laundering_server.common.jwt.Jwt;
import com.laundering.laundering_server.common.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor  // 생성자 자동생성
@EnableMethodSecurity(securedEnabled = true)  // 메서드 보안 활성화
public class SecurityConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public Jwt jwt() {
        return new Jwt(
                jwtProperties.getClientSecret(),
                jwtProperties.getIssuer(),
                jwtProperties.getTokenExpire(),
                jwtProperties.getRefreshTokenExpire()
        );
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // CORS 설정을 정의합니다.
        CorsConfiguration configuration = new CorsConfiguration();

        // 모든 출처를 허용합니다.
        configuration.setAllowedOrigins(List.of("*"));

        // 특정 HTTP 메소드만 허용합니다.
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "PATCH", "DELETE"));

        // CORS 요청에서 허용할 헤더를 설정합니다.
        configuration.setAllowedHeaders(
                List.of(
                        "Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method",
                        "Access-Control-Request-Headers", "Authorization", "access_token", "refresh_token"
                ));

        //위의 설정을 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        // 접근 거부 오류 처리
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        // 인증 오류 처리
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public CustomAuthenticationFilter CustomAuthenticationFilter() {
        return new CustomAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)  // CSRF 보호를 비활성화합니다.

                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/**").permitAll()  // 모든 엔드포인트에 대해 모든 요청을 허용합니다.
                        .anyRequest().authenticated()  // 나머지 모든 요청은 인증이 필요합니다.
                )
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 비활성화 (중복 호출)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 상태 비유지 세션 사용

                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint())  // 인증 실패 시 사용자 정의 엔트리 포인트 사용
                        .accessDeniedHandler(accessDeniedHandler()))  // 접근 거부 시 사용자 정의 핸들러 사용

                .addFilterBefore(CustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .cors(withDefaults());  // 기본 설정으로 CORS를 활성화합니다.

        return http.build();  // 설정을 적용하여 SecurityFilterChain을 빌드합니다.
    }
}

