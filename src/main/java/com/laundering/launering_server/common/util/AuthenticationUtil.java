package com.laundering.launering_server.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

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
     * 주어진 회원 ID가 현재 인증된 사용자와 일치하는지 확인합니다.
     *
     * @param memberId 확인할 회원 ID
     * @return 일치하면 true, 그렇지 않으면 false를 반환합니다.
     */
    public static boolean isValidAccess(Long memberId) {
        return Objects.equals(AuthenticationUtil.getMemberId(), memberId);
    }

    /**
     * 현재 인증 객체를 가져옵니다.
     *
     * @return 현재 인증 객체
     */
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
