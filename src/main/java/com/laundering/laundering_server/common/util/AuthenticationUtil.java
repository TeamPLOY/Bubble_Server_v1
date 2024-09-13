package com.laundering.laundering_server.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * 인증 관련 유틸리티 클래스입니다.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationUtil {

    /**
     인증된 사용자의 회원 ID를 가져옵니다.
     @return 인증된 사용자의 회원 ID. 인증되지 않은 경우 null을 반환합니다.
     **/
    public static Long getMemberId() {
        var anonymous = String.valueOf(isAnonymous());
        log.warn("known : "+anonymous);
        if (isAnonymous()) {
            return null;
        }
        return (Long) getAuthentication().getPrincipal();
    }

    /**
     * 사용자가 익명 사용자(인증되지 않은 사용자)인지 확인합니다.
     *
     * @return 익명 사용자이면 true, 그렇지 않으면 false를 반환합니다.
     */
    public static boolean isAnonymous() {
        Authentication authentication = getAuthentication();
        return authentication == null || authentication.getPrincipal().equals("anonymousUser");
    }

    /**
     * 현재 인증 객체를 가져옵니다.
     *
     * @return 현재 인증 객체
     */
    private static Authentication getAuthentication() {
        var context = SecurityContextHolder.getContext();
        log.info("Context :"+ context);
        var result = context.getAuthentication();
        log.info("result : " + result);
        return result;    }
}
