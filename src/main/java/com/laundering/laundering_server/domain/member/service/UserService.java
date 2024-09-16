package com.laundering.laundering_server.domain.member.service;

import com.laundering.laundering_server.common.exception.BusinessException;
import com.laundering.laundering_server.common.exception.ErrorCode;
import com.laundering.laundering_server.domain.member.model.dto.request.SignUpRequest;
import com.laundering.laundering_server.domain.member.model.dto.response.UserResponse;
import com.laundering.laundering_server.domain.member.model.entity.User;
import com.laundering.laundering_server.domain.member.model.vo.WashingRoom;
import com.laundering.laundering_server.domain.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void create(SignUpRequest req) {
        try {
            // roomNum에서 prefix ("A", "B")와 숫자 추출
            String prefix = req.roomNum().substring(0, 1);
            int roomNumber = Integer.parseInt(req.roomNum().substring(1));

            // Enum을 이용해 washingRoom 값 결정
            String washingRoom = WashingRoom.findWashingRoom(prefix, roomNumber);

            userRepository.save(User.builder()
                    .email(req.email()) // 사용자 아이디
                    .password(new BCryptPasswordEncoder().encode(req.password())) // 비밀번호
                    .name(req.name())
                    .stuNum(req.stuNum()) // 아이 아이디
                    .roomNum(req.roomNum()) // roomNum 저장
                    .washingRoom(washingRoom) // 계산된 washingRoom 저장
                    .build()
            );
        } catch (Exception e) {
            log.error("회원 생성 중 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }


    public UserResponse getUserInfo(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNKNOWN_ERROR));

        return UserResponse.builder()
                .name(user.getName())
                .studentNum(user.getStuNum())
                .email(user.getEmail())
                .roomNum(user.getRoomNum())
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