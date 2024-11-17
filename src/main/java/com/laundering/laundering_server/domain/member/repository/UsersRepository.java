package com.laundering.laundering_server.domain.member.repository;


import com.laundering.laundering_server.domain.member.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users>
{
    Optional<Users> findByRefreshToken(String refreshToken);

    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    Users findWashingRoomById(Long userId);

    Users findNameById(Long id);
}

