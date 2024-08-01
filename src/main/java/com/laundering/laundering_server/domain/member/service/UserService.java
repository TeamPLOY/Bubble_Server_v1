package com.laundering.laundering_server.domain.member.service;

import com.laundering.laundering_server.common.socialPlatform.SocialPlatformType;
import com.laundering.laundering_server.domain.member.model.entity.User;
import com.laundering.laundering_server.domain.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getBySocialAccount(String socialUid) {
        return userRepository.findBySocialAccountUid(socialUid).orElse(null);
    }

    @Transactional
    public User createBySocialAccount(String socialUid)
    {
        return userRepository.save(User.builder()
                .socialAccountUid(socialUid)
                .build()
        );
    }
}

