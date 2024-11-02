package com.ploy.bubble_server_v1.common.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j // 로그를 사용하기 위한 어노테이션
@RequiredArgsConstructor // 필요한 인스턴스 변수를 생성자 주입으로 초기화해주는 어노테이션
public class CustomAuthenticationFilter extends GenericFilter { // 커스텀 인증 필터 클래스

    private static final Map<String, UserInfo> TOKEN_USER_MAP = new HashMap<>(); // 토큰과 사용자 정보를 매핑하는 해시맵

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request; // HTTP 요청 객체로 변환
        HttpServletResponse httpServletResponse = (HttpServletResponse) response; // HTTP 응답 객체로 변환

        if (SecurityContextHolder.getContext().getAuthentication() == null) { // 현재 인증 객체가 존재하지 않는 경우
            String token = getAccessToken(httpServletRequest); // 요청에서 액세스 토큰을 가져옴
            if (token != null) { // 토큰이 존재하는 경우
                try {
                    UserInfo userInfo = TOKEN_USER_MAP.get(token); // 토큰에 매핑된 사용자 정보를 가져옴
                    if (userInfo != null) { // 사용자 정보가 존재하는 경우
                        Long memberId = userInfo.memberId; // 사용자 ID를 가져옴
                        List<GrantedAuthority> authorities = getAuthorities(userInfo); // 사용자 권한 목록을 가져옴

                        if (memberId != null && !authorities.isEmpty()) { // 사용자 ID와 권한이 존재하는 경우
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(memberId, null, authorities); // 인증 객체를 생성
                            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 인증 객체를 설정
                        }
                    }
                } catch (Exception e) { // 예외가 발생한 경우
                    log.warn("인증 처리중 문제가 발생하였습니다 : {}", e.getMessage()); // 경고 로그를 남김
                }
            }
        } else { // 이미 인증 객체가 존재하는 경우
            log.debug("이미 인증 객체가 존재합니다 : {}", SecurityContextHolder.getContext().getAuthentication()); // 디버그 로그를 남김
        }

        chain.doFilter(httpServletRequest, httpServletResponse); // 다음 필터로 요청과 응답 객체를 전달
    }

    private String getAccessToken(HttpServletRequest request) {
        return request.getHeader("access_token"); // 요청 헤더에서 액세스 토큰을 가져옴
    }

    private List<GrantedAuthority> getAuthorities(UserInfo userInfo) {
        List<GrantedAuthority> authorities = new ArrayList<>(); // 권한 목록을 저장할 리스트 생성
        for (String role : userInfo.roles) { // 사용자 정보의 각 역할에 대해
            authorities.add(new SimpleGrantedAuthority(role)); // 권한 객체를 생성하여 리스트에 추가
        }
        return authorities; // 권한 목록을 반환
    }

    private static class UserInfo {
        public Long memberId; // 사용자 ID
        public String[] roles; // 사용자 역할 배열
        public String provider; // 인증 제공자

        public UserInfo(Long memberId, String[] roles, String provider) { // 사용자 정보를 초기화하는 생성자
            this.memberId = memberId; // 사용자 ID를 초기화
            this.roles = roles; // 사용자 역할 배열을 초기화
            this.provider = provider; // 인증 제공자를 초기화
        }
    }
}

