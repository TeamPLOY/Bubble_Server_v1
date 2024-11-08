package com.ploy.bubble_server_v1.common.jwt;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String issuer;
    private String clientSecret;
    private int tokenExpire;
    private int refreshTokenExpire;
}

