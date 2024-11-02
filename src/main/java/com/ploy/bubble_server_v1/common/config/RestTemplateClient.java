package com.ploy.bubble_server_v1.common.config;

import org.springframework.boot.web.client.RestTemplateBuilder;  // RestTemplate을 빌드하는 데 사용되는 빌더 클래스
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateClient {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // RestTemplateBuilder를 사용하여 RestTemplate 객체를 생성하고 반환
        // RestTemplateBuilder는 RestTemplate의 설정을 쉽게 구성할 수 있도록 도와줌
        return builder.build();
    }
}
