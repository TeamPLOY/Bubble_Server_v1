package com.ploy.bubble_server_v1.domain.user.repository;


import com.ploy.bubble_server_v1.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>
{
    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

