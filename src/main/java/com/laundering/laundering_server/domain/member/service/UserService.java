package com.laundering.laundering_server.domain.member.service;

import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.common.socialPlatform.SocialPlatformType;
import com.laundering.laundering_server.domain.member.model.dto.response.UserResponse;
import com.laundering.laundering_server.domain.member.model.entity.User;
import com.laundering.laundering_server.domain.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public UserResponse getUserInfo(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNKNOWN_ERROR));

        return UserResponse.builder()
                .name(user.getName())
                .studentNum(Long.parseLong(user.getStudentId()))
                .email(user.getEmail())
                .roomNum(Long.parseLong(user.getRoomNum()))
                .build();
    }

    public void deleteUser(Long memberId) {
        Optional<User> userOptional = userRepository.findById(memberId);

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
        } else {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        }
    }
}