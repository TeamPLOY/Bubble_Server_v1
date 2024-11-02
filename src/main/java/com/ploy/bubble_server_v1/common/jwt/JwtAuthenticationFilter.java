package com.ploy.bubble_server_v1.common.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final Jwt jwt;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("getAuthentication is null");
            String token = getAccessToken(httpServletRequest);

            log.info("토큰 : " + token);

            if (token != null) {
                try {
                    Jwt.Claims claims = verify(token);
                    Long memberId = claims.getMemberId();

                    log.info("memberId : " + memberId);

                    if (memberId != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(memberId, null);
                        log.info("authentication : " + authentication.toString());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    log.warn("Jwt 처리중 문제가 발생하였습니다 : {}", e.getMessage());
                }
            }
        } else {
            log.debug("이미 인증 객체가 존재합니다 : {}",
                    SecurityContextHolder.getContext().getAuthentication());
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getAccessToken(HttpServletRequest request) {
        log.warn("리퀘스트 로그 시작");

        log.warn("Method: " + request.getMethod());
        log.warn("Request URI: " + request.getRequestURI());
        log.warn("Query String: " + request.getQueryString());

        log.warn("Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("헤더: " + headerName + " 값: " + request.getHeader(headerName));
        }

        String authorizationHeader = request.getHeader("Authorization"); // Authorization 헤더에서 토큰을 가져옴
        log.info("Raw Authorization Header : " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("Authorization 헤더에 Bearer 토큰 존재");
            String token = authorizationHeader.substring(7); // 'Bearer ' 이후의 토큰만 추출
            try {
                String decodedToken = URLDecoder.decode(token, StandardCharsets.UTF_8);
                log.info("Decoded accessToken : " + decodedToken);
                return decodedToken;
            } catch (Exception e) {
                log.error("엑세스 토큰 디코딩 실패: " + e.getMessage(), e);
            }
        } else {
            log.warn("Authorization 헤더가 null이거나 Bearer 토큰이 없습니다.");
        }

        return null;
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }
}


