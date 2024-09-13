package com.laundering.laundering_server.common.config;



import com.laundering.laundering_server.common.exception.CustomAccessDeniedHandler;
import com.laundering.laundering_server.common.exception.CustomAuthenticationEntryPoint;
import com.laundering.laundering_server.common.jwt.Jwt;
import com.laundering.laundering_server.common.jwt.JwtAuthenticationFilter;
import com.laundering.laundering_server.common.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class  SecurityConfig {

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
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(
                List.of(
                        "Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method",
                        "Access-Control-Request-Headers", "Authorization", "access_token", "refresh_token"
                ));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwt());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())

                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
//					.requestMatchers("/login/**").permitAll()
//					.requestMatchers(HttpMethod.POST, "/logout").hasAnyRole("ADMIN", "USER")
//					.requestMatchers("/member").hasAnyRole("ADMIN", "USER")
//					.requestMatchers(HttpMethod.GET, "/job").hasAnyRole("ADMIN", "USER")
//					.requestMatchers(HttpMethod.GET, "/exam").hasAnyRole("ADMIN", "USER")
//					.requestMatchers(HttpMethod.GET, "/api/v1/orders/**").permitAll()
                )
                .csrf((csrf) -> csrf.disable())
                .headers((headers) -> headers.disable())
                .formLogin((formLogin) -> formLogin.disable())
                .httpBasic((httpBasic) -> httpBasic.disable())
                .rememberMe((rememberMe) -> rememberMe.disable())
                .logout((logout) -> logout.disable())

                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler()))

                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .cors(withDefaults());

        return http.build();
    }
}


