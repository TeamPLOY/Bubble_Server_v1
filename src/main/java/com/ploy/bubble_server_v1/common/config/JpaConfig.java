package com.ploy.bubble_server_v1.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  // JPA Auditing => 엔티티 생성 및 수정 시 자동으로 시간 정보를 관리
public class JpaConfig {
}
