package com.laundering.laundering_server.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@WebFilter(urlPatterns = "/*")
public class CustomDateHeaderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 날짜 포맷 설정 (영어로 설정)
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));  // 타임존은 Asia/Seoul로 유지

        // 현재 시간 설정
        String formattedDate = sdf.format(new Date());

        // 응답 헤더에 날짜 추가 (Asia/Seoul 시간)
        HttpServletResponse httpResponse = (HttpServletResponse) response;  // jakarta.servlet.http.HttpServletResponse 사용
        httpResponse.setHeader("Date", formattedDate);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
