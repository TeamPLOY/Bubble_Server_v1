package com.ploy.bubble_server_v1.domain.user.repository;

import com.ploy.bubble_server_v1.domain.user.model.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

    // 이메일과 코드로 가장 최근 날짜의 하나의 레코드만 조회
    Optional<Email> findTop1ByEmailOrderByIdDesc(String email);

}

