package com.laundering.laundering_server.common.config;

import com.laundering.laundering_server.common.filter.CustomDateHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CustomDateHeaderFilter> loggingFilter() {
        FilterRegistrationBean<CustomDateHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CustomDateHeaderFilter());
        registrationBean.addUrlPatterns("/*"); // 모든 URL 패턴에 대해 필터 적용
        return registrationBean;
    }
}

