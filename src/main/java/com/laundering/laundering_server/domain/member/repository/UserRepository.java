package com.laundering.laundering_server.domain.member.repository;


import com.laundering.laundering_server.common.socialPlatform.SocialPlatformType;
import com.laundering.laundering_server.domain.member.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>
{
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findBySocialAccountUid(String socialAccountUid);

}

