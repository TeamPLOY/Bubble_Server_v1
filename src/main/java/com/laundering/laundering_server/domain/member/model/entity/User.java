package com.laundering.laundering_server.domain.member.model.entity;

import com.laundering.laundering_server.common.socialPlatform.SocialPlatformType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    private String socialAccountUid;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private SocialPlatformType socialPlatformType;

    private String name; // 이름

    private String grade; // 학년

    private String classId; // 반

    private String studentId; // 번호

    private String email; // 이메일

    private String washLocation; // 세탁실 위치
}
